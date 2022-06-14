package com.bhs.gtk.expression.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		return evaluateExpression(expression,scripName,marketTime);
	}

	private double evaluateExpression(Expression expression,String scripName, Date marketTime) {
		double result = Double.NEGATIVE_INFINITY;
		if(expression instanceof ExpressionGroup) {
			result = evaluateExpressionGroup((ExpressionGroup)expression, scripName, marketTime);
		} else if(expression instanceof FunctionChain) {
			result = evaluateFunctionChain((FunctionChain)expression, scripName, marketTime);
		}else if(expression instanceof NumericValue) {
			result = evaluateNumericExpression((NumericValue)expression);
		}
		return result;
	}

	private double evaluateFunctionChain(FunctionChain expression,String scripName, Date marketTime) {
		//TODO: write logic to execute functaionChain
		return 600.5;
	}

	private double evaluateExpressionGroup(ExpressionGroup expression,String scripName, Date marketTime) {
		Double result = null;
		List<Double> intermediateResult = new ArrayList<>();
		for( Expression exp : expression.getExpressions()) {
			double expResult = evaluateExpression(exp, scripName, marketTime);
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
		double result = Double.NEGATIVE_INFINITY;
		
		if(intermediateResult == null || intermediateResult.size() != 2) {
			throw new IllegalStateException("Operation "+operation+ " require excatly two operands");
		}
		
		double leftOperand = intermediateResult.get(0);
		double rightOperand = intermediateResult.get(1);
		
		switch (operation) {
		case "+":
			result = leftOperand + rightOperand;
			break;
		case "-":
			result = leftOperand - rightOperand;
			break;
		case "*":
			result = leftOperand * rightOperand;
			break;
		case "/":
			result = leftOperand / rightOperand;
			break;
		case "^":
			result = getPowerValue(leftOperand, rightOperand);
			break;
		default:
			new IllegalArgumentException("Operation " + operation + " not supported");
		}
		return result;
	}
	
private double getPowerValue(double leftOperand, double rightOperand) {
		double result = 1;
		for(int i=0; i<rightOperand; i++) {
			result = result *leftOperand; 
		}
		return result;
	}


	private double evaluateNumericExpression(NumericValue expression) {
		return expression.getValue();
	}


}
