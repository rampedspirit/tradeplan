package com.bhs.gtk.screener.service;

import java.util.List;

import com.bhs.gtk.screener.persistence.ConditionResultEntity;

public interface ConditionResultService {
	public List<ConditionResultEntity> runConditions(List<ConditionResultEntity> conditions);
	public List<ConditionResultEntity> runAllQueuedConditions();
}
