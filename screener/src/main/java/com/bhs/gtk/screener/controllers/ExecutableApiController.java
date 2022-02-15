package com.bhs.gtk.screener.controllers;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.bhs.gtk.screener.api.ExecutableApi;
import com.bhs.gtk.screener.model.ExecutableDetailedResponse;
import com.bhs.gtk.screener.model.ExecutablePatchData;
import com.bhs.gtk.screener.model.ExecutableResponse;
import com.bhs.gtk.screener.service.ExecutableServiceImpl;

@Controller
@CrossOrigin
public class ExecutableApiController implements ExecutableApi {

	@Autowired
	private ExecutableServiceImpl executableServiceImpl;
	
	@Override
	public ResponseEntity<ExecutableResponse> getExecutable(UUID executableId) {
		ExecutableResponse executable = executableServiceImpl.getExecutable(executableId);
		return new ResponseEntity<ExecutableResponse>(executable, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ExecutableDetailedResponse> getResult(UUID executableId) {
		ExecutableDetailedResponse executable = executableServiceImpl.getResult(executableId);
		return new ResponseEntity<ExecutableDetailedResponse>(executable, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ExecutableResponse> updateExecutable(@Valid ExecutablePatchData body, UUID executableId) {
		 ExecutableResponse executable = executableServiceImpl.updateExecutable(body, executableId);
		return new ResponseEntity<ExecutableResponse>(executable, HttpStatus.OK);
	}

}
