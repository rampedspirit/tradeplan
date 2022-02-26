package com.bhs.gtk.expression.model;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class ExecutableExpression {
	private String hash;
	private String parseTree;
	private Date marketTime;
	private String scripName;
	private String status;
	
	protected ExecutableExpression() {}
	
	public ExecutableExpression(String hash, String parseTree, Date marketTime, String scripName, String status) {
		this.hash = hash;
		this.parseTree = parseTree;
		this.marketTime = marketTime;
		this.scripName = scripName;
		this.status = status;
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

	public Date getMarketTime() {
		return marketTime;
	}

	public void setMarketTime(Date marketTime) {
		this.marketTime = marketTime;
	}

	public String getScripName() {
		return scripName;
	}

	public void setScripName(String scripName) {
		this.scripName = scripName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
