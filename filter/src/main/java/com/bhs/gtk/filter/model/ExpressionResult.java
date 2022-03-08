package com.bhs.gtk.filter.model;

import org.springframework.stereotype.Component;

@Component
public class ExpressionResult {

	private String hash;
	private String status;
	private String type;

	protected ExpressionResult() {
	}

	public ExpressionResult(String hash, String status, String type) {
		this.hash = hash;
		this.status = status;
		this.type = type;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
