package com.tradeplan.mockfeed.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.threeten.bp.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeplan.mockfeed.model.Bar;
import com.tradeplan.mockfeed.service.SymbolService;

public class BarDataStreamJob implements Job {

	private DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm");
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobData = context.getMergedJobDataMap();
		SymbolService symbolService = (SymbolService) jobData.get("symbolService");
		WebSocketSession session = (WebSocketSession) jobData.get("session");
		Supplier<Set<Long>> symbolIdSupplier = (Supplier<Set<Long>>) jobData.get("symbolIdSupplier");

		for (Long symbolId : symbolIdSupplier.get()) {
			Bar bar = symbolService.getBar();

			String[] data = new String[8];
			data[0] = String.valueOf(symbolId);
			data[1] = bar.getTime().format(TIME_FORMATTER);
			data[2] = String.valueOf(bar.getOpen());
			data[3] = String.valueOf(bar.getHigh());
			data[4] = String.valueOf(bar.getLow());
			data[5] = String.valueOf(bar.getClose());
			data[6] = String.valueOf(bar.getVolume());
			data[7] = String.valueOf(bar.getOi());

			Map<String, String[]> barData = new HashMap<>();
			barData.put("bar1min", data);

			try {
				String responseText = mapper.writeValueAsString(barData);
				session.sendMessage(new TextMessage(responseText.getBytes()));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
