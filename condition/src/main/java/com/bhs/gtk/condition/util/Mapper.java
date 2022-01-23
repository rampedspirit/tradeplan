package com.bhs.gtk.condition.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bhs.gtk.condition.model.ConditionDetailedResponse;
import com.bhs.gtk.condition.model.ConditionResponse;
import com.bhs.gtk.condition.model.ConditionResultResponse;
import com.bhs.gtk.condition.model.Filter;
import com.bhs.gtk.condition.model.Filter.StatusEnum;
import com.bhs.gtk.condition.model.FilterResult;
import com.bhs.gtk.condition.model.ConditionResultResponse.ConditionResultEnum;
import com.bhs.gtk.condition.persistence.ConditionEntity;
import com.bhs.gtk.condition.persistence.ConditionResultEntity;
import com.bhs.gtk.condition.persistence.FilterEntity;
import com.bhs.gtk.condition.persistence.FilterResultEntity;

@Component
public class Mapper {
	
	public ConditionResultResponse getConditionResultResponse(ConditionEntity condition,
			ConditionResultEntity conditionResult) {
		ConditionResultResponse conditionResultResponse = new ConditionResultResponse();
		conditionResultResponse.setId(condition.getId());
		conditionResultResponse.setName(condition.getName());
		conditionResultResponse.setScripName(conditionResult.getScripName());
		conditionResultResponse.setDescription(condition.getDescription());
		conditionResultResponse.setCode(condition.getCode());
		conditionResultResponse.setConditionResult(ConditionResultEnum.valueOf(conditionResult.getStatus()));
		conditionResultResponse.setMarketTime(conditionResult.getMarketTimeAsOffsetDateTime());
		conditionResultResponse.setFiltersResult(getFilterResults(conditionResult));
		return conditionResultResponse;
	}

	private List<FilterResult> getFilterResults(ConditionResultEntity conditionResult) {
		List<FilterResult> filterResults = new ArrayList<>();
		List<FilterResultEntity> filterResultEntities = conditionResult.getFilterResultEntities();
		for(FilterResultEntity filter : filterResultEntities) {
			FilterResult fResult = new FilterResult();
			fResult.setFilterId(filter.getFilterId());
			fResult.setStatus(com.bhs.gtk.condition.model.FilterResult.StatusEnum.fromValue(filter.getStatus()));
			filterResults.add(fResult);
		}
		return filterResults;
	}
	
	public List<ConditionResponse> getConditionResponses(List<ConditionEntity> conditionEntities) {
		List<ConditionResponse> responses = new ArrayList<>();
		for(ConditionEntity entity : conditionEntities) {
			responses.add(getConditionResponse(entity));
		}
		return responses;
	}
	
	private ConditionResponse getConditionResponse(ConditionEntity entity) {
		ConditionResponse response = new ConditionResponse();
		response.setName(entity.getName());
		response.setId(entity.getId());
		response.setDescription(entity.getDescription());
		return response;
	}
	
	public ConditionDetailedResponse getConditionDetailedResponse(ConditionEntity conditionEntity) {
		ConditionDetailedResponse condition = new ConditionDetailedResponse();
		if(conditionEntity == null || conditionEntity.getId() == null) {
			//throw exception
		}
		condition.setId(conditionEntity.getId());
		condition.setName(conditionEntity.getName());
		condition.setDescription(conditionEntity.getDescription());
		condition.setCode(conditionEntity.getCode());
		List<Filter> filters = getFilters(conditionEntity.getFilters());
		condition.setFilters(filters);
		return condition;
	}

	private List<Filter> getFilters(List<FilterEntity> filterEntities) {
		List<Filter> mappedFilters = new ArrayList<>();
		for(FilterEntity ft : filterEntities) {
			Filter filter = new Filter();
			filter.setFilterId(ft.getId());
			filter.setStatus(StatusEnum.valueOf(ft.getStatus()));
			mappedFilters.add(filter);
		}
		return mappedFilters;
	}
	
}
