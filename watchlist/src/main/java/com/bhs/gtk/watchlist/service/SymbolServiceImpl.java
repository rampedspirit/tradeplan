package com.bhs.gtk.watchlist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.watchlist.model.SymbolResponse;
import com.bhs.gtk.watchlist.persistence.EntityReader;
import com.bhs.gtk.watchlist.persistence.EntityWriter;
import com.bhs.gtk.watchlist.persistence.SymbolEntity;
import com.bhs.gtk.watchlist.util.Mapper;

@Component
public class SymbolServiceImpl implements SymbolService {

	@Autowired
	private EntityWriter entityWriter;

	@Autowired
	private EntityReader entityReader;

	@Autowired
	private Mapper mapper;

	@Override
	public List<SymbolResponse> getAllSymbols() {
		List<SymbolEntity> stocks = entityReader.getAllSymbolEntites();
		return mapper.getAllSymbolResponses(stocks);
	}

	@Override
	public void updateAllSymbols(String exchange, String url) {
		entityWriter.updateAllSymbols(exchange, url);
	}
}
