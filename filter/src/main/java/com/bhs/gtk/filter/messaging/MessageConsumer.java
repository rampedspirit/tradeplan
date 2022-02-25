package com.bhs.gtk.filter.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bhs.gtk.filter.model.ExecutableFilter;
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
		filterServiceImpl.executeFilter(executableFilter);
		return true;
	}
	
	@KafkaListener(topics = TopicNames.INPUT_EXECUTION_RESPONSE)
	public boolean receiveExpressionExecutionResponse(String message) {
		System.err.println("FS <-: expression execution response:"+message);
		return true;
	}
	

}
