package com.bhs.gtk.filter.model;

public class CompareExpression implements BooleanExpression{

	private String hash;
	private String parseTree;
	private String operation;
	private ArithmeticExpression leftArithmeticExpression;
	private ArithmeticExpression rightArithmeticExpression;
	
	protected CompareExpression() {}
	
	public CompareExpression(String operation) {
		this.operation = operation;
	}
	
	public CompareExpression(String parseTree, String operation, ArithmeticExpression leftArithmeticExpression,
			ArithmeticExpression rightArithmeticExpression, String hash) {
		this.parseTree = parseTree;
		this.operation = operation;
		this.leftArithmeticExpression = leftArithmeticExpression;
		this.rightArithmeticExpression = rightArithmeticExpression;
		this.hash = hash;
	}

	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public ArithmeticExpression getLeftArithmeticExpression() {
		return leftArithmeticExpression;
	}
	public void setLeftArithmeticExpression(ArithmeticExpression leftArithmeticExpression) {
		this.leftArithmeticExpression = leftArithmeticExpression;
	}
	public ArithmeticExpression getRightArithmeticExpression() {
		return rightArithmeticExpression;
	}
	public void setRightArithmeticExpression(ArithmeticExpression rightArithmeticExpression) {
		this.rightArithmeticExpression = rightArithmeticExpression;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getParseTree() {
		return parseTree;
	}
	public void setParseTree(String parseTree) {
		this.parseTree = parseTree;
	}

}
