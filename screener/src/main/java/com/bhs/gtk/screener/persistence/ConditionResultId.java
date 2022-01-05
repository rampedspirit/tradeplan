package com.bhs.gtk.screener.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class ConditionResultId implements Serializable{
	
	private static final long serialVersionUID = 7114909181260672051L;

	private UUID conditionId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date marketTime;
	
	private String scripName;
	
	public ConditionResultId() {
	}
	
	public ConditionResultId(UUID conditionId, Date marketTime, String scripName) {
		this.setConditionId(conditionId);
		this.setMarketTime(marketTime);
		this.setScripName(scripName);
	}

	public String getScripName() {
		return scripName;
	}

	public void setScripName(String scripName) {
		this.scripName = scripName;
	}

	public UUID getConditionId() {
		return conditionId;
	}

	public void setConditionId(UUID conditionId) {
		this.conditionId = conditionId;
	}

	public Date getMarketTime() {
		return marketTime;
	}

	public void setMarketTime(Date marketTime) {
		this.marketTime = marketTime;
	}

}
