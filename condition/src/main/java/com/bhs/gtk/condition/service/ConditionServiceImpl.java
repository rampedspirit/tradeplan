package com.bhs.gtk.condition.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.condition.model.Condition;
import com.bhs.gtk.condition.model.PatchModel;
import com.bhs.gtk.condition.persistence.ConditionEntity;
import com.bhs.gtk.condition.persistence.ConditionRepository;
import com.bhs.gtk.condition.util.Mapper;
import com.bhs.gtk.condition.util.UpdateHelper;

@Service
public class ConditionServiceImpl implements ConditionService{

	@Autowired
	private ConditionRepository conditionRepository;
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
	private UpdateHelper updateHelper;
	
	@Override
	public Condition createCondition(Condition condition) {
		ConditionEntity conditionEntity = conditionRepository.save(mapper.getConditionEntityToPersists(condition));
		return mapper.getCondition(conditionEntity);
	}

	@Override
	public List<Condition> getAllConditions() {
		Iterator<ConditionEntity> iterator = conditionRepository.findAll().iterator();
		List<Condition> condition = new ArrayList<>();
		while(iterator.hasNext()) {
			condition.add(mapper.getCondition(iterator.next()));
		}
		return condition;
	}

	@Override
	public Condition getCondition(UUID id) {
		if(id == null) {
			//throw exception
			return null;
		}
		Optional<ConditionEntity> conditionEntityContainer = conditionRepository.findById(id);
		if(conditionEntityContainer.isPresent()) {
			return mapper.getCondition(conditionEntityContainer.get());
		}
		//throw exception condition not found
		return null;
	}

	@Override
	public Condition deleteCondition(UUID id) {
		if(id == null) {
			//throw exception
			return null;
		}
		if(conditionRepository.existsById(id)) {
			ConditionEntity conditionEntity = conditionRepository.findById(id).get();
			conditionRepository.delete(conditionEntity);
			return mapper.getCondition(conditionEntity);
		}
		//throw exception
		return null;
	}

	@Override
	public Condition updateCondition(PatchModel patchModel, UUID id) {
		if(patchModel == null || id == null) {
			//throw exception if validation fails
			return null;
		}
		Optional<ConditionEntity> persistedConditionContainer = conditionRepository.findById(id);
 		if(persistedConditionContainer.isPresent()) {
 			ConditionEntity conditionTobeUpdated = persistedConditionContainer.get();
 			ConditionEntity updatedFilterEntity = updateHelper.getUpdatedConditionEntity(patchModel,conditionTobeUpdated);
 			ConditionEntity savedFilterEntity = conditionRepository.save(updatedFilterEntity);
 			return mapper.getCondition(savedFilterEntity);
 		}
 		//throw filter not found exception.
		return null;
	}

}
