package com.bhs.gtk.watchlist.service;

import java.util.List;
import java.util.UUID;

import com.bhs.gtk.watchlist.model.WatchlistCreateRequest;
import com.bhs.gtk.watchlist.model.WatchlistPatchData;
import com.bhs.gtk.watchlist.model.WatchlistResponse;

public interface WatchlistService {
	public WatchlistResponse createWatchlist(WatchlistCreateRequest watchlist);

	public List<WatchlistResponse> getAllWatchlists();

	public WatchlistResponse getWatchlist(UUID watchlistId);

	public WatchlistResponse deleteWatchlist(UUID id);

	public WatchlistResponse updateWatchlist(List<WatchlistPatchData> patchData, UUID watchlistId);
}
