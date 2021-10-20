package com.bhs.gtk.screener.util;

import org.springframework.stereotype.Component;

import com.bhs.gtk.screener.model.ScreenerRequest;
import com.bhs.gtk.screener.model.ScreenerResponse;
import com.bhs.gtk.screener.persistence.ScreenerEntity;

@Component
public class Mapper {

	public ScreenerEntity getScreenerEntity(ScreenerRequest screenerRequest) {
		ScreenerEntity entity = new ScreenerEntity(screenerRequest.getName(), screenerRequest.getDescription(),
				screenerRequest.getWatchListId(), screenerRequest.getConditionId());
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
	
}
