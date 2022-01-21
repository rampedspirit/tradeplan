package com.bhs.gtk.condition.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class FilterEntity {
	
	@Id
	private UUID id;
	
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String status;

	@ManyToMany(mappedBy = "filters")
	private List<ConditionEntity> conditions;
	
	protected FilterEntity() {}
	
	public FilterEntity(UUID id, String status) {
		this.id = id;
		this.status = status;
		this.conditions = new ArrayList<>();
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ConditionEntity> getConditions() {
		return conditions;
	}

	public void setConditions(List<ConditionEntity> conditions) {
		this.conditions = conditions;
	}

}
