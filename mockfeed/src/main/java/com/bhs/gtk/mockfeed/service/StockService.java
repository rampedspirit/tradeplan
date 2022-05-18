package com.bhs.gtk.mockfeed.service;

import java.util.List;

import org.threeten.bp.OffsetDateTime;

import com.bhs.gtk.mockfeed.model.Bar;
import com.bhs.gtk.mockfeed.persistence.StockEntity;

public interface StockService {
	public void updateAllSymbols(String exchange, String url);

	public int getAllSymbolsCount();

	public List<StockEntity> getAllSymbols(String exchange);

	public byte[] getAllSymbolsAsCSV(String exchange);

	public Bar getBar();
	
	public byte[] getBarsAsCSV(String symbol, OffsetDateTime from, OffsetDateTime to);
}
