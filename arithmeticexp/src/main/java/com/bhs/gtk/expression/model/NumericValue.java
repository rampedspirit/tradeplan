package com.bhs.gtk.expression.model;

import org.springframework.stereotype.Component;

@Component
public class NumericValue extends Expression{

	private double value;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
