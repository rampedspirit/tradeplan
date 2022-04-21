package com.bhs.gtk.stock.persistence;

import java.io.Serializable;
import java.util.Date;

public class CandleId implements Serializable {
	private static final long serialVersionUID = 1L;

	private String exchange;
	private String symbol;
	private Date marketTime;

	public CandleId() {
	}

	public CandleId(String exchange, String symbol) {
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

	public Date getMarketTime() {
		return marketTime;
	}

	public void setMarketTime(Date marketTime) {
		this.marketTime = marketTime;
	}
}
