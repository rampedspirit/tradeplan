package com.bhs.gtk.expression.model;

import org.springframework.stereotype.Component;

@Component
public class EvaluationResponse {

	private long checksum;
	private String expression;
	private double result;
	private String status;
	
	public EvaluationResponse() {
		// TODO Auto-generated constructor stub
	}
	
	public EvaluationResponse(long checksum, String expression, double result, String status) {
		this.checksum = checksum;
		this.expression = expression;
		this.result = result;
		this.status = status;
	}
	public String getExpression() {
		return expression;
	}

	public double getResult() {
		return result;
	}
	public String getStatus() {
		return status;
	}
	public long getChecksum() {
		return checksum;
	}
	
}
