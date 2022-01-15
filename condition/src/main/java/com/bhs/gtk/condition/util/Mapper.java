package com.bhs.gtk.condition.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bhs.gtk.condition.model.ConditionDetailedResponse;
import com.bhs.gtk.condition.model.ConditionResponse;
import com.bhs.gtk.condition.model.Filter;
import com.bhs.gtk.condition.model.Filter.StatusEnum;
import com.bhs.gtk.condition.persistence.ConditionEntity;
import com.bhs.gtk.condition.persistence.FilterEntity;

@Component
public class Mapper {

	
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
