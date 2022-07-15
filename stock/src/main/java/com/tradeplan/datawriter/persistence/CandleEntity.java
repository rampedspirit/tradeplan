package com.tradeplan.datawriter.persistence;

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
	private float open;

	@Column
	private float close;

	@Column
	private float low;

	@Column
	private float high;

	@Column
	private float volume;

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

	public float getOpen() {
		return open;
	}

	public void setOpen(float open) {
		this.open = open;
	}

	public float getClose() {
		return close;
	}

	public void setClose(float close) {
		this.close = close;
	}

	public float getLow() {
		return low;
	}

	public void setLow(float low) {
		this.low = low;
	}

	public float getHigh() {
		return high;
	}

	public void setHigh(float high) {
		this.high = high;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public float getVolume() {
		return volume;
	}
}
