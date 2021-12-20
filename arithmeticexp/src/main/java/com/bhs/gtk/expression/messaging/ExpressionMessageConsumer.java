package com.bhs.gtk.expression.messaging;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bhs.gtk.expression.model.EvaluationResponse;
import com.bhs.gtk.expression.service.EvaluationServiceImpl;

@Service
public class ExpressionMessageConsumer {
	
	@Autowired
	private EvaluationServiceImpl evaluationServiceImpl;
	
	@Autowired
	private ExpressionMessageProducer expressionMessageProducer;
	
	
	/**
	 * @param message : the expression string that reach the topic/s is available in this parameter 
	 */
	@KafkaListener(topics = MessageConstants.INPUT_TOPIC_NAME)
	public boolean receiveMessageFromInputTopic(String message) {
		processExpression(message);
		return true;
	}
	
	private boolean processExpression(String evaluationRequest) {
		EvaluationResponse response =  evaluationServiceImpl.evaluate(evaluationRequest);
		JSONObject responseAsJson = new JSONObject(response);
		expressionMessageProducer.sendMessage(responseAsJson.toString());
		return true;
	}


}
