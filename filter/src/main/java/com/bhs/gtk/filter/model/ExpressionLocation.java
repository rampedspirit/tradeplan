package com.bhs.gtk.filter.model;

import org.springframework.stereotype.Component;

@Component
public class ExpressionLocation {

	private ExpressionPosition start;
	private ExpressionPosition end;
	
	
	public ExpressionLocation(ExpressionPosition start, ExpressionPosition end) {
		this.start = start;
		this.end = end;
	}
	
	protected ExpressionLocation() {}
	
	public ExpressionPosition getStart() {
		return start;
	}
	public void setStart(ExpressionPosition start) {
		this.start = start;
	}
	public ExpressionPosition getEnd() {
		return end;
	}
	public void setEnd(ExpressionPosition end) {
		this.end = end;
	}
}
