package com.bhs.gtk.condition.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bhs.gtk.condition.service.ExecutableServiceImpl;
import com.bhs.gtk.condition.util.Converter;

@Service
public class MessageConsumer {
	
	@Autowired
	private ExecutableServiceImpl executableServiceImpl;
	
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
		System.err.println("CS :"+message);
		return true;
	}
	
	@KafkaListener(topics = TopicNames.INPUT_CHANGE_NOTIFICATION)
	public boolean receiveFilterChangeNotification(String message) {
		System.err.println("CS :"+message);
		return true;
	}
	


}
