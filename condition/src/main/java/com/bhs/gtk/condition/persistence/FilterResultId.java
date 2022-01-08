package com.bhs.gtk.condition.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class FilterResultId implements Serializable {
	
	private static final long serialVersionUID = 4346766076221679270L;

	private UUID filterId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date marketTime;
	
	private String scripName;
	
	public FilterResultId() {
	}
	
	public FilterResultId(UUID conditionId, Date marketTime, String scripName) {
		this.setFilterId(conditionId);
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

	public UUID getFilterId() {
		return filterId;
	}

	public void setFilterId(UUID filterId) {
		this.filterId = filterId;
	}

}
