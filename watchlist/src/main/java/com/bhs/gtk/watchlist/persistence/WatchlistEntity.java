package com.bhs.gtk.watchlist.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class WatchlistEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID id;

	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String name;

	@Column(length = PersistenceConstants.LARGE_TEXT_LIMIT)
	private String description;

	@ElementCollection
	@CollectionTable(name = "watchlist_scripnames", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "scripNames")
	private List<String> scripNames;

	protected WatchlistEntity() {
	}

	public WatchlistEntity(String name, String description) {
		this.name = name;
		this.description = description;
		this.scripNames = new ArrayList<>();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public List<String> getScripNames() {
		return scripNames;
	}

	public void setScripNames(List<String> scripNames) {
		this.scripNames = scripNames;
	}
}
