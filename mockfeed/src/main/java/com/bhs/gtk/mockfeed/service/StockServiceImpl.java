package com.bhs.gtk.mockfeed.service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
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
import org.springframework.stereotype.Service;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import com.bhs.gtk.mockfeed.model.Bar;
import com.bhs.gtk.mockfeed.persistence.EntityReader;
import com.bhs.gtk.mockfeed.persistence.EntityWriter;
import com.bhs.gtk.mockfeed.persistence.StockEntity;

@Service
public class StockServiceImpl implements StockService {

	private static final String SYMBOL = "SYMBOL";
	private static final String NAME_OF_COMPANY = "NAME OF COMPANY";
	private static final String SERIES = " SERIES";
	private static final String DATE_OF_LISTING = " DATE OF LISTING";

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yy");
	private DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm");

	@Autowired
	private EntityReader entityReader;

	@Autowired
	private EntityWriter entityWriter;

	@Override
	public void updateAllSymbols(String exchange, String url) {
		List<StockEntity> stocks = parse(exchange, url);
		entityWriter.updateAllSymbols(stocks);
	}

	@Override
	public List<StockEntity> getAllSymbols(String exchange) {
		return entityReader.getAllSymbols(exchange);
	}

	@Override
	public byte[] getAllSymbolsAsCSV(String exchange) {
		StringBuilder csv = new StringBuilder();
		csv.append("ID;SYMBOL;NAME").append("\r\n");

		List<StockEntity> stocks = entityReader.getAllSymbols(exchange);
		for (StockEntity stock : stocks) {
			csv.append(stock.getSymbol()).append(";");
			csv.append(stock.getSymbol()).append(";");
			csv.append(stock.getName()).append(";");
			csv.append("\r\n");
		}

		return csv.toString().getBytes(Charset.forName("UTF-8"));
	}

	@Override
	public int getAllSymbolsCount() {
		return (int) entityReader.getAllSymbolsCount();
	}

	@Override
	public Bar getBar() {
		return getBar(OffsetDateTime.now(ZoneId.of("UTC+05:30")));
	}

	@Override
	public byte[] getBarsAsCSV(String symbol, OffsetDateTime from, OffsetDateTime to) {
		List<OffsetDateTime> minutes = getMarketMinutesBetween(from, to);

		StringBuilder csv = new StringBuilder();
		csv.append("time;open;high;low;close;volume;openinterest").append("\r\n");

		for (OffsetDateTime time : minutes) {
			Bar bar = getBar(time);
			csv.append(time.format(TIME_FORMATTER)).append(";");
			csv.append(bar.getOpen()).append(";");
			csv.append(bar.getHigh()).append(";");
			csv.append(bar.getLow()).append(";");
			csv.append(bar.getClose()).append(";");
			csv.append(bar.getVolume()).append(";");
			csv.append(bar.getOi()).append(";");
			csv.append("\r\n");
		}

		return csv.toString().getBytes(Charset.forName("UTF-8"));
	}

	private Bar getBar(OffsetDateTime time) {
		int multipler = getMultipler();
		Bar bar = new Bar();
		bar.setTime(time);
		bar.setOpen(getRandomValue(multipler));
		bar.setHigh(getRandomValue(multipler));
		bar.setLow(getRandomValue(multipler));
		bar.setClose(getRandomValue(multipler));
		bar.setVolume(getRandomValue(1000));
		bar.setOi(0f);
		return bar;
	}

	private List<StockEntity> parse(String exchange, String urlStr) {
		List<StockEntity> stocks = new ArrayList<>();
		try {
			URL url = new URL(urlStr);
			CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();
			CSVParser csvParser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat);
			for (CSVRecord csvRecord : csvParser) {
				StockEntity stock = new StockEntity(exchange, csvRecord.get(SYMBOL), csvRecord.get(NAME_OF_COMPANY));
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

	private List<OffsetDateTime> getMarketMinutesBetween(OffsetDateTime from, OffsetDateTime to) {
		List<OffsetDateTime> minutes = new ArrayList<>();
		List<DayOfWeek> excludedDaysOfWeek = List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
		OffsetDateTime time = from;
		while (ChronoUnit.MINUTES.between(time, to) != 0) {
			if (!excludedDaysOfWeek.contains(time.getDayOfWeek()) && (9 <= time.getHour() && time.getHour() <= 16)) {
				minutes.add(time);
			}
			time = time.plusMinutes(1);
		}
		return minutes;
	}

	private float getRandomValue(int multiplier) {
		double random = Math.random();
		return (float) random * multiplier;
	}

	private int getMultipler() {
		return (int) (Math.random() * 100);
	}
}
