package com.tradeplan.dataReader.persistence;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@IdClass(StockDetailsId.class)
public class StockDetailsEntity {
	
	@Id
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Calendar userDefinedMarketTime;
	
	@Id
	@Column(length = 10)
	private String symbol;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Calendar modifiedTime;
	
	@Column
	private Double open;
	
	@Column
	private Double low;
	
	@Column
	private Double high;
	
	@Column
	private Double close;
	
	@Column
	private Integer volume;

	@PrePersist
	void onCreate() {
		this.modifiedTime = Calendar.getInstance();
	}
	
	protected StockDetailsEntity() {}
	
	public StockDetailsEntity(Calendar userDefinedMarketTime, String symbol, Double open, Double low, Double high,
			Double close, Integer volume) {
		this.userDefinedMarketTime = userDefinedMarketTime;
		this.symbol = symbol;
		this.open = open;
		this.low = low;
		this.high = high;
		this.close = close;
		this.volume = volume;
	}
	

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	public Calendar getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Calendar modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Calendar getUserDefinedMarketTime() {
		return userDefinedMarketTime;
	}

	public void setUserDefinedMarketTime(Calendar userDefinedMarketTime) {
		this.userDefinedMarketTime = userDefinedMarketTime;
	}

}
