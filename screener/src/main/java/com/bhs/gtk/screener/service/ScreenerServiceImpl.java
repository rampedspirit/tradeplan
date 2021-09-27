package com.bhs.gtk.screener.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bhs.gtk.screener.model.PatchModel;
import com.bhs.gtk.screener.model.ScreenerRequest;
import com.bhs.gtk.screener.model.ScreenerResponse;

@Service
public class ScreenerServiceImpl implements ScreernerService {

	@Override
	public ScreenerResponse createScreener(ScreenerRequest screenerRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ScreenerResponse> getAllScreeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScreenerResponse getScreener(UUID screenerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScreenerResponse deleteScreener(UUID screenerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScreenerResponse updateScreener(PatchModel patchModel, UUID screenerId) {
		// TODO Auto-generated method stub
		return null;
	}

}
