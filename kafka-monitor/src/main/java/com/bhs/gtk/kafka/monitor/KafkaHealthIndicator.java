package com.bhs.gtk.kafka.monitor;

import java.util.Map;

import org.apache.kafka.clients.admin.TopicDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import com.bhs.gtk.kafka.monitor.util.TopicNameConstants;

/**
 * Class to monitor the Kafka Health
 *
 */
@Component("kafkaHealth")
public class KafkaHealthIndicator implements HealthIndicator {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMonitorApplication.class);

	@Autowired
	private KafkaAdmin kafkaAdmin;

	@Override
	public Health health() {
		LOGGER.info("Health check started.");
		try {
			Map<String, TopicDescription> registry = kafkaAdmin.describeTopics(TopicNameConstants.STARTUP_COMPLETE);
			if (registry.values().size() > 0) {
				LOGGER.info("Health check successful.");
				return Health.up().build();
			}
		} catch (Exception ex) {
			LOGGER.error("Health check failed.", ex);
			return Health.down(ex).build();
		}
		LOGGER.error("Health check failed as kafka broker is missing the startup topic.");
		return Health.down().build();
	}

}
