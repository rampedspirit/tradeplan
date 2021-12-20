package com.bhs.gtk.expression.service;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.bhs.gtk.expression.model.Expression;
import com.bhs.gtk.expression.model.ExpressionGroup;
import com.bhs.gtk.expression.model.ExpressionType;
import com.bhs.gtk.expression.model.Function;
import com.bhs.gtk.expression.model.FunctionChain;
import com.bhs.gtk.expression.model.NumericValue;

/**
 * Responsibility of this class is to convert evaluation request to evaluation model.
 * 
 * Evaluation request is in the form of JSON, received over message queue.
 * The request will be converted to Evaluation model ({@link Expression}},
 * which will be used to evaluate the logic and get result.
 */

@Service
public class EvaluationRequestConverter {
	
	public Expression convert(String evalRequest) {
		try {
			JSONObject evalObject = new JSONObject(evalRequest);
			String type = (String) evalObject.get("type");
			Expression expression = prepareExpression(getExpression(type), evalObject);
			return expression;
		}catch (JSONException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	private Expression getExpression(String type) {
		Expression expression = null;
		switch (ExpressionType.valueOf(type.toUpperCase())) {
		case EXPRESSIONGROUP:
				expression = new ExpressionGroup(); break;
		case FUNCTIONCHAIN:
			expression = new FunctionChain(); break;
		case VALUE:
			expression = new NumericValue(); break;
	   default:
		   //throw exception
		   return null;
		}
		return expression;
	}
	
	private Expression prepareExpression(Expression expression, JSONObject evalObject) {
		Expression readyExpression = expression;
		if(readyExpression instanceof ExpressionGroup) {
			readyExpression = updateExpressionGroup(readyExpression, evalObject);
		}else if(readyExpression instanceof FunctionChain) {
			readyExpression = updateFunctionChain(readyExpression, evalObject);
		}else if(readyExpression instanceof NumericValue) {
			readyExpression = updateNumericValue(readyExpression, evalObject);
		}
		readyExpression.setEvalTime(getEvaluationTime(evalObject));
		readyExpression.setScrip(getScrip(evalObject));
		return readyExpression;
	}
	
	
	private String getScrip(JSONObject evalObject) {
		String scrip = StringUtils.EMPTY;
		try {
			scrip = (String) evalObject.get("scrip");
		} catch (JSONException jsonException) {
			// no scrip exists.
			return StringUtils.EMPTY;
		}
		return scrip;
	}
	
	private Expression updateExpressionGroup(Expression expression, JSONObject evalObject) {
		ExpressionGroup expressionGroup = (ExpressionGroup)expression;
		JSONArray expressionArray = evalObject.getJSONArray("expressions");
		for(Object expJson : expressionArray ) {
			JSONObject nextEvalObject = (JSONObject)expJson;
			String type = (String)nextEvalObject.get("type");
			Expression nextExpression = prepareExpression(getExpression(type), nextEvalObject);
			expressionGroup.addExpression(nextExpression);
		}
		expressionGroup.setOperation(getOperation(evalObject));
		return expressionGroup;
	}
	
	private String getOperation(JSONObject evalObject) {
		String operation = StringUtils.EMPTY;
		try {
			operation = (String) evalObject.get("operation");
		} catch (JSONException jsonException) {
			// no operation exists.
			return StringUtils.EMPTY;
		}
		return operation;
	}
	
	private Expression updateFunctionChain(Expression expression, JSONObject evalObject) {
		FunctionChain functionChain = (FunctionChain) expression;
		JSONArray functions = evalObject.getJSONArray("functions");
		for (Object fn : functions) {
			JSONObject nextFunction = (JSONObject) fn;
			if (StringUtils.equals("function", nextFunction.getString("type"))) {
				functionChain.addFunction(getFunction(nextFunction));
			} else {
				// no other type expected here. throw illegal argument exception.
			}
		}
		functionChain.setOperation(getOperation(evalObject));
		return functionChain;
	}
	
	private Function getFunction(JSONObject functionJson) {
		Function function = new Function((String)functionJson.get("name"));
		//Arguments is array of array. Hence, two loops are required.
		//Example : "args": [ [ "15m", "0", "-2" ] ]
		//TODO: Fix the array of array issue from grammar.
		for( Object argumentArray: functionJson.getJSONArray("args"))  {
			for(Object arg : ((JSONArray)argumentArray)) {
				function.addArgument((String)arg);
			}
		}
		return function;
	}
	
	private Expression updateNumericValue(Expression readyExpression, JSONObject evalObject) {
		NumericValue numericValue = (NumericValue) readyExpression;
		double value = -99999;
		try {
			String valueText = (String) evalObject.get("value");
			value = Double.valueOf(valueText);
		} catch (JSONException jsonException) {
//			throw new Exception("This should not happen, for type 'value', numeric value should always present.\r\n" + 
//					"Either grammar is changed or has error or grammar text is modified from\r\n" + 
//					"outside the system.");
		} catch (NumberFormatException numberFormatException) {
//			throw new Exception("This should not happen, for type 'value', numeric value should always present in correct format.\r\n" + 
//					"Either grammar is changed or has error or grammar text is modified from\r\n" + 
//					"outside the system.");
		}
		numericValue.setValue(value);
		numericValue.setOperation(getOperation(evalObject));
		return numericValue;
	}
	
	
	private String getEvaluationTime(JSONObject evalObject) {
		String evalTime = StringUtils.EMPTY;
		try {
			evalTime = (String) evalObject.get("evalTime");
		} catch (JSONException jsonException) {
			// no eval time exists.
			return StringUtils.EMPTY;
		}
		return evalTime;
	}
	

}
