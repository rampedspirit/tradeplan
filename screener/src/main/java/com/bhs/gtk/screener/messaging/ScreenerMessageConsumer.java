package com.bhs.gtk.screener.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ScreenerMessageConsumer {
	
	/**
	 * @param message : the expression string that reach the topic/s is available in this parameter 
	 */
//	@KafkaListener(topics = MessageConstants.INPUT_TOPIC_NAME)
//	public boolean receiveMessageFromInputTopic(String message) {
//		processExpression(message);
//		return true;
//	}
	
//	private boolean processExpression(String evaluationRequest) {
////		EvaluationResponse response =  evaluationServiceImpl.evaluate(evaluationRequest);
////		JSONObject responseAsJson = new JSONObject(response);
////		expressionMessageProducer.sendMessage(responseAsJson.toString());
//		return true;
//	}


}
