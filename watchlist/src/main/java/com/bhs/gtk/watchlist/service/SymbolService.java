package com.bhs.gtk.watchlist.service;

import java.util.List;

import com.bhs.gtk.watchlist.model.SymbolResponse;

public interface SymbolService {
	public List<SymbolResponse> getAllSymbols();

	public void updateAllSymbols(String exchange, String url);
}
