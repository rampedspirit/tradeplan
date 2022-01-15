package com.bhs.gtk.condition.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class ConditionEntity {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID id;
	
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String name;
	@Column(length = PersistenceConstants.LARGE_TEXT_LIMIT)
	private String description;
	@Column(length = PersistenceConstants.LARGE_TEXT_LIMIT)
	private String code;
	@Column(length = PersistenceConstants.LARGE_TEXT_LIMIT)
	private String parseTree;
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private List<FilterEntity> filters;
	
	protected ConditionEntity() {}
	
	public ConditionEntity(String name, String description, String code, String parseTree) {
		this.name = name;
		this.description = description;
		this.code = code;
		this.parseTree = parseTree;
		this.filters = new ArrayList<>();
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getParseTree() {
		return parseTree;
	}
	public void setParseTree(String parseTree) {
		this.parseTree = parseTree;
	}

	public List<FilterEntity> getFilters() {
		return filters;
	}

	public void setFilters(List<FilterEntity> filters) {
		this.filters = filters;
	}
}
