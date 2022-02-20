package com.bhs.gtk.expression.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class ExpressionEntity {

	@Id
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String hash;
	
	@Column(length = PersistenceConstants.LARGE_TEXT_LIMIT)
	private String parseTree;

	protected ExpressionEntity() {}
	
	public ExpressionEntity(String hash, String parseTree) {
		this.hash = hash;
		this.parseTree = parseTree;
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
}
