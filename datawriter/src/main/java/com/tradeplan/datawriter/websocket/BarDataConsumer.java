package com.tradeplan.datawriter.websocket;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeplan.datawriter.persistence.CandleEntity;
import com.tradeplan.datawriter.persistence.CandleRespository;
import com.tradeplan.datawriter.service.SymbolService;

@Service
public class BarDataConsumer extends TextWebSocketHandler {

	private static final List<String> AUTH_ERROR_MESSAGES = Arrays.asList("invalid login credentials",
			"user subscription expired", "invalid request", "invalid token", "User Already Connected");

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm");

	private final ObjectMapper mapper = new ObjectMapper();

	@Value(value = "${stock.datasource.url}")
	private String dataSourceUrl;

	@Value(value = "${stock.datasource.username}")
	private String username;

	@Value(value = "${stock.datasource.password}")
	private String password;

	@Autowired
	private SymbolService symbolService;

	@Autowired
	private CandleRespository candleRespository;

	private WebSocketSession session;

	public void start() throws InterruptedException, ExecutionException {
		WebSocketClient webSocketClient = new StandardWebSocketClient();
		String url = "ws://" + dataSourceUrl + "?user=" + username + "&password=" + password;
		webSocketClient.doHandshake(this, new WebSocketHttpHeaders(), URI.create(url)).get();
	}

	public void stop() throws IOException {
		if (session != null) {
			session.close();
			session = null;
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		WebsocketResponse response = mapper.readValue(message.getPayload(), WebsocketResponse.class);
		if (response.isSuccess()) {
			handlerSuccess(response, session);
		} else {
			handlerFailure(response, session);
		}
	}

	public void updateSymbolSubscription() throws IOException {
		if (session != null) {
			SubscriptionRequest request = new SubscriptionRequest();
			request.setMethod("addsymbol");

			List<String> symbols = symbolService.getAllSymbols();
			Iterator<Integer> batchIndices = getBatchedIndices(symbols.size(), 100).iterator();

			int lastIndex = 0;
			while (batchIndices.hasNext()) {
				int nextIndex = batchIndices.next();
				request.setSymbols(symbols.subList(lastIndex, nextIndex));
				TextMessage textMessage = new TextMessage(mapper.writeValueAsString(request).getBytes());
				session.sendMessage(textMessage);
				lastIndex = nextIndex;
			}
		}
	}

	private void handlerSuccess(WebsocketResponse response, WebSocketSession session)
			throws SchedulerException, IOException, ParseException {
		if (isAuthSuccess(response.getMessage())) {
			this.session = session;
			updateSymbolSubscription();
		} else if (response.getBar1min() != null) {
			insertCandle(response.getBar1min());
		}
	}

	private void handlerFailure(WebsocketResponse response, WebSocketSession session) throws IOException {
		if (isAuthError(response.getMessage())) {
			session.close();
		}
	}

	private boolean isAuthError(String message) {
		return AUTH_ERROR_MESSAGES.stream().anyMatch(errorMsg -> errorMsg.contains(message));
	}

	private boolean isAuthSuccess(String message) {
		return message != null && message.endsWith("Real Time Data Service");
	}

	private void insertCandle(List<String> bar) throws ParseException {
		Long symbolId = Long.parseLong(bar.get(0));
		String symbol = symbolService.getSymbolBySymbolId(symbolId);
		Date date = DATE_FORMAT.parse(bar.get(1));

		CandleEntity candle = new CandleEntity("NSE", symbol, date);
		candle.setOpen(Float.parseFloat(bar.get(2)));
		candle.setHigh(Float.parseFloat(bar.get(3)));
		candle.setLow(Float.parseFloat(bar.get(4)));
		candle.setClose(Float.parseFloat(bar.get(5)));
		candle.setVolume(Float.parseFloat(bar.get(6)));
		candleRespository.save(candle);
	}

	private List<Integer> getBatchedIndices(int totalSize, int batchSize) {
		List<Integer> batches = new ArrayList<>();
		int count = 0;
		while (count < totalSize) {
			if (totalSize - count >= batchSize) {
				count += batchSize;
			} else {
				count += totalSize - count;
			}
			batches.add(count);
		}
		return batches;
	}
}