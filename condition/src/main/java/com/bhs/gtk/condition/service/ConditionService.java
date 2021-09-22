package com.bhs.gtk.condition.service;

import java.util.List;
import java.util.UUID;

import com.bhs.gtk.condition.model.Condition;
import com.bhs.gtk.condition.model.PatchModel;

public interface ConditionService {
	public Condition createCondition(Condition condition);
	public List<Condition> getAllConditions();
	public Condition getCondition(UUID id);
	public Condition deleteCondition(UUID id);
	public Condition updateCondition(PatchModel patchModel, UUID id);
}
