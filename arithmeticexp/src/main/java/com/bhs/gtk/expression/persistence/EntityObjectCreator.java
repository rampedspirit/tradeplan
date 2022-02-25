package com.bhs.gtk.expression.persistence;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class EntityObjectCreator {
	
	public ExpressionResultId createExpressionResultIdObject(String hash, Date marketTime, String scripName) {
		return new ExpressionResultId(hash, marketTime, scripName);
	}

}
