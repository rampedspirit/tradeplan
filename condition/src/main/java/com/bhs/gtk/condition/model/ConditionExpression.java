package com.bhs.gtk.condition.model;

public abstract class ConditionExpression {

	private String result; //PASS/FAIL/ERROR
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}