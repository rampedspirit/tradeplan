package com.bhs.gtk.stock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.stock.persistence.EntityReader;
import com.bhs.gtk.stock.persistence.EntityWriter;
import com.bhs.gtk.stock.persistence.StockEntity;
import com.bhs.gtk.stock.util.Mapper;
import com.bhs.gtk.watchlist.model.StockResponse;

@Component
public class StockServiceImpl implements StockService {

	@Autowired
	private EntityWriter entityWriter;

	@Autowired
	private EntityReader entityReader;

	@Autowired
	private Mapper mapper;

	@Override
	public List<StockResponse> getAllStocks() {
		List<StockEntity> stocks = entityReader.getAllStockEntites();
		return mapper.getAllStockResponses(stocks);
	}

	@Override
	public void updateAllStocks(String exchange, String url) {
		entityWriter.updateAllStocks(exchange, url);
	}
}
