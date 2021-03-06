package com.bhs.gtk.condition.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.condition.messaging.MessageProducer;
import com.bhs.gtk.condition.messaging.MessageType;
import com.bhs.gtk.condition.model.BooleanExpression;
import com.bhs.gtk.condition.model.ConditionExpression;
import com.bhs.gtk.condition.model.ConditionResultResponse;
import com.bhs.gtk.condition.model.ConditionResultResponse.ConditionResultEnum;
import com.bhs.gtk.condition.model.FilterExpression;
import com.bhs.gtk.condition.model.communication.FilterResult;
import com.bhs.gtk.condition.persistence.ConditionEntity;
import com.bhs.gtk.condition.persistence.ConditionResultEntity;
import com.bhs.gtk.condition.persistence.EntityReader;
import com.bhs.gtk.condition.persistence.EntityWriter;
import com.bhs.gtk.condition.persistence.FilterEntity;
import com.bhs.gtk.condition.persistence.FilterResultEntity;
import com.bhs.gtk.condition.util.Converter;
import com.bhs.gtk.condition.util.Mapper;

@Service
public class ResultServiceImpl implements ResultService{
	
	@Autowired
	private EntityReader entityReader;
	
	@Autowired
	private EntityWriter entityWriter;
	
	@Autowired
	private MessageProducer messageProducer;
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
	private Converter converter;
	
	@Override
	public ConditionResultResponse getResult(UUID conditionId, Date marketTime, String scripName) {
		ConditionResultEntity conditionResult = entityReader.getConditionResultEntity(conditionId, marketTime, scripName);
		ConditionEntity condition = entityReader.getCondition(conditionId);
		if(condition == null || conditionResult == null) {
			return null;
		}
		return mapper.getConditionResultResponse(condition, conditionResult);
	}

	@Override
	public boolean updateConditionResult(FilterResult filterResult) {
		if(!updateFilterResultEntity(filterResult)) {
			return false;
		}
		List<ConditionResultEntity> conditionResultEntities = getConditionResultWhichAreReadyToDeriveResult(
				filterResult);
		List<ConditionResultEntity> conditionResults = updateConditionResult(conditionResultEntities);
		if(sendMessage(conditionResults)) {
			return true;
		}
		//TODO: log warn/debug at-least one of the msg is failed to send from CS -> SS
		return false;
	}

	private boolean sendMessage(List<ConditionResultEntity> conditionResults) {
		boolean msgSentStatus = true;
		for(ConditionResultEntity cResult : conditionResults) {
			String message = getConditionExecutionResponseMessage(cResult);
			if(messageProducer.sendMessage(message, MessageType.EXECUTION_RESPONSE)) {
				//TODO: log debug: msg sent successfully from CS -> SS
			}else {
				//TODO: log debug: msg sending failed from CS -> SS
				msgSentStatus = false;
			}
		}
		return msgSentStatus;
	}

	private String getConditionExecutionResponseMessage(ConditionResultEntity cResult) {
		Map<String, String> msgMap = new HashMap<>();
		msgMap.put("conditionId", cResult.getConditionId().toString());
		msgMap.put("marketTime", cResult.getMarketTimeAsOffsetDateTime().toString());
		msgMap.put("scripName", cResult.getScripName());
		msgMap.put("status", cResult.getStatus());
		return new JSONObject(msgMap).toString();
	}

	private List<ConditionResultEntity> updateConditionResult(List<ConditionResultEntity> conditionResultEntities) {
		List<ConditionResultEntity>  conditionResultsToBePersisted = new ArrayList<>();
		for(ConditionResultEntity cResult : conditionResultEntities) {
			if(deriveConditionResult(cResult.getConditionId(),cResult.getFilterResultEntities())) {
				cResult.setStatus(ConditionResultEnum.PASS.name());
			}else {
				cResult.setStatus(ConditionResultEnum.FAIL.name());
			}
			conditionResultsToBePersisted.add(cResult);
		}
		return entityWriter.saveConditionResultEntities(conditionResultsToBePersisted);
	}

	private boolean deriveConditionResult(UUID conditionId, List<FilterResultEntity> filterResultEntities) {
		ConditionEntity conditionEntity = entityReader.getCondition(conditionId);
		String parseTree = conditionEntity.getParseTree();
		ConditionExpression conditionExpression = converter.convertToConditionExpression(parseTree);
		return evaluateConditionExpression(conditionExpression, filterResultEntities);
	}

	private boolean evaluateConditionExpression(ConditionExpression expression, List<FilterResultEntity> filterResultEntities) {
		if(expression instanceof FilterExpression) {
			FilterExpression filterExp = (FilterExpression) expression;
			return getFilterResult(filterExp.getFilterId(), filterResultEntities);
		}else if(expression instanceof BooleanExpression) {
			BooleanExpression booleanExpression = (BooleanExpression) expression;
			String operation = booleanExpression.getOperation();
			List<Boolean> expResults = new ArrayList<>();
			for(ConditionExpression cExp : booleanExpression.getConditionExpressions()) {
				expResults.add(evaluateConditionExpression(cExp, filterResultEntities));
			}
			return deriveConditionResult(operation,expResults);
		}
		throw new IllegalStateException(expression + " is not a valid condition expression");
	}

	private boolean deriveConditionResult(String operation, List<Boolean> expResults) {
		boolean conditionResult = true;
		if(StringUtils.equals(operation, "AND")) {
			if(expResults.contains(Boolean.FALSE)) {
				conditionResult = false;
			}
		}else if(StringUtils.equals(operation, "OR")) {
			if(!expResults.contains(Boolean.TRUE)) {
				conditionResult = false;
			}
		}
		return conditionResult;
	}

	private boolean getFilterResult(String filterId, List<FilterResultEntity> filterResultEntities) {
		List<FilterResultEntity> filterResults = filterResultEntities.stream()
				.filter(ft -> StringUtils.equals(filterId, ft.getFilterId().toString())).collect(Collectors.toList());
		if(filterResults == null || filterResults.isEmpty() || filterResults.size() > 1) {
			throw new IllegalStateException(
					"filterId : " + filterId + " Filter result is available more than once for given condition");
		}
		String status = filterResults.get(0).getStatus();
		if(StringUtils.equals(status, "PASS")) {
			return true;
		}else if(StringUtils.equals(status, "FAIL")) {
			return false;
		}
		throw new IllegalStateException(
				"filterId : " + filterId + " Filter result status = " + status + " is neither PASS or FAIL");
	}

	private List<ConditionResultEntity> getConditionResultWhichAreReadyToDeriveResult(FilterResult filterResult) {
		UUID filterId = filterResult.getFilterId();
		FilterEntity filterEntity = entityReader.getFilterEntity(filterId);
		if(filterEntity == null) {
			return new ArrayList<>();
		}
		List<UUID> conditionIds = filterEntity.getConditions().stream().map( c -> c.getId()).collect(Collectors.toList());
		String scripName = filterResult.getScripName();
		Date marketTime = filterResult.getMarketTime();
		List<ConditionResultEntity> conditionResults = entityReader.getConditionResultEntity(conditionIds, scripName,
				marketTime);
		return getConditionResultWhichAreReadyToDeriveResult(conditionResults);
	}

	private List<ConditionResultEntity> getConditionResultWhichAreReadyToDeriveResult(
			List<ConditionResultEntity> conditionResults) {
		List<ConditionResultEntity> conditionResultEntities = new ArrayList<>();
		for(ConditionResultEntity cResult : conditionResults) {
			if(isConditionReadyToDeriveResult(cResult)) {
				conditionResultEntities.add(cResult);
			}
		}
		return conditionResultEntities;
	}

	private boolean isConditionReadyToDeriveResult(ConditionResultEntity cResult) {
		for(FilterResultEntity fResult : cResult.getFilterResultEntities()) {
			//TODO: fix it before merge... use enum from generated FilterResult instead of string literals.
			String status = fResult.getStatus();
			if (StringUtils.equals("QUEUED", status) || StringUtils.equals("RUNNING", status)) {
				return false;
			}
		}
		return true;
	}

	private boolean updateFilterResultEntity(FilterResult filterResult) {
		UUID filterId = filterResult.getFilterId();
		Date marketTime = filterResult.getMarketTime();
		String scripName = filterResult.getScripName();
		String status = filterResult.getStatus();
		FilterResultEntity fResultEntity = entityReader.getFilterResultEntity(filterId, marketTime, scripName);
		if(fResultEntity == null) {
			//log debug: no such filter result found to update.
			return false;
		}
		//TODO: save only when there is change in status.
		fResultEntity.setStatus(status);
		entityWriter.saveFilterResultEntity(fResultEntity);
		return true;
	}


}
