package com.bhs.gtk.condition.service;

import com.bhs.gtk.condition.model.ConditionDetailedResponse;
import com.bhs.gtk.condition.model.ConditionRequest;

public interface ConditionService {
	public ConditionDetailedResponse createCondition(ConditionRequest condition);
//	public List<Condition> getAllConditions();
//	public Condition getCondition(UUID id);
//	public Condition deleteCondition(UUID id);
//	public Condition updateCondition(PatchModel patchModel, UUID id);
}
