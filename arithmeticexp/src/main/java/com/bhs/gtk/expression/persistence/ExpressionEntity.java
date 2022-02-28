package com.bhs.gtk.expression.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class ExpressionEntity {

	@Id
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String hash;
	
	@Column(length = PersistenceConstants.LARGE_TEXT_LIMIT)
	private String parseTree;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ExpressionResultEntity> expressionResults;
	
	protected ExpressionEntity() {}
	
	public ExpressionEntity(String hash, String parseTree) {
		this.hash = hash;
		this.parseTree = parseTree;
		this.expressionResults = new ArrayList<>();
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

	public List<ExpressionResultEntity> getExpressionResults() {
		return expressionResults;
	}

	public void setExpressionResults(List<ExpressionResultEntity> expressionResults) {
		this.expressionResults = expressionResults;
	}
}
