package com.bhs.gtk.expression.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {
	
	@KafkaListener(topics = TopicNames.INPUT_EXECUTION_REQUEST)
	public boolean receiveFilterExecutionRequest(String message) {
		System.err.println("ES:<-"+message);
		return true;
	}

}
