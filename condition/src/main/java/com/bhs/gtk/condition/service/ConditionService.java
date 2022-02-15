package com.bhs.gtk.condition.service;

import java.util.List;
import java.util.UUID;

import com.bhs.gtk.condition.messaging.ChangeNotification;
import com.bhs.gtk.condition.model.ConditionDetailedResponse;
import com.bhs.gtk.condition.model.ConditionRequest;
import com.bhs.gtk.condition.model.ConditionResponse;
import com.bhs.gtk.condition.model.PatchData;

public interface ConditionService {
	public ConditionDetailedResponse createCondition(ConditionRequest condition);
	public List<ConditionResponse> getAllConditions();
	public ConditionDetailedResponse getCondition(UUID conditionId);
	public ConditionDetailedResponse deleteCondition(UUID id);
	public ConditionDetailedResponse updateCondition(List<PatchData> patchData, UUID conditionId);
	public boolean adaptChangeInFilter(ChangeNotification changeNotification);
}
