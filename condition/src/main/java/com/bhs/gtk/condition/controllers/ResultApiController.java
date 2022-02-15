package com.bhs.gtk.condition.controllers;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.OffsetDateTime;

import com.bhs.gtk.condition.api.ResultApi;
import com.bhs.gtk.condition.model.ConditionResultResponse;
import com.bhs.gtk.condition.service.ResultServiceImpl;

@Controller
@CrossOrigin
public class ResultApiController implements ResultApi{
	
	@Autowired
	private ResultServiceImpl resultServiceImpl;

	@Override
	public ResponseEntity<ConditionResultResponse> getResult(UUID id, String marketTime, String scripName) {
		Date marketTimeAsDate = DateTimeUtils.toDate(OffsetDateTime.parse(marketTime).toInstant());
		ConditionResultResponse conditionResultResponse = resultServiceImpl.getResult(id, marketTimeAsDate, scripName);
		return new ResponseEntity<ConditionResultResponse>(conditionResultResponse, HttpStatus.OK);
	}

}
