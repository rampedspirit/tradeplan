package com.bhs.gtk.screener.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.bhs.gtk.screener.api.ScreenerApi;
import com.bhs.gtk.screener.model.ExecutableCreateRequest;
import com.bhs.gtk.screener.model.ScreenerCreateRequest;
import com.bhs.gtk.screener.model.ScreenerDetailedResponse;
import com.bhs.gtk.screener.model.ScreenerPatchData;
import com.bhs.gtk.screener.model.ScreenerResponse;
import com.bhs.gtk.screener.service.ScreenerServiceImpl;

@Controller
@CrossOrigin
public class ScreenerApiController implements ScreenerApi {

	@Autowired
	private ScreenerServiceImpl screenerServiceImpl;

	@Override
	public ResponseEntity<ScreenerResponse> createScreener(@Valid ScreenerCreateRequest body) {
		ScreenerResponse screenerResponse = screenerServiceImpl.createScreener(body);
		return new ResponseEntity<ScreenerResponse>(screenerResponse, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ScreenerResponse> deleteScreener(UUID screenerId) {
		ScreenerResponse screenerResponse = screenerServiceImpl.deleteScreener(screenerId);
		return new ResponseEntity<ScreenerResponse>(screenerResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<ScreenerResponse>> getAllScreeners() {
		List<ScreenerResponse> screeners = screenerServiceImpl.getAllScreeners();
		return new ResponseEntity<List<ScreenerResponse>>(screeners, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ScreenerDetailedResponse> getScreener(UUID screenerId) {
		ScreenerDetailedResponse screener = screenerServiceImpl.getScreener(screenerId);
		return new ResponseEntity<ScreenerDetailedResponse>(screener, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ScreenerResponse> updateScreener(@Valid List<ScreenerPatchData> body, UUID screenerId) {
		ScreenerResponse screener = screenerServiceImpl.updateScreener(body, screenerId);
		return new ResponseEntity<ScreenerResponse>(screener, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ScreenerDetailedResponse> runScreener(@Valid ExecutableCreateRequest body, UUID screenerId) {
		System.err.println("SCRIPS : " + body.getScripNames());
		ScreenerDetailedResponse screener = screenerServiceImpl.runScreener(body, screenerId);
		return new ResponseEntity<ScreenerDetailedResponse>(screener, HttpStatus.OK);
	}

}
