package com.bhs.gtk.screener.service;

import java.util.List;
import java.util.UUID;

import com.bhs.gtk.screener.messaging.ChangeNotification;
import com.bhs.gtk.screener.model.ExecutableCreateRequest;
import com.bhs.gtk.screener.model.ScreenerCreateRequest;
import com.bhs.gtk.screener.model.ScreenerDetailedResponse;
import com.bhs.gtk.screener.model.ScreenerPatchData;
import com.bhs.gtk.screener.model.ScreenerResponse;

public interface ScreernerService {
	public ScreenerResponse createScreener(ScreenerCreateRequest screenerCreateRequest);
	public List<ScreenerResponse> getAllScreeners();
	public ScreenerDetailedResponse getScreener(UUID screenerId);
	public ScreenerResponse deleteScreener(UUID screenerId);
	public ScreenerResponse updateScreener(List<ScreenerPatchData> patchData, UUID screenerId);
	public ScreenerDetailedResponse runScreener(ExecutableCreateRequest executableCreateRequest, UUID screenerId);
	public boolean adaptChangeInCondition(ChangeNotification changeNotification);
	public boolean adaptExecutionResponse(String message);
}
