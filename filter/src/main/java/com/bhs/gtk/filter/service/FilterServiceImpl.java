package com.bhs.gtk.filter.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.filter.model.FilterRequest;
import com.bhs.gtk.filter.model.FilterResponse;
import com.bhs.gtk.filter.persistence.EntityWriter;
import com.bhs.gtk.filter.persistence.FilterEntity;
import com.bhs.gtk.filter.util.Mapper;

@Service
public class FilterServiceImpl implements FilterService{
	
	@Autowired
	private EntityWriter entityWriter;
	
	@Autowired
	private Mapper mapper;

	public FilterResponse createFilter(@Valid FilterRequest filterRequest) {
		FilterEntity filterEntity = entityWriter.createFilter(filterRequest);
		return mapper.getFilterResponse(filterEntity);
	}
	

//	@Autowired
//	private FilterRepository filterRepository;
//	
//	@Autowired
//	private Mapper mapper;
//	
//	@Override
//	public Filter createFilter(Filter filter) {
//		FilterEntity filterEntity = filterRepository.save(mapper.getFilterEntityToPersists(filter));
//		return mapper.getFilter(filterEntity);
//	}
//
//	@Override
//	public Filter deleteFilter(UUID id) {
//		if(id == null) {
//			//throw exception
//			return null;
//		}
//		if(filterRepository.existsById(id)) {
//			FilterEntity filterEntity = filterRepository.findById(id).get();
//			filterRepository.delete(filterEntity);
//			return mapper.getFilter(filterEntity);
//		}
//		//throw exception
//		return null;
//	}
//
//	@Override
//	public List<Filter> getAllFilters() {
//		Iterator<FilterEntity> iterator = filterRepository.findAll().iterator();
//		List<Filter> filters = new ArrayList<>();
//		while(iterator.hasNext()) {
//			filters.add(mapper.getFilter(iterator.next()));
//		}
//		return filters;
//	}
//
//	@Override
//	public Filter getFilter(UUID id) {
//		if(id == null) {
//			//throw exception
//			return null;
//		}
//		Optional<FilterEntity> filterEntityContainer = filterRepository.findById(id);
//		if(filterEntityContainer.isPresent()) {
//			return mapper.getFilter(filterEntityContainer.get());
//		}
//		//throw exception filter not found
//		return null;
//	}
//
//	@Override
//	public Filter updateFilter(PatchModel patchModel, UUID id) {
//		// TODO Auto-generated method stub
//		if(patchModel == null || id == null) {
//			//throw exception if validation fails
//			return null;
//		}
//		Optional<FilterEntity> persistedFilterContainer = filterRepository.findById(id);
// 		if(persistedFilterContainer.isPresent()) {
// 			FilterEntity filterTobeUpdated = persistedFilterContainer.get();
// 			FilterEntity updatedFilterEntity = getUpdatedFilterEntity(patchModel,filterTobeUpdated);
// 			FilterEntity savedFilterEntity = filterRepository.save(updatedFilterEntity);
// 			return mapper.getFilter(savedFilterEntity);
// 		}
// 		//throw filter not found exception.
//		return null;
//	}
//
//	private FilterEntity getUpdatedFilterEntity(PatchModel patchModel, FilterEntity filterTobeUpdated) {
//	  //input validation.
//		FilterEntity updatedFilterEntity = filterTobeUpdated;
//		for(PatchData patchData : patchModel.getPatchData()) {
//			updatedFilterEntity = getUpdatedFilterEntity(patchData, updatedFilterEntity);
//		}
//		return updatedFilterEntity;
//	}
//
//	private FilterEntity getUpdatedFilterEntity(PatchData patchData, FilterEntity filterEntity) {
//		//input validation.
//		FilterEntity updatedFilterEntity = filterEntity;
//		if(!StringUtils.equals(patchData.getOperation().name(),OperationEnum.REPLACE.name())) {
//			//throw unsupported operation exception.
//			return null;
//		}
//		switch(patchData.getProperty()) {
//		case NAME: updatedFilterEntity.setName(patchData.getValue()); break;
//		case DESCRIPTION: updatedFilterEntity.setDescription(patchData.getValue()); break;
//		case CODE: updatedFilterEntity.setCode(patchData.getValue()); break;
//		case PARSE_TREE: updatedFilterEntity.setParseTree(patchData.getValue()); break;
//		default:
//			//throw unsupported property.
//		}
//		return updatedFilterEntity;
//	}

}
