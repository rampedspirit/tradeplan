package com.bhs.gtk.condition;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "condition-health")
public class HealthCheckEndPoint {

	@ReadOperation
	public String check() {
		return "condition service is healthy";
	}
}