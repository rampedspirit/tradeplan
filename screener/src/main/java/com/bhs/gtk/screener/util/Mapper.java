package com.bhs.gtk.screener.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;

import com.bhs.gtk.screener.model.ExecutableCreateRequest;
import com.bhs.gtk.screener.model.ExecutableResponse;
import com.bhs.gtk.screener.model.ScreenerCreateRequest;
import com.bhs.gtk.screener.model.ScreenerDetailedResponse;
import com.bhs.gtk.screener.model.ScreenerResponse;
import com.bhs.gtk.screener.model.ScripResult;
import com.bhs.gtk.screener.persistence.ConditionResultEntity;
import com.bhs.gtk.screener.persistence.ConditionResultId;
import com.bhs.gtk.screener.persistence.ConditionResultRepository;
import com.bhs.gtk.screener.persistence.ExecutableEntity;
import com.bhs.gtk.screener.persistence.ScreenerEntity;

@Component
public class Mapper {

	
	@Autowired
	private ConditionResultRepository  conditionResultRepository;
	
	public ExecutableEntity getExecutionEntity(ExecutableCreateRequest executableCreateRequest, UUID watchlistId,UUID conditionId) {
		String note = executableCreateRequest.getNote();
		Date marketTime = DateTimeUtils.toDate(executableCreateRequest.getMarketTime().toInstant());
		return  new ExecutableEntity(note, marketTime, watchlistId, conditionId);
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
		response.setExecutable(getExecutableResponses(screenerEntity.getExecutionEntities()));
		return response;
	}

	private List<ExecutableResponse> getExecutableResponses(List<ExecutableEntity> executionEntities) {
		List<ExecutableResponse> executableResponses = new ArrayList<>();
		for(ExecutableEntity entity : executionEntities) {
			ExecutableResponse response = new ExecutableResponse();
			response.setExecutionId(entity.getExecutionId());
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
