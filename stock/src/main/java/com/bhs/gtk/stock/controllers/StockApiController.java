package com.bhs.gtk.stock.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.bhs.gtk.stock.service.StockService;
import com.bhs.gtk.watchlist.api.StockApi;
import com.bhs.gtk.watchlist.model.StockResponse;

@Controller
@CrossOrigin
public class StockApiController implements StockApi {

	@Autowired
	private StockService stockService;

	@Override
	public ResponseEntity<List<StockResponse>> getAllStocks() {
		List<StockResponse> stockResponses = stockService.getAllStocks();
		return new ResponseEntity<List<StockResponse>>(stockResponses, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> updateAllStocks(String exchange, String url) {
		stockService.updateAllStocks(exchange, url);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
