package com.bhs.gtk.screener.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {
	
	@KafkaListener(topics = TopicNames.INPUT_EXECUTION_RESPONSE)
	public boolean receiveMessageFromInputTopic(String message) {
		System.err.println("Screener received :"+message);
		//processExpression(message);
		return true;
	}
	
//	private boolean processExpression(String evaluationRequest) {
////		EvaluationResponse response =  evaluationServiceImpl.evaluate(evaluationRequest);
////		JSONObject responseAsJson = new JSONObject(response);
////		expressionMessageProducer.sendMessage(responseAsJson.toString());
//		return true;
//	}


}
