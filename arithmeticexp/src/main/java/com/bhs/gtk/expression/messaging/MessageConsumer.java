package com.bhs.gtk.expression.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bhs.gtk.expression.model.ExecutableExpression;
import com.bhs.gtk.expression.service.ExpressionServiceImpl;
import com.bhs.gtk.expression.util.Converter;

@Service
public class MessageConsumer {
	
	@Autowired
	private Converter converter;
	
	@Autowired
	private ExpressionServiceImpl expressionServiceImpl;
	
	@KafkaListener(topics = TopicNames.INPUT_EXECUTION_REQUEST)
	public boolean receiveFilterExecutionRequest(String message) {
		ExecutableExpression executableExpression = converter.convertToExecutableExpression(message);
		return expressionServiceImpl.ExecuteExpression(executableExpression);
	}

}
