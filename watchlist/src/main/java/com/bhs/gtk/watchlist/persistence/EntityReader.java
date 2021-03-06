package com.bhs.gtk.watchlist.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityReader {

	@Autowired
	private WatchlistRespository watchlistRespository;

	@Autowired
	private SymbolRespository symbolRespository;
	
	public WatchlistEntity getWatchlistEntity(UUID id) {
		Optional<WatchlistEntity> entityContainer = watchlistRespository.findById(id);
		if (entityContainer.isPresent()) {
			return entityContainer.get();
		}
		return null;
	}

	public List<WatchlistEntity> getAllWatchlistEntites() {
		Iterable<WatchlistEntity> iterableWatchlists = watchlistRespository.findAll();
		List<WatchlistEntity> watchlists = new ArrayList<>();
		for (WatchlistEntity ft : iterableWatchlists) {
			watchlists.add(ft);
		}
		return watchlists;
	}

	public List<SymbolEntity> getAllSymbolEntites() {
		Iterable<SymbolEntity> iterable = symbolRespository.findAll();
		List<SymbolEntity> stocks = new ArrayList<>();
		for (SymbolEntity ft : iterable) {
			stocks.add(ft);
		}
		return stocks;
	}
}
