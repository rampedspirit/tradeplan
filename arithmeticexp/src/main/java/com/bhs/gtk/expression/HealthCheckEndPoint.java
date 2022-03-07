package com.bhs.gtk.expression;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "expression-health")
public class HealthCheckEndPoint {

	@ReadOperation
	public String check() {
		return "expression service is healthy";
	}
}