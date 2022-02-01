package com.bhs.gtk.filter.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.bhs.gtk.filter.api.FilterApi;
import com.bhs.gtk.filter.model.FilterRequest;
import com.bhs.gtk.filter.model.FilterResponse;
import com.bhs.gtk.filter.model.PatchData;
import com.bhs.gtk.filter.service.FilterServiceImpl;

@Controller
@CrossOrigin
public class FilterApiController implements FilterApi {

	
	@Autowired
	private FilterServiceImpl filterServiceImpl;
	
	@Override
	public ResponseEntity<FilterResponse> createFilter(@Valid FilterRequest body) {
		FilterResponse filterResponse = filterServiceImpl.createFilter(body);
		return new ResponseEntity<FilterResponse>(filterResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<FilterResponse> deleteFilter(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public ResponseEntity<List<FilterResponse>> getAllFilters() {
			List<FilterResponse> filterResponses = filterServiceImpl.getAllFilters();
			return new ResponseEntity<List<FilterResponse>>(filterResponses, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<FilterResponse> getFilter(UUID id) {
		FilterResponse filterResponse = filterServiceImpl.getFilter(id);
		return new ResponseEntity<FilterResponse>(filterResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<FilterResponse> updateFilter(@Valid List<PatchData> body, UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Autowired
//	private FilterServiceImpl filterServiceImpl;
//
//	@Override
//	public ResponseEntity<Void> checkHealth() {
//		return new ResponseEntity<Void>(HttpStatus.OK);
//	}
//
//	@Override
//	public ResponseEntity<Filter> createFilter(@Valid Filter body) {
//		Filter filter = filterServiceImpl.createFilter(body);
//		return new ResponseEntity<Filter>(filter, HttpStatus.OK);
//	}
//
//	@Override
//	public ResponseEntity<Filter> deleteFilter(UUID id) {
//		Filter filter = filterServiceImpl.deleteFilter(id);
//		return new ResponseEntity<Filter>(filter, HttpStatus.OK);
//	}
//
//	@Override
//	public ResponseEntity<List<Filter>> getAllFilters() {
//		List<Filter> filters = filterServiceImpl.getAllFilters();
//		return new ResponseEntity<List<Filter>>(filters, HttpStatus.OK);
//	}
//
//	@Override
//	public ResponseEntity<Filter> getFilter(UUID id) {
//		Filter filter = filterServiceImpl.getFilter(id);
//		return new ResponseEntity<Filter>(filter, HttpStatus.OK);
//	}
//
//	@Override
//	public ResponseEntity<Filter> updateFilter(@Valid PatchModel body, UUID id) {
//		Filter filter = filterServiceImpl.updateFilter(body, id);
//		return new ResponseEntity<Filter>(filter, HttpStatus.OK);
//	}
}
