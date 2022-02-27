package com.bhs.gtk.filter.model.communication;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class ArithmeticExpressionResult {
	
	private String hash;
	private String scripName;
	private Date marketTime;
	private String status;
	
	protected ArithmeticExpressionResult() {}
	
	public ArithmeticExpressionResult(String hash, String scripName, Date marketTime, String status) {
		this.hash = hash;
		this.scripName = scripName;
		this.marketTime = marketTime;
		this.status = status;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getScripName() {
		return scripName;
	}

	public void setScripName(String scripName) {
		this.scripName = scripName;
	}

	public Date getMarketTime() {
		return marketTime;
	}

	public void setMarketTime(Date marketTime) {
		this.marketTime = marketTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
