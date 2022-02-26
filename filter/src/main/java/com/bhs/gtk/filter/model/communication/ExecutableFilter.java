package com.bhs.gtk.filter.model.communication;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class ExecutableFilter {
	private UUID filterId;
	private Date marketTime;
	private String scripName;
	private String status;
	
	protected ExecutableFilter() {}
	
	public ExecutableFilter(UUID filterId, Date marketTime, String scripName, String status) {
		this.setFilterId(filterId);
		this.setMarketTime(marketTime);
		this.setScripName(scripName);
		this.setStatus(status);
	}

	public UUID getFilterId() {
		return filterId;
	}

	public void setFilterId(UUID filterId) {
		this.filterId = filterId;
	}

	public Date getMarketTime() {
		return marketTime;
	}

	public void setMarketTime(Date marketTime) {
		this.marketTime = marketTime;
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
	
	
}
