package com.bhs.gtk.filter.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.filter.model.ArithmeticExpression;
import com.bhs.gtk.filter.model.BooleanExpression;
import com.bhs.gtk.filter.model.CompareExpression;
import com.bhs.gtk.filter.model.ExpressionType;
import com.bhs.gtk.filter.model.FilterRequest;
import com.bhs.gtk.filter.model.LogicalExpression;
import com.bhs.gtk.filter.util.Converter;

@Component
public class EntityWriter {
	
	@Autowired
	private FilterRepository filterRepository;
	
	@Autowired
	private Converter converter;
	
	public FilterEntity createFilter(FilterRequest filter) {
		String name = filter.getName();
		String description = filter.getDescription();
		String code = filter.getCode();
		String parseTree = filter.getParseTree();
		//TODO: handle validations
		FilterEntity filterEntity = new FilterEntity(name, description, code, parseTree);
		BooleanExpression booleanExpression = converter.convertToBooleanExpression(parseTree);
		List<ExpressionEntity> expressionEntities = createExpressionEntityObjects(booleanExpression);
		filterEntity.setExpressions(expressionEntities);
		return filterRepository.save(filterEntity);
	}
	
	private List<ExpressionEntity> createExpressionEntityObjects(BooleanExpression booleanExpression) {
		List<ExpressionEntity> expressions = new ArrayList<>();
		if(booleanExpression instanceof LogicalExpression) {
			LogicalExpression logicalExpression = (LogicalExpression) booleanExpression;
			for( BooleanExpression bexp : logicalExpression.getBooleanExpressions()) {
				expressions.addAll(createExpressionEntityObjects(bexp));
			}
		}else if(booleanExpression instanceof CompareExpression) {
			CompareExpression cmpExp = (CompareExpression) booleanExpression;
			expressions.addAll(createExpressionEntityObjects(cmpExp));
		}
		return expressions;
	}

	private List<ExpressionEntity> createExpressionEntityObjects(CompareExpression cmpExp) {
		List<ExpressionEntity> expressions = new ArrayList<>();
		
		expressions.add(new ExpressionEntity(cmpExp.getHash(), cmpExp.getParseTree(),
				ExpressionType.COMPARE_EXPRESSION.name()));
		
		ArithmeticExpression leftExp = cmpExp.getLeftArithmeticExpression();
		expressions.add(new ExpressionEntity(leftExp.getHash(), leftExp.getParseTree(),
				ExpressionType.ARITHEMETIC_EXPRESSION.name()));
		
		ArithmeticExpression rightExp = cmpExp.getRightArithmeticExpression();
		expressions.add(new ExpressionEntity(rightExp.getHash(), rightExp.getParseTree(),
				ExpressionType.ARITHEMETIC_EXPRESSION.name()));
		
		return expressions;
	}

}
