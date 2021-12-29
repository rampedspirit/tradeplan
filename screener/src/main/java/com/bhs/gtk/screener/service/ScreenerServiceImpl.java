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
import com.bhs.gtk.screener.persistence.ExecutableRespository;
import com.bhs.gtk.screener.persistence.ScreenerEntity;
import com.bhs.gtk.screener.persistence.ScreenerRepository;
import com.bhs.gtk.screener.util.Mapper;

@Service
public class ScreenerServiceImpl implements ScreernerService {
	
	@Autowired
	Mapper mapper;
	
	@Autowired
	private ScreenerRepository screenerRepository;
	
	@Autowired
	private ExecutableRespository executableRespository;

	@Override
	public ScreenerResponse createScreener(ScreenerCreateRequest screenerCreateRequest) {
		ScreenerEntity entity = screenerRepository.save(mapper.getScreenerEntity(screenerCreateRequest));
		return mapper.getScreenerResponse(entity);
	}

	@Override
	public List<ScreenerResponse> getAllScreeners() {
		Iterator<ScreenerEntity> iterator = screenerRepository.findAll().iterator();
		List<ScreenerResponse> screenerResponses = new ArrayList<>();
		while(iterator.hasNext()) {
			screenerResponses.add(mapper.getScreenerResponse(iterator.next()));
		}
		return screenerResponses;
	}

	@Override
	public ScreenerDetailedResponse getScreener(UUID screenerId) {
		ScreenerEntity screenerEntity = getScreenerEntity(screenerId);
		if(screenerEntity != null) {
			return mapper.getScreenerDetailedResponse(screenerEntity);
		}
		//throw exception screener not found
		return null;
	}

	@Override
	public ScreenerResponse deleteScreener(UUID screenerId) {
		ScreenerEntity screenerEntity = getScreenerEntity(screenerId);
		if(screenerEntity != null) {
			screenerRepository.delete(screenerEntity);
			return mapper.getScreenerResponse(screenerEntity);
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
			return mapper.getScreenerResponse(savedEntity);
		}
		return null;
	}
	
	@Override
	public ScreenerDetailedResponse runScreener(ExecutableCreateRequest executableCreateRequest, UUID screenerId) {
		ScreenerEntity screenerEntity = getScreenerEntity(screenerId);
		if(screenerEntity != null) {
			ExecutableEntity executable = mapper.getExecutableEntity(executableCreateRequest,
					screenerEntity.getWatchlistId(), screenerEntity.getConditionId());
			List<ConditionResultEntity> resultEntities = mapper.getConditionResultEntities(executableCreateRequest, screenerEntity.getConditionId());
			executable.setConditionResultEntities(resultEntities);
			
			screenerEntity.getExecutableEntities().add(executable);
			
		//	ExecutableEntity savedEntity = executableRespository.save(executable);
			ScreenerEntity savedScreenerEntity = screenerRepository.save(screenerEntity);
			
			//TODO: send async message to output topic of screener service and change status to EVALUATING and save in DB.
			
			return mapper.getScreenerDetailedResponse(savedScreenerEntity);
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
