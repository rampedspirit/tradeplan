package com.bhs.gtk.mockfeed.persistence;

import java.io.Serializable;

public class StockId implements Serializable {
	private static final long serialVersionUID = 1L;

	private String exchange;
	private String symbol;

	public StockId() {
	}

	public StockId(String exchange, String symbol) {
		this.exchange = exchange;
		this.symbol = symbol;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
