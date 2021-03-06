package com.bhs.gtk.expression.messaging;

import org.apache.kafka.common.errors.InterruptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class MessageProducer {
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public boolean sendMessage(String message, MessageType type) {
		String topicName;
		try {
			switch (type) {
			case EXECUTION_RESPONSE:
				topicName = TopicNames.OUTPUT_EXECUTION_RESPONSE;
				break;
			default:
				throw new IllegalArgumentException(type+" is not supported message type in Execution service");
			}
			System.err.println("FS:->"+message);
			kafkaTemplate.send(topicName,message);
			return true;
		}catch (KafkaException kafkaException) {
			//log error
			System.err.println("This happens when Kafka borker is unavailable.");
			kafkaException.printStackTrace();
		}catch (InterruptException interruptException) {
			//log error
			System.err.println("This happens when Screener service is stopped, restarted while messaging is being sent.");
			interruptException.printStackTrace();
		}catch (Exception ex) {
			//log error
			System.err.println("Reason not known");
			ex.printStackTrace();
		}
		return false;
	}
	
}
