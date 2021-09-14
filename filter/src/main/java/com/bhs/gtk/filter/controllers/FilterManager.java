package com.bhs.gtk.filter.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.bhs.gtk.filter.api.FilterApi;
import com.bhs.gtk.filter.model.Filter;
import com.bhs.gtk.filter.model.PatchModel;
import com.bhs.gtk.filter.service.FilterServiceImpl;


@Controller
public class FilterManager implements FilterApi{
	
	@Autowired
	private FilterServiceImpl filterServiceImpl;

	@Override
	public ResponseEntity<Filter> createFilter(@Valid Filter body) {
		Filter filter = filterServiceImpl.createFilter(body);
		return new ResponseEntity<Filter>(filter, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Filter> deleteFilter(UUID id) {
		Filter filter = filterServiceImpl.deleteFilter(id);
		return new ResponseEntity<Filter>(filter, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Filter>> getAllFilters() {
		List<Filter> filters = filterServiceImpl.getAllFilters();
		return new ResponseEntity<List<Filter>>(filters, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Filter> getFilter(UUID id) {
		Filter filter = filterServiceImpl.getFilter(id);
		return new ResponseEntity<Filter>(filter, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Filter> updateFilter(@Valid PatchModel body, UUID id) {
		Filter filter = filterServiceImpl.updateFilter(body, id);
		return new ResponseEntity<Filter>(filter, HttpStatus.OK);
	}


}
