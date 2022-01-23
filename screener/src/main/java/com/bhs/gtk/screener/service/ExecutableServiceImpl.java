package com.bhs.gtk.screener.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

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
	public  boolean updateStatusOfExecutablesBasedOnConditions(List<ConditionResultEntity> conditionsWithChangedStatus) {
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
		return true;
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
	public ExecutableEntity updateStatusOfExecutable(ExecutableEntity executableEntity) {
		List<ExecutableEntity> executableEntites = new ArrayList<>();
		executableEntites.add(executableEntity);
		List<ExecutableEntity> executablesToBeSaved = getExecutablesWithUpdatedStatus(executableEntites);
		if(!executablesToBeSaved.isEmpty()) {
				return executableRespository.save(executablesToBeSaved.get(0));
		}
		return executableEntity;
	}
	
	@Override
	public boolean updateStatusOfExecutables(List<ExecutableEntity> executableEntites) {
		List<ExecutableEntity> executablesToBeSaved = getExecutablesWithUpdatedStatus(executableEntites);
		if(!executablesToBeSaved.isEmpty()) {
				executableRespository.saveAll(executablesToBeSaved);
		}
		return true;
	}

	private List<ExecutableEntity> getExecutablesWithUpdatedStatus(List<ExecutableEntity> executableEntites) {
		List<ExecutableEntity> executablesWithChangedStatus = new ArrayList<>();
		for( ExecutableEntity executable : executableEntites) {
			int oldResultCount = executable.getNumberOfScripWithResultAvailable();
			String oldStatus = executable.getStatus();
			
			String newStatus = deriveExecutableStatus(executable);
			long newResultCount = executable.getConditionResultEntities().stream()
					.filter(c -> StringUtils.equals(ScripResult.StatusEnum.PASS.name(), c.getStatus())
							|| StringUtils.equals(ScripResult.StatusEnum.FAIL.name(), c.getStatus()))
					.count();
			
			if(!StringUtils.equals(oldStatus, newStatus) || oldResultCount != newResultCount) {
				executable.setStatus(newStatus);
				executable.setNumberOfScripWithResultAvailable((int)newResultCount);
				executablesWithChangedStatus.add(executable);
			}
		}
		return executablesWithChangedStatus;
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
