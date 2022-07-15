package com.tradeplan.mockfeed.service;

import java.util.List;

import org.threeten.bp.OffsetDateTime;

import com.tradeplan.mockfeed.model.Bar;
import com.tradeplan.mockfeed.persistence.SymbolEntity;

public interface SymbolService {
	public void updateAllSymbols(String exchange, String url);

	public int getAllSymbolsCount();

	public List<SymbolEntity> getAllSymbols(String exchange);

	public byte[] getAllSymbolsAsCSV(String exchange);

	public Bar getBar();
	
	public byte[] getBarsAsCSV(String symbol, OffsetDateTime from, OffsetDateTime to);
}
