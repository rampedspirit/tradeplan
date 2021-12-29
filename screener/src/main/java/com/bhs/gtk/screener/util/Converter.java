package com.bhs.gtk.screener.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;

import com.bhs.gtk.screener.model.ExecutableCreateRequest;
import com.bhs.gtk.screener.model.ExecutableDetailedResponse;
import com.bhs.gtk.screener.model.ExecutableResponse;
import com.bhs.gtk.screener.model.ScreenerCreateRequest;
import com.bhs.gtk.screener.model.ScreenerDetailedResponse;
import com.bhs.gtk.screener.model.ScreenerResponse;
import com.bhs.gtk.screener.model.ScripResult;
import com.bhs.gtk.screener.model.ScripResult.StatusEnum;
import com.bhs.gtk.screener.persistence.ConditionResultEntity;
import com.bhs.gtk.screener.persistence.ConditionResultId;
import com.bhs.gtk.screener.persistence.ConditionResultRepository;
import com.bhs.gtk.screener.persistence.ExecutableEntity;
import com.bhs.gtk.screener.persistence.ScreenerEntity;

@Component
public class Converter {

	
	@Autowired
	private ConditionResultRepository  conditionResultRepository;
	
	public ExecutableEntity getExecutableEntity(ExecutableCreateRequest executableCreateRequest, UUID watchlistId,UUID conditionId) {
		String note = executableCreateRequest.getNote();
		Date marketTime = DateTimeUtils.toDate(executableCreateRequest.getMarketTime().toInstant());
		return  new ExecutableEntity(note, marketTime, watchlistId, conditionId);
	}
	
	public ExecutableResponse convertToExecutableResponse(ExecutableEntity executable) {
		ExecutableResponse response = new ExecutableResponse();
		response.setExecutableId(executable.getExecutableId());
		response.setMarketTime(convertToOffSetDateTime(executable.getMarketTime()));
		response.setNote(executable.getNote());
		response.setStatus(executable.getStatus());
		response.setNumberOfScripForExecution(new BigDecimal(executable.getNumberOfScripForExecution()));
		response.setNumberOfScripWithResultAvailable(new BigDecimal(executable.getNumberOfScripWithResultAvailable()));
		return response;
	}
	
	
	public ExecutableDetailedResponse convertToExecutableDetailedResponse(ExecutableEntity executable) {
		ExecutableDetailedResponse response = new ExecutableDetailedResponse();
		response.setExecutableId(executable.getExecutableId());
		response.setMarketTime(convertToOffSetDateTime(executable.getMarketTime()));
		response.setNote(executable.getNote());
		response.setStatus(executable.getStatus());
		response.setNumberOfScripForExecution(new BigDecimal(executable.getNumberOfScripForExecution()));
		response.setNumberOfScripWithResultAvailable(new BigDecimal(executable.getNumberOfScripWithResultAvailable()));
		response.setResult(getScripResult(executable.getConditionResultEntities()));
		return response;
	}
	
	private List<ScripResult> getScripResult(List<ConditionResultEntity> conditionResultEntities) {
		List<ScripResult> scripResults = new ArrayList<>();
		for(ConditionResultEntity resultEntity : conditionResultEntities) {
			ScripResult result = new ScripResult();
			result.setScripName(resultEntity.getScripName());
			result.setStatus(StatusEnum.valueOf(resultEntity.getStatus()));
			scripResults.add(result);
		}
		return scripResults;
	}

	public List<ConditionResultEntity> getConditionResultEntities(ExecutableCreateRequest executableCreateRequest, UUID conditionId) {
		List<ConditionResultEntity> resultEntities = new ArrayList<>();
		List<ConditionResultEntity> savedResultEntities = new ArrayList<>();
		Date marketTime = DateTimeUtils.toDate(executableCreateRequest.getMarketTime().toInstant());
		for(String scripName: executableCreateRequest.getScripNames()) {
			ConditionResultId resultId = new ConditionResultId(conditionId, marketTime, scripName);
			Optional<ConditionResultEntity> entityInContainer = conditionResultRepository.findById(resultId);
			if(entityInContainer.isPresent()) {
				savedResultEntities.add(entityInContainer.get());
			}else {
				resultEntities.add(new ConditionResultEntity(conditionId, marketTime, scripName,
						ScripResult.StatusEnum.SCHEDULED.name()));
			}
		}
		if(!resultEntities.isEmpty()) {
			Iterable<ConditionResultEntity> iterableSavedEntities = conditionResultRepository.saveAll(resultEntities);
			iterableSavedEntities.forEach(e -> savedResultEntities.add(e));
		}
		return  savedResultEntities;
	}
	
	public ScreenerEntity getScreenerEntity(ScreenerCreateRequest screenerRequest) {
		ScreenerEntity entity = new ScreenerEntity(screenerRequest.getName(), screenerRequest.getDescription(),
				screenerRequest.getWatchListId(), screenerRequest.getConditionId());
		return entity;
	}
	
	
	public ScreenerResponse convertToScreenerResponse(ScreenerEntity screenerEntity) {
		ScreenerResponse response = new ScreenerResponse();
		response.setScreenerId(screenerEntity.getScreenerId());
		response.setName(screenerEntity.getName());
		response.setDescription(screenerEntity.getDescription());
		response.setWatchListId(screenerEntity.getWatchlistId());
		response.setConditionId(screenerEntity.getConditionId());
		return response;
	}
	
	public ScreenerDetailedResponse convertToScreenerDetailedResponse(ScreenerEntity screenerEntity) {
		ScreenerDetailedResponse response = new ScreenerDetailedResponse();
		response.setScreenerId(screenerEntity.getScreenerId());
		response.setName(screenerEntity.getName());
		response.setDescription(screenerEntity.getDescription());
		response.setWatchListId(screenerEntity.getWatchlistId());
		response.setConditionId(screenerEntity.getConditionId());
		response.setExecutables(convertToExecutableResponses(screenerEntity.getExecutableEntities()));
		return response;
	}

	private List<ExecutableResponse> convertToExecutableResponses(List<ExecutableEntity> executables) {
		List<ExecutableResponse> executableResponses = new ArrayList<>();
		for(ExecutableEntity entity : executables) {
			ExecutableResponse response = new ExecutableResponse();
			response.setExecutableId(entity.getExecutableId());
			response.setMarketTime(convertToOffSetDateTime(entity.getMarketTime()));
			response.setNote(entity.getNote());
			response.setNumberOfScripForExecution(new BigDecimal(entity.getNumberOfScripForExecution()));
			response.setNumberOfScripWithResultAvailable(new BigDecimal(entity.getNumberOfScripWithResultAvailable()));
			response.setStatus(entity.getStatus());
			executableResponses.add(response);
		}
		return executableResponses;
	}
	
	private OffsetDateTime convertToOffSetDateTime(final Date date) {
		return OffsetDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
	}
	
}
