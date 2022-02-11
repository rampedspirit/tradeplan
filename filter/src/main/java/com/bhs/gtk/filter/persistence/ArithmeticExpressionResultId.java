package com.bhs.gtk.filter.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class ArithmeticExpressionResultId implements Serializable{
	
	private static final long serialVersionUID = 4346766076221679270L;

	private String hash;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date marketTime;
	
	private String scripName;
	
	public ArithmeticExpressionResultId() {
	}
	
	public ArithmeticExpressionResultId(String hash, Date marketTime, String scripName) {
		this.setHash(hash);
		this.setMarketTime(marketTime);
		this.setScripName(scripName);
	}

	public String getScripName() {
		return scripName;
	}

	public void setScripName(String scripName) {
		this.scripName = scripName;
	}

	public Date getMarketTime() {
		return marketTime;
	}

	public void setMarketTime(Date marketTime) {
		this.marketTime = marketTime;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	

}
