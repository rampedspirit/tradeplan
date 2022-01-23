package com.bhs.gtk.condition.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.condition.model.ConditionResultResponse;
import com.bhs.gtk.condition.persistence.ConditionEntity;
import com.bhs.gtk.condition.persistence.ConditionResultEntity;
import com.bhs.gtk.condition.persistence.EntityReader;
import com.bhs.gtk.condition.util.Mapper;

@Service
public class ResultServiceImpl implements ResultService{

	
	@Autowired
	private EntityReader entityReader;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	public ConditionResultResponse getResult(UUID conditionId, Date marketTime, String scripName) {
		ConditionResultEntity conditionResult = entityReader.getConditionResultEntity(conditionId, marketTime, scripName);
		ConditionEntity condition = entityReader.getCondition(conditionId);
		if(condition == null || conditionResult == null) {
			return null;
		}
		return mapper.getConditionResultResponse(condition, conditionResult);
	}


}
