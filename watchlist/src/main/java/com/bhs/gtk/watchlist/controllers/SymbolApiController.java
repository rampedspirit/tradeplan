package com.bhs.gtk.watchlist.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.bhs.gtk.watchlist.api.SymbolApi;
import com.bhs.gtk.watchlist.model.SymbolResponse;
import com.bhs.gtk.watchlist.service.SymbolService;

@Controller
@CrossOrigin
public class SymbolApiController implements SymbolApi {

	@Autowired
	private SymbolService SymbolService;

	@Override
	public ResponseEntity<List<SymbolResponse>> getAllSymbols() {
		List<SymbolResponse> SymbolResponses = SymbolService.getAllSymbols();
		return new ResponseEntity<List<SymbolResponse>>(SymbolResponses, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> updateAllSymbols(String exchange, String url) {
		SymbolService.updateAllSymbols(exchange, url);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
