package com.bhs.gtk.expression.model;

import org.springframework.stereotype.Component;

@Component
public abstract class Expression {
	private String operation;
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
}
