package com.bhs.gtk.filter.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.filter.model.ArithmeticExpression;
import com.bhs.gtk.filter.model.BooleanExpression;
import com.bhs.gtk.filter.model.CompareExpression;
import com.bhs.gtk.filter.model.ExpressionResultResponse;
import com.bhs.gtk.filter.model.LogicalExpression;

@Component
public class EntityObjectCreator {

	//EntityReader and EntityObjectCreator are autowired each other. However, it is no
	//cyclic dependency because they are initialised via constructor. Consider to re-deign it if required.
	@Autowired
	private EntityReader entityReader;
	
	public FilterResultId createFilterResultIdObject(UUID filterId,Date marketTime,String scripName) {
		return new FilterResultId(filterId, marketTime, scripName);
	}
	
	public ArithmeticExpressionResultId createArithmeticExpressionResultIdObject(String hash,Date marketTime,String scripName) {
		return new ArithmeticExpressionResultId(hash, marketTime, scripName);
	}
	
	public CompareExpressionResultId createCompareExpressionResultEntityIdObject(String cmpHash, Date marketTime, String scripName) {
		return new CompareExpressionResultId(cmpHash, marketTime, scripName);
	}
	
	public List<CompareExpressionResultId> createCompareExpressionResultEntityIdObjects(
			List<ExpressionEntity> compareExpressions, Date marketTime, String scripName) {
		List<CompareExpressionResultId> cmpExpResultIds = new ArrayList<>();
		for(ExpressionEntity cmExp : compareExpressions) {
			cmpExpResultIds.add(new CompareExpressionResultId(cmExp.getHash(), marketTime, scripName));
		}
		return cmpExpResultIds;
	}
	
	public List<ExpressionEntity> createExpressionEntityObjects(BooleanExpression booleanExpression) {
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

	public List<ExpressionEntity> createExpressionEntityObjects(CompareExpression cmpExp) {
		List<ExpressionEntity> expressions = new ArrayList<>();
		
		String cmpHash = cmpExp.getHash();
		ExpressionEntity compareExp = entityReader.getExpressionEntity(cmpHash);
		if(compareExp == null) {
			compareExp = createExpressionEntityObject(cmpHash, cmpExp.getParseTree(),
					ExpressionResultResponse.TypeEnum.COMPARE_EXPRESSION.name());
		}
		
		ArithmeticExpression leftExpObject = cmpExp.getLeftArithmeticExpression();
		String leftARhash = leftExpObject.getHash();
		ExpressionEntity leftARexp = entityReader.getExpressionEntity(leftARhash);
		if(leftARexp == null) {
			leftARexp = createExpressionEntityObject(leftARhash, leftExpObject.getParseTree(),
					ExpressionResultResponse.TypeEnum.ARITHEMETIC_EXPRESSION.name());
		}
		
		ArithmeticExpression rightExpObject = cmpExp.getRightArithmeticExpression();
		String rightHash = rightExpObject.getHash();
		ExpressionEntity rightARexp = entityReader.getExpressionEntity(rightHash);
		if(rightARexp == null) {
			rightARexp = createExpressionEntityObject(rightHash, rightExpObject.getParseTree(),
					ExpressionResultResponse.TypeEnum.ARITHEMETIC_EXPRESSION.name());
		}
		
		expressions.add(compareExp);
		expressions.add(leftARexp);
		expressions.add(rightARexp);
		
		return expressions;
	}

	private ExpressionEntity createExpressionEntityObject( String hash,String parseTree, String type) {
		return new ExpressionEntity(hash, parseTree,type);
	}
	
}
