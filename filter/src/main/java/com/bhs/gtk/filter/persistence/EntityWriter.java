package com.bhs.gtk.filter.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.filter.model.BooleanExpression;
import com.bhs.gtk.filter.model.ExecutionStatus;
import com.bhs.gtk.filter.model.FilterRequest;
import com.bhs.gtk.filter.util.Converter;

@Component
public class EntityWriter {

	@Autowired
	private FilterRepository filterRepository;

	@Autowired
	private ExpressionEntityRepository expressionEntityRepository;

	@Autowired
	private ArithmeticExpressionResultRepository arithmeticExpressionResultRepository;

	@Autowired
	private CompareExpressionResultRepository compareExpressionResultRepository;

	@Autowired
	private FilterResultRepository filterResultRepository;

	@Autowired
	private EntityObjectCreator entityObjectCreator;

	@Autowired
	private Converter converter;

	public boolean removePreviousAssociations(FilterEntity filterEntity) {
		deleteExpressionsNotAssociatedToAnyFilter();
		deleteFilterResultEntity(filterEntity.getId());
		return true;
	}

	public boolean deleteFilterEntity(FilterEntity filterEntity) {
		filterRepository.delete(filterEntity);
		return true;
	}

	public boolean deleteFilterResultEntity(UUID filterId) {
		List<FilterResultEntity> filterResults = filterResultRepository.findByFilterId(filterId);
		if (!filterResults.isEmpty()) {
			filterResultRepository.deleteAll(filterResults);
		}
		return true;
	}

	public boolean deleteExpressionsNotAssociatedToAnyFilter() {
		expressionEntityRepository.deleteAll(expressionEntityRepository.findAll());
		return true;
	}

	/**
	 * save expressions which are not already available in database
	 * @param expressions
	 * @return all expressionEntity that are equivalent to expressions.
	 */
	public List<ExpressionEntity> saveExpressionEntities(List<ExpressionEntity> expressions) {
		List<ExpressionEntity> expressionsInDatabase = new ArrayList<>();
		if(expressions == null || expressions.isEmpty()) {
			return expressionsInDatabase;
		}
		Set<String> hashes = expressions.stream().map( exp -> exp.getHash()).collect(Collectors.toSet());
		
		Iterable<ExpressionEntity> existingExpsContainer = expressionEntityRepository.findAllById(hashes);
		Set<String> hashesInDataBase = new HashSet<>();
		for(ExpressionEntity expEntity : existingExpsContainer) {
			expressionsInDatabase.add(expEntity);
			hashesInDataBase.add(expEntity.getHash());
		}
		hashes.removeAll(hashesInDataBase);
		
		List<ExpressionEntity> expressionsToBePersisted  = expressions.stream().filter(exp -> hashes.contains(exp.getHash())).collect(Collectors.toList());
		
		Iterable<ExpressionEntity> savedExpEntityContainer = expressionEntityRepository.saveAll(expressionsToBePersisted);
		
		for(ExpressionEntity expEnity : savedExpEntityContainer) {
			expressionsInDatabase.add(expEnity);
		}
		
		return expressionsInDatabase;
	}
	
	public FilterEntity saveFilterEntity(FilterEntity filterEntity) {
		return filterRepository.save(filterEntity);
	}

	public FilterResultEntity createFilterResultEntity(UUID filterId, Date marketTime, String scripName, String status,
			List<CompareExpressionResultEntity> compareResults) {
		FilterResultEntity filterEntity = new FilterResultEntity(filterId, marketTime, scripName, status);
		return filterResultRepository.save(filterEntity);
	}

	public List<FilterResultEntity> saveFilterResultEntities(List<FilterResultEntity> filterResults) {
		Iterable<FilterResultEntity> savedFilterResults = filterResultRepository.saveAll(filterResults);
		List<FilterResultEntity> filterResultEntities = new ArrayList<>();
		for(FilterResultEntity fResult : savedFilterResults) {
			filterResultEntities.add(fResult);
		}
		return filterResultEntities;
	}
	
	
	public List<CompareExpressionResultEntity> saveCompareExpressionResultEntities(
			List<CompareExpressionResultEntity> compareResults) {
		Iterable<CompareExpressionResultEntity> savedCmpResults = compareExpressionResultRepository
				.saveAll(compareResults);
		List<CompareExpressionResultEntity> cmpResults = new ArrayList<>();
		for (CompareExpressionResultEntity cr : savedCmpResults) {
			cmpResults.add(cr);
		}
		return cmpResults;
	}

	public ArithmeticExpressionResultEntity saveArithmeticExpressionResultEntity(ArithmeticExpressionResultEntity arExpResult) {
		return arithmeticExpressionResultRepository.save(arExpResult);
	}
	
	public List<ArithmeticExpressionResultEntity> saveArithmeticExpressionResultEntities(
			List<ArithmeticExpressionResultEntity> arResultEntities) {
		Iterable<ArithmeticExpressionResultEntity> savedARresults = arithmeticExpressionResultRepository
				.saveAll(arResultEntities);
		List<ArithmeticExpressionResultEntity> arResults = new ArrayList<>();
		for (ArithmeticExpressionResultEntity ar : savedARresults) {
			arResults.add(ar);
		}
		return arResults;
	}

	public List<ArithmeticExpressionResultEntity> queueARexpression(List<ExpressionEntity> arExpressions,
			Date marketTime, String scripName) {
		List<ArithmeticExpressionResultEntity> arResults = new ArrayList<>();
		for (ExpressionEntity ar : arExpressions) {
			arResults.add(new ArithmeticExpressionResultEntity(ar.getHash(), marketTime, scripName,
					ExecutionStatus.QUEUED.name()));
		}
		Iterable<ArithmeticExpressionResultEntity> savedARresults = arithmeticExpressionResultRepository
				.saveAll(arResults);
		List<ArithmeticExpressionResultEntity> queuedARresults = new ArrayList<>();
		for (ArithmeticExpressionResultEntity ar : savedARresults) {
			queuedARresults.add(ar);
		}
		return queuedARresults;
	}

	public FilterEntity createFilter(FilterRequest filter) {
		String name = filter.getName();
		String description = filter.getDescription();
		String code = filter.getCode();
		String parseTree = filter.getParseTree();
		FilterEntity filterEntity = new FilterEntity(name, description, code, parseTree);
		if (StringUtils.isNotEmpty(parseTree)) {
			BooleanExpression booleanExpression = converter.convertToBooleanExpression(parseTree);
			List<ExpressionEntity> expressionEntities = entityObjectCreator
					.createExpressionEntityObjects(booleanExpression);
			filterEntity.setExpressions(getUniqueExpressionEntities(expressionEntities));
		}
		return filterRepository.save(filterEntity);
	}

	private List<ExpressionEntity> getUniqueExpressionEntities(List<ExpressionEntity> expressionEntities) {
		List<ExpressionEntity> uniqueExpression = new ArrayList<>();
		List<String> hashValues = new ArrayList<>();
		for(ExpressionEntity exp : expressionEntities) {
			if(!hashValues.contains(exp.getHash())) {
				uniqueExpression.add(exp);
				hashValues.add(exp.getHash());
			}
		}
		return uniqueExpression;
	}

}
