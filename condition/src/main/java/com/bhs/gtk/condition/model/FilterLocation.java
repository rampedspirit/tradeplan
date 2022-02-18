package com.bhs.gtk.condition.model;

import org.springframework.stereotype.Component;

@Component
public class FilterLocation {

	private FilterPosition start;
	private FilterPosition end;
	
	
	public FilterLocation(FilterPosition start, FilterPosition end) {
		this.start = start;
		this.end = end;
	}
	
	protected FilterLocation() {}
	
	public FilterPosition getStart() {
		return start;
	}
	public void setStart(FilterPosition start) {
		this.start = start;
	}
	public FilterPosition getEnd() {
		return end;
	}
	public void setEnd(FilterPosition end) {
		this.end = end;
	}
}
