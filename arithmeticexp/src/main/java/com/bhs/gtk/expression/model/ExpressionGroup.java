package com.bhs.gtk.expression.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ExpressionGroup extends Expression {

	private List<Expression> expressions;

	public List<Expression> getExpressions() {
		return Collections.unmodifiableList(expressions);
	}

	public boolean addExpression(Expression exp) {
		if (this.expressions == null) {
			this.expressions = new ArrayList<>();
		}
		return this.expressions.add(exp);
	}

}
