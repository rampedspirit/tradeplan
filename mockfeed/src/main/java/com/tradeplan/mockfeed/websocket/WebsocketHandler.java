package com.tradeplan.mockfeed.websocket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeplan.mockfeed.model.AuthResponse;
import com.tradeplan.mockfeed.model.AuthResponse.SubscriptionEnum;
import com.tradeplan.mockfeed.model.LogoutResponse;
import com.tradeplan.mockfeed.model.MethodRequest;
import com.tradeplan.mockfeed.model.MethodRequest.MethodEnum;
import com.tradeplan.mockfeed.model.Touchline;
import com.tradeplan.mockfeed.model.UnSubscribeResponse;
import com.tradeplan.mockfeed.persistence.UserEntity;
import com.tradeplan.mockfeed.service.SymbolService;
import com.tradeplan.mockfeed.service.UserService;

@Component
public class WebsocketHandler extends TextWebSocketHandler {

	@Autowired
	private UserService userService;

	@Autowired
	private SymbolService symbolService;

	private final List<DataStreamer> dataStreamers = new ArrayList<>();

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		MultiValueMap<String, String> params = UriComponentsBuilder.fromUri(session.getUri()).build().getQueryParams();
		UserEntity user = userService.getUserByNameAndPassword(params.getFirst("user"), params.getFirst("password"));

		AuthResponse authResponse = new AuthResponse();
		if (user == null) {
			authResponse.setSuccess(false);
			authResponse.setMessage("invalid login credentials");
		} else if (!user.isSubscriptionActive()) {
			authResponse.setSuccess(false);
			authResponse.setMessage("user subscription expired");
		} else if (dataStreamers.stream().anyMatch(streamer -> streamer.matchName(user.getName()))) {
			authResponse.setSuccess(false);
			authResponse.setMessage("User Already Connected");
		} else {
			authResponse.setSuccess(true);
			authResponse.setMessage("MockData Real Time Data Service");
			authResponse.segments(Arrays.asList("nseeq", "nsefo", "mcx", "cds"));
			authResponse.setMaxsymbols(symbolService.getAllSymbolsCount());
			authResponse.setSubscription(SubscriptionEnum._1MIN);

			DataStreamer dataStreamer = new DataStreamer(user.getName(), session, symbolService);
			dataStreamer.start();
			dataStreamers.add(dataStreamer);
		}

		String responseText = mapper.writeValueAsString(authResponse);
		session.sendMessage(new TextMessage(responseText.getBytes()));
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		removeDataStreamer(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		MethodRequest methodRequest = mapper.readValue(message.getPayload(), MethodRequest.class);
		if (methodRequest.getMethod() == MethodEnum.LOGOUT) {
			removeDataStreamer(session);

			LogoutResponse response = new LogoutResponse();
			response.setSuccess(true);

			String responseText = mapper.writeValueAsString(response);
			session.sendMessage(new TextMessage(responseText.getBytes()));
		} else if (methodRequest.getMethod() == MethodEnum.ADDSYMBOL) {
			Optional<DataStreamer> dataStreamer = getDataStreamer(session);
			if (dataStreamer.isPresent()) {
				Touchline touchline = dataStreamer.get().addSymbols(methodRequest.getSymbols());
				String responseText = mapper.writeValueAsString(touchline);
				session.sendMessage(new TextMessage(responseText.getBytes()));
			}
		} else if (methodRequest.getMethod() == MethodEnum.REMOVESYMBOL) {
			Optional<DataStreamer> dataStreamer = getDataStreamer(session);
			if (dataStreamer.isPresent()) {
				dataStreamer.get().removeSymbols(methodRequest.getSymbols());

				UnSubscribeResponse response = new UnSubscribeResponse();
				response.setSuccess(true);
				response.setMessage("symbols removed");
				response.symbolsremoved(methodRequest.getSymbols().size());

				String responseText = mapper.writeValueAsString(response);
				session.sendMessage(new TextMessage(responseText.getBytes()));
			}
		}
	}

	private void removeDataStreamer(WebSocketSession session) {
		getDataStreamer(session).ifPresent(dataStreamer -> {
			dataStreamer.stop();
			dataStreamers.remove(dataStreamer);
		});
	}

	private Optional<DataStreamer> getDataStreamer(WebSocketSession session) {
		return dataStreamers.stream().filter(streamer -> streamer.matchSession(session)).findFirst();
	}
}
