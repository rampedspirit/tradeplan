package com.bhs.gtk.filter.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.bhs.gtk.filter.api.FilterApi;
import com.bhs.gtk.filter.model.Filter;
import com.bhs.gtk.filter.model.PatchModel;


@Controller
public class FilterManager implements FilterApi{

	@Override
	public ResponseEntity<Filter> createFilter(@Valid Filter body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Filter> deleteFilter(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<Filter>> getAllFilters() {
		List<Filter> body = new ArrayList<>();
		for(int i=0; i<3;i++) {
			Filter f = new Filter();
			f.setName("Filter_"+i);
			f.setCode("Code____##__"+i);
			f.setDescription("Desc****"+i);
			f.setParseTree("parseTree__***__"+i);
			f.setId(UUID.randomUUID());
			body.add(f);
		}
		// TODO Auto-generated method stub
		return new ResponseEntity<List<Filter>>(body, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Filter>> getFilters(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ResponseEntity<Filter> updateFilter(@Valid PatchModel body, UUID id) {
		// TODO Auto-generated method stub
		return null;
	}



}
