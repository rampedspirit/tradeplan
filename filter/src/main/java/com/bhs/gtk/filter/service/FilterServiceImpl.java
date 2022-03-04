package com.bhs.gtk.filter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.OffsetDateTime;

import com.bhs.gtk.filter.messaging.ChangeNotification.ChangeStatusEnum;
import com.bhs.gtk.filter.messaging.MessageProducer;
import com.bhs.gtk.filter.messaging.MessageType;
import com.bhs.gtk.filter.model.ArithmeticExpression;
import com.bhs.gtk.filter.model.BooleanExpression;
import com.bhs.gtk.filter.model.CompareExpression;
import com.bhs.gtk.filter.model.ExecutionStatus;
import com.bhs.gtk.filter.model.ExpressionLocation;
import com.bhs.gtk.filter.model.ExpressionResult;
import com.bhs.gtk.filter.model.ExpressionResultResponse;
import com.bhs.gtk.filter.model.FilterRequest;
import com.bhs.gtk.filter.model.FilterResponse;
import com.bhs.gtk.filter.model.FilterResultResponse;
import com.bhs.gtk.filter.model.FilterResultResponse.FilterResultEnum;
import com.bhs.gtk.filter.model.Location;
import com.bhs.gtk.filter.model.LogicalExpression;
import com.bhs.gtk.filter.model.OperationType;
import com.bhs.gtk.filter.model.PatchData;
import com.bhs.gtk.filter.model.PatchData.PropertyEnum;
import com.bhs.gtk.filter.model.communication.ArithmeticExpressionResult;
import com.bhs.gtk.filter.model.communication.ExecutableFilter;
import com.bhs.gtk.filter.persistence.ArithmeticExpressionResultEntity;
import com.bhs.gtk.filter.persistence.CompareExpressionResultEntity;
import com.bhs.gtk.filter.persistence.EntityObjectCreator;
import com.bhs.gtk.filter.persistence.EntityReader;
import com.bhs.gtk.filter.persistence.EntityWriter;
import com.bhs.gtk.filter.persistence.ExpressionEntity;
import com.bhs.gtk.filter.persistence.FilterEntity;
import com.bhs.gtk.filter.persistence.FilterResultEntity;
import com.bhs.gtk.filter.util.Converter;
import com.bhs.gtk.filter.util.Mapper;

@Service
public class FilterServiceImpl implements FilterService{
	
	@Autowired
	private EntityWriter entityWriter;
	
	@Autowired
	private EntityReader entityReader;
	
	@Autowired
	private EntityObjectCreator entityObjectCreator;
	
	@Autowired
	private Mapper mapper;

	@Autowired
	private MessageProducer messageProducer;
	
	@Autowired
	private Converter converter;
	
	@Override
	public FilterResponse createFilter(@Valid FilterRequest filterRequest) {
		FilterEntity filterEntity = entityWriter.createFilter(filterRequest);
		return mapper.getFilterResponse(filterEntity);
	}

	@Override
	public FilterResponse getFilter(UUID id) {
		FilterEntity filterEntity = entityReader.getFilterEntity(id);
		return mapper.getFilterResponse(filterEntity);
	}

	@Override
	public List<FilterResponse> getAllFilters() {
		List<FilterEntity> filters = entityReader.getAllFilterEntites();
		return mapper.getAllFilterResponses(filters);
	}

	
	@Override
	public List<ArithmeticExpressionResultEntity> runArithmeticExpressionResultEntities(List<ArithmeticExpressionResultEntity> arResultEntitites) {
		List<ArithmeticExpressionResultEntity> arResultEntitypSentForExecution = new ArrayList<>();
		for( ArithmeticExpressionResultEntity arResult : arResultEntitites) {
		  ExpressionEntity expression = entityReader.getExpressionEntity(arResult.getHash());
		  String message = getARexecutionRequestMessag(arResult, expression.getParseTree());
		// Map<String, String> entityMap = getEntityMapForJson(arResult);
		//  entityMap.put("parseTree", expression.getParseTree());
		//  JSONObject entityAsJson = new JSONObject(entityMap);
			if (messageProducer.sendMessage(message, MessageType.EXECUTION_REQUEST)) {
				arResult.setStatus(ExecutionStatus.RUNNING.name());
				arResultEntitypSentForExecution.add(arResult);
			}else {
				System.err.println(" failed to request AR expression for execution from FS to ES : "+ message);
			}	
		}
		return entityWriter.saveArithmeticExpressionResultEntities(arResultEntitypSentForExecution);
	}
	
	private String getARexecutionRequestMessag(ArithmeticExpressionResultEntity entity, String parseTree) {
		Map<String, String> entityMap = new HashMap<>();
		entityMap.put("hash",entity.getHash().toString());
		entityMap.put("marketTime",entity.getMarketTimeAsOffsetDateTime());
		entityMap.put("scripName",entity.getScripName());
		entityMap.put("status",entity.getStatus());
		entityMap.put("parseTree", parseTree);
		return new JSONObject(entityMap).toString();
	}

	@Override
	public FilterResultEntity executeFilter(ExecutableFilter executableFilter) {
		UUID filterId = executableFilter.getFilterId();
		Date marketTime =  executableFilter.getMarketTime();
		String scripName = executableFilter.getScripName();
		
		FilterResultEntity filterResultEntity = entityReader.getFilterResultEntity(filterId, marketTime, scripName);
		if(filterResultEntity != null) {
			return filterResultEntity;
		}
		return createFilterResultEntity(filterId, marketTime, scripName);
	}

	@Override
	public FilterResultResponse getFilterResult(UUID filterId, String marketTime, String scripName) {
		Date mTime = DateTimeUtils.toDate(OffsetDateTime.parse(marketTime).toInstant());
		FilterResultEntity filterResultEntity = entityReader.getFilterResultEntity(filterId, mTime, scripName);
		if(filterResultEntity == null) {
			//Log no result available for given input.
			return null;
		}
		FilterResultResponse filterResultResponse = new FilterResultResponse();
		filterResultResponse.setFilterId(filterId);
		filterResultResponse.setMarketTime(filterResultEntity.getMarketTimeAsOffsetDateTime());
		filterResultResponse.setScripName(scripName);
		filterResultResponse.setFilterResult(FilterResultEnum.fromValue(filterResultEntity.getStatus()));
		filterResultResponse.setExpressionResults(getExpressionResults(filterResultEntity));
		return filterResultResponse;
	}
	
	@Override
	public FilterResponse deleteFilter(UUID filterId) {
		FilterEntity filterEntity = entityReader.getFilterEntity(filterId);
		if(filterEntity == null) {
			return null;
		}
		if(messageProducer.sendChangeNotification(filterEntity.getId(), ChangeStatusEnum.DELETED)) {
			entityWriter.deleteFilterResultEntity(filterId);
			filterEntity.setExpressions(new ArrayList<>());
			entityWriter.saveFilterEntity(filterEntity);
			entityWriter.deleteFilterEntity(filterEntity);
			entityWriter.deleteExpressionsNotAssociatedToAnyFilter();
			return mapper.getFilterResponse(filterEntity);
		}
		return null;
	}
	
	
	@Override
	public FilterResponse updateFilter(@Valid List<PatchData> patchData, UUID filterId) {
		if(patchData == null || filterId == null) {
			//throw exception if validation fails
			return null;
		}
		if(!isValidatePatchData(patchData)) {
			return null;
		}
		
		FilterEntity filterEntity = entityReader.getFilterEntity(filterId);
		boolean logicChanged = false;
		
		for( PatchData pd : patchData) {
			@NotNull
			String value = pd.getValue();
			switch (pd.getProperty()) {
			case NAME: 
				filterEntity.setName(value);
				break;
			case DESCRIPTION: 
				filterEntity.setDescription(value); 
				break;
			case CODE: 
				logicChanged = true;
				filterEntity.setCode(value);
				break;
			case PARSE_TREE: 
				logicChanged = true;
				filterEntity.setParseTree(value);
				break;
			}
		}
		
		if(logicChanged) {
			if(!messageProducer.sendChangeNotification(filterEntity.getId(), ChangeStatusEnum.UPDATED)) {
				//not able to notify change in logic and hence logic change is aborted.
				return null;
			}
		}
		
		FilterEntity changedFilterEntity;
		if(logicChanged) {
			changedFilterEntity = updateFilterEntity(filterEntity);

		}else {
			changedFilterEntity = entityWriter.saveFilterEntity(filterEntity);
		}
		return mapper.getFilterResponse(changedFilterEntity);
	}
	
	
	private FilterEntity updateFilterEntity(FilterEntity filterEntity) {
		BooleanExpression booleanExpression = converter.convertToBooleanExpression(filterEntity.getParseTree());
		List<ExpressionEntity> expressions = entityObjectCreator.createExpressionEntityObjects(booleanExpression);
		filterEntity.setExpressions(expressions);
		FilterEntity savedFilterEntity = entityWriter.saveFilterEntity(filterEntity);
		entityWriter.removePreviousAssociations(filterEntity);
		return savedFilterEntity;            
	}

	private boolean isValidatePatchData(List<PatchData> patchData) {
		if(patchData == null || patchData.isEmpty()) {
			return false;
		}
		List<@NotNull PropertyEnum> properties = patchData.stream().map( p -> p.getProperty()).collect(Collectors.toList());
		// ^ is XOR operation. 
		return !(properties.contains(PropertyEnum.CODE) ^ properties.contains(PropertyEnum.PARSE_TREE));
	}
	
	
	private List<ExpressionResultResponse> getExpressionResults(FilterResultEntity filterResultEntity) {
		List<ExpressionResult> expressionResults = getExpressionsResultMap(filterResultEntity);
		HashMap<String, ExpressionLocation> locationMap = getExpressionsLocationMap(filterResultEntity.getFilterId());
		List<ExpressionResultResponse> expressionResultsResponse = new ArrayList<>();
		for ( ExpressionResult result : expressionResults) {
			ExpressionResultResponse response = new ExpressionResultResponse();
			response.setResult(result.getStatus());
			Location location = mapper.getLocation(locationMap.get(result.getHash()));
			response.setLocation(location);
			response.setType(ExpressionResultResponse.TypeEnum.valueOf(result.getType()));
			expressionResultsResponse.add(response);
		}
		return expressionResultsResponse;
	}

	private HashMap<String, ExpressionLocation> getExpressionsLocationMap(UUID filterId) {
		HashMap<String, ExpressionLocation> locationMap = new HashMap<>();
		
		FilterEntity filterEntity = entityReader.getFilterEntity(filterId);
		if(filterEntity == null) {
			return locationMap;
		}
		BooleanExpression booleanExpression = converter.convertToBooleanExpression(filterEntity.getParseTree());
		locationMap.putAll(getExpressionsLocationMap(booleanExpression));
		return locationMap;
	}
	
	private HashMap<String, ExpressionLocation> getExpressionsLocationMap(BooleanExpression expression) {
		HashMap<String, ExpressionLocation> locationMap = new HashMap<>();
		
		if(expression instanceof LogicalExpression) {
			LogicalExpression logicalExpression = (LogicalExpression) expression;
			for(BooleanExpression exp : logicalExpression.getBooleanExpressions()) {
				locationMap.putAll(getExpressionsLocationMap(exp));
			}
		}else if(expression instanceof CompareExpression) {
			CompareExpression compareExpression = (CompareExpression) expression;
			locationMap.putAll(getExpressionsLocationMap(compareExpression));
		}
		return locationMap;
	}
	
	private HashMap<String, ExpressionLocation> getExpressionsLocationMap(CompareExpression compareExpression) {
		ArithmeticExpression leftARexp = compareExpression.getLeftArithmeticExpression();
		ArithmeticExpression rightARexp = compareExpression.getRightArithmeticExpression();
		
		HashMap<String, ExpressionLocation> locationMap = new HashMap<>();
		locationMap.put(compareExpression.getHash(), compareExpression.getLocation());
		locationMap.put(leftARexp.getHash(), leftARexp.getLocation());
		locationMap.put(rightARexp.getHash(), rightARexp.getLocation());
		return locationMap;
	}

	private List<ExpressionResult> getExpressionsResultMap(FilterResultEntity filterResultEntity) {
		List<ExpressionResult> expressionResult = new ArrayList<>();
		for(CompareExpressionResultEntity cmp : filterResultEntity.getCompareExpressionResultEntities()) {
			expressionResult.add(new ExpressionResult(cmp.getHash(), cmp.getStatus(),
					ExpressionResultResponse.TypeEnum.COMPARE_EXPRESSION.name()));
			for(ArithmeticExpressionResultEntity arExp : cmp.getArithmeticExpressionResultEntities()) {
				expressionResult.add(new ExpressionResult(arExp.getHash(), arExp.getStatus(),
						ExpressionResultResponse.TypeEnum.ARITHEMETIC_EXPRESSION.name()));
			}
		}
		return expressionResult;
	}

	private FilterResultEntity createFilterResultEntity(UUID filterId, Date marketTime, String scripName) {
		List<CompareExpressionResultEntity> compareResults = createCompareResultEntities(filterId, marketTime, scripName);
		FilterEntity filterEntity = entityReader.getFilterEntity(filterId);
		ExecutionStatus status = calculateFilterResultStatusFromCompareEntities(compareResults,
				filterEntity.getParseTree());
		return entityWriter.createFilterResultEntity(filterId, marketTime, scripName, status.toString(), compareResults);
	}

	private ExecutionStatus calculateFilterResultStatusFromCompareEntities(List<CompareExpressionResultEntity> compareResults, String parseTree) {
		ExecutionStatus status = null;
		if(compareResults.size() == 1) {
			return ExecutionStatus.valueOf(compareResults.get(0).getStatus());
		}
		status = deriveFilterEntityResultStatus(compareResults);
		if(status == null) {
			String operation = converter.getOperationFromParseTree(parseTree);
			List<String> cmpStatuses = compareResults.stream().map( e -> e.getStatus()).collect(Collectors.toList());
			status =  calculateFilterResultStatusFromCompareStatuses(cmpStatuses, operation);
		}
		return status;
	}

	private ExecutionStatus calculateFilterResultStatusFromCompareStatuses(List<String> cmpStatuses, String operation) {
		if(cmpStatuses == null || cmpStatuses.isEmpty()) {
			return ExecutionStatus.ERROR;
		}
		ExecutionStatus status = ExecutionStatus.ERROR;
		if(StringUtils.equals(OperationType.AND.name(), operation)) {
			if(cmpStatuses.contains(ExecutionStatus.FAIL.name())) {
				status = ExecutionStatus.FAIL;
			}else {
				status = ExecutionStatus.PASS;
			}
		}else if(StringUtils.equals(OperationType.OR.name(), operation)) {
			if(cmpStatuses.contains(ExecutionStatus.PASS.name())) {
				status = ExecutionStatus.PASS;
			}else {
				status = ExecutionStatus.FAIL;
			}
		}
		return status;
	}

	
	private ExecutionStatus deriveFilterEntityResultStatus(List<CompareExpressionResultEntity> compareResults) {
		boolean error = false, queued = false, running = false;
		for(CompareExpressionResultEntity cr : compareResults) {
			if(StringUtils.equals(cr.getStatus(), ExecutionStatus.ERROR.name())) {
				error = true;
				break;
			}else if(StringUtils.equals(cr.getStatus(), ExecutionStatus.QUEUED.name())) {
				queued = true;
			}else if(StringUtils.equals(cr.getStatus(), ExecutionStatus.RUNNING.name())) {
				running = true;
			}
		}
		return getFilterEntityResultStatus(error,queued,running);
	}

	private ExecutionStatus getFilterEntityResultStatus(boolean error, boolean queued, boolean running) {
		ExecutionStatus status = null;
		if(error) {
			status = ExecutionStatus.ERROR;
		}else if(queued) {
			status =  ExecutionStatus.QUEUED;
		}else if(running) {
			status =  ExecutionStatus.RUNNING;
		}
		return status;
	}

	private List<CompareExpressionResultEntity> createCompareResultEntities(UUID filterId, Date marketTime, String scripName) {
		List<ExpressionEntity> expressions = entityReader.getExpressionEntities(filterId);
		
		//TODO: handle empty expressions
		List<ExpressionEntity> arExpressions = expressions.stream().filter(e -> StringUtils.equals(e.getType(), ExpressionResultResponse.TypeEnum.ARITHEMETIC_EXPRESSION.name()))
				.collect(Collectors.toList());
		
		List<ArithmeticExpressionResultEntity> arResults = executeArtithmeticExpressions(arExpressions, marketTime,
				scripName);
		
		List<ExpressionEntity> compareExpressions = expressions.stream().filter(e -> StringUtils.equals(e.getType(), ExpressionResultResponse.TypeEnum.COMPARE_EXPRESSION.name()))
		.collect(Collectors.toList());
		
		return  executeCompareExpressions(compareExpressions, arResults, marketTime, scripName);
	}

	private List<CompareExpressionResultEntity> executeCompareExpressions(List<ExpressionEntity> compareExpressions,
			List<ArithmeticExpressionResultEntity> arResults, Date marketTime, String scripName) {
		List<CompareExpressionResultEntity> compareResultsToBePersisted = new ArrayList<>();
		List<CompareExpressionResultEntity> existingCompareResults = new ArrayList<>();
		for(ExpressionEntity cmpExp : compareExpressions) {
			String parseTree = cmpExp.getParseTree();
			CompareExpressionResultEntity compareResult = entityReader.getCompareResultEntity(converter.generateHash(parseTree),marketTime,scripName);
			if(compareResult == null) {
				compareResultsToBePersisted.add(createCompareResultEntityObject(arResults, marketTime, scripName, parseTree));
			}else {
				existingCompareResults.add(compareResult);
			}
		}
		existingCompareResults.addAll(entityWriter.saveCompareExpressionResultEntities(compareResultsToBePersisted));
		return existingCompareResults;
	}

	private CompareExpressionResultEntity createCompareResultEntityObject(List<ArithmeticExpressionResultEntity> arResults, Date marketTime, String scripName,
			String cmpParseTree) {
		
		String cmpHash = converter.generateHash(cmpParseTree);
		String leftARexpHash = converter.getARexpHashFromCompareParseTree(cmpParseTree, true);
		String rightARexpHash = converter.getARexpHashFromCompareParseTree(cmpParseTree, false);
		
		ArithmeticExpressionResultEntity leftARresult = null;
		ArithmeticExpressionResultEntity rightARresult = null;
		
		for(ArithmeticExpressionResultEntity ar : arResults) {
			Date mTime = ar.getMarketTime();
			String sName = ar.getScripName();
			String hash = ar.getHash();
			if(isSameScripNameAndMarketTime(mTime, sName, marketTime, scripName)) {
				if(StringUtils.equals(leftARexpHash, hash)) {
					leftARresult = ar;
				}else if(StringUtils.equals(rightARexpHash, hash)) {
					rightARresult = ar;
				}
			}
			if(leftARresult != null && rightARresult != null) {
				break;
			}
		}
		if(leftARresult == null || rightARexpHash == null) {
			throw new IllegalStateException("Compare expression should have both left and right Arithmetic expression");
		}
		
		ExecutionStatus compareStatus = calculateCompareExpressionStatus(cmpParseTree, leftARresult.getStatus(),
				rightARresult.getStatus()); 
		CompareExpressionResultEntity compareExpressionResultEntity=  new CompareExpressionResultEntity(cmpHash, marketTime, scripName, compareStatus.name());
		compareExpressionResultEntity.getArithmeticExpressionResultEntities().add(leftARresult);
		compareExpressionResultEntity.getArithmeticExpressionResultEntities().add(rightARresult);
		return compareExpressionResultEntity;
	}

	
	private ExecutionStatus calculateCompareExpressionStatus(String cmpParseTree, String leftStatus, String righStatus) {
		ExecutionStatus status = null;
		status = deriveCompareExpressionStatus(leftStatus, righStatus);
		if(status != null) {
			return status;
		}
		try {
			String operation = converter.getOperationFromParseTree(cmpParseTree);
			Double leftValue = Double.valueOf(leftStatus);
			Double rightValue = Double.valueOf(righStatus);
			return calculateCompareExpressionStatus(operation,leftValue, rightValue);
		}catch (NumberFormatException ex) {
			//Log number format exception.
			return ExecutionStatus.ERROR;
		}
	}
	
	private ExecutionStatus calculateCompareExpressionStatus(String operation, Double leftValue, Double rightValue) {
		boolean pass = false;
		switch(operation) {
		case "=":
			pass = (leftValue.equals(rightValue)); break;
		case ">":
			pass = leftValue.compareTo(rightValue) > 0.0; break;
		case "<":
			pass = leftValue.compareTo(rightValue) < 0.0; break;
		case ">=":
			pass = leftValue.compareTo(rightValue) >= 0.0; break;
		case "<=":
			pass = leftValue.compareTo(rightValue) <= 0.0; break;
		default: 
				 throw new IllegalArgumentException(operation+ " is in valid operation");
		}
		
		if(pass) {
			return ExecutionStatus.PASS;
		}
		return ExecutionStatus.FAIL;
	}

	private ExecutionStatus deriveCompareExpressionStatus(String leftStatus, String righStatus) {
		ExecutionStatus derivedExecutionStatus = null;
		if (StringUtils.equals(leftStatus, ExecutionStatus.ERROR.name())
				|| StringUtils.equals(righStatus, ExecutionStatus.ERROR.name())) {
			derivedExecutionStatus =  ExecutionStatus.ERROR;
		}else if (StringUtils.equals(leftStatus, ExecutionStatus.QUEUED.name())
				|| StringUtils.equals(righStatus, ExecutionStatus.QUEUED.name())) {
			derivedExecutionStatus = ExecutionStatus.QUEUED;
		}else if (StringUtils.equals(leftStatus, ExecutionStatus.RUNNING.name())
				&& StringUtils.equals(righStatus, ExecutionStatus.RUNNING.name())) {
			derivedExecutionStatus = ExecutionStatus.RUNNING;
		}
		return derivedExecutionStatus;
	}

	private boolean isSameScripNameAndMarketTime(Date mTime, String sName, Date marketTime, String scripName) {
		return mTime.equals(marketTime) && StringUtils.equals(sName, scripName);
	}

	private List<ArithmeticExpressionResultEntity> executeArtithmeticExpressions(List<ExpressionEntity> arExpressions, Date marketTime,
			String scripName) {
		List<ArithmeticExpressionResultEntity> arResultEntitites = new ArrayList<>();
		List<ExpressionEntity> arExpressionsToBeExecuted = new ArrayList<>();
		for( ExpressionEntity arExp : arExpressions) {
			ArithmeticExpressionResultEntity arResult = entityReader.getArithmeticExpressionResultEntity(arExp.getHash(),marketTime,scripName);
			if(arResult == null) {
				arExpressionsToBeExecuted.add(arExp);
			}else {
				arResultEntitites.add(arResult);
			}
		}
		List<ArithmeticExpressionResultEntity> queuedARresultEntitites = entityWriter
				.queueARexpression(arExpressionsToBeExecuted, marketTime, scripName);
		arResultEntitites.addAll(runArithmeticExpressionResultEntities(queuedARresultEntitites));
		return arResultEntitites;
	}

//	private Map<String, String> getEntityMapForJson(ArithmeticExpressionResultEntity entity) {
//		Map<String, String> entityMap = new HashMap<>();
//		entityMap.put("hash",entity.getHash().toString());
//		entityMap.put("marketTime",entity.getMarketTimeAsOffsetDateTime());
//		entityMap.put("scripName",entity.getScripName());
//		entityMap.put("status",entity.getStatus());
//		return entityMap;
//	}

	@Override
	public boolean updateFilterResult(ArithmeticExpressionResult arResult) {
		String hash = arResult.getHash();
		Date marketTime = arResult.getMarketTime();
		String scripName = arResult.getScripName();
		String status = arResult.getStatus();
		updateARexpressionResultEntity(hash,marketTime, scripName, status);
		List<UUID> filterIds = getAllFilterIdsWhichRequireResultUpdate(hash);
		if(filterIds.isEmpty()) {
			throw new IllegalStateException("There is no filter to use AR exp result :"+arResult);
		}
		List<FilterResultEntity> filterResults = getAllFilterResultEntitiesWhichRequireResultUpdate(filterIds, marketTime, scripName);
		Set<CompareExpressionResultEntity> cmpExpResults = getAllCMPexpressionResultsWhichRequireResultUpdate(filterResults, marketTime, scripName);
		updateCMPexpressionResultEntity(cmpExpResults);
		List<FilterResultEntity> filterResultEntities =  updateFilterResultEntities(filterResults);
		if(sendMessage(filterResultEntities)) {
			return true;
		}
		//TODO:log info/debug : at-least one of filter`s result msg is not sent to CS from FS
		return false;
	}

	
	private boolean sendMessage(List<FilterResultEntity> filterResultEntities) {
		boolean msgSentStatus = true;
		for(FilterResultEntity fResult : filterResultEntities) {
			String message = getFilterExecutionResponseMessage(fResult);
			if(messageProducer.sendMessage(message, MessageType.EXECUTION_RESPONSE)) {
				//TODO: log debug : message successfully send from FS to CS
			}else {
				//TODO: log debug : message failed to send from FS to CS.
				msgSentStatus = false;
			}
		}
		return msgSentStatus;
	}

	private String getFilterExecutionResponseMessage(FilterResultEntity fResult) {
		Map<String, String> msgMap = new HashMap<>();
		msgMap.put("filterId", fResult.getFilterId().toString());
		msgMap.put("marketTime", fResult.getMarketTimeAsOffsetDateTime().toString());
		msgMap.put("scripName", fResult.getScripName());
		msgMap.put("status", fResult.getStatus());
		return new JSONObject(msgMap).toString();
	}

	private List<FilterResultEntity> updateFilterResultEntities(List<FilterResultEntity> filterResults) {
		List<FilterResultEntity> filterResultsToBeUpdated = getAllFilterReadyForResultUpdate(filterResults);
		List<FilterResultEntity> filterResultsToBePersisted = new ArrayList<>();
		for(FilterResultEntity fResult : filterResultsToBeUpdated) {
			String operation = getFilterOperation(fResult.getFilterId());
			String result = deriveFilterResult(operation, fResult.getCompareExpressionResultEntities());
			fResult.setStatus(result);
			filterResultsToBePersisted.add(fResult);
		}
		return entityWriter.saveFilterResultEntities(filterResultsToBePersisted);
	}

	private String deriveFilterResult(String operation,
			List<CompareExpressionResultEntity> compareExpressionResultEntities) {
		
		String result = ExecutionStatus.FAIL.name();
		if(RandomUtils.nextInt()%2 == 2) {
			result = ExecutionStatus.PASS.name();
		}
		
		//TODO: implement filter result derivation.
//		List<String> cmpResults = compareExpressionResultEntities.stream().map(c -> c.getStatus())
//				.collect(Collectors.toList());
//		
//		if (cmpResults.contains(ExecutionStatus.ERROR.name())) {
//			result = ExecutionStatus.ERROR.name();
//		} else if (StringUtils.equals(operation, OperationType.AND.name())) {
//			if (cmpResults.contains(ExecutionStatus.FAIL.name())) {
//				result = ExecutionStatus.FAIL.name();
//			} else {
//				result = ExecutionStatus.PASS.name();
//			}
//		} else if (StringUtils.equals(operation, OperationType.OR.name())) {
//			if (cmpResults.contains(ExecutionStatus.PASS.name())) {
//				result = ExecutionStatus.PASS.name();
//			} else {
//				result = ExecutionStatus.FAIL.name();
//			}
//		} else {
//			throw new IllegalArgumentException(operation + " is not a valid filter operation");
//		}
		return result;
	}

	private String getFilterOperation(UUID filterId) {
		String operation = StringUtils.EMPTY;
		FilterEntity filterEntity = entityReader.getFilterEntity(filterId);
		if(filterEntity != null) {
			operation = converter.getOperationFromParseTree(filterEntity.getParseTree());
		}
		if(isValidFilterOperation(operation)) {
			return operation;
		}
		throw new IllegalArgumentException("No valid filter operation found for filter id = "+filterId);
	}

	private boolean isValidFilterOperation(String operation) {
		switch (operation) {
		case "AND":
		case "OR":
			return true;
		default:
			return false;
		}
	}

	private List<FilterResultEntity> getAllFilterReadyForResultUpdate(List<FilterResultEntity> filterResults) {
		List<FilterResultEntity> filterResultsToBeUpdated = new ArrayList<>();
		for(FilterResultEntity fResult : filterResults) {
			if(isFilterReadyForResultDerivation(fResult)) {
				filterResultsToBeUpdated.add(fResult);
			}
		}
		return filterResultsToBeUpdated;
	}

	private boolean isFilterReadyForResultDerivation(FilterResultEntity filterResultEntity) {
		for(CompareExpressionResultEntity cmpResult : filterResultEntity.getCompareExpressionResultEntities()) {
			String cmpStatus = cmpResult.getStatus();
			if (StringUtils.equals(ExecutionStatus.QUEUED.name(), cmpStatus)
					|| StringUtils.equals(ExecutionStatus.RUNNING.name(), cmpStatus)) {
				return false;
			}
		}
		return true;
	}

	private List<CompareExpressionResultEntity> updateCMPexpressionResultEntity(Set<CompareExpressionResultEntity> cmpExpResults) {
		List<CompareExpressionResultEntity> cmpExpResultsToBeUpdated = getAllCMPexpReadyForResultUpdate(cmpExpResults);
		List<CompareExpressionResultEntity> cmpExpResultsToBePersisted = new ArrayList<>();
		for(CompareExpressionResultEntity cmpResult : cmpExpResultsToBeUpdated) {
			String operation = getCMPexpOperation(cmpResult.getHash());
			List<ArithmeticExpressionResultEntity> arExpResults = cmpResult.getArithmeticExpressionResultEntities();
			if(deriveCMPexpResult(operation, arExpResults.get(0).getStatus(), arExpResults.get(1).getStatus())) {
				cmpResult.setStatus(ExecutionStatus.PASS.name());
			}else {
				cmpResult.setStatus(ExecutionStatus.FAIL.name());
			}
			cmpExpResultsToBePersisted.add(cmpResult);
		}
		return entityWriter.saveCompareExpressionResultEntities(cmpExpResultsToBePersisted);
	}

	private String getCMPexpOperation(String cmpHash) {
		ExpressionEntity expressionEntity = entityReader.getExpressionEntity(cmpHash);
		expressionEntity.getParseTree();
		String operation = converter.getOperationFromParseTree(expressionEntity.getParseTree());
		if(isValidCMPoperation(operation)) {
			return operation;
		}
		throw new IllegalArgumentException("No valid compare operation found for hash = "+cmpHash);
	}

	private boolean isValidCMPoperation(String operation) {
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

	private boolean deriveCMPexpResult(String operation, String leftARStatus, String rightARstatus) {
		Double leftArg = null;
		Double rightArg = null;
		try {
			leftArg = Double.valueOf(leftARStatus);
			rightArg = Double.valueOf(rightARstatus);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(
					"One of AR expression do not have valid result :" + leftARStatus + "::" + rightARstatus);
		}

		boolean result = false;
		switch (operation) {
		case "=":
			result = leftArg == rightArg;
			break;
		case ">":
			result = leftArg > rightArg;
			break;
		case "<":
			result = leftArg < rightArg;
			break;
		case ">=":
			result = leftArg >= rightArg;
			break;
		case "<=":
			result = leftArg <= rightArg;
			break;
		default:
			throw new IllegalArgumentException(operation + " is not a valid compare expression operation");
		}
		return result;
	}

	private List<CompareExpressionResultEntity> getAllCMPexpReadyForResultUpdate(Set<CompareExpressionResultEntity> cmpExpResults) {
		List<CompareExpressionResultEntity> cmpExpToBeUpdated = new ArrayList<>();
		for(CompareExpressionResultEntity cmpResult : cmpExpResults) {
			List<ArithmeticExpressionResultEntity> arExpResults = cmpResult.getArithmeticExpressionResultEntities();
			if(arExpResults.size() != 2) {
				throw new IllegalStateException("Compare expression should have two AR expression results. cmpHash =  "+cmpResult.getHash());
			}
			int numberOfARexpResultsAvailable = 0;
			for(ArithmeticExpressionResultEntity arResult : arExpResults) {
				if(isARexpResultAvailable(arResult)) {
					numberOfARexpResultsAvailable++;
				}
			}
			if(numberOfARexpResultsAvailable == 2) {
				cmpExpToBeUpdated.add(cmpResult);
			}
		}
		return cmpExpToBeUpdated;
	}

	private boolean isARexpResultAvailable(ArithmeticExpressionResultEntity arResult) {
		String status = arResult.getStatus();
		if(StringUtils.equals(ExecutionStatus.QUEUED.name(), status) || StringUtils.equals(ExecutionStatus.RUNNING.name(), status) ) {
			return false;
		}
		return true;
	}

	
	private List<FilterResultEntity> getAllFilterResultEntitiesWhichRequireResultUpdate(List<UUID> filterIds, Date marketTime, String scripName) {
		return entityReader.getAllFilterResultEntities(filterIds, marketTime, scripName);
	}
	
	
	private Set<CompareExpressionResultEntity> getAllCMPexpressionResultsWhichRequireResultUpdate(List<FilterResultEntity> filterResults, Date marketTime, String scripName) {
		Set<CompareExpressionResultEntity> compareExpresionResults = new HashSet<>();
		for(FilterResultEntity fResult : filterResults) {
			compareExpresionResults.addAll(fResult.getCompareExpressionResultEntities());
		}
		return compareExpresionResults;
	}

	private List<UUID> getAllFilterIdsWhichRequireResultUpdate(String arExpHash) {
		List<UUID> ids = new ArrayList<>();
		ExpressionEntity expressionEntity = entityReader.getExpressionEntity(arExpHash);
		if(expressionEntity == null) {
			return ids;
		}
		List<FilterEntity> filters = expressionEntity.getFilters();
		if(filters == null || filters.isEmpty()) {
			return ids;
		}
		ids = filters.stream().map( f -> f.getId()).collect(Collectors.toList());
		return ids;
	}

	private boolean updateARexpressionResultEntity(String hash, Date marketTime, String scripName, String status) {
		ArithmeticExpressionResultEntity arResultEntity = entityReader.getArithmeticExpressionResultEntity(hash, marketTime, scripName);
		if(arResultEntity == null) {
			//log error
			throw new IllegalStateException("ArithmeticExpressionResultEntity to which result is recevied from ES is not found.");
		}
		arResultEntity.setStatus(status);
		entityWriter.saveArithmeticExpressionResultEntity(arResultEntity);
		return true;
	}

}
