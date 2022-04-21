package com.bhs.gtk.stock.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@IdClass(CandleId.class)
public class CandleEntity {

	@Id
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String exchange;

	@Id
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String symbol;

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date marketTime;

	@Column
	private int open;

	@Column
	private int close;

	@Column
	private int low;

	@Column
	private int high;

	@Column
	private int volume;

	protected CandleEntity() {
	}

	public CandleEntity(String exchange, String symbol, Date marketTime) {
		this.exchange = exchange;
		this.symbol = symbol;
		this.marketTime = marketTime;
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

	public int getOpen() {
		return open;
	}

	public void setOpen(int open) {
		this.open = open;
	}

	public int getClose() {
		return close;
	}

	public void setClose(int close) {
		this.close = close;
	}

	public int getLow() {
		return low;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}
}
