package com.bhs.gtk.condition.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.condition.messaging.ChangeNotification.ChangeStatusEnum;
import com.bhs.gtk.condition.messaging.MessageProducer;
import com.bhs.gtk.condition.model.ConditionRequest;
import com.bhs.gtk.condition.model.ConditionResultResponse.ConditionResultEnum;
import com.bhs.gtk.condition.model.Filter.StatusEnum;
import com.bhs.gtk.condition.model.FilterResultResponse;
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
	private FilterRespository filterRespository;
	
	@Autowired
	private MessageProducer messageProducer;
	
	
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
		if (!conditionResults.isEmpty()) {
			conditionResults.stream().forEach(c -> c.setFilterResultEntities(new ArrayList<>()));
			conditionResultRepository.saveAll(conditionResults);
			conditionResultRepository.deleteAll(conditionResults);
		}
		return true;
	}
	
	private boolean detachAssociatedFilters(ConditionEntity condition) {
		List<FilterEntity> filters = filterRespository.findByConditionsId(condition.getId());
		List<FilterEntity> filtersAssociatedOnlyToGivenCondition = filters.stream()
				.filter(f -> f.getConditions().size() == 1).collect(Collectors.toList());
		for(FilterEntity f : filtersAssociatedOnlyToGivenCondition) {
			f.getConditions().remove(condition); 
		}
		return true;
	}
	
	
	public boolean removePreviousLogicRelatedAssociations(ConditionEntity conditionEntity) {
		deleteFiletersNotAssociatedToanyConditions();
		deleteConditionResultEntity(conditionEntity.getId());
		return true;
	}

	private void deleteFiletersNotAssociatedToanyConditions() {
		filterRespository.deleteAll(filterRespository.findAll());
	}
	
	public ConditionEntity deleteCondition(UUID id) {
		ConditionEntity conditionEntity = entityReader.getCondition(id);
		if(conditionEntity != null) {
			if(detachAssociatedFilters(conditionEntity)) {
				conditionRepository.delete(conditionEntity);
			}
		}
		return conditionEntity;
	}
	
	public ConditionEntity createConditionEntity(ConditionRequest condition) {
		@NotNull
		String parseTree = condition.getParseTree();
		ConditionEntity conditionEntity = new ConditionEntity(condition.getName(), condition.getDescription(), condition.getCode(),
				parseTree);
		
		if(StringUtils.isNotEmpty(parseTree)) {
			conditionEntity.getFilters().addAll(createFilterEntityObjects(parseTree));
		}
		return conditionRepository.save(conditionEntity);
	}

	public List<FilterEntity> createFilterEntityObjects(@NotNull String parseTree) {
		List<FilterEntity> filterEntities = new ArrayList<>();
		Set<UUID> filterIds = converter.getFilterIds(parseTree);
		for(UUID id : filterIds) {
			FilterEntity savedFilterEntity = entityReader.getFilterEntity(id);
			if(savedFilterEntity != null) {
				filterEntities.add(savedFilterEntity);
			}else {
				filterEntities.add(new FilterEntity(id, StatusEnum.ACTIVE.name()));
			}
		}
		return filterEntities;
	}
	
	public ConditionEntity saveConditionEntity(ConditionEntity entity) {
		return conditionRepository.save(entity);
	}
	
	public List<ConditionResultEntity> saveConditionResultEntities(List<ConditionResultEntity> conditionResultEntities) {
		List<ConditionResultEntity> conditionResults = new ArrayList<>();
		if(conditionResultEntities != null && !conditionResultEntities.isEmpty()) {
			conditionResultRepository.saveAll(conditionResultEntities).forEach( c -> conditionResults.add(c));
		}
		return conditionResults;
	}
	
	public FilterResultEntity saveFilterResultEntity(FilterResultEntity filterResultEntity) {
		return filterResultRepository.save(filterResultEntity);
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
				newlyCreatedFilterResults.add(new FilterResultEntity(id, marketTime, scripName, FilterResultResponse.StatusEnum.QUEUED.name()));
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
		//TODO: verify whether value of status is valid or not.
		conditionResultEntity.setStatus(status);
		return conditionResultRepository.save(conditionResultEntity);
	}

	public boolean adaptFilterDelete(UUID filterId) {
		FilterEntity filterEntity = entityReader.getFilterEntity(filterId);
		if(filterEntity == null) {
			return false;
		}
		List<ConditionEntity> conditions = filterEntity.getConditions();
		List<UUID> conditionIds = conditions.stream().map(c -> c.getId()).collect(Collectors.toList());
		
		removeResults(conditionIds, filterEntity.getId());
		conditions.forEach(c -> c.getFilters().remove(filterEntity));
		conditionRepository.saveAll(conditions);
		filterRespository.deleteById(filterEntity.getId());
		return false;
	}
	
	public boolean adaptFilterUpdate(UUID filterId) {
		FilterEntity filterEntity = updateFilterEntity(filterId, StatusEnum.UPDATED.name());
		if(filterEntity == null) {
			return false;
		}
		List<UUID> conditionIds = filterEntity.getConditions().stream().map(c -> c.getId())
				.collect(Collectors.toList());
		removeResults(conditionIds, filterEntity.getId());
		return true;
	}

	private void removeResults(List<UUID> conditionIds, UUID filterId) {
		sendUpdateNotification(conditionIds);
		removeConditionResults(conditionIds);
		List<FilterResultEntity> filterResultEntities = filterResultRepository.findByFilterId(filterId);
		filterResultRepository.deleteAll(filterResultEntities);
	}
	
	private boolean sendUpdateNotification(List<UUID> conditionIds) {
		for(UUID id : conditionIds) {
			if(!messageProducer.sendChangeNotification(id, ChangeStatusEnum.UPDATED)) {
				//not able to notify change in logic and hence logic change is aborted.
				//TODO: Log condition ids to which update could not be sent.
			}
		}
		return true;
	}

	private boolean removeConditionResults(List<UUID> conditionIds) {
		List<ConditionResultEntity> conditionResults = conditionResultRepository.findByConditionIdIn(conditionIds);
		for(ConditionResultEntity cr : conditionResults) {
			cr.setFilterResultEntities(new ArrayList<>());
		}
		conditionResultRepository.deleteAll(conditionResults);
		return true;
	}

	private FilterEntity updateFilterEntity(UUID filterId, String status) {
		FilterEntity filterEntity = entityReader.getFilterEntity(filterId);
		if(filterEntity == null) {
			return null;
		}
		filterEntity.setStatus(status);
		return filterRespository.save(filterEntity);
	}

}
