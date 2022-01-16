package com.bhs.gtk.condition.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.condition.model.ConditionRequest;
import com.bhs.gtk.condition.model.ConditionResultResponse.ConditionResultEnum;
import com.bhs.gtk.condition.model.Filter.StatusEnum;
import com.bhs.gtk.condition.model.FilterResult;
import com.bhs.gtk.condition.util.Converter;

@Component
public class EntityWriter {
	
	@Autowired 
	private ConditionRepository conditionRepository;
	
	@Autowired
	private FilterResultRepository filterResultRepository;
	
	@Autowired
	private ConditionResultRepository conditionResultRepository;
	
	@Autowired
	private Converter converter;
	
	@Autowired
	private EntityReader entityReader;
	
	/**
	 * remove all condition execution results associated with condition Id
	 * @param conditionId
	 * @return
	 */
	public boolean deleteConditionResultEntity(UUID conditionId) {
		List<ConditionResultEntity> conditionResults = conditionResultRepository.findByConditionId(conditionId);
		conditionResultRepository.deleteAll(conditionResults);
		return true;
	}
	
	public ConditionEntity deleteCondition(UUID id) {
		ConditionEntity conditionEntity = entityReader.getCondition(id);
		if(conditionEntity != null) {
			conditionRepository.delete(conditionEntity);
		}
		return conditionEntity;
	}
	
	public ConditionEntity createConditionEntity(ConditionRequest condition) {
		@NotNull
		String parseTree = condition.getParseTree();
		ConditionEntity conditionEntity = new ConditionEntity(condition.getName(), condition.getDescription(), condition.getCode(),
				parseTree);
		conditionEntity.getFilters().addAll(createFilterEntityObjects(parseTree));
		return conditionRepository.save(conditionEntity);
	}

	private List<FilterEntity> createFilterEntityObjects(@NotNull String parseTree) {
		List<FilterEntity> filterEntities = new ArrayList<>();
		Set<UUID> filterIds = converter.getFilterIds(parseTree);
		for(UUID id : filterIds) {
			FilterEntity savedFilterEntity = entityReader.getFilterInRepository(id);
			if(savedFilterEntity != null) {
				filterEntities.add(savedFilterEntity);
			}else {
				filterEntities.add(new FilterEntity(id, StatusEnum.ACTIVE.name()));
			}
		}
		return filterEntities;
	}
	
	public List<FilterResultEntity> saveFilterResultEntities(List<FilterResultEntity> filterResultEntities) {
		List<FilterResultEntity> filters = new ArrayList<>();
		if(filterResultEntities != null) {
			filterResultRepository.saveAll(filterResultEntities).forEach( f -> filters.add(f));
		}
		return filters;
	}
	
	public ConditionResultEntity createConditionResultEntity(UUID conditionId, Date marketTime, String scripName,List<FilterResultEntity> filterResultEntities) {
		ConditionResultEntity entity = entityReader.getConditionResultEntity(conditionId, marketTime, scripName);
		if(entity == null) {
			entity = new ConditionResultEntity(conditionId, marketTime, scripName, ConditionResultEnum.QUEUED.name());
			entity.setFilterResultEntities(filterResultEntities);
			return conditionResultRepository.save(entity);
		}
		return entity;
	}
	
	public List<FilterResultEntity> createFilterResultEntities(List<UUID>  filterIds, Date marketTime, String scripName) {
		List<FilterResultEntity> newlyCreatedFilterResults = new ArrayList<>();
		List<FilterResultEntity> availableFilterResults = new ArrayList<>();
		for( UUID id : filterIds) {
			FilterResultEntity entity =  entityReader.getFilterResultEntity(id, marketTime, scripName);
			if(entity == null) {
				newlyCreatedFilterResults.add(new FilterResultEntity(id, marketTime, scripName, FilterResult.StatusEnum.QUEUED.name()));
			}else {
				availableFilterResults.add(entity);
			}
		}
		List<FilterResultEntity> associatedFilterResults = new ArrayList<>();
		if(!newlyCreatedFilterResults.isEmpty()) {
			Iterable<FilterResultEntity> savedEntities = filterResultRepository.saveAll(newlyCreatedFilterResults);
			for( FilterResultEntity entity : savedEntities) {
				associatedFilterResults.add(entity);
			}
		}
		associatedFilterResults.addAll(availableFilterResults);
		return associatedFilterResults;
	}
	
	public ConditionResultEntity updateStatus(String status, ConditionResultEntity conditionResultEntity) {
		if(status == null || conditionResultEntity == null) {
			return null;
		}
		//ConditionResultEnum.valueOf(evaluatedStatus);
		//TODO: verify whether value of status is valid or not.
		conditionResultEntity.setStatus(status);
		return conditionResultRepository.save(conditionResultEntity);
	}

}
