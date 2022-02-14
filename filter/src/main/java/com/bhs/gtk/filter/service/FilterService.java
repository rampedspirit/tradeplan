package com.bhs.gtk.filter.service;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.bhs.gtk.filter.model.ExecutableFilter;
import com.bhs.gtk.filter.model.FilterRequest;
import com.bhs.gtk.filter.model.FilterResponse;
import com.bhs.gtk.filter.model.FilterResultResponse;
import com.bhs.gtk.filter.persistence.ArithmeticExpressionResultEntity;
import com.bhs.gtk.filter.persistence.FilterResultEntity;

public interface FilterService {
	public FilterResponse createFilter(@Valid FilterRequest filterRequest);
	public FilterResponse getFilter(UUID id);
	public List<FilterResponse> getAllFilters();
	public FilterResultEntity executeFilter(ExecutableFilter executableFilter);
	public List<ArithmeticExpressionResultEntity> runArithmeticExpressionResultEntities(List<ArithmeticExpressionResultEntity> arResultEntitites);
	public FilterResultResponse getFilterResult(UUID filterId, String marketTime, String scripName);
}
