package com.bhs.gtk.filter.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class LogicalExpression implements BooleanExpression {

	private String operation;
	private List<BooleanExpression> booleanExpressions;	
	
	protected LogicalExpression() {}
	
	public LogicalExpression(String operation) {
		this.operation = operation;
		this.booleanExpressions = new ArrayList<>();
	}

	public List<BooleanExpression> getBooleanExpressions() {
		return booleanExpressions;
	}

	public boolean addBooleanExpression(BooleanExpression booleanExpression) {
		if(getBooleanExpressions() == null) {
			setBooleanExpressions(new ArrayList<>());
		}
		return getBooleanExpressions().add(booleanExpression);
	}

	public void setBooleanExpressions(List<BooleanExpression> booleanExpressions) {
		this.booleanExpressions = booleanExpressions;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
}
