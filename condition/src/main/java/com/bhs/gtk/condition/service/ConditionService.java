package com.bhs.gtk.condition.service;

import java.util.List;

import com.bhs.gtk.condition.model.ConditionDetailedResponse;
import com.bhs.gtk.condition.model.ConditionRequest;
import com.bhs.gtk.condition.model.ConditionResponse;

public interface ConditionService {
	public ConditionDetailedResponse createCondition(ConditionRequest condition);
	public List<ConditionResponse> getAllConditions();
//	public List<Condition> getAllConditions();
//	public Condition getCondition(UUID id);
//	public Condition deleteCondition(UUID id);
//	public Condition updateCondition(PatchModel patchModel, UUID id);
}
