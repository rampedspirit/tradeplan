package com.bhs.gtk.expression.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.OffsetDateTime;

import com.bhs.gtk.expression.model.ExecutableExpression;
import com.bhs.gtk.expression.model.Expression;
import com.bhs.gtk.expression.model.ExpressionGroup;
import com.bhs.gtk.expression.model.ExpressionType;
import com.bhs.gtk.expression.model.Function;
import com.bhs.gtk.expression.model.FunctionChain;
import com.bhs.gtk.expression.model.NumericValue;

@Component
	
public class Converter {
	public ExecutableExpression convertToExecutableExpression(String message) {
		JSONObject jsonObject = new JSONObject(message);
		String hash =  jsonObject.getString("hash");		
		String parseTree =  jsonObject.getString("parseTree");		
		String scripName =  jsonObject.getString("scripName");
		String status =  jsonObject.getString("status");
		String marketTimeAsString = jsonObject.getString("marketTime");
		Date marketTime = DateTimeUtils.toDate(OffsetDateTime.parse(marketTimeAsString).toInstant());
		return new ExecutableExpression(hash, parseTree, marketTime, scripName, status);
	}
	
	public Expression convertToExpression(String parseTree) {
		try {
			JSONObject jsonObject = new JSONObject(parseTree);
			if(jsonObject.isNull("type")) {
				throw new IllegalArgumentException("type=<expressionType> is missing in parseTree : "+parseTree);
			}
			String type = jsonObject.getString("type");
			Expression expression = updateExpression(getExpression(type), jsonObject);
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
			expression = new ExpressionGroup();
			break;
		case FUNCTIONCHAIN:
			expression = new FunctionChain();
			break;
		case VALUE:
			expression = new NumericValue();
			break;
		default:
			throw new IllegalArgumentException(type + " is not a supported arithmetic expression type");
		}
		return expression;
	}
	
	private Expression updateExpression(Expression expression, JSONObject evalObject) {
		Expression readyExpression = expression;
		if(readyExpression instanceof ExpressionGroup) {
			readyExpression = updateExpressionGroup(readyExpression, evalObject);
		}else if(readyExpression instanceof FunctionChain) {
			readyExpression = updateFunctionChain(readyExpression, evalObject);
		}else if(readyExpression instanceof NumericValue) {
			readyExpression = updateNumericValue(readyExpression, evalObject);
		}
		return readyExpression;
	}
	
	
	private Expression updateExpressionGroup(Expression expression, JSONObject evalObject) {
		ExpressionGroup expressionGroup = (ExpressionGroup)expression;
		JSONArray expressionArray = evalObject.getJSONArray("expressions");
		for(Object expJson : expressionArray ) {
			JSONObject nextEvalObject = (JSONObject)expJson;
			Expression nextExpression = getExpression(nextEvalObject.getString("type"));
			expressionGroup.addExpression(updateExpression(nextExpression, nextEvalObject));
		}
		expressionGroup.setOperation(getOperation(evalObject));
		return expressionGroup;
	}
	
	private String getOperation(JSONObject jsonObject) {
		String operation = StringUtils.EMPTY;
		if(jsonObject.isNull("operation")) {
			return operation;
		}
		return jsonObject.getString("operation");
	}
	
	private Expression updateFunctionChain(Expression expression, JSONObject evalObject) {
		FunctionChain functionChain = (FunctionChain) expression;
		JSONArray functions = evalObject.getJSONArray("functions");
		for (Object fn : functions) {
			JSONObject nextFunction = (JSONObject) fn;
			if(nextFunction.isNull("type") || !StringUtils.equals(nextFunction.getString("type"),"function")) {
				throw new IllegalArgumentException("type=function is missing in a function: "+nextFunction);
			}
			functionChain.addFunction(getFunction(nextFunction));
		}
		functionChain.setOperation(getOperation(evalObject));
		return functionChain;
	}
	
	private Function getFunction(JSONObject functionJson) {
		if(functionJson.isNull("name")) {
			throw new IllegalArgumentException("name=<xyz> is missing in function: "+ functionJson);
		}
		Function function = new Function(functionJson.getString("name"));
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
	
	private Expression updateNumericValue(Expression readyExpression, JSONObject jsonObject) {
		NumericValue numericValue = (NumericValue) readyExpression;
		double value = -99999;
		
		if(jsonObject.isNull("value")) {
			throw new IllegalArgumentException("value = <123> is missing in numeric expression :"+jsonObject);
		}
		String valueText = jsonObject.getString("value");
		try {
			value = Double.valueOf(valueText);
		}  catch (NumberFormatException numberFormatException) {
			throw new IllegalArgumentException("value = <123> is missing, <123> is not a proper number in : "+ valueText);
		}
		numericValue.setValue(value);
		numericValue.setOperation(getOperation(jsonObject));
		return numericValue;
	}

}
