package com.tradeplan.dataReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class DataReaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataReaderApplication.class, args);
	}

	
	@EventListener(ApplicationReadyEvent.class)
	public void handleAfterStartupTasks() {
		System.err.println("Data reader service started :-) ");
	}
}
