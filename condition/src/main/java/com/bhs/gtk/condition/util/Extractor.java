package com.bhs.gtk.condition.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class Extractor {
	
	
	public JSONObject removeLocationFromCondition(JSONObject jsonObject) {
		if(jsonObject == null) {
			throw new IllegalArgumentException(jsonObject + " is not a valid condition json object");
		}
		if (jsonObject.isNull("location")) {
			JSONArray expressions = jsonObject.getJSONArray("expressions");
			for (Object exp : expressions) {
				if (exp instanceof JSONObject) {
					JSONObject expObj = (JSONObject) exp;
					if (expObj.isNull("location")) {
						removeLocationFromCondition(expObj);
					}else {
						expObj.remove("location");
					}
				}
			}
		}else {
			jsonObject.remove("location");
		}
		return jsonObject;
	}

}
