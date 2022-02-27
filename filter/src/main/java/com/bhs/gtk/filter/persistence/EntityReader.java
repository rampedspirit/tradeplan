package com.bhs.gtk.filter.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityReader {
	
	@Autowired
	private FilterRepository filterRepository;
	
	@Autowired
	private ExpressionEntityRepository expressionEntityRepository;
	
	@Autowired
	private FilterResultRepository filterResultRepository;
	
	@Autowired
	private ArithmeticExpressionResultRepository arithmeticExpressionResultRepository;
	
	@Autowired
	private CompareExpressionResultRepository compareExpressionResultRepository;
	
	@Autowired
	private EntityObjectCreator entityObjectCreator;
	
	
	public List<FilterResultEntity> getAllFilterResultEntities(List<FilterResultId> filterResultIds) {
		Iterable<FilterResultEntity> resultsContainer = filterResultRepository.findAllById(filterResultIds);
		List<FilterResultEntity> filterResults = new ArrayList<>();
		for(FilterResultEntity result : resultsContainer) {
			filterResults.add(result);
		}
		return filterResults;
	}
	
	public List<FilterResultEntity> getAllFilterResultEntities(List<UUID> filterIds, Date marketTime, String scripName) {
		List<FilterResultId> filterResultIds = new ArrayList<>();
		for(UUID id : filterIds) {
			filterResultIds.add(entityObjectCreator.createFilterResultIdObject(id,marketTime,scripName));
		}
		return getAllFilterResultEntities(filterResultIds);
	}
	
	public FilterResultEntity getFilterResultEntity(FilterResultId filterResultId) {
		Optional<FilterResultEntity> filterResultContainer = filterResultRepository.findById(filterResultId);
		if(filterResultContainer.isPresent()) {
			return filterResultContainer.get();
		}
		return null;
	}
	
	public FilterResultEntity getFilterResultEntity(UUID filterId, Date marketTime, String scripName) {
		FilterResultId filterResultId = entityObjectCreator.createFilterResultIdObject(filterId,marketTime,scripName);
		FilterResultEntity filterResultEntity = getFilterResultEntity(filterResultId);
		return filterResultEntity;
	}
	
	
	public ExpressionEntity getExpressionEntity(String hash) {
		Optional<ExpressionEntity> expressionContainer = expressionEntityRepository.findById(hash);
		if(expressionContainer.isPresent()) {
			return expressionContainer.get();
		}
		return null;
	}
	
	public ArithmeticExpressionResultEntity getArithmeticExpressionResultEntity(ArithmeticExpressionResultId arResultId) {
		Optional<ArithmeticExpressionResultEntity> arithmeticExpressionResultContainer = arithmeticExpressionResultRepository.findById(arResultId);
		if(arithmeticExpressionResultContainer.isPresent()) {
			return arithmeticExpressionResultContainer.get();
		}
		return null;
	}
	
	public CompareExpressionResultEntity getCompareExpressionResultEntity(CompareExpressionResultId cmpResultId) {
		Optional<CompareExpressionResultEntity> compareExpressionResultContainer = compareExpressionResultRepository.findById(cmpResultId);
		if(compareExpressionResultContainer.isPresent()) {
			return compareExpressionResultContainer.get();
		}
		return null;
	}
	
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

	public List<ExpressionEntity> getExpressionEntities(UUID filterId) {
		FilterEntity filterEntity = getFilterEntity(filterId);
		if (filterEntity == null) {
			return null;
		}
		return filterEntity.getExpressions();
	}
	
	public ArithmeticExpressionResultEntity getArithmeticExpressionResultEntity(String hash, Date marketTime, String scripName) {
		ArithmeticExpressionResultId arResultId = entityObjectCreator.createArithmeticExpressionResultIdObject(hash,
				marketTime, scripName);
		ArithmeticExpressionResultEntity arResultEntity = getArithmeticExpressionResultEntity(arResultId);
		if(arResultEntity != null) {
			return arResultEntity;
		}
		return null;
	}
	
	public CompareExpressionResultEntity getCompareResultEntity(String cmpHash, Date marketTime, String scripName) {
		CompareExpressionResultEntity compareExpressionResultEntity  = null;
		CompareExpressionResultId compareResultId = entityObjectCreator.createCompareExpressionResultEntityIdObject(cmpHash, marketTime, scripName);
		compareExpressionResultEntity = getCompareExpressionResultEntity(compareResultId);
		return compareExpressionResultEntity; 
	}
	
}
