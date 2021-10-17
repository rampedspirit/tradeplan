package com.bhs.gtk.expression.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ExpressionResultEntity {
	
	@Id
	@Column
	private long checksum;
	
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String expression;
	
	@Column
	private double result;
	
    @Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String status;

//    public ExpressionResultEntity() {
//    }
    
    public ExpressionResultEntity(long checksum, String expression, double result, String status) {
    	this.checksum = checksum;
    	this.expression = expression;
    	this.result = result;
    	this.status = status;
    }
    
	public String getExpression() {
		return expression;
	}

	
//	public void setExpression(String expression) {
//		this.expression = expression;
//	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getChecksum() {
		return checksum;
	}

//	public void setChecksum(long checksum) {
//		this.checksum = checksum;
//	}

}
