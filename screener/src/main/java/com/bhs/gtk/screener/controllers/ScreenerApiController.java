package com.bhs.gtk.screener.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.bhs.gtk.screener.api.ScreenerApi;
import com.bhs.gtk.screener.model.PatchModel;
import com.bhs.gtk.screener.model.ScreenerRequest;
import com.bhs.gtk.screener.model.ScreenerResponse;

@Controller
public class ScreenerApiController implements ScreenerApi {

	@Override
	public ResponseEntity<ScreenerResponse> createScreener(@Valid ScreenerRequest body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<ScreenerResponse> deleteScreener(UUID screenerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<ScreenerResponse>> getAllScreeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<ScreenerResponse> getScreener(UUID screenerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<ScreenerResponse> updateScreener(@Valid PatchModel body, UUID screenerId) {
		// TODO Auto-generated method stub
		return null;
	}

}
