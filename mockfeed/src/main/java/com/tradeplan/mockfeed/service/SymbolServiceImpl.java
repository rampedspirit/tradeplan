package com.tradeplan.mockfeed.service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.tradeplan.mockfeed.model.Bar;
import com.tradeplan.mockfeed.persistence.EntityReader;
import com.tradeplan.mockfeed.persistence.EntityWriter;
import com.tradeplan.mockfeed.persistence.SymbolEntity;

@Service
public class SymbolServiceImpl implements SymbolService {

	private static final String SYMBOL = "SYMBOL";
	private static final String NAME_OF_COMPANY = "NAME OF COMPANY";
	private static final String SERIES = " SERIES";
	private static final String DATE_OF_LISTING = " DATE OF LISTING";

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yy");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm");
	private static final List<DayOfWeek> EXCLUDED_DAYS_OF_WEEK = Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

	@Autowired
	private EntityReader entityReader;

	@Autowired
	private EntityWriter entityWriter;

	@Override
	public void updateAllSymbols(String exchange, String url) {
		List<SymbolEntity> symbols = parse(exchange, url);
		entityWriter.updateAllSymbols(symbols);
	}

	@Override
	public List<SymbolEntity> getAllSymbols(String exchange) {
		return entityReader.getAllSymbols(exchange);
	}

	@Override
	public byte[] getAllSymbolsAsCSV(String exchange) {
		StringBuilder csv = new StringBuilder();
		csv.append("ID,SYMBOL,NAME").append("\r\n");

		List<SymbolEntity> symbols = entityReader.getAllSymbols(exchange);
		for (SymbolEntity symbol : symbols) {
			csv.append(symbol.getId()).append(",");
			csv.append(symbol.getSymbol()).append(",");
			csv.append(symbol.getName());
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
		csv.append("time,open,high,low,close,volume,openinterest").append("\r\n");

		for (OffsetDateTime time : minutes) {
			Bar bar = getBar(time);
			csv.append(time.format(TIME_FORMATTER)).append(";");
			csv.append(bar.getOpen()).append(",");
			csv.append(bar.getHigh()).append(",");
			csv.append(bar.getLow()).append(",");
			csv.append(bar.getClose()).append(",");
			csv.append(bar.getVolume()).append(",");
			csv.append(bar.getOi());
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

	private List<SymbolEntity> parse(String exchange, String urlStr) {
		List<SymbolEntity> symbols = new ArrayList<>();
		try {
			URL url = new URL(urlStr);
			CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();
			CSVParser csvParser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat);
			for (CSVRecord csvRecord : csvParser) {
				Long id = (long) (Math.random() * 10000);
				SymbolEntity symbol = new SymbolEntity(id, csvRecord.get(SYMBOL), csvRecord.get(NAME_OF_COMPANY));
				symbols.add(symbol);
			}
		} catch (IOException e) {
			LoggerFactory.getLogger(getClass()).error("failed to parse symbol csv", e);
		}
		return symbols;
	}

	private List<OffsetDateTime> getMarketMinutesBetween(OffsetDateTime from, OffsetDateTime to) {
		List<OffsetDateTime> minutes = new ArrayList<>();
		OffsetDateTime time = from;
		while (ChronoUnit.MINUTES.between(time, to) != 0) {
			if (!EXCLUDED_DAYS_OF_WEEK.contains(time.getDayOfWeek()) && (9 <= time.getHour() && time.getHour() <= 16)) {
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
