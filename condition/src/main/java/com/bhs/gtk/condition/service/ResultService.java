package com.bhs.gtk.condition.service;

import java.util.Date;
import java.util.UUID;

import com.bhs.gtk.condition.model.ConditionResultResponse;
import com.bhs.gtk.condition.model.communication.FilterResult;

public interface ResultService {
	public ConditionResultResponse getResult(UUID conditionId, Date marketTime, String scripName);
	public boolean updateConditionResult(FilterResult filterResult);
}
