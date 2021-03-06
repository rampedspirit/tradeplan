package com.bhs.gtk.screener.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.OffsetDateTime;

import com.bhs.gtk.screener.messaging.ChangeNotification;
import com.bhs.gtk.screener.messaging.ChangeNotification.ChangeStatusEnum;
import com.bhs.gtk.screener.model.ExecutableCreateRequest;
import com.bhs.gtk.screener.model.ScreenerCreateRequest;
import com.bhs.gtk.screener.model.ScreenerDetailedResponse;
import com.bhs.gtk.screener.model.ScreenerPatchData;
import com.bhs.gtk.screener.model.ScreenerPatchData.PropertyEnum;
import com.bhs.gtk.screener.model.ScreenerResponse;
import com.bhs.gtk.screener.model.ScripResult.StatusEnum;
import com.bhs.gtk.screener.persistence.ConditionResultEntity;
import com.bhs.gtk.screener.persistence.EntityReader;
import com.bhs.gtk.screener.persistence.EntityWriter;
import com.bhs.gtk.screener.persistence.ExecutableEntity;
import com.bhs.gtk.screener.persistence.ScreenerEntity;
import com.bhs.gtk.screener.util.Converter;

@Service
public class ScreenerServiceImpl implements ScreernerService {

	@Autowired
	private Converter converter;

	@Autowired
	private EntityWriter entityWriter;

	@Autowired
	private EntityReader entityReader;

	@Autowired
	private ExecutableServiceImpl executableServiceImpl;

	@Autowired
	private ConditionResultServiceImpl conditionResultServiceImpl;

	@Override
	public ScreenerResponse createScreener(ScreenerCreateRequest screenerCreateRequest) {
		ScreenerEntity entity = entityWriter.createScreenerEntity(screenerCreateRequest);
		return converter.convertToScreenerResponse(entity);
	}

	@Override
	public List<ScreenerResponse> getAllScreeners() {
		List<ScreenerEntity> screenerEntities = entityReader.getAllScreenerEntities();
		List<ScreenerResponse> screenerResponses = new ArrayList<>();
		for (ScreenerEntity entity : screenerEntities) {
			screenerResponses.add(converter.convertToScreenerResponse(entity));
		}
		return screenerResponses;
	}

	@Override
	public ScreenerDetailedResponse getScreener(UUID screenerId) {
		ScreenerEntity screenerEntity = entityReader.getScreenerEntity(screenerId);
		if (screenerEntity != null) {
			return converter.convertToScreenerDetailedResponse(screenerEntity);
		}
		// throw exception screener not found
		return null;
	}

	@Override
	public ScreenerResponse deleteScreener(UUID screenerId) {
		ScreenerEntity deletedScreener = entityWriter.deleteScreener(screenerId);
		if (deletedScreener != null) {
			return converter.convertToScreenerResponse(deletedScreener);
		}
		// throw exception
		return null;
	}

	@Override
	public ScreenerResponse updateScreener(List<ScreenerPatchData> patchData, UUID screenerId) {
		// TODO: improve update of screener.
		ScreenerEntity screenerEntity = entityReader.getScreenerEntity(screenerId);
		if (screenerEntity != null) {
			for (ScreenerPatchData pData : patchData) {
				update(screenerEntity, pData.getProperty(), pData.getValue());
			}
			ScreenerEntity savedEntity = entityWriter.saveScreenerEntity(screenerEntity);
			return converter.convertToScreenerResponse(savedEntity);
		}
		return null;
	}

	@Override
	public ScreenerDetailedResponse runScreener(ExecutableCreateRequest executableCreateRequest, UUID screenerId) {
		ScreenerEntity screenerEntity = entityReader.getScreenerEntity(screenerId);
		if (screenerEntity != null) {
			Date marketTime = DateTimeUtils.toDate(executableCreateRequest.getMarketTime().toInstant());
			UUID conditionId = screenerEntity.getConditionId();
			UUID watchlistId = screenerEntity.getWatchlistId();
			String note = executableCreateRequest.getNote();
			List<String> scripNames = executableCreateRequest.getScripNames();
			ExecutableEntity executable = addExecutableToScreener(screenerEntity, marketTime, conditionId, watchlistId, note, scripNames);
			runExecutable(executable);
			return converter.convertToScreenerDetailedResponse(entityReader.getScreenerEntity(screenerId));
		}
		// throw exception
		return null;
	}

	private ExecutableEntity runExecutable(ExecutableEntity executable) {
		//ExecutableEntity executable = executableServiceImpl.getExecutable(conditionId, marketTime, watchlistId);
		List<ConditionResultEntity> queuedConditions = executable.getConditionResultEntities().stream()
				.filter(e -> StringUtils.equals(e.getStatus(), StatusEnum.QUEUED.name())).collect(Collectors.toList());
		conditionResultServiceImpl.runConditions(queuedConditions);
		return executableServiceImpl.updateStatusOfExecutable(executable);
	}

	private ExecutableEntity addExecutableToScreener(ScreenerEntity screenerEntity, Date marketTime, UUID conditionId,
			UUID watchlistId, String note, List<String> scripNames) {
		//TODO: refactor to createExecEntityObject
		ExecutableEntity executable = entityWriter.createExecutableEntity(marketTime, note, watchlistId, conditionId);
		List<ConditionResultEntity> resultEntities = entityWriter.queueConditionsToExecute(scripNames, marketTime,
				conditionId);
		executable.setConditionResultEntities(resultEntities);
		executable.setNumberOfScripForExecution(resultEntities.size());
		ExecutableEntity savedExecutableEntity = entityWriter.saveExecutableEntity(executable);
		screenerEntity.getExecutableEntities().add(savedExecutableEntity);
		entityWriter.saveScreenerEntity(screenerEntity);
		return savedExecutableEntity;
	}

	private boolean update(ScreenerEntity screenerEntity, PropertyEnum propertyName, String value) {
		switch (propertyName) {
		case NAME:
			screenerEntity.setName(value);
			break;
		case DESCRIPTION:
			screenerEntity.setDescription(value);
			break;
		case WATCHLIST_ID:
			screenerEntity.setWatchlistId(UUID.fromString(value));
			break;
		case CONDITION_ID:
			screenerEntity.setConditionId(UUID.fromString(value));
			break;
		default:
			// throw unsupported exception.
			break;
		}
		return true;
	}

	@Override
	public boolean adaptChangeInCondition(ChangeNotification changeNotification) {
		ChangeStatusEnum status = ChangeStatusEnum.fromValue(changeNotification.getStatus());
		switch (status) {
		case UPDATED:
			entityWriter.adaptConditionUpdate(changeNotification.getId());
			break;
		case DELETED:
			entityWriter.adaptConditionDelete(changeNotification.getId());
			break;
		default:
			throw new IllegalArgumentException();
		}
		return false;
	}

	@Override
	public boolean adaptExecutionResponse(String message) {

		JSONObject jsonObject = new JSONObject(message);
		UUID conditionId = UUID.fromString((String) jsonObject.get("conditionId"));
		String scripName = (String) jsonObject.get("scripName");
		String status = (String) jsonObject.get("status");
		String marketTimeAsString = (String) jsonObject.get("marketTime");
		Date marketTime = DateTimeUtils.toDate(OffsetDateTime.parse(marketTimeAsString).toInstant());

		if (entityWriter.adaptConditionResultEntity(conditionId, scripName, marketTime, status)) {
			return executableServiceImpl
					.updateStatusOfExecutables(entityReader.getExecutableEntitites(conditionId, marketTime));
		}
		return false;
	}

	public boolean adaptChangeInWatchlist(ChangeNotification changeNotification) {
		ChangeStatusEnum status = ChangeStatusEnum.fromValue(changeNotification.getStatus());
		switch (status) {
		case UPDATED:
		case DELETED:
			//TODO: verify whether it is required and then continue.
			//entityWriter.adaptWatchlistModification(changeNotification.getId());
			break;
		default:
			throw new IllegalArgumentException();
		}
		return false;
	}

}
