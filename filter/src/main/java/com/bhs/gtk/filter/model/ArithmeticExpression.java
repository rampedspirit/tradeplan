package com.bhs.gtk.filter.model;

public class ArithmeticExpression {

	private String hash;
	private String parseTree;
	
	protected ArithmeticExpression() {}
	
	public ArithmeticExpression(String parseTree, String hash) {
		this.parseTree = parseTree;
		this.hash = hash;
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
