package com.bhs.gtk.watchlist.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bhs.gtk.watchlist.model.WatchlistResponse;
import com.bhs.gtk.watchlist.persistence.WatchlistEntity;

@Component
public class Mapper {

	public WatchlistResponse getWatchlistResponse(WatchlistEntity watchlistEntity) {
		WatchlistResponse response = new WatchlistResponse();
		if (watchlistEntity == null) {
			return response;
		}
		response.setWatchlistId(watchlistEntity.getId());
		response.setName(watchlistEntity.getName());
		response.setDescription(watchlistEntity.getDescription());
		response.setScripNames(watchlistEntity.getScripNames());
		return response;
	}

	public List<WatchlistResponse> getAllWatchlistResponses(List<WatchlistEntity> watchlistEntities) {
		List<WatchlistResponse> responses = new ArrayList<>();
		for (WatchlistEntity entity : watchlistEntities) {
			responses.add(getWatchlistResponse(entity));
		}
		return responses;
	}
}
