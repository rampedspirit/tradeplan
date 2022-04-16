package com.bhs.gtk.filter.util;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class Extractor {
	
	public JSONObject removeLocationFromFilter(JSONObject parseTree) {
		if(parseTree.isNull("left")) {
			return removeLocationFromLogicalExpression(parseTree);
		}
		return removeLocationFromCmpExpression(parseTree);
	}

	private JSONObject removeLocationFromLogicalExpression(JSONObject parseTree) {
		if(parseTree.isNull("expressions")) {
			throw new IllegalArgumentException("Invalid parse tree :"+ parseTree);
		}
		JSONArray expressions = parseTree.getJSONArray("expressions");
		for (Object exp : expressions) {
			if (exp instanceof JSONObject) {
				JSONObject expObject = (JSONObject) exp;
				if (expObject.isNull("left")) {
					removeLocationFromLogicalExpression(expObject);
				}else {
					removeLocationFromCmpExpression(expObject);
				}
			}
		}
		parseTree.put("expressions", expressions);
		return parseTree;
	}

	private JSONObject removeLocationFromCmpExpression(JSONObject parseTree) {
		JSONObject leftObject = parseTree.getJSONObject("left");
		JSONObject rightObject = parseTree.getJSONObject("right");
		leftObject = removeLocationFromARexpression(leftObject);
		rightObject = removeLocationFromARexpression(rightObject);
		parseTree.put("left", leftObject);
		parseTree.put("right", rightObject);
		return parseTree;
	}
	
	public JSONObject removeLocationFromARexpression(JSONObject parseTree) {
		String type = parseTree.getString("type");
		if(StringUtils.equals(type, "functionChain") || StringUtils.equals(type, "value")) {
			parseTree.remove("location");
		}else if(StringUtils.equals(type, "expressionGroup")) {
			JSONArray expressionObjects = (JSONArray) parseTree.get("expressions");
			for(Object obj : expressionObjects) {
				if(obj instanceof JSONObject) {
					JSONObject expObj = (JSONObject) obj;
					String expType = expObj.getString("type");
					if(StringUtils.equals(expType, "expressionGroup")) {
						expObj = removeLocationFromARexpression(expObj);
					}else {
						expObj.remove("location");
					}
				}
			}
			parseTree.put("expressions", expressionObjects);
		}
		return parseTree;
	}

}
