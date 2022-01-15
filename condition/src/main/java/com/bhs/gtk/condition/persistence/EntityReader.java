package com.bhs.gtk.condition.persistence;

import java.util.Date;
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
	
	public FilterEntity getFilterInRepository(UUID id) {
		Optional<FilterEntity> entityContainer = filterRespository.findById(id);
		if(entityContainer.isPresent()) {
			return entityContainer.get();
		}
		return null;
	}
	
	public ConditionEntity getConditionInRepository(UUID id) {
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
	
	public ConditionResultEntity getConditionResultEntity(UUID id, Date marketTime, String scripName) {
		ConditionResultId conditionResultId = new ConditionResultId(id, marketTime, scripName);
		Optional<ConditionResultEntity> entityContainer = conditionResultRepository.findById(conditionResultId); 
		if(entityContainer.isPresent()) {
			return entityContainer.get();
		}
		return null;
	}
}
