package com.bhs.gtk.watchlist.persistence;

import java.io.Serializable;

public class SymbolId implements Serializable {
	private static final long serialVersionUID = 1L;

	private String exchange;
	private String symbol;

	public SymbolId() {
	}

	public SymbolId(String exchange, String symbol) {
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
