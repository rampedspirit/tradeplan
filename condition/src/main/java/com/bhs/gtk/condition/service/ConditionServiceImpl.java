package com.bhs.gtk.condition.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.condition.model.ConditionDetailedResponse;
import com.bhs.gtk.condition.model.ConditionRequest;
import com.bhs.gtk.condition.model.ConditionResponse;
import com.bhs.gtk.condition.persistence.ConditionEntity;
import com.bhs.gtk.condition.persistence.EntityReader;
import com.bhs.gtk.condition.persistence.EntityWriter;
import com.bhs.gtk.condition.util.Mapper;

@Service
public class ConditionServiceImpl implements ConditionService{

	@Autowired
	private EntityWriter entityWriter;
	
	@Autowired
	private EntityReader entityReader;
	
	@Autowired
	private Mapper mapper;
	
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
			return mapper.getConditionDetailedResponse(deletedCondition);
		}
		return null;
	}
	
//
//	@Override
//	public Condition deleteCondition(UUID id) {
//		if(id == null) {
//			//throw exception
//			return null;
//		}
//		if(conditionRepository.existsById(id)) {
//			ConditionEntity conditionEntity = conditionRepository.findById(id).get();
//			conditionRepository.delete(conditionEntity);
//			return mapper.getCondition(conditionEntity);
//		}
//		//throw exception
//		return null;
//	}
//
//	@Override
//	public Condition updateCondition(PatchModel patchModel, UUID id) {
//		if(patchModel == null || id == null) {
//			//throw exception if validation fails
//			return null;
//		}
//		Optional<ConditionEntity> persistedConditionContainer = conditionRepository.findById(id);
// 		if(persistedConditionContainer.isPresent()) {
// 			ConditionEntity conditionTobeUpdated = persistedConditionContainer.get();
// 			ConditionEntity updatedFilterEntity = updateHelper.getUpdatedConditionEntity(patchModel,conditionTobeUpdated);
// 			ConditionEntity savedFilterEntity = conditionRepository.save(updatedFilterEntity);
// 			return mapper.getCondition(savedFilterEntity);
// 		}
// 		//throw filter not found exception.
//		return null;
//	}

}
