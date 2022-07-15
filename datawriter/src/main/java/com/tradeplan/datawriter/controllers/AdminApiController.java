package com.tradeplan.datawriter.controllers;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.tradeplan.datawriter.api.AdminApi;
import com.tradeplan.datawriter.service.SymbolService;
import com.tradeplan.datawriter.websocket.BarDataConsumer;

@Controller
@CrossOrigin
public class AdminApiController implements AdminApi {

	@Autowired
	private SymbolService symbolService;

	@Autowired
	private BarDataConsumer bardataConsumer;

	@Override
	public ResponseEntity<Void> updateAllSymbols() {
		try {
			symbolService.updateAllSymbols();
			bardataConsumer.updateSymbolSubscription();
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Void> start() {
		try {
			bardataConsumer.start();
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Void> stop() {
		try {
			bardataConsumer.stop();
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
