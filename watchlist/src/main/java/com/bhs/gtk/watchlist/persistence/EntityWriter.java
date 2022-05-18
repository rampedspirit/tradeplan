package com.bhs.gtk.watchlist.persistence;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.watchlist.model.WatchlistCreateRequest;

@Component
public class EntityWriter {

	private static final String SYMBOL = "SYMBOL";
	private static final String NAME_OF_COMPANY = "NAME OF COMPANY";
	private static final String SERIES = " SERIES";
	private static final String DATE_OF_LISTING = " DATE OF LISTING";

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yy");

	@Autowired
	private WatchlistRespository watchlistRespository;

	@Autowired
	private SymbolRespository symbolRespository;

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

	public void updateAllSymbols(String exchange, String url) {
		List<SymbolEntity> stocks = parse(exchange, url);
		symbolRespository.saveAll(stocks);
	}

	private List<SymbolEntity> parse(String exchange, String urlStr) {
		List<SymbolEntity> stocks = new ArrayList<>();
		try {
			URL url = new URL(urlStr);
			CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();
			CSVParser csvParser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat);
			for (CSVRecord csvRecord : csvParser) {
				SymbolEntity stock = new SymbolEntity();
				stock.setExchange(exchange);
				stock.setSymbol(csvRecord.get(SYMBOL));
				stock.setName(csvRecord.get(NAME_OF_COMPANY));
				stock.setSeries(csvRecord.get(SERIES));

				String dateStr = csvRecord.get(DATE_OF_LISTING);
				stock.setDol(DATE_FORMAT.parse(dateStr));
				stocks.add(stock);
			}
		} catch (IOException | ParseException e) {
			LoggerFactory.getLogger(getClass()).error("failed to parse stock csv", e);
		}
		return stocks;
	}
}
