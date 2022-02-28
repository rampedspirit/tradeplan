package com.bhs.gtk.expression.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.expression.messaging.MessageProducer;
import com.bhs.gtk.expression.messaging.MessageType;
import com.bhs.gtk.expression.model.ExecutableExpression;
import com.bhs.gtk.expression.model.Expression;
import com.bhs.gtk.expression.model.ExpressionGroup;
import com.bhs.gtk.expression.model.FunctionChain;
import com.bhs.gtk.expression.model.NumericValue;
import com.bhs.gtk.expression.persistence.EntityObjectCreator;
import com.bhs.gtk.expression.persistence.EntityReader;
import com.bhs.gtk.expression.persistence.EntityWriter;
import com.bhs.gtk.expression.persistence.ExpressionEntity;
import com.bhs.gtk.expression.persistence.ExpressionResultEntity;
import com.bhs.gtk.expression.persistence.ExpressionResultId;
import com.bhs.gtk.expression.util.Converter;

@Service
public class ExpressionServiceImpl implements ExpressionService {

	@Autowired
	private EntityReader entityReader;
	
	@Autowired
	private EntityWriter entityWriter;
	
	@Autowired
	private EntityObjectCreator entityObjectCreator;
	
	@Autowired
	private Converter converter;
	
	@Autowired
	private MessageProducer messageProducer;
	
	@Override
	public boolean ExecuteExpression(ExecutableExpression expression) {
		ExpressionEntity expressionEntity = getExpressionEntityForExecution(expression);
		if(expressionEntity == null) {
			//Log that the expression is already considered for execution from different request
			return true;
		}
		String scripName = expression.getScripName();
		Date marketTime = expression.getMarketTime();
		String hash = expression.getHash();
		double result = evaluateExpression(expressionEntity, scripName, marketTime);
		
		ExpressionResultId resultId = entityObjectCreator.createExpressionResultIdObject(hash, marketTime, scripName);
		ExpressionResultEntity expressionResultEntity = entityReader.getExpressionResultEntity(resultId);
		
		if(expressionResultEntity == null) {
			throw new IllegalStateException("Expression result entity should not null for hash = " + hash
					+ " scripName = " + scripName + " marketTime = " + marketTime);
		}
		String status = Double.toString(result);
		expressionResultEntity.setStatus(status);
		ExpressionResultEntity savedExpressionResult = entityWriter.saveExpressionResult(expressionResultEntity);
		Map<String, String> jsonMap = getEntityMapForJson(savedExpressionResult);
		JSONObject jsonObject = new JSONObject(jsonMap);
		String message = jsonObject.toString();
		if(messageProducer.sendMessage(message, MessageType.EXECUTION_RESPONSE)) {
			//TODO: log success message
			System.err.println(" ES: Message sent successfully : "+ message);
		}else {
			//TODO: log failure message
			System.err.println(" failed to request "+ message);
		}
		return true;
	}
	
	private Map<String, String> getEntityMapForJson(ExpressionResultEntity entity) {
		Map<String, String> entityMap = new HashMap<>();
		entityMap.put("hash",entity.getHash().toString());
		entityMap.put("marketTime",entity.getMarketTimeAsOffsetDateTime().toString());
		entityMap.put("scripName",entity.getScripName());
		entityMap.put("status",entity.getStatus());
		return entityMap;
	}

	/**
	 * @param expression
	 * @return {@link ExpressionEntity} which need to be executed or NULL if the
	 *         execution is already requested by other call.
	 */
	private ExpressionEntity getExpressionEntityForExecution(ExecutableExpression expression) {
		String hash = expression.getHash();
		String scripName = expression.getScripName();
		Date marketTime = expression.getMarketTime();
		
		ExpressionEntity expressionEntity = entityReader.getExpressionEntity(hash);
		ExpressionEntity savedExpressionEntity = null;
		if (expressionEntity != null) {
			ExpressionResultId resultId = entityObjectCreator.createExpressionResultIdObject(hash, marketTime,
					scripName);
			ExpressionResultEntity expressionResult = entityReader.getExpressionResultEntity(resultId);
			if (expressionResult == null) {
				savedExpressionEntity = entityWriter.queueExpressionResultForExecution(expression, expressionEntity);
			}else {
				return null;
			}
		}else {
			savedExpressionEntity = entityWriter.queueExpressionForExecution(expression);
		}
		return savedExpressionEntity;
	}
	
	private double evaluateExpression(ExpressionEntity expressionEntity, String scripName, Date marketTime) {
		//TODO logic to be completed...
		String parseTree = expressionEntity.getParseTree();
		Expression expression = converter.convertToExpression(parseTree);
		return evaluateExpression(expression);
	}

	private double evaluateExpression(Expression expression) {
		double result = -99999;
		if(expression instanceof ExpressionGroup) {
			result = evaluateExpressionGroup((ExpressionGroup)expression);
		} else if(expression instanceof FunctionChain) {
			result = evaluateFunctionChain((FunctionChain)expression);
		}else if(expression instanceof NumericValue) {
			result = evaluateNumericExpression((NumericValue)expression);
		}
		return result;
	}

	private double evaluateFunctionChain(FunctionChain expression) {
		//TODO: write logic to execute functaionChain
		return RandomUtils.nextDouble(0.0, 100.0);
		
	}

	private double evaluateExpressionGroup(ExpressionGroup expression) {
		Double result = null;
		List<Double> intermediateResult = new ArrayList<>();
		for( Expression exp : expression.getExpressions()) {
			double expResult = evaluateExpression(exp);
			intermediateResult.add(expResult);
			if(StringUtils.isNotBlank(exp.getOperation())) {
				result = calculateResult(exp.getOperation(), intermediateResult);
				intermediateResult.clear();
				intermediateResult.add(result);
			}
		}
		return result;
	}

	private double calculateResult(String operation, List<Double> intermediateResult) {
		//TODO: write logic
		return RandomUtils.nextDouble(0.0, 100.0);
	}

	private double evaluateNumericExpression(NumericValue expression) {
		return expression.getValue();
	}


}
