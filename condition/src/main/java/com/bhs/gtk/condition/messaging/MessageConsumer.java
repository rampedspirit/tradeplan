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
	public boolean receiveExecutionRequest(String message) {
		return executableServiceImpl.RunCondition(converter.convertToExecutableCondition(message));
	}


}
