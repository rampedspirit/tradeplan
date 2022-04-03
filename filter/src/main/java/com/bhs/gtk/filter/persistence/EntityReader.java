package com.bhs.gtk.filter.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.filter.model.ExpressionResultResponse;
import com.bhs.gtk.filter.util.Converter;

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
	
	@Autowired
	private Converter converter;
	
	
	public List<CompareExpressionResultEntity> getCmpExpResultEntities(FilterResultEntity filterResultEntity) {
		List<CompareExpressionResultEntity> cmpExpResults = new ArrayList<>();
		UUID filterId = filterResultEntity.getFilterId();
		Date marketTime = filterResultEntity.getMarketTime();
		String scripName = filterResultEntity.getScripName();
		List<ExpressionEntity> expressions = getExpressionEntities(filterId);
		if(expressions == null || expressions.isEmpty()) {
			return cmpExpResults;
		}
		List<ExpressionEntity> compareExpressions = expressions.stream().filter(e -> StringUtils.equals(e.getType(), ExpressionResultResponse.TypeEnum.COMPARE_EXPRESSION.name()))
		.collect(Collectors.toList());
		List<CompareExpressionResultId> cmpExpResultIds = entityObjectCreator.createCompareExpressionResultEntityIdObjects(compareExpressions, marketTime, scripName);
		return getCompareExpressionResultEntities(cmpExpResultIds);
	}
	
	/**
	 * 
	 * @param cmpResultEntity
	 * @return list of {@link ArithmeticExpressionResultEntity}, left of compareExpression is at 0th index, and right at 1st index. 
	 */
	public List<ArithmeticExpressionResultEntity> getARexpressionResultEntities(CompareExpressionResultEntity cmpResultEntity) {
		List<ArithmeticExpressionResultEntity> arResults = new ArrayList<>();
		String hash = cmpResultEntity.getHash();
		Date mTime = cmpResultEntity.getMarketTime();
		String scripName = cmpResultEntity.getScripName();
		ExpressionEntity cmpExpression = getExpressionEntity(hash);
		if(cmpExpression == null) {
			return arResults;
		}
		String parseTree = cmpExpression.getParseTree();
		String leftHash = converter.getARexpHashFromCompareParseTree(parseTree, true);
		String rightHash = converter.getARexpHashFromCompareParseTree(parseTree, false);
		
		ArithmeticExpressionResultEntity leftResult = getArithmeticExpressionResultEntity(leftHash, mTime, scripName);
		ArithmeticExpressionResultEntity rightResult = getArithmeticExpressionResultEntity(rightHash, mTime, scripName);
		
		if(leftResult == null || rightResult == null) {
			return arResults;
		}
		arResults.add(leftResult);
		arResults.add(rightResult);
		return arResults;
	}
	
	
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
	
	public List<CompareExpressionResultEntity> getCompareExpressionResultEntities(List<CompareExpressionResultId> cmpResultIds) {
		List<CompareExpressionResultEntity> cmpExpResultEntities = new ArrayList<>();
		if(cmpResultIds == null || cmpResultIds.isEmpty() || cmpResultIds.contains(null)) {
			return cmpExpResultEntities;
		}
		Iterable<CompareExpressionResultEntity> resultsIterator = compareExpressionResultRepository.findAllById(cmpResultIds);
		for(CompareExpressionResultEntity cmpResult : resultsIterator) {
			cmpExpResultEntities.add(cmpResult);
		}
		return cmpExpResultEntities;
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
