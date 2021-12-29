package com.bhs.gtk.screener.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.screener.model.ExecutableCreateRequest;
import com.bhs.gtk.screener.model.ScreenerCreateRequest;
import com.bhs.gtk.screener.model.ScreenerDetailedResponse;
import com.bhs.gtk.screener.model.ScreenerPatchData;
import com.bhs.gtk.screener.model.ScreenerPatchData.PropertyEnum;
import com.bhs.gtk.screener.model.ScreenerResponse;
import com.bhs.gtk.screener.persistence.ConditionResultEntity;
import com.bhs.gtk.screener.persistence.ExecutableEntity;
import com.bhs.gtk.screener.persistence.ScreenerEntity;
import com.bhs.gtk.screener.persistence.ScreenerRepository;
import com.bhs.gtk.screener.util.Converter;

@Service
public class ScreenerServiceImpl implements ScreernerService {
	
	@Autowired
	Converter converter;
	
	@Autowired
	private ScreenerRepository screenerRepository;
	
	@Override
	public ScreenerResponse createScreener(ScreenerCreateRequest screenerCreateRequest) {
		ScreenerEntity entity = screenerRepository.save(converter.getScreenerEntity(screenerCreateRequest));
		return converter.convertToScreenerResponse(entity);
	}

	@Override
	public List<ScreenerResponse> getAllScreeners() {
		Iterator<ScreenerEntity> iterator = screenerRepository.findAll().iterator();
		List<ScreenerResponse> screenerResponses = new ArrayList<>();
		while(iterator.hasNext()) {
			screenerResponses.add(converter.convertToScreenerResponse(iterator.next()));
		}
		return screenerResponses;
	}

	@Override
	public ScreenerDetailedResponse getScreener(UUID screenerId) {
		ScreenerEntity screenerEntity = getScreenerEntity(screenerId);
		if(screenerEntity != null) {
			return converter.convertToScreenerDetailedResponse(screenerEntity);
		}
		//throw exception screener not found
		return null;
	}

	@Override
	public ScreenerResponse deleteScreener(UUID screenerId) {
		ScreenerEntity screenerEntity = getScreenerEntity(screenerId);
		if(screenerEntity != null) {
			screenerRepository.delete(screenerEntity);
			return converter.convertToScreenerResponse(screenerEntity);
		}
		//throw exception
		return null;
	}

	@Override
	public ScreenerResponse updateScreener(List<ScreenerPatchData> patchData, UUID screenerId) {
		ScreenerEntity screenerEntity = getScreenerEntity(screenerId);
		if(screenerEntity != null) {
			for(ScreenerPatchData pData : patchData) {
				update(screenerEntity, pData.getProperty(),pData.getValue());
			}
			ScreenerEntity savedEntity = screenerRepository.save(screenerEntity);
			return converter.convertToScreenerResponse(savedEntity);
		}
		return null;
	}
	
	@Override
	public ScreenerDetailedResponse runScreener(ExecutableCreateRequest executableCreateRequest, UUID screenerId) {
		ScreenerEntity screenerEntity = getScreenerEntity(screenerId);
		if(screenerEntity != null) {
			ExecutableEntity executable = converter.getExecutableEntity(executableCreateRequest,
					screenerEntity.getWatchlistId(), screenerEntity.getConditionId());
			List<ConditionResultEntity> resultEntities = converter.getConditionResultEntities(executableCreateRequest, screenerEntity.getConditionId());
			executable.setConditionResultEntities(resultEntities);
			
			screenerEntity.getExecutableEntities().add(executable);
			
			ScreenerEntity savedScreenerEntity = screenerRepository.save(screenerEntity);
			
			//TODO: send async message to output topic of screener service and change status to EVALUATING and save in DB.
			
			return converter.convertToScreenerDetailedResponse(savedScreenerEntity);
		}
		//throw exception
		return null;
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
			screenerEntity.setConditionId(UUID.fromString(value));
			break;
		case CONDITION_ID:
			screenerEntity.setWatchlistId(UUID.fromString(value));
			break;
		default:
			//throw unsupported exception.
			break;
		}
		return true;
	}

	
	private ScreenerEntity getScreenerEntity(UUID screenerId) {
		if (screenerId != null) {
			Optional<ScreenerEntity> screenerEntityContainer = screenerRepository.findById(screenerId);
			if (screenerEntityContainer.isPresent()) {
				return screenerEntityContainer.get();
			}
		}
		return null;
	}

}
