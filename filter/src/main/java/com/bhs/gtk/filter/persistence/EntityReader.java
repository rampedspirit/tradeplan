package com.bhs.gtk.filter.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityReader {
	
	@Autowired
	private FilterRepository filterRepository;
	
	public FilterEntity getFilterEntity(UUID id) {
		Optional<FilterEntity> filterContainer = filterRepository.findById(id);
		if(filterContainer.isPresent()) {
			return filterContainer.get();
		}
		return null;
	}
	
	public List<FilterEntity> getAllFilterEntites() {
		Iterable<FilterEntity> iterableFilters = filterRepository.findAll();
		List<FilterEntity> filters = new ArrayList<>();
		for(FilterEntity ft : iterableFilters) {
			filters.add(ft);
		}
		return filters;
	}

}
