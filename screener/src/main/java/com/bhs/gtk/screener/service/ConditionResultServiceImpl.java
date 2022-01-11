package com.bhs.gtk.screener.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.screener.messaging.ScreenerMessageProducer;
import com.bhs.gtk.screener.model.ScripResult;
import com.bhs.gtk.screener.persistence.ConditionResultEntity;
import com.bhs.gtk.screener.persistence.ConditionResultRepository;
import com.bhs.gtk.screener.util.Converter;

@Service
public class ConditionResultServiceImpl implements ConditionResultService{
	
	@Autowired
	private ScreenerMessageProducer screenerMessageProducer;
	
	@Autowired
	private ConditionResultRepository conditionResultRepository;
	
	@Autowired
	private Converter converter;

	@Override
	public List<ConditionResultEntity> runConditions(List<ConditionResultEntity> conditions) {
		List<ConditionResultEntity> conditionsSuccessfullySentForExecution = new ArrayList<>();
		for(ConditionResultEntity entity : conditions) {
			Map<String, String> entityMap = getEntityMapForJson(entity);
			JSONObject entityAsJson = new JSONObject(entityMap);
			if(screenerMessageProducer.sendMessage(entityAsJson.toString())) {
				entity.setStatus(ScripResult.StatusEnum.RUNNING.name());
				conditionsSuccessfullySentForExecution.add(entity);
			}else {
				System.err.println(" failed to request "+ entityAsJson);
			}
		}
		List<ConditionResultEntity> savedConditionsAsList = new ArrayList<>();
		if(!conditionsSuccessfullySentForExecution.isEmpty()) {
			Iterable<ConditionResultEntity> savedConditions = conditionResultRepository.saveAll(conditionsSuccessfullySentForExecution);
			savedConditions.forEach(e -> savedConditionsAsList.add(e));
		}
		return savedConditionsAsList;
	}

	private Map<String, String> getEntityMapForJson(ConditionResultEntity entity) {
		Map<String, String> entityMap = new HashMap<>();
		entityMap.put("conditionId",entity.getConditionId().toString());
		entityMap.put("marketTime",entity.getMarketTimeAsOffsetDateTime());
		entityMap.put("scripName",entity.getScripName());
		entityMap.put("status",entity.getStatus());
		return entityMap;
	}

	@Override
	public List<ConditionResultEntity> runAllQueuedConditions() {
		List<ConditionResultEntity> conditions = conditionResultRepository.findByStatus(ScripResult.StatusEnum.QUEUED.name());
		return runConditions(conditions);
	}

}
