package com.bhs.gtk.stock.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneOffset;

import com.bhs.gtk.stock.persistence.StockEntity;
import com.bhs.gtk.watchlist.model.StockResponse;

@Component
public class Mapper {

	public List<StockResponse> getAllStockResponses(List<StockEntity> stockEntities) {
		List<StockResponse> responses = new ArrayList<>();
		for (StockEntity entity : stockEntities) {
			responses.add(getStockResponse(entity));
		}
		return responses;
	}

	public StockResponse getStockResponse(StockEntity stockEntity) {
		StockResponse response = new StockResponse();
		if (stockEntity == null) {
			return response;
		}
		response.setExchange(stockEntity.getExchange());
		response.setSymbol(stockEntity.getSymbol());
		response.setName(stockEntity.getName());
		response.setSeries(stockEntity.getSeries());
		response.setDol(convertToLocalDate(stockEntity.getDol()));
		return response;
	}

	public LocalDate convertToLocalDate(final Date date) {
		return DateTimeUtils.toInstant(date).atZone(ZoneOffset.UTC).toLocalDate();
	}
}
