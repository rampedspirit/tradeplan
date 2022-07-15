package com.tradeplan.datawriter.service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tradeplan.datawriter.persistence.SymbolEntity;
import com.tradeplan.datawriter.persistence.SymbolRespository;

@Service
public class SymbolServiceImpl implements SymbolService {

	@Value(value = "${stock.datasource.url}")
	private String dataSourceUrl;

	@Value(value = "${stock.datasource.username}")
	private String username;

	@Value(value = "${stock.datasource.password}")
	private String password;

	@Autowired
	SymbolRespository symbolRespository;

	@Override
	public void updateAllSymbols() throws IOException {
		String urlStr = "http://" + dataSourceUrl + "/getAllSymbols?segment=eq&user=" + username + "&password="
				+ password + "&csv=true&allexpiry=false";
		List<SymbolEntity> symbolEntities = parse(urlStr);
		symbolRespository.saveAll(symbolEntities);
	}

	@Override
	public List<String> getAllSymbols() {
		Iterable<SymbolEntity> iterable = symbolRespository.findAll();
		return StreamSupport.stream(iterable.spliterator(), false).map(SymbolEntity::getSymbol)
				.collect(Collectors.toList());
	}

	@Override
	public String getSymbolBySymbolId(Long id) {
		return symbolRespository.findById(id).map(SymbolEntity::getSymbol).orElse(null);
	}

	private List<SymbolEntity> parse(String urlStr) throws IOException{
		List<SymbolEntity> symbols = new ArrayList<>();
		URL url = new URL(urlStr);
		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();
		CSVParser csvParser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat);
		for (CSVRecord csvRecord : csvParser) {
			Long symbolId = Long.parseLong(csvRecord.get("ID"));
			SymbolEntity symbol = new SymbolEntity(symbolId, csvRecord.get("SYMBOL"), csvRecord.get("NAME"));
			symbols.add(symbol);
		}
		return symbols;
	}
}
