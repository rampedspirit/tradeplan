package com.bhs.gtk.expression.persistence;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.expression.model.ExecutableExpression;
import com.bhs.gtk.expression.service.Constants;

@Component
public class EntityWriter {
	
	@Autowired
	private ExpressionRespository expressionRespository;
	
	@Autowired
	private ExpressionResultRepository expressionResultRepository;
	
	public ExpressionEntity queueExpressionForExecution(ExecutableExpression expression) {
		String hash =  expression.getHash();
		String parseTree = expression.getParseTree();
		Date marketTime = expression.getMarketTime();
		String scripName = expression.getScripName();
		ExpressionResultEntity resultEntity = new ExpressionResultEntity(hash, marketTime, scripName, Constants.QUEUED);
		ExpressionEntity expressionEntity = new ExpressionEntity(hash, parseTree);
		expressionEntity.getExpressionResults().add(resultEntity);
		return expressionRespository.save(expressionEntity);
	}
	
	
	public ExpressionEntity queueExpressionResultForExecution(ExecutableExpression expression, ExpressionEntity expressionEntity) {
		String hash =  expression.getHash();
		Date marketTime = expression.getMarketTime();
		String scripName = expression.getScripName();
		ExpressionResultEntity resultEntity = new ExpressionResultEntity(hash, marketTime, scripName, Constants.QUEUED);
		expressionEntity.getExpressionResults().add(resultEntity);
		return expressionRespository.save(expressionEntity);
	}
	
	public ExpressionResultEntity saveExpressionResult(ExpressionResultEntity expressionResultEntity) {
		return expressionResultRepository.save(expressionResultEntity);
	}

}
