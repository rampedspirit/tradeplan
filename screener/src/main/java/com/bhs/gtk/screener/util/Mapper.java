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
public class Mapper {

	
	@Autowired
	private ConditionResultRepository  conditionResultRepository;
	
	public ExecutableEntity getExecutableEntity(ExecutableCreateRequest executableCreateRequest, UUID watchlistId,UUID conditionId) {
		String note = executableCreateRequest.getNote();
		Date marketTime = DateTimeUtils.toDate(executableCreateRequest.getMarketTime().toInstant());
		return  new ExecutableEntity(note, marketTime, watchlistId, conditionId);
	}
	
	public ExecutableResponse getExecutableResponse(ExecutableEntity executable) {
		ExecutableResponse response = new ExecutableResponse();
		response.setExecutableId(executable.getExecutableId());
		response.setMarketTime(convertDateToOffSetDatTime(executable.getMarketTime()));
		response.setNote(executable.getNote());
		response.setStatus(executable.getStatus());
		response.setNumberOfScripForExecution(new BigDecimal(executable.getNumberOfScripForExecution()));
		response.setNumberOfScripWithResultAvailable(new BigDecimal(executable.getNumberOfScripWithResultAvailable()));
		return response;
	}
	
	
	public ExecutableDetailedResponse getExecutableDetailedResponse(ExecutableEntity executable) {
		ExecutableDetailedResponse response = new ExecutableDetailedResponse();
		response.setExecutableId(executable.getExecutableId());
		response.setMarketTime(convertDateToOffSetDatTime(executable.getMarketTime()));
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
//		for(int i=0; i<2;i++) {
//			ExecutionEntity executionEntity = new ExecutionEntity(RandomStringUtils.randomAlphabetic(50), new Date(),
//					screenerRequest.getWatchListId(), screenerRequest.getConditionId());
//			for(int j=0; j<5; j++) {
//				ConditionResultEntity conditionResultEntity = new ConditionResultEntity(UUID.randomUUID(), new Date(),
//						RandomStringUtils.randomAlphabetic(4).toUpperCase(), "READY");
//				conditionResultRepository.save(conditionResultEntity);
//				executionEntity.getConditionResultEntities().add(conditionResultEntity);
//			}
//			entity.getExecutionEntities().add(executionEntity);
//		}
		return entity;
	}
	
	
	public ScreenerResponse getScreenerResponse(ScreenerEntity screenerEntity) {
		ScreenerResponse response = new ScreenerResponse();
		response.setScreenerId(screenerEntity.getScreenerId());
		response.setName(screenerEntity.getName());
		response.setDescription(screenerEntity.getDescription());
		response.setWatchListId(screenerEntity.getWatchlistId());
		response.setConditionId(screenerEntity.getConditionId());
		return response;
	}
	
	public ScreenerDetailedResponse getScreenerDetailedResponse(ScreenerEntity screenerEntity) {
		ScreenerDetailedResponse response = new ScreenerDetailedResponse();
		response.setScreenerId(screenerEntity.getScreenerId());
		response.setName(screenerEntity.getName());
		response.setDescription(screenerEntity.getDescription());
		response.setWatchListId(screenerEntity.getWatchlistId());
		response.setConditionId(screenerEntity.getConditionId());
		response.setExecutables(getExecutableResponses(screenerEntity.getExecutableEntities()));
		return response;
	}

	private List<ExecutableResponse> getExecutableResponses(List<ExecutableEntity> executables) {
		List<ExecutableResponse> executableResponses = new ArrayList<>();
		for(ExecutableEntity entity : executables) {
			ExecutableResponse response = new ExecutableResponse();
			response.setExecutableId(entity.getExecutableId());
			response.setMarketTime(convertDateToOffSetDatTime(entity.getMarketTime()));
			response.setNote(entity.getNote());
			response.setNumberOfScripForExecution(new BigDecimal(entity.getNumberOfScripForExecution()));
			response.setNumberOfScripWithResultAvailable(new BigDecimal(entity.getNumberOfScripWithResultAvailable()));
			response.setStatus(entity.getStatus());
			executableResponses.add(response);
		}
		return executableResponses;
	}
	
	private OffsetDateTime convertDateToOffSetDatTime(final Date date) {
		return OffsetDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault()); // NOSONAR
	}
	
}
