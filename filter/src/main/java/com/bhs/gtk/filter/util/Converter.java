package com.bhs.gtk.filter.util;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.OffsetDateTime;

import com.bhs.gtk.filter.model.ArithmeticExpression;
import com.bhs.gtk.filter.model.BooleanExpression;
import com.bhs.gtk.filter.model.CompareExpression;
import com.bhs.gtk.filter.model.ExpressionLocation;
import com.bhs.gtk.filter.model.ExpressionPosition;
import com.bhs.gtk.filter.model.FilterResultResponse;
import com.bhs.gtk.filter.model.LogicalExpression;
import com.bhs.gtk.filter.model.communication.ArithmeticExpressionResult;
import com.bhs.gtk.filter.model.communication.ExecutableFilter;

@Component
public class Converter {
	
	public ArithmeticExpressionResult convertToARexpressionResult(String message) {
		try {
			JSONObject jsonObject = new JSONObject(message);
			String hash = jsonObject.getString("hash");
			String scripName = jsonObject.getString("scripName");
			String marketTimeAsString = jsonObject.getString("marketTime");
			Date marketTime = DateTimeUtils.toDate(OffsetDateTime.parse(marketTimeAsString).toInstant());
			String status = jsonObject.getString("status");
			return new ArithmeticExpressionResult(hash, scripName, marketTime, status);
		}catch (JSONException jsonException) {
			//log exception
			throw new IllegalStateException("Convertion of message from ES in FS failed with JSONexception. Message = "+ message);
		}
	}
	
	/**
	 * @param parseTree
	 * @return value of key  'operation' in the json (parseTree), EMPTY string if the no operation found.
	 */
	public String getOperationFromParseTree(String parseTree) {
		JSONObject jsonObject = new JSONObject(parseTree);
		if (jsonObject.isNull("operation")) {
			throw new IllegalArgumentException("No operation found in parseTree = "+parseTree);
		}
		return jsonObject.getString("operation");
	}
	
	public String getARexpHashFromCompareParseTree(String parseTree, boolean leftAR) {
		JSONObject jsonObject = new JSONObject(parseTree);
		JSONObject arExp;
		if(leftAR) {
			arExp = (JSONObject) jsonObject.get("left");
		}else {
			arExp = (JSONObject) jsonObject.get("right");
		}
		return generateHash(arExp.toString());
	}
	
	
	public ExecutableFilter convertToExecutableFilter(String message) {
		JSONObject jsonObject = new JSONObject(message);
		UUID filterId = UUID.fromString((String)jsonObject.get("filterId"));
		String scripName = (String)jsonObject.get("scripName");
		String status = FilterResultResponse.FilterResultEnum.QUEUED.name();
		String marketTimeAsString = (String)jsonObject.get("marketTime");
		Date marketTime = DateTimeUtils.toDate(OffsetDateTime.parse(marketTimeAsString).toInstant());
		return new ExecutableFilter(filterId, marketTime, scripName, status);
	}
	
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
		case "=":
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
		
		ExpressionLocation leftARlocation = getLocation(leftARobj);
		ExpressionLocation rightARlocation = getLocation(rightARobj);
		ExpressionLocation cmpExpLocation = getCompareExpressionLocation(leftARlocation, rightARlocation);
		
		JSONObject leftARobjWithoutLocation = getJSONobjectWithOutLocation(leftARobj);
		JSONObject rightARobjWithoutLocation = getJSONobjectWithOutLocation(rightARobj);
		
		String leftParseTree = leftARobjWithoutLocation.toString();
		String rightParseTree = rightARobjWithoutLocation.toString();
		String cmpExParseTree = jsonObject.toString();
		
		String cmpHash = generateHash(cmpExParseTree);
		String leftHash = generateHash(leftParseTree);
		String rightHash = generateHash(rightParseTree);

		ArithmeticExpression leftARexp = new ArithmeticExpression(leftParseTree, leftHash, leftARlocation);
		ArithmeticExpression rightARexp = new ArithmeticExpression(rightParseTree, rightHash, rightARlocation);
		return new CompareExpression(cmpExParseTree, operation, leftARexp, rightARexp, cmpHash, cmpExpLocation);
	}

	private JSONObject getJSONobjectWithOutLocation(JSONObject arObject) {
		String type = (String)arObject.get("type");
		if(StringUtils.equals(type, "functionChain") || StringUtils.equals(type, "value")) {
			arObject.remove("location");
		}else if(StringUtils.equals(type, "expressionGroup")) {
			JSONArray expressionObjects = (JSONArray) arObject.get("expressions");
			for(Object obj : expressionObjects) {
				if(obj instanceof JSONObject) {
					JSONObject jsonObject = (JSONObject) obj;
					jsonObject.remove("location");
				}
			}
			arObject.put("expressions", expressionObjects);
		}
		return arObject;
	}

	private ExpressionLocation getCompareExpressionLocation(ExpressionLocation leftARlocation, ExpressionLocation rightARlocation) {
		ExpressionPosition start = leftARlocation.getStart();
		ExpressionPosition end = rightARlocation.getEnd();
		return new ExpressionLocation(start, end);
	}

	private ExpressionLocation getLocation(JSONObject arExp) {
		String type = (String)arExp.get("type");
		Object firstFunction = null;
		Object lastFunction = null;
		if(StringUtils.equals(type, "functionChain") || StringUtils.equals(type, "value")) {
			firstFunction = arExp;
			lastFunction = arExp;
		}else if(StringUtils.equals(type, "expressionGroup")) {
			JSONArray expressionObjects = (JSONArray) arExp.get("expressions");
			int numberOfFunctions = expressionObjects.length();
			firstFunction = getExpression(expressionObjects.get(0),true);			
			lastFunction = getExpression(expressionObjects.get(numberOfFunctions - 1),false);
		}
		if(!(firstFunction instanceof JSONObject) || !(lastFunction instanceof JSONObject) ) {
			return null;
		}
		return getArithmeticExpressionLocation((JSONObject) firstFunction, (JSONObject) lastFunction);
	}

	private JSONObject getExpression(Object expGroup, boolean start) {
		
		JSONObject jsonObj = null;
		if(expGroup instanceof JSONObject) {
			jsonObj = (JSONObject) expGroup;
		}else {
			throw new IllegalArgumentException(expGroup +" is not a valid JSONobject");
		}
		String type  = jsonObj.getString("type");
		
		if(StringUtils.equals(type, "functionChain") || StringUtils.equals(type, "value")) {
			return jsonObj;
		}else if(StringUtils.equals(type, "expressionGroup")) {
			JSONArray expressionObjects = (JSONArray) jsonObj.get("expressions");
			int numberOfExp = expressionObjects.length();
			if(start) {
				jsonObj = expressionObjects.getJSONObject(0);
			}else {
				jsonObj = expressionObjects.getJSONObject(numberOfExp - 1);
			}
			type  = jsonObj.getString("type");
			if(StringUtils.equals(type, "expressionGroup")) {
				return getExpression(jsonObj, start);
			}
		}
		return jsonObj;
	}

	private ExpressionLocation getArithmeticExpressionLocation(JSONObject firstFunction, JSONObject lastFunction) {
		
		JSONObject firstFunctionLocationObject = (JSONObject) firstFunction.get("location");
		JSONObject startPosition = (JSONObject) firstFunctionLocationObject.get("start");
		
		JSONObject lastFunctionLocationObject = (JSONObject) lastFunction.get("location");
		JSONObject endPosition = (JSONObject) lastFunctionLocationObject.get("end");
		
		return getLocation(startPosition, endPosition);
	}

	private ExpressionLocation getLocation(JSONObject startPosition, JSONObject endPosition) {
		ExpressionPosition start = getPostion(startPosition);
		ExpressionPosition end = getPostion(endPosition);
		return new ExpressionLocation(start, end);
	}

	private ExpressionPosition getPostion(JSONObject position) {
		int offset = (int)position.get("offset"); 
		int line = (int)position.get("line");
		int column = (int)position.get("column");
		return new ExpressionPosition(offset, line, column);
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
