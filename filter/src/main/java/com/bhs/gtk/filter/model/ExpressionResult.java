package com.bhs.gtk.filter.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ExpressionResult {

	private String hash;
	private String status;
	private String type;
	private List<String> locations;

	protected ExpressionResult() {
	}

	public ExpressionResult(String hash, String status, String type) {
		this.hash = hash;
		this.status = status;
		this.type = type;
		this.locations = new ArrayList<>();
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

	public List<String> getLocations() {
		return locations;
	}

	public void addLocation(String location) {
		this.locations.add(location);
	}

}
