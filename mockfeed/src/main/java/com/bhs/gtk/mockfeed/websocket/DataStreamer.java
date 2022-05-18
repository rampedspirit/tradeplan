package com.bhs.gtk.mockfeed.websocket;

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

import com.bhs.gtk.mockfeed.model.Touchline;
import com.bhs.gtk.mockfeed.persistence.StockEntity;
import com.bhs.gtk.mockfeed.service.StockService;

class DataStreamer extends TimerTask implements Supplier<Set<String>> {
	private static final long MAX_CONNECTION_TIME = 5400000; // in ms

	private final String user;
	private final Set<String> symbols;
	private final WebSocketSession session;
	private final StockService stockService;

	private final Set<Scheduler> schedulers;

	DataStreamer(String user, WebSocketSession session, StockService stockService) {
		this.user = user;
		this.session = session;
		this.symbols = new HashSet<>();
		this.stockService = stockService;
		this.schedulers = new HashSet<>();

		new Timer().schedule(this, MAX_CONNECTION_TIME);
	}

	boolean matchName(String name) {
		return StringUtils.compare(user, name) == 0;
	}

	boolean matchSession(WebSocketSession otherSession) {
		return session.equals(otherSession);
	}

	Touchline addSymbols(List<String> symbolNames) {
		List<StockEntity> stockEntities = getStockEntitiesBySymbols(symbolNames);
		symbols.addAll(stockEntities.stream().map(StockEntity::getSymbol).collect(Collectors.toList()));

		Touchline touchline = createTouchLine(stockEntities);
		touchline.setTotalsymbolsubscribed(symbols.size());
		return touchline;
	}

	void removeSymbols(List<String> symbolNames) {
		List<StockEntity> stockEntities = getStockEntitiesBySymbols(symbolNames);
		symbols.removeAll(stockEntities.stream().map(StockEntity::getSymbol).collect(Collectors.toList()));
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
	public Set<String> get() {
		return symbols;
	}

	private List<StockEntity> getStockEntitiesBySymbols(Collection<String> symbols) {
		return stockService.getAllSymbols("nse").stream()
				.filter(stockEntity -> symbols.contains(stockEntity.getSymbol())).collect(Collectors.toList());
	}

	private Touchline createTouchLine(List<StockEntity> stockEntities) {
		Touchline touchline = new Touchline();
		touchline.setSuccess(true);
		touchline.setMessage("touchline");
		touchline.setSymbolsadded(stockEntities.size());

		List<List<String>> symbolList = stockEntities.stream().map(stockEntity -> {
			List<String> values = new ArrayList<>();
			values.add(stockEntity.getSymbol());
			values.add(stockEntity.getSymbol());
			return values;
		}).collect(Collectors.toList());
		touchline.setSymbollist(symbolList);

		return touchline;
	}

	private Scheduler startBarDataStreamJob() throws SchedulerException {
		Trigger trigger = createTriggerInIST("* * * ? * *");

		JobDetail jobDetail = JobBuilder.newJob(BarDataStreamJob.class).build();
		JobDataMap dataMap = jobDetail.getJobDataMap();
		dataMap.put("session", session);
		dataMap.put("stockService", stockService);
		dataMap.put("symbolsSupplier", this);

		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();
		return scheduler;
	}

	private Trigger createTriggerInIST(String cronExpression) {
		TimeZone timeZone = TimeZone.getTimeZone("UTC+05:30");
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
