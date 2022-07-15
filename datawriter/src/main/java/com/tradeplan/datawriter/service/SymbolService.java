package com.tradeplan.datawriter.service;

import java.io.IOException;
import java.util.List;

public interface SymbolService {

	public void updateAllSymbols() throws IOException;

	List<String> getAllSymbols();

	String getSymbolBySymbolId(Long id);
}
