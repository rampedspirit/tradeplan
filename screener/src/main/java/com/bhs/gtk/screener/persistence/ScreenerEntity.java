package com.bhs.gtk.screener.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.bhs.gtk.screener.persistence.PersistenceConstants;

@Entity
public class ScreenerEntity {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID screenerId;
	
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String name;
	
	@Column(length = PersistenceConstants.LARGE_TEXT_LIMIT)
	private String description;
	
	@Column
	private UUID watchlistId;
	
	@Column
	private UUID conditionId;
	
	@OneToMany (cascade = CascadeType.ALL)
	private List<ExecutableEntity> executionEntities;

	protected ScreenerEntity() {}
	
	public ScreenerEntity(String name, String description, UUID watchlistId, UUID conditionId) {
		this.name = name;
		this.setDescription(description);
		this.watchlistId = watchlistId;
		this.conditionId = conditionId;
		this.executionEntities = new ArrayList<>();
	}
	
	public UUID getScreenerId() {
		return screenerId;
	}

	public void setScreenerId(UUID screenerId) {
		this.screenerId = screenerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public List<ExecutableEntity> getExecutionEntities() {
		return executionEntities;
	}

	public void setExecutionEntities(List<ExecutableEntity> executionEntities) {
		this.executionEntities = executionEntities;
	}

}
