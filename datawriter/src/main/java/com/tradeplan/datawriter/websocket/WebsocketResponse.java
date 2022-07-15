package com.tradeplan.datawriter.websocket;

import java.util.List;

public class WebsocketResponse {
	private boolean success;
	private String message;
	private List<List<String>> symbollist;
	private List<String> bar1min;

	public boolean isSuccess() {
		return success || bar1min != null;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<List<String>> getSymbollist() {
		return symbollist;
	}

	public void setSymbollist(List<List<String>> symbollist) {
		this.symbollist = symbollist;
	}

	public List<String> getBar1min() {
		return bar1min;
	}

	public void setBar1min(List<String> bar1min) {
		this.bar1min = bar1min;
	}
}
