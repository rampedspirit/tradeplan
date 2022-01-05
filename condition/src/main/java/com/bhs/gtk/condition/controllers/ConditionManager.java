package com.bhs.gtk.condition.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.bhs.gtk.condition.api.ConditionApi;
import com.bhs.gtk.condition.model.Condition;
import com.bhs.gtk.condition.model.PatchModel;
import com.bhs.gtk.condition.service.ConditionServiceImpl;


@Controller
@CrossOrigin
public class ConditionManager implements ConditionApi {
	
	@Autowired
	private ConditionServiceImpl conditionServiceImpl;


	@Override
	public ResponseEntity<Void> checkHealth() {
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Condition> createCondition(@Valid Condition body) {
		Condition condition = conditionServiceImpl.createCondition(body);
		return new ResponseEntity<Condition>(condition, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Condition> deleteCondition(UUID id) {
		Condition condition = conditionServiceImpl.deleteCondition(id);
		return new ResponseEntity<Condition>(condition, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Condition>> getAllConditions() {
		List<Condition> conditions = conditionServiceImpl.getAllConditions();
		return new ResponseEntity<List<Condition>>(conditions, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Condition> getCondition(UUID id) {
		Condition condition = conditionServiceImpl.getCondition(id);
		return new ResponseEntity<Condition>(condition, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Condition> updateCondition(@Valid PatchModel body, UUID id) {
		Condition condition = conditionServiceImpl.updateCondition(body, id);
		return new ResponseEntity<Condition>(condition, HttpStatus.OK);
	}

}
