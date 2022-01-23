package com.bhs.gtk.screener;

import java.util.List;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaAdmin;

import com.bhs.gtk.screener.messaging.TopicNames;
import com.bhs.gtk.screener.persistence.ConditionResultEntity;
import com.bhs.gtk.screener.service.ConditionResultServiceImpl;
import com.bhs.gtk.screener.service.ExecutableServiceImpl;

@SpringBootApplication
public class ScreenerApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(ScreenerApplication.class, args);
	}
	
	@Autowired
	private KafkaAdmin kafkaAdmin;
	
	@Autowired
	private ConditionResultServiceImpl conditionResultServiceImpl;
	
	@Autowired
	private ExecutableServiceImpl executableServiceImpl;
	
	@EventListener(ApplicationReadyEvent.class)
	public void handleAfterStartupTasks() {
		if(createTopicsRequiredForScreenerService()) {
			List<ConditionResultEntity> conditions = conditionResultServiceImpl.runAllQueuedConditions();
			executableServiceImpl.updateStatusOfExecutablesBasedOnConditions(conditions);
		}
	}

	private boolean createTopicsRequiredForScreenerService() {
		try {
			NewTopic outputExecutionRequest = new NewTopic(TopicNames.OUTPUT_EXECUTION_REQUEST, 1, (short) 1);
			NewTopic inputExecutionResponse = new NewTopic(TopicNames.INPUT_EXECUTION_RESPONSE, 1, (short) 1);
			NewTopic inputChangeNotification = new NewTopic(TopicNames.INPUT_CONDITION_CHANGE_NOTIFICATION, 1, (short) 1);
			kafkaAdmin.createOrModifyTopics(outputExecutionRequest,inputExecutionResponse,inputChangeNotification);
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
