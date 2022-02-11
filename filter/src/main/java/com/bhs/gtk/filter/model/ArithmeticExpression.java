package com.bhs.gtk.filter.model;

public class ArithmeticExpression {

	private String hash;
	private String parseTree;
	private ExpressionLocation location;
	
	protected ArithmeticExpression() {}
	
	public ArithmeticExpression(String parseTree, String hash, ExpressionLocation location) {
		this.parseTree = parseTree;
		this.hash = hash;
		this.setLocation(location);
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

	public ExpressionLocation getLocation() {
		return location;
	}

	public void setLocation(ExpressionLocation location) {
		this.location = location;
	}

}
