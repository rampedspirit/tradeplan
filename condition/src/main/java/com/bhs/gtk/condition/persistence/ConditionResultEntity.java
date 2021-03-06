package com.bhs.gtk.condition.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;


@Entity
@IdClass(ConditionResultId.class)
public class ConditionResultEntity {

	@Column
	private UUID conditionId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date marketTime;
	
	@Id
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String scripName;
	
	@Column
	private String status;
	
	//TODO: remove many to many (unlink condition result and filter result). Similar to as in filterService.
	@ManyToMany(fetch = FetchType.EAGER)
	private List<FilterResultEntity> filterResultEntities;
	
	protected ConditionResultEntity() {};
	
	public ConditionResultEntity(UUID conditionId, Date marketTime, String scripName, String status) {
		this.setConditionId(conditionId);
		this.setMarketTime(marketTime);
		this.setScripName(scripName);
		this.setStatus(status);
		this.filterResultEntities = new ArrayList<>();
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

	public List<FilterResultEntity> getFilterResultEntities() {
		return filterResultEntities;
	}

	public void setFilterResultEntities(List<FilterResultEntity> filterResultEntities) {
		this.filterResultEntities = filterResultEntities;
	}
	
	public OffsetDateTime getMarketTimeAsOffsetDateTime() {
		return OffsetDateTime.ofInstant(Instant.ofEpochMilli(marketTime.getTime()), ZoneId.systemDefault());
	}

}
