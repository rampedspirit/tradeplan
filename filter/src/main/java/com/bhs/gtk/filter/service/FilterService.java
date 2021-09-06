package com.bhs.gtk.filter.service;

import java.util.List;
import java.util.UUID;

import com.bhs.gtk.filter.model.Filter;
import com.bhs.gtk.filter.model.PatchModel;

public interface FilterService {
	
	/**
	 * @param filter - {@link Filter} object that need to created.
	 * @return {@link Filter} if it is created and stored successfully, else throw custom exception.
	 * @throws InvalidInputException...
	 */
	public Filter createFilter(Filter filter);
	
	/**
	 * @param id of filter to be deleted
	 * @return Filter which was deleted successfully, else exception
	 * @throws resource not found exception
	 */
	public Filter deleteFilter(UUID id);
	
	/**
	 * @return all filters 
	 */
	public List<Filter> getAllFilters();
	
	/**
	 * @param id
	 * @return filter with given id.
	 */
	public Filter getFilter(UUID id);
	
	/**
	 * 
	 * @param patchModel
	 * @param id
	 * @return updated filter
	 */
	public Filter updateFilter(PatchModel patchModel, UUID id);
}
