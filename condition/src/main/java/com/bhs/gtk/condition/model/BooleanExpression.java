package com.bhs.gtk.condition.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class BooleanExpression extends ConditionExpression {
	private String operation; //AND, OR
	private List<ConditionExpression> conditionExpressions;

	protected BooleanExpression() {}
	
	public BooleanExpression(String operation) {
		this.operation = operation;
		this.conditionExpressions = new ArrayList<>();
	}
	
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public List<ConditionExpression> getConditionExpressions() {
		return conditionExpressions;
	}

	public void setConditionExpressions(List<ConditionExpression> conditionExpressions) {
		this.conditionExpressions = conditionExpressions;
	}

}
