package com.bhs.gtk.condition.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.condition.model.BooleanExpression;
import com.bhs.gtk.condition.model.ConditionDetailedResponse;
import com.bhs.gtk.condition.model.ConditionExpression;
import com.bhs.gtk.condition.model.ConditionResponse;
import com.bhs.gtk.condition.model.ConditionResultResponse;
import com.bhs.gtk.condition.model.ConditionResultResponse.ConditionResultEnum;
import com.bhs.gtk.condition.model.Filter;
import com.bhs.gtk.condition.model.Filter.StatusEnum;
import com.bhs.gtk.condition.model.FilterExpression;
import com.bhs.gtk.condition.model.FilterLocation;
import com.bhs.gtk.condition.model.FilterPosition;
import com.bhs.gtk.condition.model.FilterResultResponse;
import com.bhs.gtk.condition.model.Location;
import com.bhs.gtk.condition.model.Position;
import com.bhs.gtk.condition.persistence.ConditionEntity;
import com.bhs.gtk.condition.persistence.ConditionResultEntity;
import com.bhs.gtk.condition.persistence.EntityReader;
import com.bhs.gtk.condition.persistence.FilterEntity;
import com.bhs.gtk.condition.persistence.FilterResultEntity;

@Component
public class Mapper {

	// TODO: reconsider autowiring converter and entityReader here. Re-designing
	// classes may help.
	@Autowired
	private Converter converter;

	@Autowired
	private EntityReader entityReader;

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
		conditionResultResponse.setFiltersResult(getFilterResultResponses(conditionResult));
		return conditionResultResponse;
	}

	private List<FilterResultResponse> getFilterResultResponses(ConditionResultEntity conditionResult) {
		ConditionExpression conditionExpression = getConditionExpression(conditionResult);
		Map<String, List<FilterLocation>> filterLocations = getFilterLocations(conditionExpression);
		List<FilterResultResponse> filterResults = new ArrayList<>();
		for (FilterResultEntity filter : conditionResult.getFilterResultEntities()) {
			UUID filterId = filter.getFilterId();
			FilterResultResponse fResult = new FilterResultResponse();
			fResult.setFilterId(filterId);
			fResult.setLocation(getLocationResponses(filterLocations.get(filterId.toString())));
			fResult.setStatus(FilterResultResponse.StatusEnum.fromValue(filter.getStatus()));
			filterResults.add(fResult);
		}
		return filterResults;
	}

	private List<Location> getLocationResponses(List<FilterLocation> filterLocations) {
		List<Location> locationResponses =  new ArrayList<>();
		for (FilterLocation location : filterLocations) {
			locationResponses.add(getLocation(location));
		}
		return locationResponses;
	}

	private Location getLocation(FilterLocation filterLocation) {
		Location location = new Location();
		location.setStart(getPosition(filterLocation.getStart()));
		location.setEnd(getPosition(filterLocation.getEnd()));
		return location;
	}

	private Position getPosition(FilterPosition filterPosition) {
		int offset = filterPosition.getOffset();
		int line = filterPosition.getLine();
		int column = filterPosition.getColumn();
		Position position = new Position();
		position.setOffset(offset);
		position.setLine(line);
		position.setColumn(column);
		return position;
	}

	private Map<String, List<FilterLocation>> getFilterLocations(ConditionExpression expression) {
		Map<String, List<FilterLocation>> filterLocations = new HashMap<>();
		if (expression instanceof FilterExpression) {
			FilterExpression filterExpression = (FilterExpression) expression;
			if (!filterLocations.containsKey(filterExpression.getFilterId())) {
				filterLocations.put(filterExpression.getFilterId(), new ArrayList<>());
			}
			filterLocations.get(filterExpression.getFilterId()).add(filterExpression.getFilterLocation());
		} else if (expression instanceof BooleanExpression) {
			BooleanExpression booleanExpression = (BooleanExpression) expression;
			for (ConditionExpression exp : booleanExpression.getConditionExpressions()) {
				addFilterLocations(filterLocations, getFilterLocations(exp));
			}
		}
		return filterLocations;
	}

	private void addFilterLocations(Map<String, List<FilterLocation>> filterLocations,
			Map<String, List<FilterLocation>> fLocations) {
		for(Entry<String, List<FilterLocation>> entrySet : fLocations.entrySet()) {
			String key = entrySet.getKey();
			List<FilterLocation> value = entrySet.getValue();
			if(filterLocations.containsKey(key)) {
				filterLocations.get(key).addAll(value);
			}else {
				filterLocations.put(key, value);
			}
		}
	}

	private ConditionExpression getConditionExpression(ConditionResultEntity conditionResult) {
		UUID conditionId = conditionResult.getConditionId();
		ConditionEntity conditionEntity = entityReader.getCondition(conditionId);
		return converter.convertToConditionExpression(conditionEntity.getParseTree());
	}

	public List<ConditionResponse> getConditionResponses(List<ConditionEntity> conditionEntities) {
		List<ConditionResponse> responses = new ArrayList<>();
		for (ConditionEntity entity : conditionEntities) {
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
		if (conditionEntity == null || conditionEntity.getId() == null) {
			// throw exception
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
		for (FilterEntity ft : filterEntities) {
			Filter filter = new Filter();
			filter.setFilterId(ft.getId());
			filter.setStatus(StatusEnum.valueOf(ft.getStatus()));
			mappedFilters.add(filter);
		}
		return mappedFilters;
	}

}
