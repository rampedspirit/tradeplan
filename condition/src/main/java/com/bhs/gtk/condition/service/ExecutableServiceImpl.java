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
import com.bhs.gtk.condition.model.ConditionResultResponse.ConditionResultEnum;
import com.bhs.gtk.condition.model.ExecutableCondition;
import com.bhs.gtk.condition.model.FilterResult;
import com.bhs.gtk.condition.persistence.ConditionEntity;
import com.bhs.gtk.condition.persistence.ConditionResultEntity;
import com.bhs.gtk.condition.persistence.EntityReader;
import com.bhs.gtk.condition.persistence.EntityWriter;
import com.bhs.gtk.condition.persistence.FilterResultEntity;

@Service
public class ExecutableServiceImpl implements ExecutableService{
	
	@Autowired
	private MessageProducer messageProducer;
	
	@Autowired
	private ConditionEvaluator conditionEvaluator;
	
	@Autowired
	private EntityReader entityReader;
	
	@Autowired
	private EntityWriter entityWriter;

	@Override
	public boolean RunCondition(ExecutableCondition executableCondition) {
		UUID conditionId = executableCondition.getConditionId();
		Date marketTime = executableCondition.getMarketTime();
		String scripName = executableCondition.getScripName();
		ConditionResultEntity conditionResultEntity = queueConditionExecution(conditionId,marketTime,scripName);
		return runCondition(conditionId,conditionResultEntity);
	}

	private boolean runCondition(UUID conditionId, ConditionResultEntity conditionResultEntity) {
		String status = conditionResultEntity.getStatus();
		if(StringUtils.equals(status, ConditionResultEnum.RUNNING.name())) {
			return true;
		}
		if(StringUtils.equals(status, ConditionResultEnum.QUEUED.name())) {
			runFilters(conditionResultEntity.getFilterResultEntities());
			updateStatusAndSendMessage(conditionResultEntity);
		}
		return true;
	}

	private boolean updateStatusAndSendMessage(ConditionResultEntity conditionResultEntity) {
		String oldStatus = conditionResultEntity.getStatus();
		String evaluatedStatus = conditionEvaluator.evaluateConditionResult(conditionResultEntity);
		if(StringUtils.equals(oldStatus, evaluatedStatus)) {
			return false;
		}
		ConditionResultEntity savedConditionResultEntity = entityWriter.updateStatus(evaluatedStatus,conditionResultEntity);
		sendExecutionResponse(savedConditionResultEntity);
		return true;
	}

	private boolean sendExecutionResponse(ConditionResultEntity conditionResultEntity) {
		String status = conditionResultEntity.getStatus();
		if(StringUtils.equals(status, ConditionResultEnum.QUEUED.name()) || StringUtils.equals(status, ConditionResultEnum.RUNNING.name())   ) {
			//return false;
		}
		Map<String, String> entityMap = getEntityMapForJson(conditionResultEntity);
		JSONObject entityAsJson = new JSONObject(entityMap);
		return messageProducer.sendMessage(entityAsJson.toString(), MessageType.EXECUTION_RESPONSE);
	}

	@Override
	public List<FilterResultEntity> runFilters(List<FilterResultEntity> queuedFilters) {
		List<FilterResultEntity> filtersSuccessfullySentForExecution = new ArrayList<>();
		for( FilterResultEntity filter : queuedFilters) {
			Map<String, String> entityMap = getEntityMapForJson(filter);
			JSONObject entityAsJson = new JSONObject(entityMap);
			if(messageProducer.sendMessage(entityAsJson.toString(), MessageType.EXECUTION_REQUEST)) {
				filter.setStatus(FilterResult.StatusEnum.RUNNING.name());
				filtersSuccessfullySentForExecution.add(filter);
			}else {
				System.err.println(" failed to request "+ entityAsJson);
			}	
		}
		return entityWriter.saveFilterResultEntities(filtersSuccessfullySentForExecution);
	}

	
	private Map<String, String> getEntityMapForJson(ConditionResultEntity entity) {
		Map<String, String> entityMap = new HashMap<>();
		entityMap.put("conditionId",entity.getConditionId().toString());
		entityMap.put("marketTime",entity.getMarketTimeAsOffsetDateTime().toString());
		entityMap.put("scripName",entity.getScripName());
		entityMap.put("status",entity.getStatus());
		return entityMap;
	}
	
	private Map<String, String> getEntityMapForJson(FilterResultEntity entity) {
		Map<String, String> entityMap = new HashMap<>();
		entityMap.put("filterId",entity.getFilterId().toString());
		entityMap.put("marketTime",entity.getMarketTimeAsOffsetDateTime());
		entityMap.put("scripName",entity.getScripName());
		entityMap.put("status",entity.getStatus());
		return entityMap;
	}
	
	private ConditionResultEntity queueConditionExecution(UUID conditionId, Date marketTime, String scripName) {
		ConditionEntity conditionEntity = entityReader.getCondition(conditionId);
		if (conditionEntity == null) {
			return null;
		}
		List<UUID> filtersUsedInConditions = conditionEntity.getFilters().stream().map(e -> e.getId())
				.collect(Collectors.toList());
		List<FilterResultEntity> filterResultEntities = entityWriter.createFilterResultEntities(filtersUsedInConditions,
				marketTime, scripName);
		return entityWriter.createConditionResultEntity(conditionId, marketTime, scripName, filterResultEntities);
	}


}
