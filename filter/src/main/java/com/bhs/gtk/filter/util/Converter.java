package com.bhs.gtk.filter.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.bhs.gtk.filter.model.ArithmeticExpression;
import com.bhs.gtk.filter.model.BooleanExpression;
import com.bhs.gtk.filter.model.CompareExpression;
import com.bhs.gtk.filter.model.LogicalExpression;

@Component
public class Converter {
	
	public String generateHash(String baseValue) {
		return DigestUtils.sha256Hex(baseValue);
	}
	
	public BooleanExpression convertToBooleanExpression(String parseTree) {
		JSONObject jsonObject = new JSONObject(parseTree);
		String operation = (String)jsonObject.get("operation");
		return createBooleanExpression(operation, parseTree, jsonObject);
	}


	private BooleanExpression createBooleanExpression(String operation, String parseTree, JSONObject jsonObject) {
		switch(operation) {
		case "AND":
		case "OR":
			return createLogicalExpression(operation, jsonObject);
		case"+":
		case "-":
		case "^":
		case ">":
		case "<":
		case ">=":
		case "<=":
			return createCompareExpression(operation,jsonObject);
		default: 
				 throw new IllegalArgumentException(operation+ " is in valid operation");
		}
	}

	private BooleanExpression createCompareExpression(String operation, JSONObject jsonObject) {
		JSONObject leftARobj = (JSONObject) jsonObject.get("left");
		JSONObject rightARobj = (JSONObject) jsonObject.get("right");
		
		String leftParseTree = leftARobj.toString();
		String rightParseTree = rightARobj.toString();
		
		String cmpHash = generateHash(jsonObject.toString());
		String leftHash = generateHash(leftParseTree);
		String rightHash = generateHash(rightParseTree);
		
		ArithmeticExpression leftARexp = new ArithmeticExpression(leftARobj.toString(), leftHash);
		ArithmeticExpression rightARexp = new ArithmeticExpression(rightARobj.toString(), rightHash);
		return new CompareExpression(jsonObject.toString(), operation, leftARexp, rightARexp,cmpHash);
	}

	private BooleanExpression createLogicalExpression(String operation, JSONObject jsonObject) {
		LogicalExpression logicalOperation = new LogicalExpression(operation);
		JSONArray expressions = (JSONArray) jsonObject.get("expressions");
		for( Object ex : expressions) {
			if(ex instanceof JSONObject) {
				JSONObject expAsObject = (JSONObject) ex;
				String operationOfObject = (String)expAsObject.get("operation");
				BooleanExpression bex = createBooleanExpression(operationOfObject, expAsObject.toString(), expAsObject);
				logicalOperation.getBooleanExpressions().add(bex);
				
			}
		}
		return logicalOperation;
	}

}
