package com.bhs.gtk.screener.persistence;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

}
