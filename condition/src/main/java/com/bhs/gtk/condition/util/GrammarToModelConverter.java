package com.bhs.gtk.condition.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.bhs.gtk.condition.model.BooleanExpression;
import com.bhs.gtk.condition.model.ConditionExpression;
import com.bhs.gtk.condition.model.FilterExpression;

@Component
public class GrammarToModelConverter {
	
	public ConditionExpression convert(String parseTree) {
		try {
			if(isSingleFilterExpression(parseTree)) {
				return new FilterExpression(parseTree);
			}
			JSONObject expressionObject = new JSONObject(parseTree);
			BooleanExpression booleanExpression = getBooleanExpression(expressionObject);
			return booleanExpression;
		}catch (JSONException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	private BooleanExpression getBooleanExpression(JSONObject expressionObject) {
		String operation = (String) expressionObject.get("operation");
		BooleanExpression booleanExpression = new BooleanExpression(operation);
		JSONArray jsonExpressions = expressionObject.getJSONArray("expressions");
		List<ConditionExpression> expressions = getConditionExpressions(jsonExpressions);
		booleanExpression.getConditionExpressions().addAll(expressions);
		return booleanExpression;
	}
	
	private List<ConditionExpression> getConditionExpressions(JSONArray jsonExpressions) {
		List<ConditionExpression> conditionExpressions = new ArrayList<>();
		for( Object obj : jsonExpressions) {
			 if(obj instanceof String && isSingleFilterExpression((String)obj)) {
				 conditionExpressions.add(new FilterExpression((String)obj));
			 }else if(obj instanceof JSONObject) {
				 conditionExpressions.add(getBooleanExpression((JSONObject)obj));
			 }
		}
		return conditionExpressions;
	}

	private boolean isSingleFilterExpression(String parseTree) {
		if(!StringUtils.startsWith(parseTree, "{")) {
		   try {
			   UUID.fromString(parseTree);
		   }catch(IllegalArgumentException exception) {
			   throw new IllegalArgumentException(); 
		   }
			return true;
		}
		return false;
	}

	public Set<UUID> getFilterIds(String parseTree) {
		Set<UUID> filterIds = new HashSet<>();
		ConditionExpression conditionExpression = convert(parseTree);
		filterIds.addAll(getFilterIds(conditionExpression));
		return filterIds; 
	}

	private Collection<? extends UUID> getFilterIds(ConditionExpression conditionExpression) {
		Set<UUID> filterIds = new HashSet<>();
		if(conditionExpression instanceof FilterExpression) {
			FilterExpression filterExpression = (FilterExpression) conditionExpression;
			filterIds.add(UUID.fromString(filterExpression.getFilterId()));
		}else if(conditionExpression instanceof BooleanExpression) {
			BooleanExpression booleanExpression = (BooleanExpression) conditionExpression;
			for(ConditionExpression exp : booleanExpression.getConditionExpressions()) {
				filterIds.addAll(getFilterIds(exp));
			}
		}
		return filterIds;
	}

}
