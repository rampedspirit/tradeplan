package com.bhs.gtk.expression.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class ExpressionMessageProducer {
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public void sendMessage(String message) {
        
		System.out.println("sending....");
	    ListenableFuture<SendResult<String, String>> future = 
	      kafkaTemplate.send(MessageConstants.OUTPUT_TOPIC_NAME, message);
		
	    future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
	        @Override
	        public void onSuccess(SendResult<String, String> result) {
	            System.out.println("Sent message=[" + message + 
	              "] with offset=[" + result.getRecordMetadata().offset() + "]");
	        }
	        @Override
	        public void onFailure(Throwable ex) {
	            System.out.println("Unable to send message=[" 
	              + message + "] due to : " + ex.getMessage());
	        }
	    });
	}
	
	
	

//	public boolean sendMessage(EvaluationResponse evaluationResponse) {
//		Producer<String, String> producer = getKafkaProducer();
//		String key = String.valueOf(evaluationResponse.getChecksum()); 
//		ObjectMapper mapper = new ObjectMapper();
//		String value = null;;
//		try {
//			value = mapper.writeValueAsString(evaluationResponse);
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}; 
//		System.err.println("producer sending "+value);
////		producer.send(new ProducerRecord<String, String>(MessageConstants.TOPIC_NAME, key, value));
////		producer.close();
//		return true;
//	}
//
//	private Producer<String, String> getKafkaProducer() {
//		Properties props = new Properties();
//		 props.put("bootstrap.servers", "localhost:9092");
//		 props.put("acks", "all");
//		 props.put("retries", 0);
//		 props.put("linger.ms", 1);
//		 props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//		 props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//		 
//		 Producer<String, String> producer = new KafkaProducer<String, String>(props);
//		return producer;
//	}

}
