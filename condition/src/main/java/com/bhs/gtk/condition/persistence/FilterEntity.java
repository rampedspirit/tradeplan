package com.bhs.gtk.condition.persistence;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class FilterEntity {
	
	@Id
	private UUID id;
	
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String status;

	protected FilterEntity() {}
	
	public FilterEntity(UUID id, String status) {
		this.id = id;
		this.status = status;
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

}
