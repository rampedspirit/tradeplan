package com.bhs.gtk.condition.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.bhs.gtk.condition.api.ConditionApi;
import com.bhs.gtk.condition.model.Condition;
import com.bhs.gtk.condition.model.PatchModel;


@Controller
public class ConditionManager implements ConditionApi {

	@Override
	public ResponseEntity<Condition> createCondition(@Valid Condition body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Condition> deleteCondition(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<Condition>> getAllConditions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Condition> getCondition(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Condition> updateCondition(@Valid PatchModel body, UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

}
