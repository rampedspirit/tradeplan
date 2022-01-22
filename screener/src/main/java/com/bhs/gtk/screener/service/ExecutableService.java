package com.bhs.gtk.screener.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.bhs.gtk.screener.model.ExecutableDetailedResponse;
import com.bhs.gtk.screener.model.ExecutablePatchData;
import com.bhs.gtk.screener.model.ExecutableResponse;
import com.bhs.gtk.screener.persistence.ConditionResultEntity;
import com.bhs.gtk.screener.persistence.ExecutableEntity;

public interface ExecutableService {
	public ExecutableResponse getExecutable(UUID executableId);

	public ExecutableDetailedResponse getResult(UUID executableId);
	
	public ExecutableEntity getExecutable(UUID conditionId, Date marketTime,UUID watchlistId);

	public ExecutableResponse updateExecutable(ExecutablePatchData executablePatchData, UUID executableId);

	public boolean updateStatusOfExecutables(List<ExecutableEntity> executableEntites);
	
	public ExecutableEntity updateStatusOfExecutable(ExecutableEntity executableEntity);
	
	public  boolean updateStatusOfExecutablesBasedOnConditions(List<ConditionResultEntity> conditionsWithChangedStatus);
	
}
