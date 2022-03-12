package com.bhs.gtk.watchlist;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaAdmin;

import com.bhs.gtk.watchlist.messaging.TopicNames;

@SpringBootApplication
public class WatchlistApplication {

	@Autowired
	private KafkaAdmin kafkaAdmin;

	public static void main(String[] args) {
		SpringApplication.run(WatchlistApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void handleAfterStartupTasks() {
		if (createTopicsRequiredForScreenerService()) {
			// handle maintenance tasks here
		}
	}

	private boolean createTopicsRequiredForScreenerService() {
		try {
			NewTopic watchlistChangeNotification = new NewTopic(TopicNames.WATCHLIST_CHANGE_NOTIFICATION, 1, (short) 1);
			kafkaAdmin.createOrModifyTopics(watchlistChangeNotification);
			return true;
		} catch (KafkaException kafkaException) {
			// Log error
			System.err.println("Kafka is not up");
			kafkaException.printStackTrace();
		} catch (Exception ex) {
			// Log error
			System.err.println("Unknow issue");
			ex.printStackTrace();
		}
		return false;
	}
}
