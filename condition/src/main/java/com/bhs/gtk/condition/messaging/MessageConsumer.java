package com.bhs.gtk.condition.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bhs.gtk.condition.model.communication.FilterResult;
import com.bhs.gtk.condition.service.ConditionServiceImpl;
import com.bhs.gtk.condition.service.ExecutableServiceImpl;
import com.bhs.gtk.condition.service.ResultServiceImpl;
import com.bhs.gtk.condition.util.Converter;

@Service
public class MessageConsumer {
	
	@Autowired
	private ExecutableServiceImpl executableServiceImpl;
	
	@Autowired
	private ConditionServiceImpl conditionServiceImpl;
	
	@Autowired
	private ResultServiceImpl resultServiceImpl;
	
	@Autowired
	private Converter converter;
	
	@KafkaListener(topics = TopicNames.INPUT_EXECUTION_REQUEST)
	public boolean receiveConditionExecutionRequest(String message) {
		//TODO: handle condition already in RUNNING or COMPLETED status.
		System.err.println("CS:<-"+message);
		return executableServiceImpl.RunCondition(converter.convertToExecutableCondition(message));
	}
	
	@KafkaListener(topics = TopicNames.INPUT_EXECUTION_RESPONSE)
	public boolean receiveFilterExecutionResponse(String message) {
		System.err.println("CS <-:"+message);
		FilterResult filterResult = converter.convertToFilterResult(message);
		return resultServiceImpl.updateConditionResult(filterResult);
	}
	
	@KafkaListener(topics = TopicNames.INPUT_CHANGE_NOTIFICATION)
	public boolean receiveFilterChangeNotification(String message) {
		ChangeNotification changeNotification = converter.convertToChangeNotification(message);
		conditionServiceImpl.adaptChangeInFilter(changeNotification);
		return true;
	}
	
}
