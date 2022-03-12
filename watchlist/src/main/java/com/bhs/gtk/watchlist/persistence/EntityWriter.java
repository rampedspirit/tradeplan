package com.bhs.gtk.watchlist.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.watchlist.model.WatchlistCreateRequest;

@Component
public class EntityWriter {

	@Autowired
	private WatchlistRespository watchlistRespository;

	public WatchlistEntity createWatchlistEntity(WatchlistCreateRequest watchlist) {
		WatchlistEntity watchlistEntity = new WatchlistEntity(watchlist.getName(), watchlist.getDescription());
		return watchlistRespository.save(watchlistEntity);
	}

	public WatchlistEntity saveWatchlistEntity(WatchlistEntity watchlistEntity) {
		return watchlistRespository.save(watchlistEntity);
	}

	public void deleteWatchlistEntity(WatchlistEntity watchlistEntity) {
		watchlistRespository.delete(watchlistEntity);
	}
}
