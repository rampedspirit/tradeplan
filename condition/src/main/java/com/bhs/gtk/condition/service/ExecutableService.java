package com.bhs.gtk.condition.service;

import java.util.List;

import com.bhs.gtk.condition.model.ExecutableCondition;
import com.bhs.gtk.condition.persistence.FilterResultEntity;

public interface ExecutableService {
	public boolean RunCondition(ExecutableCondition executableCondition);
	public List<FilterResultEntity> runFilters(List<FilterResultEntity> queuedFilters);
}
