package com.bhs.gtk.expression.model;

import org.springframework.stereotype.Component;

@Component
public abstract class Expression {
	private String operation;
	private double result;
	private String evalTime;
	private String scrip;
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public double getResult() {
		return result;
	}
	public void setResult(double result) {
		this.result = result;
	}
	public String getEvalTime() {
		return evalTime;
	}
	public void setEvalTime(String evalTime) {
		this.evalTime = evalTime;
	}
	public String getScrip() {
		return scrip;
	}
	public void setScrip(String scrip) {
		this.scrip = scrip;
	}
}
