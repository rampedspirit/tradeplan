package com.bhs.gtk.screener.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.screener.model.PatchData;
import com.bhs.gtk.screener.model.PatchData.PropertyEnum;
import com.bhs.gtk.screener.model.PatchModel;
import com.bhs.gtk.screener.model.ScreenerRequest;
import com.bhs.gtk.screener.model.ScreenerResponse;
import com.bhs.gtk.screener.persistence.ScreenerEntity;
import com.bhs.gtk.screener.persistence.ScreenerRepository;
import com.bhs.gtk.screener.util.Mapper;

@Service
public class ScreenerServiceImpl implements ScreernerService {

	@Autowired
	Mapper mapper;
	
	@Autowired
	ScreenerRepository screenerRepository;
	
	@Override
	public ScreenerResponse createScreener(ScreenerRequest screenerRequest) {
		ScreenerEntity entity = screenerRepository.save(mapper.getScreenerEntity(screenerRequest));
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
	public ScreenerResponse getScreener(UUID screenerId) {
		ScreenerEntity screenerEntity = getScreenerEntity(screenerId);
		if(screenerEntity != null) {
			return mapper.getScreenerResponse(screenerEntity);
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
	public ScreenerResponse updateScreener(PatchModel patchModel, UUID screenerId) {
		ScreenerEntity screenerEntity = getScreenerEntity(screenerId);
		if(screenerEntity == null) {
			return null;
		}
		for(PatchData pd : patchModel.getPatchData()) {
			@NotNull PropertyEnum propertyName = pd.getProperty();
			String value = pd.getValue();
			update(screenerEntity, propertyName,value);
		}
		ScreenerEntity savedScreenerEntity = screenerRepository.save(screenerEntity);
		return mapper.getScreenerResponse(savedScreenerEntity);
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
			setWatchlistId(screenerEntity,value);
			break;
		case CONDITION_ID:
			setConditionId(screenerEntity,value);
			break;
		default:
			//throw unsupported exception.
			break;
		}
		return true;
	}

	private void setConditionId(ScreenerEntity screenerEntity, String value) {
		screenerEntity.setConditionId(UUID.fromString(value));
		// TODO handle illegal argument exception
		//TODO clear results associated with screener
		
	}

	private void setWatchlistId(ScreenerEntity screenerEntity, String value) {
		screenerEntity.setWatchlistId(UUID.fromString(value));
		// TODO handle illegal argument exception
		//TODO clear results associated with screener
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
