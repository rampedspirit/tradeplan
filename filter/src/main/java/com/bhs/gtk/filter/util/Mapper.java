package com.bhs.gtk.filter.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bhs.gtk.filter.model.ExpressionLocation;
import com.bhs.gtk.filter.model.ExpressionPosition;
import com.bhs.gtk.filter.model.FilterResponse;
import com.bhs.gtk.filter.model.Location;
import com.bhs.gtk.filter.model.Position;
import com.bhs.gtk.filter.persistence.FilterEntity;

@Component
public class Mapper {
	
	public Location getLocation(ExpressionLocation expLocation) {
		Location location = new Location();
		location.setStart(getPosition(expLocation.getStart()));
		location.setEnd(getPosition(expLocation.getEnd()));
		return location;
	}
	
	private Position getPosition(ExpressionPosition expPosition) {
		Position position = new Position();
		position.setOffset(expPosition.getOffset());
		position.setLine(expPosition.getLine());
		position.setColumn(expPosition.getColumn());
		return position;
	}
	
	public FilterResponse getFilterResponse(FilterEntity filterEntity) {
		FilterResponse response = new FilterResponse();
		if (filterEntity == null) {
			return response;
		}
		response.setId(filterEntity.getId());
		response.setName(filterEntity.getName());
		response.setDescription(filterEntity.getDescription());
		response.setCode(filterEntity.getCode());
		return response;
	}
	
	public List<FilterResponse> getAllFilterResponses(List<FilterEntity> filterEntities) {
		List<FilterResponse> responses = new ArrayList<>();
		for(FilterEntity entity : filterEntities) {
			responses.add(getFilterResponse(entity));
		}
		return responses;
	}
	
}
