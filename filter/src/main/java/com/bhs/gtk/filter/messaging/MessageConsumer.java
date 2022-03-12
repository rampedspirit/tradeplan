package com.bhs.gtk.filter.messaging;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bhs.gtk.filter.model.FilterResultResponse;
import com.bhs.gtk.filter.model.communication.ArithmeticExpressionResult;
import com.bhs.gtk.filter.model.communication.ExecutableFilter;
import com.bhs.gtk.filter.persistence.FilterResultEntity;
import com.bhs.gtk.filter.service.FilterServiceImpl;
import com.bhs.gtk.filter.util.Converter;

@Service
public class MessageConsumer {
	
	@Autowired
	private Converter converter;
	
	@Autowired
	private FilterServiceImpl filterServiceImpl;
	
	
	@KafkaListener(topics = TopicNames.INPUT_EXECUTION_REQUEST)
	public boolean receiveFilterExecutionRequest(String message) {
		ExecutableFilter executableFilter =  converter.convertToExecutableFilter(message);
		FilterResultEntity fResult = filterServiceImpl.executeFilter(executableFilter);
		String status = fResult.getStatus();
		if(isFilterExecutionComplete(status)) {
			return filterServiceImpl.sendMessage(fResult);
		}
		return true;
	}
	
	private boolean isFilterExecutionComplete(String status) {
		if (StringUtils.equals(status, FilterResultResponse.FilterResultEnum.QUEUED.name())
				|| StringUtils.equals(status, FilterResultResponse.FilterResultEnum.RUNNING.name())) {
			return false;
		}
		return true;
	}

	@KafkaListener(topics = TopicNames.INPUT_EXECUTION_RESPONSE)
	public boolean receiveExpressionExecutionResponse(String message) {
		System.err.println("FS <-: expression execution response:"+message);
		ArithmeticExpressionResult arResult = converter.convertToARexpressionResult(message);
		filterServiceImpl.updateFilterResult(arResult);
		return true;
	}
	

}
