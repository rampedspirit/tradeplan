package com.bhs.gtk.screener.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.screener.messaging.ScreenerMessageProducer;
import com.bhs.gtk.screener.model.ScripResult;
import com.bhs.gtk.screener.persistence.ConditionResultEntity;
import com.bhs.gtk.screener.persistence.ConditionResultRepository;

@Service
public class ConditionResultServiceImpl implements ConditionResultService{
	
	@Autowired
	private ScreenerMessageProducer screenerMessageProducer;
	
	@Autowired
	private ConditionResultRepository conditionResultRepository;

	@Override
	public List<ConditionResultEntity> runConditions(List<ConditionResultEntity> conditions) {
		List<ConditionResultEntity> conditionsSuccessfullySentForExecution = new ArrayList<>();
		for(ConditionResultEntity entity : conditions) {
			JSONObject entityAsJson = new JSONObject(entity);
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

	@Override
	public List<ConditionResultEntity> runAllQueuedConditions() {
		List<ConditionResultEntity> conditions = conditionResultRepository.findByStatus(ScripResult.StatusEnum.QUEUED.name());
		return runConditions(conditions);
	}

}
