package com.bhs.gtk.stock.service;

import java.util.List;

import com.bhs.gtk.watchlist.model.StockResponse;

public interface StockService {
	public List<StockResponse> getAllStocks();

	public void updateAllStocks(String exchange, String url);
}
