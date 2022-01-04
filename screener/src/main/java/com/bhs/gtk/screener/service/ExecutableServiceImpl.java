package com.bhs.gtk.screener.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.screener.model.ExecutableDetailedResponse;
import com.bhs.gtk.screener.model.ExecutablePatchData;
import com.bhs.gtk.screener.model.ExecutableResponse;
import com.bhs.gtk.screener.model.ExecutableStatus;
import com.bhs.gtk.screener.model.ScripResult;
import com.bhs.gtk.screener.persistence.ConditionResultEntity;
import com.bhs.gtk.screener.persistence.ExecutableEntity;
import com.bhs.gtk.screener.persistence.ExecutableRespository;
import com.bhs.gtk.screener.util.Converter;

@Service
public class ExecutableServiceImpl implements ExecutableService{

	
	@Autowired
	private Converter converter;
	
	@Autowired
	private ExecutableRespository executableRespository;
	
	@Override
	public ExecutableResponse getExecutable(UUID executableId) {
		ExecutableEntity entity = getExecutableEntity(executableId);
		if(entity != null) {
			return converter.convertToExecutableResponse(entity);
		}
		//thow exeception
		return null;
	}

	@Override
	public ExecutableDetailedResponse getResult(UUID executableId) {
		ExecutableEntity entity = getExecutableEntity(executableId);
		if(entity != null) {
			return converter.convertToExecutableDetailedResponse(entity);
		}
		//thow exeception
		return null;
	}
	
	@Override
	public ExecutableEntity getExecutable(UUID conditionId, Date marketTime, UUID watchlistId) {
		return executableRespository.findByConditionIdAndMarketTimeAndWatchlistId(conditionId, marketTime, watchlistId);
	}

	@Override
	public ExecutableResponse updateExecutable(ExecutablePatchData executablePatchData, UUID executableId) {
		ExecutableEntity entity = getExecutableEntity(executableId);
		if (entity != null) {
			if (StringUtils.equals(executablePatchData.getProperty().name(),
					ExecutablePatchData.PropertyEnum.NOTE.name())) {
				entity.setNote(executablePatchData.getValue());
				ExecutableEntity savedEntity = executableRespository.save(entity);
				return converter.convertToExecutableResponse(savedEntity);
			}
		}
		return null;
	}
	
	@Override
	public ExecutableEntity updateStatusOfExecutable(ExecutableEntity executable) {
		executable.setStatus(deriveExecutableStatus(executable));
		ExecutableEntity savedExecutable = executableRespository.save(executable);
		return savedExecutable;
	}
	
	private String deriveExecutableStatus(ExecutableEntity executable) {
		List<String> conditionStatuses = new ArrayList<>();
		executable.getConditionResultEntities().stream().forEach(e -> conditionStatuses.add(e.getStatus()));
		if (conditionStatuses.contains(ScripResult.StatusEnum.RUNNING.name())) {
			return ExecutableStatus.RUNNING.name();
		} else if (conditionStatuses.contains(ScripResult.StatusEnum.QUEUED.name())) {
			return ExecutableStatus.QUEUED.name();
		}
		return ExecutableStatus.COMPLETED.name();
	}
	
	@Override
	public  List<ExecutableEntity> updateStatusOfExecutablesBasedOnConditions(List<ConditionResultEntity> conditionsWithChangedStatus) {
		Map<UUID,List<Date>> conditionWithMarketTimes = new HashMap<>();
		for(ConditionResultEntity condition : conditionsWithChangedStatus) {
			UUID id = condition.getConditionId();
			if(conditionWithMarketTimes.containsKey(id)) {
				conditionWithMarketTimes.get(id).add(condition.getMarketTime());
			}else {
				List<Date> marketTimes = new ArrayList<>();
				marketTimes.add(condition.getMarketTime());
				conditionWithMarketTimes.put(id, marketTimes);
			}
		}
		List<ExecutableEntity> executables = new ArrayList<>();
		for( Entry<UUID, List<Date>> dd : conditionWithMarketTimes.entrySet()) {
			 UUID conditionId = dd.getKey();
			 for( Date marketTime : dd.getValue()) {
				executables.addAll(executableRespository.findByConditionIdAndMarketTime(conditionId, marketTime));
			 }
		}
		return updateStatusOfExecutables(executables);
	}
	
	private List<ExecutableEntity> updateStatusOfExecutables(List<ExecutableEntity> executableEntites) {
		Set<ExecutableEntity> executables = new HashSet<>();
		executableEntites.stream().forEach(e -> executables.add(e));
		for( ExecutableEntity entity : executables ) {
			String newStatus = deriveExecutableStatus(entity);
			entity.setStatus(newStatus);
		} 
		Iterable<ExecutableEntity> savedEntitites = executableRespository.saveAll(executables);
		List<ExecutableEntity> savedExectables = new ArrayList<>();
		for(ExecutableEntity entity : savedEntitites) {
			savedExectables.add(entity);
		}
		return savedExectables;
	}
	
	
	private ExecutableEntity getExecutableEntity(UUID executableId) {
		if (executableId != null) {
			Optional<ExecutableEntity> executableInContainer = executableRespository.findById(executableId);
			if (executableInContainer.isPresent()) {
				return executableInContainer.get();
			}
		}
		return null;
	}

}
