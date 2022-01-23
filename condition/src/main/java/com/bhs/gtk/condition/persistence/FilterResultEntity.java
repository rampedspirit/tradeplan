package com.bhs.gtk.condition.persistence;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;

@Entity
@IdClass(FilterResultId.class)
public class FilterResultEntity {
	
	@Column
	private UUID filterId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date marketTime;
	
	@Id
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String scripName;
	
	@Column
	private String status;
	
	protected FilterResultEntity() {};
	
	public FilterResultEntity(UUID filterId, Date marketTime, String scripName, String status) {
		this.setFilterId(filterId);
		this.setMarketTime(marketTime);
		this.setScripName(scripName);
		this.setStatus(status);
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

	public UUID getFilterId() {
		return filterId;
	}

	public void setFilterId(UUID filterId) {
		this.filterId = filterId;
	}
	
	public String getMarketTimeAsOffsetDateTime() {
		return OffsetDateTime.ofInstant(Instant.ofEpochMilli(marketTime.getTime()), ZoneId.systemDefault()).toString();
	}

}
