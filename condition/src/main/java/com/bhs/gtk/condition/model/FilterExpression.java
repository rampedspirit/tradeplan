package com.bhs.gtk.condition.model;

import org.springframework.stereotype.Component;

@Component
public class FilterExpression extends ConditionExpression{

	private String filterId;
	
	private FilterLocation filterLocation;

	protected FilterExpression() {}
	
	public FilterExpression(String filterId, FilterLocation filterLocation) {
		this.filterId = filterId;
		this.setFilterLocation(filterLocation);
	}
	
	public String getFilterId() {
		return filterId;
	}

	public void setFilterId(String filterId) {
		this.filterId = filterId;
	}

	public FilterLocation getFilterLocation() {
		return filterLocation;
	}

	public void setFilterLocation(FilterLocation filterLocation) {
		this.filterLocation = filterLocation;
	}
	
}
