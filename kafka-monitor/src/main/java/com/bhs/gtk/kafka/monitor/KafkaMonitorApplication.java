package com.bhs.gtk.kafka.monitor;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMonitorApplication.class);

	@Autowired
	private KafkaAdmin kafkaAdmin;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public static void main(String[] args) {
		SpringApplication.run(KafkaMonitorApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void handleAfterStartupTasks() {
		LOGGER.info("Initiating Startup Task");
		NewTopic startupTopic = new NewTopic(TopicNameConstants.STARTUP_COMPLETE, 1, (short) 1);
		try {
			LOGGER.info("Creating topic for {}.", TopicNameConstants.STARTUP_COMPLETE);
			kafkaAdmin.createOrModifyTopics(startupTopic);
			LOGGER.info("{} topic created successfully.", TopicNameConstants.STARTUP_COMPLETE);

			LOGGER.info("Sending  message on {}", TopicNameConstants.STARTUP_COMPLETE);
			kafkaTemplate.send(TopicNameConstants.STARTUP_COMPLETE, "Kafka Broker Started");
			LOGGER.info("Sent message on {} topic successfully.", TopicNameConstants.STARTUP_COMPLETE);

		} catch (Exception ex) {
			LOGGER.error("Failed to create and publish kafka startup topic", ex);
		}
	}
}
