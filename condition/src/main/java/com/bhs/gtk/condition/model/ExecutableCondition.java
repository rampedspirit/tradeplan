package com.bhs.gtk.condition.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class ExecutableCondition {

	private UUID conditionId;
	private Date marketTime;
	private String scripName;
	private String status;
	
	protected ExecutableCondition() {}
	
	public ExecutableCondition(UUID conditionId, Date marketTime, String scripName, String status) {
		this.conditionId = conditionId;
		this.marketTime = marketTime;
		this.scripName = scripName;
		this.status = status;
	}
	
	public UUID getConditionId() {
		return conditionId;
	}
	public void setConditionId(UUID conditionId) {
		this.conditionId = conditionId;
	}
	
	public String getScripName() {
		return scripName;
	}
	public void setScripName(String scripName) {
		this.scripName = scripName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Date getMarketTime() {
		return marketTime;
	}

	public void setMarketTime(Date marketTime) {
		this.marketTime = marketTime;
	}
	
}
