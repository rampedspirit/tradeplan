package com.bhs.gtk.kafka.monitor;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

import com.bhs.gtk.kafka.monitor.util.TopicNameConstants;

@SpringBootApplication
public class KafkaMonitorApplication {

	@Autowired
	private KafkaAdmin kafkaAdmin;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public static void main(String[] args) {
		SpringApplication.run(KafkaMonitorApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void handleAfterStartupTasks() {
		NewTopic startupTopic = new NewTopic(TopicNameConstants.STARTUP_COMPLETE, 1, (short) 1);
		try {
			kafkaAdmin.createOrModifyTopics(startupTopic);
			kafkaTemplate.send(TopicNameConstants.STARTUP_COMPLETE, "Kafka Broker Started");
		} catch (Exception ex) {
			System.err.println("Failed to create and publish kafka startup topic [" + ex.getMessage() + "]");
		}
	}
}
