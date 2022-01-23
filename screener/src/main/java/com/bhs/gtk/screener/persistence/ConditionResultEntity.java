package com.bhs.gtk.screener.persistence;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;

@Entity
@IdClass(ConditionResultId.class)
public class ConditionResultEntity {
	
	@Id
	private UUID conditionId;
	
	@Id
	@Temporal(TemporalType.TIMESTAMP)
	private Date marketTime;
	
	@Id
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String scripName;
	
	@Column
	private String status;
	
	protected ConditionResultEntity() {};
	
	public ConditionResultEntity(UUID conditionId, Date marketTime, String scripName, String status) {
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
	
	public OffsetDateTime getMarketTimeAsOffsetDateTime() {
		return OffsetDateTime.ofInstant(Instant.ofEpochMilli(marketTime.getTime()), ZoneId.systemDefault());
	}
	
}
