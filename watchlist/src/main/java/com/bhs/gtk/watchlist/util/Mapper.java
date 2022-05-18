package com.bhs.gtk.watchlist.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneOffset;

import com.bhs.gtk.watchlist.model.SymbolResponse;
import com.bhs.gtk.watchlist.model.WatchlistResponse;
import com.bhs.gtk.watchlist.persistence.SymbolEntity;
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

	public List<SymbolResponse> getAllSymbolResponses(List<SymbolEntity> stockEntities) {
		List<SymbolResponse> responses = new ArrayList<>();
		for (SymbolEntity entity : stockEntities) {
			responses.add(getSymbolResponse(entity));
		}
		return responses;
	}

	private SymbolResponse getSymbolResponse(SymbolEntity SymbolEntity) {
		SymbolResponse response = new SymbolResponse();
		if (SymbolEntity == null) {
			return response;
		}
		response.setExchange(SymbolEntity.getExchange());
		response.setSymbol(SymbolEntity.getSymbol());
		response.setName(SymbolEntity.getName());
		response.setSeries(SymbolEntity.getSeries());
		response.setDol(convertToLocalDate(SymbolEntity.getDol()));
		return response;
	}

	private LocalDate convertToLocalDate(final Date date) {
		return DateTimeUtils.toInstant(date).atZone(ZoneOffset.UTC).toLocalDate();
	}
}
