package com.tradeplan.dataReader.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {
	
	@KafkaListener(topics = TopicNames.INPUT_READ_REQUEST)
	public boolean receiveFilterExecutionRequest(String message) {
		System.err.println("dataReader received:"+message);
		return true;
	}

}
