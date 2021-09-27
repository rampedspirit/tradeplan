package com.bhs.gtk.screener.service;

import java.util.List;
import java.util.UUID;

import com.bhs.gtk.screener.model.PatchModel;
import com.bhs.gtk.screener.model.ScreenerRequest;
import com.bhs.gtk.screener.model.ScreenerResponse;

public interface ScreernerService {
	public ScreenerResponse createScreener(ScreenerRequest screenerRequest);
	public List<ScreenerResponse> getAllScreeners();
	public ScreenerResponse getScreener(UUID screenerId);
	public ScreenerResponse deleteScreener(UUID screenerId);
	public ScreenerResponse updateScreener(PatchModel patchModel, UUID screenerId);
}
