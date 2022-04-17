package com.bhs.gtk.filter.util;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bhs.gtk.filter.model.ExecutionStatus;
import com.bhs.gtk.filter.model.PatchData;
import com.bhs.gtk.filter.model.PatchData.PropertyEnum;
import com.bhs.gtk.filter.persistence.ArithmeticExpressionResultEntity;
import com.bhs.gtk.filter.persistence.CompareExpressionResultEntity;
import com.bhs.gtk.filter.persistence.EntityReader;
import com.bhs.gtk.filter.persistence.FilterResultEntity;

@Component
public class Verifier {

	@Autowired
	private Converter converter;
	
	@Autowired
	private Extractor extractor;
	
	@Autowired
	private EntityReader entityReader;

	public boolean isLogicChanged(String existingParseTree, @NotNull String newParseTree) {

		if (StringUtils.isBlank(existingParseTree) && StringUtils.isBlank(newParseTree)) {
			return false;
		} else if (StringUtils.isBlank(existingParseTree) || StringUtils.isBlank(newParseTree)) {
			return true;
		}

		JSONObject existingFilterLogic = new JSONObject(existingParseTree);
		JSONObject newFilterLogic = new JSONObject(newParseTree);

		JSONObject existingLogicWithoutLocation = extractor.removeLocationFromFilter(existingFilterLogic);
		JSONObject newFilterLogicWithoutLocation = extractor.removeLocationFromFilter(newFilterLogic);

		String existigHash = converter.generateHash(existingLogicWithoutLocation.toString());
		String newHash = converter.generateHash(newFilterLogicWithoutLocation.toString());

		if (StringUtils.equals(existigHash, newHash)) {
			return false;
		}
		return true;
	}
	
	public boolean isValidatePatchData(List<PatchData> patchData) {
		if(patchData == null || patchData.isEmpty()) {
			return false;
		}
		List<@NotNull PropertyEnum> properties = patchData.stream().map( p -> p.getProperty()).collect(Collectors.toList());
		// ^ is XOR operation. 
		return !(properties.contains(PropertyEnum.CODE) ^ properties.contains(PropertyEnum.PARSE_TREE));
	}
	
	public boolean isSameScripNameAndMarketTime(Date mTime, String sName, Date marketTime, String scripName) {
		return mTime.equals(marketTime) && StringUtils.equals(sName, scripName);
	}
	
	public boolean isValidFilterOperation(String operation) {
		switch (operation) {
		case "AND":
		case "OR":
			return true;
		default:
			return false;
		}
	}
	
	public boolean isFilterReadyForResultDerivation(FilterResultEntity filterResultEntity) {
		for(CompareExpressionResultEntity cmpResult : entityReader.getCmpExpResultEntities(filterResultEntity)) {
			String cmpStatus = cmpResult.getStatus();
			if (StringUtils.equals(ExecutionStatus.QUEUED.name(), cmpStatus)
					|| StringUtils.equals(ExecutionStatus.RUNNING.name(), cmpStatus)) {
				return false;
			}
		}
		return true;
	}

	public boolean isValidCMPoperation(String operation) {
		switch (operation) {
		case "=":
		case ">":
		case "<":
		case ">=":
		case "<=":
			return true;
		default:
			return false;
		}
	}
	
	public boolean isARexpResultAvailable(ArithmeticExpressionResultEntity arResult) {
		String status = arResult.getStatus();
		if(StringUtils.equals(ExecutionStatus.QUEUED.name(), status) || StringUtils.equals(ExecutionStatus.RUNNING.name(), status) ) {
			return false;
		}
		return true;
	}

}
