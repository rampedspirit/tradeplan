package com.bhs.gtk.expression.service;

import com.bhs.gtk.expression.model.EvaluationResponse;

public interface EvaluationService {
	public EvaluationResponse evaluate(String evalRequest);
}
