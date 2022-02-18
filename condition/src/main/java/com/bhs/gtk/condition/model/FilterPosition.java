package com.bhs.gtk.condition.model;

import org.springframework.stereotype.Component;

@Component
public class FilterPosition {

	private int offset;
	private int line;
	private int column;
	
	
	protected FilterPosition() {	}
	
	public FilterPosition(int offset, int line, int column) {
		this.offset = offset;
		this.line = line;
		this.column = column;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getLine() {
		return line;
	}
	
	public void setLine(int line) {
		this.line = line;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}
}
