package com.bhs.gtk.filter.service;

import javax.validation.Valid;

import com.bhs.gtk.filter.model.FilterRequest;
import com.bhs.gtk.filter.model.FilterResponse;

public interface FilterService {
	public FilterResponse createFilter(@Valid FilterRequest filterRequest);
}
