package com.bhs.gtk.condition.model;

import org.springframework.stereotype.Component;

@Component
public class FilterExpression extends ConditionExpression{

	private String filterId;

	protected FilterExpression() {}
	
	public FilterExpression(String filterId) {
		this.filterId = filterId;
	}
	
	public String getFilterId() {
		return filterId;
	}

	public void setFilterId(String filterId) {
		this.filterId = filterId;
	}
	
}
