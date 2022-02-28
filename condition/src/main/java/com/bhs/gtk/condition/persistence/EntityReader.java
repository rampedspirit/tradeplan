package com.bhs.gtk.condition.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityReader {

	@Autowired
	private FilterRespository filterRespository;
	
	@Autowired 
	private ConditionRepository conditionRepository;
	
	@Autowired
	private FilterResultRepository filterResultRepository;
	
	@Autowired
	private ConditionResultRepository conditionResultRepository;
	
	public FilterEntity getFilterEntity(UUID id) {
		Optional<FilterEntity> entityContainer = filterRespository.findById(id);
		if(entityContainer.isPresent()) {
			return entityContainer.get();
		}
		return null;
	}
	
	public List<ConditionEntity> getAllConditions() {
		List<ConditionEntity> conditions = new ArrayList<>();
		conditionRepository.findAll().forEach( c -> conditions.add(c));
		return conditions;
	}
	
	public ConditionEntity getCondition(UUID id) {
		Optional<ConditionEntity> entityContainer = conditionRepository.findById(id);
		if(entityContainer.isPresent()) {
			return entityContainer.get();
		}
		return null;
	}

	public FilterResultEntity getFilterResultEntity(UUID id, Date marketTime, String scripName) {
		FilterResultId filterResultId = new FilterResultId(id, marketTime, scripName);
		Optional<FilterResultEntity> entityContainer = filterResultRepository.findById(filterResultId);
		if(entityContainer.isPresent()) {
			return entityContainer.get();
		}
		return null;
	}
	
	public List<ConditionResultEntity> getConditionResultEntity(List<UUID> conditionIds, String scripName,
			Date marketTime) {
		List<ConditionResultEntity> conditionResultEntities = new ArrayList<>();
		List<ConditionResultId> conditionResultIds = new ArrayList<>();
		for (UUID id : conditionIds) {
			conditionResultIds.add(new ConditionResultId(id, marketTime, scripName));
		}
		Iterable<ConditionResultEntity> cResults = conditionResultRepository.findAllById(conditionResultIds);
		for (ConditionResultEntity cr : cResults) {
			conditionResultEntities.add(cr);
		}
		return conditionResultEntities;
	}

	public ConditionResultEntity getConditionResultEntity(UUID id, Date marketTime, String scripName) {
		ConditionResultId conditionResultId = new ConditionResultId(id, marketTime, scripName);
		Optional<ConditionResultEntity> entityContainer = conditionResultRepository.findById(conditionResultId);
		if (entityContainer.isPresent()) {
			return entityContainer.get();
		}
		return null;
	}
}
