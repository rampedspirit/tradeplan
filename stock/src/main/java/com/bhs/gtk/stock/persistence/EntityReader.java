package com.bhs.gtk.stock.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityReader {

	@Autowired
	private StockRespository stockRespository;

	public List<StockEntity> getAllStockEntites() {
		Iterable<StockEntity> iterableStocks = stockRespository.findAll();
		List<StockEntity> stocks = new ArrayList<>();
		for (StockEntity ft : iterableStocks) {
			stocks.add(ft);
		}
		return stocks;
	}
}
