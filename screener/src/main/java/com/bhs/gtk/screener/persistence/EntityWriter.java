package com.bhs.gtk.screener.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.screener.model.ScreenerCreateRequest;
import com.bhs.gtk.screener.model.ScripResult;

@Component
public class EntityWriter {
	
	@Autowired
	private EntityReader entityReader;
	
	@Autowired
	private ConditionResultRepository  conditionResultRepository;
	
	@Autowired
	private ExecutableRespository executableRespository;
	
	@Autowired
	private ScreenerRepository screenerRepository;
	
	public ExecutableEntity createExecutableEntity(Date marketTime,String note, UUID watchlistId,UUID conditionId) {
		return  new ExecutableEntity(note, marketTime, watchlistId, conditionId);
	}
	
	public List<ConditionResultEntity> queueConditionsToExecute(List<String> scripNames,Date marketTime, UUID conditionId) {
		List<ConditionResultEntity> conditionsToBeExecuted = deriveConditionsToBeExecuted(scripNames, marketTime, conditionId);
		List<ConditionResultEntity> queuedConditions = new ArrayList<>();
		if(!conditionsToBeExecuted.isEmpty()) {
			Iterable<ConditionResultEntity> savedEntities = conditionResultRepository.saveAll(conditionsToBeExecuted);
			savedEntities.forEach(e -> queuedConditions.add(e));
		}
		return  queuedConditions;
	}

	private List<ConditionResultEntity> deriveConditionsToBeExecuted(List<String> scripNames, Date marketTime, UUID conditionId) {
		Set<ConditionResultId> conditionIDsRequestedToExecute = new HashSet<>();
		Set<ConditionResultId> reUsableConditionIDs = new HashSet<>();
		for(String name : scripNames) {
			 conditionIDsRequestedToExecute.add(new ConditionResultId(conditionId, marketTime, name));
		}
		for(ConditionResultEntity cn : conditionResultRepository.findAllById(conditionIDsRequestedToExecute)) {
			reUsableConditionIDs.add(new ConditionResultId(cn.getConditionId(), cn.getMarketTime(), cn.getScripName()));
		}
		conditionIDsRequestedToExecute.removeAll(reUsableConditionIDs);
		return createConditions(conditionIDsRequestedToExecute);
	}

	private List<ConditionResultEntity> createConditions(Set<ConditionResultId> conditionIDs) {
		List<ConditionResultEntity> conditions = new ArrayList<>();
		for (ConditionResultId c : conditionIDs) {
			conditions.add(new ConditionResultEntity(c.getConditionId(), c.getMarketTime(), c.getScripName(),
					ScripResult.StatusEnum.QUEUED.name()));
		}
		return conditions;
	}
	
	public ScreenerEntity createScreenerEntity(ScreenerCreateRequest screenerRequest) {
		ScreenerEntity entity = new ScreenerEntity(screenerRequest.getName(), screenerRequest.getDescription(),
				screenerRequest.getWatchListId(), screenerRequest.getConditionId());
		return screenerRepository.save(entity);
	}
	
	public ScreenerEntity deleteScreener(UUID screenerId) {
		ScreenerEntity screenerEntity = entityReader.getScreenerEntity(screenerId);
		if(screenerEntity != null) {
			screenerRepository.delete(screenerEntity);
		}
		return screenerEntity;
	}
	
	public ScreenerEntity saveScreenerEntity(ScreenerEntity entity) {
		return screenerRepository.save(entity);
	}

	
	public boolean adaptConditionDelete(UUID conditionId) {
		//detachExecutablesOfConditionFromScreeners(conditionId);
		removeConditionFromScreener(conditionId);
		removeExecutablesOfCondition(conditionId);
		removeConditionResults(conditionId);
		return true;
	}
	
	
	private void removeConditionFromScreener(UUID conditionId) {
		List<ScreenerEntity> screenersAssociatedToCondition = screenerRepository.findByConditionId(conditionId);
		for(ScreenerEntity screener : screenersAssociatedToCondition) {
			screener.setConditionId(null);
			screener.setExecutableEntities(new ArrayList<>());
		}
		screenerRepository.saveAll(screenersAssociatedToCondition);
	}

	public boolean adaptConditionUpdate(UUID conditionId) {
		detachExecutablesOfConditionFromScreeners(conditionId);
		removeExecutablesOfCondition(conditionId);
		removeConditionResults(conditionId);
		return true;
	}

	private void detachExecutablesOfConditionFromScreeners(UUID conditionId) {
		List<ScreenerEntity> screenersAssociatedToCondition = screenerRepository.findByConditionId(conditionId);
		for(ScreenerEntity screener : screenersAssociatedToCondition) {
			screener.setExecutableEntities(new ArrayList<>());
		}
		screenerRepository.saveAll(screenersAssociatedToCondition);
	}
	
	private void removeExecutablesOfCondition(UUID conditionId) {
		List<ExecutableEntity> executables = executableRespository.findByConditionId(conditionId);
		for(ExecutableEntity entity : executables) {
			entity.setConditionResultEntities(new ArrayList<>());
		}
		List<ExecutableEntity> associationRemovedExecutables = new ArrayList<>();
		Iterable<ExecutableEntity> savedExecutables = executableRespository.saveAll(executables);
		for(ExecutableEntity entity : savedExecutables) {
			associationRemovedExecutables.add(entity);
		}
		executableRespository.deleteAll(associationRemovedExecutables);
	}

	private void removeConditionResults(UUID conditionId) {
		List<ConditionResultEntity> conditionResults = conditionResultRepository.findByConditionId(conditionId);
		conditionResultRepository.deleteAll(conditionResults);
	}




	
}
