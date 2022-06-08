package com.tradeplan.dataReader;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaAdmin;

import com.tradeplan.dataReader.messaging.TopicNames;


@SpringBootApplication
public class DataReaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataReaderApplication.class, args);
	}
	
	@Autowired
	private KafkaAdmin kafkaAdmin;
	
	@EventListener(ApplicationReadyEvent.class)
	public void handleAfterStartupTasks() {
		System.err.println("Data reader service started :-) ");
		if(createTopicsRequiredForDataReader()) {
			System.err.println("Topics required for data reader is created/modified successfully!");
			//TODO: handle maintenance tasks here
		}
	}
	
	
	private boolean createTopicsRequiredForDataReader() {
		try {
			NewTopic outputExecutionRequest = new NewTopic(TopicNames.INPUT_READ_REQUEST, 1, (short) 1);
			NewTopic outputExecutionResponse = new NewTopic(TopicNames.OUTPUT_READ_RESPONSE, 1, (short) 1);
			
			kafkaAdmin.createOrModifyTopics(outputExecutionRequest, outputExecutionResponse);
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
