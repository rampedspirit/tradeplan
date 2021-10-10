package com.bhs.gtk.expression.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ExpressionEntity {
	
	@Id
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String checksum;
	
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String expression;

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

}
