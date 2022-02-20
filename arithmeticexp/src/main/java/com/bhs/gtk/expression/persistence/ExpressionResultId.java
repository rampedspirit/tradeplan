package com.bhs.gtk.expression.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class ExpressionResultId implements Serializable {

	private static final long serialVersionUID = -16172003503514680L;

	private String hash;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date marketTime;
	
	private String scripName;
	
	public ExpressionResultId() {
	}
	
	public ExpressionResultId(String hash, Date marketTime, String scripName) {
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
