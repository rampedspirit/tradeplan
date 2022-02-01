package com.bhs.gtk.filter.persistence;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class FilterEntity {

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
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ExpressionEntity> expressions;
	
	protected FilterEntity() {}
	
	public FilterEntity(String name, String description, String code, String parseTree) {
		this.name = name;
		this.description = description;
		this.code = code;
		this.parseTree = parseTree;
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

	public List<ExpressionEntity> getExpressions() {
		return expressions;
	}

	public void setExpressions(List<ExpressionEntity> expressions) {
		this.expressions = expressions;
	}
}
