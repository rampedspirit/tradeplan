package com.bhs.gtk.expression.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EntityReader {
	
	@Autowired
	private ExpressionRespository expressionRespository;
	
	@Autowired
	private ExpressionResultRepository expressionResultRepository;
	
	public ExpressionEntity getExpressionEntity(String hash) {
		Optional<ExpressionEntity> expressionEntityContainer = expressionRespository.findById(hash);
		if (expressionEntityContainer.isPresent()) {
			return expressionEntityContainer.get();
		}
		return null;
	}
	
	public ExpressionResultEntity getExpressionResultEntity(ExpressionResultId expressionResultId) {
		Optional<ExpressionResultEntity> expressionResultContainer = expressionResultRepository.findById(expressionResultId);
		if(expressionResultContainer.isPresent()) {
			return expressionResultContainer.get();
		}
		return null;
	}
}
