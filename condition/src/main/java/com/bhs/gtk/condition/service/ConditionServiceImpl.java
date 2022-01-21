package com.bhs.gtk.condition.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.condition.messaging.ChangeNotification.ChangeStatusEnum;
import com.bhs.gtk.condition.messaging.MessageProducer;
import com.bhs.gtk.condition.model.ConditionDetailedResponse;
import com.bhs.gtk.condition.model.ConditionRequest;
import com.bhs.gtk.condition.model.ConditionResponse;
import com.bhs.gtk.condition.model.PatchData;
import com.bhs.gtk.condition.model.PatchData.PropertyEnum;
import com.bhs.gtk.condition.persistence.ConditionEntity;
import com.bhs.gtk.condition.persistence.EntityReader;
import com.bhs.gtk.condition.persistence.EntityWriter;
import com.bhs.gtk.condition.persistence.FilterEntity;
import com.bhs.gtk.condition.util.Mapper;

@Service
public class ConditionServiceImpl implements ConditionService{
	
	@Autowired
	private EntityWriter entityWriter;
	
	@Autowired
	private EntityReader entityReader;
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
	private MessageProducer messageProducer;
	
	@Override
	public ConditionDetailedResponse createCondition(ConditionRequest condition) {
		ConditionEntity conditionEntity = entityWriter.createConditionEntity(condition);
		return mapper.getConditionDetailedResponse(conditionEntity);
	}
	
	@Override
	public List<ConditionResponse> getAllConditions() {
		List<ConditionEntity> conditionEntities = entityReader.getAllConditions();
		return mapper.getConditionResponses(conditionEntities);
	}

	@Override
	public ConditionDetailedResponse getCondition(UUID conditionId) {
		return mapper.getConditionDetailedResponse(entityReader.getCondition(conditionId));
	}
	
	@Override
	public ConditionDetailedResponse deleteCondition(UUID id) {
		entityWriter.deleteConditionResultEntity(id);
		ConditionEntity deletedCondition = entityWriter.deleteCondition(id);
		if(deletedCondition != null) {
			messageProducer.sendChangeNotification(deletedCondition.getId(), ChangeStatusEnum.DELETED);
			return mapper.getConditionDetailedResponse(deletedCondition);
		}
		return null;
	}
	
	@Override
	public ConditionDetailedResponse updateCondition(List<PatchData> patchData, UUID conditionId) {
		if(patchData == null || conditionId == null) {
			//throw exception if validation fails
			return null;
		}
		if(!isValidatePatchData(patchData)) {
			return null;
		}
		ConditionEntity conditionEntity = entityReader.getCondition(conditionId);
		boolean logicChanged = false;
		for (PatchData pd : patchData) {
			@NotNull
			String value = pd.getValue();
			switch (pd.getProperty()) {
			case NAME:
				conditionEntity.setName(value);
				break;
			case DESCRIPTION:
				conditionEntity.setDescription(value);
				break;
			case CODE:
				logicChanged = true;
				conditionEntity.setCode(value);
				break;
			case PARSE_TREE:
				logicChanged = true;
				conditionEntity.setParseTree(value);
				break;
			}
		}
		if(logicChanged) {
			if(!messageProducer.sendChangeNotification(conditionEntity.getId(), ChangeStatusEnum.UPDATED)) {
				//not able to notify change in logic and hence logic change is aborted.
				//TODO: find better solution for this use-case.
				return null;
			}
		}
		ConditionEntity changedConditionEntity;
		if(logicChanged) {
			changedConditionEntity = saveConditionEntity(conditionEntity);

		}else {
			changedConditionEntity = entityWriter.saveConditionEntity(conditionEntity);
		}
		return mapper.getConditionDetailedResponse(changedConditionEntity);
	}

	private ConditionEntity saveConditionEntity(ConditionEntity conditionEntity) {
		List<FilterEntity> filters = entityWriter.createFilterEntityObjects(conditionEntity.getParseTree());
		conditionEntity.setFilters(filters);
		ConditionEntity savedConditionEntity = entityWriter.saveConditionEntity(conditionEntity);
		entityWriter.removePreviousLogicRelatedAssociations(conditionEntity);
		return savedConditionEntity;
		
	}

	private boolean isValidatePatchData(List<PatchData> patchData) {
		if(patchData == null || patchData.isEmpty()) {
			return false;
		}
		List<@NotNull PropertyEnum> properties = patchData.stream().map(p -> p.getProperty())
				.collect(Collectors.toList());
		// ^ is XOR operation. 
		return !(properties.contains(PropertyEnum.CODE) ^ properties.contains(PropertyEnum.PARSE_TREE));
	}

}
