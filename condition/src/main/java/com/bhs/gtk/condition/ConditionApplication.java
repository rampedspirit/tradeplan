package com.bhs.gtk.condition;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaAdmin;

import com.bhs.gtk.condition.messaging.TopicNames;

@SpringBootApplication
public class ConditionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConditionApplication.class, args);
	}
	
	@Autowired
	private KafkaAdmin kafkaAdmin;
	
	@EventListener(ApplicationReadyEvent.class)
	public void handleAfterStartupTasks() {
		if(createTopicsRequiredForScreenerService()) {
			// handle maintenance tasks here
		}
	}

	private boolean createTopicsRequiredForScreenerService() {
		try {
			NewTopic outputExecutionRequest = new NewTopic(TopicNames.OUTPUT_EXECUTION_REQUEST, 1, (short) 1);
			NewTopic outputExecutionResponse = new NewTopic(TopicNames.OUTPUT_EXECUTION_RESPONSE, 1, (short) 1);
			NewTopic outputChangeNotification = new NewTopic(TopicNames.OUTPUT_CHANGE_NOTIFICATION, 1, (short) 1);
			NewTopic inputExecutionRequest = new NewTopic(TopicNames.INPUT_EXECUTION_REQUEST, 1, (short) 1);
			NewTopic inputUpdateNotification = new NewTopic(TopicNames.INPUT_UPDATE_NOTIFICATION, 1, (short) 1);
			
			kafkaAdmin.createOrModifyTopics(outputExecutionRequest, outputExecutionResponse, outputChangeNotification,
					inputExecutionRequest, inputUpdateNotification);
			return true;
		}catch (KafkaException kafkaException) {
			//Log error
			System.err.println("Kafka is not up");
			kafkaException.printStackTrace();
		}catch (Exception ex) {
			//Log error
			System.err.println("Unknow issue");
			ex.printStackTrace();
		}
		return false;
	}

}
