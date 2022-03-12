package com.bhs.gtk.watchlist.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.bhs.gtk.watchlist.api.WatchlistApi;
import com.bhs.gtk.watchlist.model.WatchlistCreateRequest;
import com.bhs.gtk.watchlist.model.WatchlistPatchData;
import com.bhs.gtk.watchlist.model.WatchlistResponse;
import com.bhs.gtk.watchlist.service.WatchlistServiceImpl;

@Controller
@CrossOrigin
public class WatchlistApiController implements WatchlistApi {

	@Autowired
	private WatchlistServiceImpl watchlistServiceImpl;

	@Override
	public ResponseEntity<WatchlistResponse> createWatchlist(@Valid WatchlistCreateRequest body) {
		WatchlistResponse watchlistResponse = watchlistServiceImpl.createWatchlist(body);
		return new ResponseEntity<WatchlistResponse>(watchlistResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<WatchlistResponse> deleteWatchlist(UUID watchlistId) {
		WatchlistResponse watchlistResponse = watchlistServiceImpl.deleteWatchlist(watchlistId);
		return new ResponseEntity<WatchlistResponse>(watchlistResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<WatchlistResponse>> getAllWatchlists() {
		List<WatchlistResponse> watchlistResponses = watchlistServiceImpl.getAllWatchlists();
		return new ResponseEntity<List<WatchlistResponse>>(watchlistResponses, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<WatchlistResponse> getWatchlist(UUID watchlistId) {
		WatchlistResponse watchlistResponse = watchlistServiceImpl.getWatchlist(watchlistId);
		return new ResponseEntity<WatchlistResponse>(watchlistResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<WatchlistResponse> updateWatchlist(@Valid List<WatchlistPatchData> body, UUID watchlistId) {
		WatchlistResponse watchlistResponse = watchlistServiceImpl.updateWatchlist(body, watchlistId);
		return new ResponseEntity<WatchlistResponse>(watchlistResponse, HttpStatus.OK);
	}
}
