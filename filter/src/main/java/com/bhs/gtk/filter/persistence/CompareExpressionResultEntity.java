package com.bhs.gtk.filter.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
@IdClass(CompareExpressionResultId.class)
public class CompareExpressionResultEntity {
	@Id
	@Column
	private String hash;
	
	@Id
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date marketTime;
	
	@Id
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String scripName;
	
	@Column
	private String status;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ArithmeticExpressionResultEntity> arithmeticExpressionResultEntities;
	
//	@ManyToMany (mappedBy = "compareExpressionResultEntities", fetch = FetchType.EAGER)
//	private List<FilterResultEntity> filterResultEntities;

	protected CompareExpressionResultEntity() {};
	
	public CompareExpressionResultEntity(String hash, Date marketTime, String scripName, String status) {
		this.setHash(hash);
		this.setMarketTime(marketTime);
		this.setScripName(scripName);
		this.setStatus(status);
		this.arithmeticExpressionResultEntities = new ArrayList<>();
	//	this.filterResultEntities = new ArrayList<>();
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

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public List<ArithmeticExpressionResultEntity> getArithmeticExpressionResultEntities() {
		return arithmeticExpressionResultEntities;
	}

	public void setArithmeticExpressionResultEntities(List<ArithmeticExpressionResultEntity> arithmeticExpressionResultEntities) {
		this.arithmeticExpressionResultEntities = arithmeticExpressionResultEntities;
	}
	
//	public List<FilterResultEntity> getFilterResultEntities() {
//		return filterResultEntities;
//	}
//
//	public void setFilterResultEntities(List<FilterResultEntity> filterResultEntities) {
//		this.filterResultEntities = filterResultEntities;
//	}
	
	public String getMarketTimeAsOffsetDateTime() {
		return OffsetDateTime.ofInstant(Instant.ofEpochMilli(marketTime.getTime()), ZoneId.systemDefault()).toString();
	}
}
