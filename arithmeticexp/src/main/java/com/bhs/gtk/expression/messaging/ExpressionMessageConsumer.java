package com.bhs.gtk.expression.messaging;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
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
	public void receiveMessageFromInputTopic(String message) {
	    expressionMessageProducer.sendMessage(message.toUpperCase());
	}
	
//	public boolean receiveMessage() {
//		KafkaConsumer<String, String> consumer = getKafkaConsumer();
//		consumer.subscribe(Arrays.asList(MessageConstants.TOPIC_NAME));
//		ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
//		for (ConsumerRecord<String, String> record : records) {
//			System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
//			processExpression(record.value());
//		}
//		consumer.close();
//		System.out.println("MESSAGE RECEIVED SUCCESSFULLY");
//		return true;
//	}
//
//	private void processExpression(String evaluationRequest) {
//		EvaluationResponse response =  evaluationServiceImpl.evaluate(evaluationRequest);
//		//expressionMessageProducer.sendMessage(response);
//	}
//
//	private KafkaConsumer<String, String> getKafkaConsumer() {
//		Properties props = new Properties();
//		props.setProperty("bootstrap.servers", "localhost:9092");
//		props.setProperty("group.id", "test");
//		props.setProperty("enable.auto.commit", "true");
//		props.setProperty("auto.commit.interval.ms", "1000");
//		props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//		props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//		return consumer;
//	}

}
