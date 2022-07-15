package com.tradeplan.mockfeed.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.web.socket.WebSocketSession;

import com.tradeplan.mockfeed.model.Touchline;
import com.tradeplan.mockfeed.persistence.SymbolEntity;
import com.tradeplan.mockfeed.service.SymbolService;

class DataStreamer extends TimerTask implements Supplier<Set<Long>> {
	private static final long MAX_CONNECTION_TIME = 5400000; // in ms

	private final String user;
	private final WebSocketSession session;
	private final Set<Scheduler> schedulers;
	private final SymbolService symbolService;
	private final Set<SymbolEntity> symbolEntities;

	DataStreamer(String user, WebSocketSession session, SymbolService symbolService) {
		this.user = user;
		this.session = session;
		this.symbolService = symbolService;
		this.schedulers = new HashSet<>();
		this.symbolEntities = new HashSet<>();

		new Timer().schedule(this, MAX_CONNECTION_TIME);
	}

	boolean matchName(String name) {
		return StringUtils.compare(user, name) == 0;
	}

	boolean matchSession(WebSocketSession otherSession) {
		return session.equals(otherSession);
	}

	Touchline addSymbols(List<String> symbolNames) {
		List<SymbolEntity> entities = getSymbolEntitiesBySymbols(symbolNames);
		symbolEntities.addAll(entities);

		Touchline touchline = createTouchLine(entities);
		touchline.setTotalsymbolsubscribed(symbolEntities.size());
		return touchline;
	}

	void removeSymbols(List<String> symbolNames) {
		List<SymbolEntity> entities = getSymbolEntitiesBySymbols(symbolNames);
		symbolEntities.removeAll(entities);
	}

	void start() {
		try {
			Scheduler scheduler = startBarDataStreamJob();
			schedulers.add(scheduler);
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	void stop() {
		schedulers.forEach(this::shutdownScheduler);
		schedulers.clear();
	}

	@Override
	public void run() {
		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Set<Long> get() {
		return symbolEntities.stream().map(SymbolEntity::getId).collect(Collectors.toSet());
	}

	private List<SymbolEntity> getSymbolEntitiesBySymbols(Collection<String> symbols) {
		return symbolService.getAllSymbols("nse").stream()
				.filter(symbolEntity -> symbols.contains(symbolEntity.getSymbol())).collect(Collectors.toList());
	}

	private Touchline createTouchLine(List<SymbolEntity> symbolEntities) {
		Touchline touchline = new Touchline();
		touchline.setSuccess(true);
		touchline.setMessage("touchline");
		touchline.setSymbolsadded(symbolEntities.size());

		List<List<String>> symbolList = symbolEntities.stream().map(symbolEntity -> {
			List<String> values = new ArrayList<>();
			values.add(symbolEntity.getSymbol());
			values.add(String.valueOf(symbolEntity.getId()));
			return values;
		}).collect(Collectors.toList());
		touchline.setSymbollist(symbolList);

		return touchline;
	}

	private Scheduler startBarDataStreamJob() throws SchedulerException {
		Trigger trigger = createTriggerInIST("0 * 9-22 ? * MON-FRI");

		JobDetail jobDetail = JobBuilder.newJob(BarDataStreamJob.class).build();
		JobDataMap dataMap = jobDetail.getJobDataMap();
		dataMap.put("session", session);
		dataMap.put("symbolService", symbolService);
		dataMap.put("symbolIdSupplier", this);

		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();
		return scheduler;
	}

	private Trigger createTriggerInIST(String cronExpression) {
		TimeZone timeZone = TimeZone.getTimeZone("IST");
		return TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression).inTimeZone(timeZone)).build();
	}

	private void shutdownScheduler(Scheduler scheduler) {
		try {
			scheduler.shutdown(true);
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}
}
