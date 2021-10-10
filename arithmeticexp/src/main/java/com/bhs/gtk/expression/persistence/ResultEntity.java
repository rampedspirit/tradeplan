package com.bhs.gtk.expression.persistence;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(ResultId.class)
public class ResultEntity {

	@ManyToOne
	@JoinColumn(name = "checksum")
	@Id
	private ExpressionEntity expression;
	
	@Id
	private Date evalTime;
	
	@Id
	private String scrip;
	
	private double result;
	
	

	public ExpressionEntity getExpression() {
		return expression;
	}

	public void setExpression(ExpressionEntity expression) {
		this.expression = expression;
	}

	public Date getEvalTime() {
		return evalTime;
	}

	public void setEvalTime(Date evalTime) {
		this.evalTime = evalTime;
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

	public String getScrip() {
		return scrip;
	}

	public void setScrip(String scrip) {
		this.scrip = scrip;
	}
	
	
	
}
