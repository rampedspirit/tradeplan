package com.bhs.gtk.screener.messaging;

import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bhs.gtk.screener.messaging.ChangeNotification.ChangeStatusEnum;
import com.bhs.gtk.screener.service.ScreenerServiceImpl;

@Service
public class MessageConsumer {
	
	@Autowired
	private ScreenerServiceImpl screenerServiceImpl;
	
	@KafkaListener(topics = TopicNames.INPUT_EXECUTION_RESPONSE)
	public boolean receiveExecutionResponse(String message) {
		return screenerServiceImpl.adaptExecutionResponse(message);
	}
	
	@KafkaListener(topics = TopicNames.INPUT_CONDITION_CHANGE_NOTIFICATION)
	public boolean receiveChangeNotification(String message) {
		//TODO: similar to this handle changeInWatchList
		ChangeNotification changeNotification = createChangeNotificationObject(message);
		return screenerServiceImpl.adaptChangeInCondition(changeNotification);
	}

	private ChangeNotification createChangeNotificationObject(String message) {
		JSONObject jsonObject = new JSONObject(message);
		String id = (String)jsonObject.get("id");
		String status = (String)jsonObject.get("status");
		ChangeNotification changeNotification = new ChangeNotification(UUID.fromString(id), ChangeStatusEnum.fromValue(status));
		return changeNotification;
	}
	

}
