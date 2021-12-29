package com.bhs.gtk.screener.service;

import java.util.UUID;

import com.bhs.gtk.screener.model.ExecutableDetailedResponse;
import com.bhs.gtk.screener.model.ExecutablePatchData;
import com.bhs.gtk.screener.model.ExecutableResponse;

public interface ExecutableService {
	public ExecutableResponse getExecutable(UUID executableId);
	public ExecutableDetailedResponse getResult(UUID executableId);
	public ExecutableResponse updateExecutable(ExecutablePatchData executablePatchData, UUID executableId);
}
