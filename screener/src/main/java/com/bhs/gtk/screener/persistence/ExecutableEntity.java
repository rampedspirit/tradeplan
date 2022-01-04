package com.bhs.gtk.screener.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.bhs.gtk.screener.model.ExecutableStatus;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "marketTime", "watchlistId","conditionId" }) })
public class ExecutableEntity {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID executableId;
	
	@Column(length = PersistenceConstants.LARGE_TEXT_LIMIT)
	private String note;
	
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date marketTime;
	
	@Column
	private UUID watchlistId;
	
	@Column
	private UUID conditionId;
	
	@Column
	private int numberOfScripForExecution;
	
	@Column
	private int numberOfScripWithResultAvailable;  
	
	@OneToMany()
	private List<ConditionResultEntity> conditionResultEntities;
	
	protected ExecutableEntity() {}

	public ExecutableEntity(String note,Date marketTime, UUID watchlistId, UUID conditionId) {
		this.note = note;
		this.status = ExecutableStatus.QUEUED.name();
		this.marketTime = marketTime;
		this.conditionResultEntities = new ArrayList<>();
		this.watchlistId = watchlistId;
		this.conditionId = conditionId;
	}
	
	public UUID getExecutableId() {
		return executableId;
	}

	public void setExecutableId(UUID executableId) {
		this.executableId = executableId;
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

	public List<ConditionResultEntity> getConditionResultEntities() {
		return conditionResultEntities;
	}

	public void setConditionResultEntities(List<ConditionResultEntity> conditionResultEntities) {
		this.conditionResultEntities = conditionResultEntities;
	}

	public UUID getWatchlistId() {
		return watchlistId;
	}

	public void setWatchlistId(UUID watchlistId) {
		this.watchlistId = watchlistId;
	}

	public UUID getConditionId() {
		return conditionId;
	}

	public void setConditionId(UUID conditionId) {
		this.conditionId = conditionId;
	}

	public int getNumberOfScripForExecution() {
		return numberOfScripForExecution;
	}

	public void setNumberOfScripForExecution(int numberOfScripForExecution) {
		this.numberOfScripForExecution = numberOfScripForExecution;
	}

	public int getNumberOfScripWithResultAvailable() {
		return numberOfScripWithResultAvailable;
	}

	public void setNumberOfScripWithResultAvailable(int numberOfScripWithResultAvailable) {
		this.numberOfScripWithResultAvailable = numberOfScripWithResultAvailable;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
