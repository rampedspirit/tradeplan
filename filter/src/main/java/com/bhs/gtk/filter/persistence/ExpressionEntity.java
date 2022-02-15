package com.bhs.gtk.filter.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class ExpressionEntity {

	@Id
	private String hash;
	
	@Column(length = PersistenceConstants.LARGE_TEXT_LIMIT)
	private String parseTree;
	
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String type;
	
	@ManyToMany (mappedBy = "expressions")
	private List<FilterEntity> filters;
	
	protected ExpressionEntity() {}
	
	public ExpressionEntity(String hash, String parseTree, String type) {
		this.parseTree = parseTree;
		this.hash = hash;
		this.type = type;
		filters = new ArrayList<>();
	}
	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
