package com.bhs.gtk.filter.util;

import org.springframework.stereotype.Component;

import com.bhs.gtk.filter.model.FilterResponse;
import com.bhs.gtk.filter.persistence.FilterEntity;

@Component
public class Mapper {
	
	public FilterResponse getFilterResponse(FilterEntity filterEntity) {
		FilterResponse response = new FilterResponse();
		response.setId(filterEntity.getId());
		response.setName(filterEntity.getName());
		response.setDescription(filterEntity.getDescription());
		response.setCode(filterEntity.getCode());
		return response;
	}
	
	
//	public Filter getFilter(FilterEntity filterEntity) {
//		Filter filter = new Filter();
//		if(filterEntity.getId() == null) {
//			//throw exception..
//		}
//		filter.setId(filterEntity.getId());
//		filter.setName(filterEntity.getName());
//		filter.setDescription(filterEntity.getDescription());
//		filter.setCode(filterEntity.getCode());
//		filter.setParseTree(filterEntity.getParseTree());
//		return filter;
//	}
//	
//	public FilterEntity getFilterEntityToPersists(Filter filter) {
//		FilterEntity filterEntity = new FilterEntity(filter.getName(), filter.getDescription(), filter.getCode(),
//				filter.getParseTree());
//		return filterEntity;
//	}

}
