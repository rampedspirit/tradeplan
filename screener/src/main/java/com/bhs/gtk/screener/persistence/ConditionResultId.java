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
		this.conditionId = conditionId;
		this.marketTime = marketTime;
		this.scripName = scripName;
	}

}
