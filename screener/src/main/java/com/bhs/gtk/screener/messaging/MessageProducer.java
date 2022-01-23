package com.bhs.gtk.screener.messaging;

import org.apache.kafka.common.errors.InterruptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	
	public boolean sendMessage(String message) {
		try {
			kafkaTemplate.send(TopicNames.OUTPUT_EXECUTION_REQUEST,message);
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
