package com.tradeplan.dataReader.persistence;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class StockDetailsId implements Serializable{

	private static final long serialVersionUID = -6508304107759011975L;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar userDefinedMarketTime;
	private String symbol;

	protected StockDetailsId() {}
	
	public StockDetailsId(Calendar userDefinedMarketTime, String symbol) {
		this.userDefinedMarketTime = userDefinedMarketTime;
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Calendar getUserDefinedMarketTime() {
		return userDefinedMarketTime;
	}

	public void setUserDefinedMarketTime(Calendar userDefinedMarketTime) {
		this.userDefinedMarketTime = userDefinedMarketTime;
	}

}
