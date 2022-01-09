package com.bhs.gtk.kafka.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

/**
 * Class to monitor the Kafka Health
 *
 */
@Component("kafkaHealth")
public class KafkaHealthIndicator implements HealthIndicator {

	@Autowired
	private KafkaAdmin kafkaAdmin;

	@Override
	public Health health() {
//		try {
//			Map<String, TopicDescription> registry = kafkaAdmin.describeTopics(TopicNameConstants.STARTUP_COMPLETE);
//			if (registry.values().size() > 0) {
//				return Health.up().build();
//			}
//		} catch (Exception ex) {
//			System.out.println("Kafka rboker is unreachable");
//			return Health.down(ex).build();
//		}
//		System.out.println("Kafka broker is missing the startup topic");
		return Health.down().build();
	}

}
