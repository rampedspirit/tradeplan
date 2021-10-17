package com.bhs.gtk.expression.service;

import com.bhs.gtk.expression.model.Expression;
import com.bhs.gtk.expression.model.EvaluationResponse;

public interface EvaluationService {
	public Expression convert(String evalRequest);
	public EvaluationResponse evaluate(String evalRequest);
}
