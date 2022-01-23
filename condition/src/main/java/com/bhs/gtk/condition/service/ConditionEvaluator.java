package com.bhs.gtk.condition.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.condition.model.BooleanExpression;
import com.bhs.gtk.condition.model.ConditionExpression;
import com.bhs.gtk.condition.model.ConditionResultResponse.ConditionResultEnum;
import com.bhs.gtk.condition.model.FilterExpression;
import com.bhs.gtk.condition.model.FilterResult;
import com.bhs.gtk.condition.persistence.ConditionEntity;
import com.bhs.gtk.condition.persistence.ConditionResultEntity;
import com.bhs.gtk.condition.persistence.EntityReader;
import com.bhs.gtk.condition.persistence.FilterResultEntity;
import com.bhs.gtk.condition.util.Converter;

@Component
public class ConditionEvaluator {
	
	@Autowired
	private EntityReader entityReader;
	
	@Autowired
	private Converter converter;
	
	public String evaluateConditionResult(ConditionResultEntity conditionResultEntity) {
		Date marketTime = conditionResultEntity.getMarketTime();
		String scripName =  conditionResultEntity.getScripName();
		UUID conditionId = conditionResultEntity.getConditionId();
		List<String> filterStatuses = conditionResultEntity.getFilterResultEntities().stream().map(f -> f.getStatus())
				.collect(Collectors.toList());
		if(filterStatuses.isEmpty()) {
			return ConditionResultEnum.ERROR.name();
		}
		String conditionStatus;
		if(filterStatuses.contains(FilterResult.StatusEnum.RUNNING.name())) {
			conditionStatus =  ConditionResultEnum.RUNNING.name();
		}else if(filterStatuses.contains(FilterResult.StatusEnum.QUEUED.name())) {
			conditionStatus = ConditionResultEnum.QUEUED.name();
		}else if(filterStatuses.contains(FilterResult.StatusEnum.ERROR.name())) {
			conditionStatus = ConditionResultEnum.ERROR.name();
		}else {
			conditionStatus = evaluateConditionResult(conditionId,marketTime,scripName,filterStatuses);
		}
		return conditionStatus;
	}

	private String evaluateConditionResult(UUID conditionId, Date marketTime, String scripName, List<String> filterStatuses) {
		ConditionEntity conditionEntity = entityReader.getCondition(conditionId);
		String parseTree = conditionEntity.getParseTree();
		ConditionExpression expression = converter.convertToConditionExpression(parseTree);
		return evaluateConditionExpression(marketTime, scripName, expression);
	}

	private String evaluateConditionExpression(Date marketTime, String scripName,ConditionExpression expression) {
		if(expression instanceof FilterExpression) {
			return evaluateFilterExpression(marketTime, scripName, expression);
		}else if(expression instanceof BooleanExpression) {
			return evaluateBooleanExpression(marketTime, scripName, expression);
		}
		throw new IllegalArgumentException(expression+ "is invalid condition expression");
	}

	private String evaluateBooleanExpression(Date marketTime, String scripName, ConditionExpression expression) {
		BooleanExpression booleanExpression = (BooleanExpression) expression;
		String operation = booleanExpression.getOperation();
		List<String> results = new ArrayList<>();
		for(ConditionExpression conditionExpression : booleanExpression.getConditionExpressions()) {
			results.add(evaluateConditionExpression(marketTime, scripName, conditionExpression));
		}
		return evaluateResults(operation,results);
	}

	private String evaluateFilterExpression(Date marketTime, String scripName, ConditionExpression expression) {
		FilterExpression filterExpression = (FilterExpression) expression;
		UUID filterId = UUID.fromString(filterExpression.getFilterId());
		FilterResultEntity filterResult = entityReader.getFilterResultEntity(filterId, marketTime, scripName);
		//TODO: expression`s result is not used, remove it.
		return filterResult.getStatus();
	}

	private String evaluateResults(String operation, List<String> results) {
		//TODO: make operation in boolean expression an enum.
		switch (operation) {
		case "AND": return evaluateANDoperation(results);
		case "OR": return evalutaeORoperation(results);
		default:
			throw new IllegalArgumentException(operation+ " is not a supported operation in conditions");
		}
	}

	private String evalutaeORoperation(List<String> results) {
		if(results.isEmpty()) {
			throw new IllegalArgumentException("invalid results");
		}
		if(results.contains(FilterResult.StatusEnum.PASS.name())) {
			return FilterResult.StatusEnum.PASS.name();
		}
		return FilterResult.StatusEnum.FAIL.name();
	}

	private String evaluateANDoperation(List<String> results) {
		if(results.isEmpty()) {
			throw new IllegalArgumentException("invalid results");
		}
		if(results.contains(FilterResult.StatusEnum.FAIL.name())) {
			return FilterResult.StatusEnum.FAIL.name();
		}
		return FilterResult.StatusEnum.PASS.name();
	}

}
