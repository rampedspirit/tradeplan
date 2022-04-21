package com.bhs.gtk.stock.persistence;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityWriter {

	private static final String SYMBOL = "SYMBOL";
	private static final String NAME_OF_COMPANY = "NAME OF COMPANY";
	private static final String SERIES = " SERIES";
	private static final String DATE_OF_LISTING = " DATE OF LISTING";

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yy");

	@Autowired
	private StockRespository stockRespository;

	@Autowired
	private EventRespository eventRespository;

	public void updateAllStocks(String exchange, String url) {
		Date startTime = new Date();

		List<StockEntity> stocks = parse(exchange, url);
		stockRespository.saveAll(stocks);

		EventEntity eventEntity = new EventEntity();
		eventEntity.setType("stock");
		eventEntity.setStartTime(startTime);
		eventEntity.setEndTime(new Date());
		eventRespository.save(eventEntity);
	}

	private List<StockEntity> parse(String exchange, String urlStr) {
		List<StockEntity> stocks = new ArrayList<>();
		try {
			URL url = new URL(urlStr);
			CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();
			CSVParser csvParser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat);
			for (CSVRecord csvRecord : csvParser) {
				StockEntity stock = new StockEntity();
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
