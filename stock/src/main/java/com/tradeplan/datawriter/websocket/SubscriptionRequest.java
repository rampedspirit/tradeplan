package com.tradeplan.datawriter.websocket;

import java.util.List;

public class SubscriptionRequest {

	private String method;
	private List<String> symbols;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public List<String> getSymbols() {
		return symbols;
	}

	public void setSymbols(List<String> symbols) {
		this.symbols = symbols;
	}
}
