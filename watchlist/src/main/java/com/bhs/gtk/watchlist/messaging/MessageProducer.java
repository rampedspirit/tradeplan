package com.bhs.gtk.watchlist.messaging;

import java.util.UUID;

import org.apache.kafka.common.errors.InterruptException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.bhs.gtk.watchlist.messaging.ChangeNotification.ChangeStatusEnum;

@Service
public class MessageProducer {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public boolean sendChangeNotification(UUID id, ChangeStatusEnum status) {
		ChangeNotification changeNotification = new ChangeNotification(id, status);
		JSONObject changeNotificationJsonObject = new JSONObject(changeNotification);
		return sendMessage(changeNotificationJsonObject.toString(), MessageType.CHANGE_NOTIFICATION);
	}

	private boolean sendMessage(String message, MessageType type) {
		String topicName;
		try {
			switch (type) {
			case CHANGE_NOTIFICATION:
				topicName = TopicNames.WATCHLIST_CHANGE_NOTIFICATION;
				break;
			default:
				throw new IllegalArgumentException(type + " is not supported message type in Condition service");
			}
			System.err.println("WS:->" + message);
			kafkaTemplate.send(topicName, message);
			return true;
		} catch (KafkaException kafkaException) {
			// log error
			System.err.println("This happens when Kafka borker is unavailable.");
			kafkaException.printStackTrace();
		} catch (InterruptException interruptException) {
			// log error
			System.err
					.println("This happens when Screener service is stopped, restarted while messaging is being sent.");
			interruptException.printStackTrace();
		} catch (Exception ex) {
			// log error
			System.err.println("Reason not known");
			ex.printStackTrace();
		}
		return false;
	}

}
