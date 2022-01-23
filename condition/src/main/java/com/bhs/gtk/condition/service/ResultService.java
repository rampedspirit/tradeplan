package com.bhs.gtk.condition.service;

import java.util.Date;
import java.util.UUID;

import com.bhs.gtk.condition.model.ConditionResultResponse;

public interface ResultService {
	public ConditionResultResponse getResult(UUID conditionId, Date marketTime, String scripName);
}
