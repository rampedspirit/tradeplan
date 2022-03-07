package com.bhs.gtk.filter;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "filter-health")
public class HealthCheckEndPoint {

	@ReadOperation
	public String check() {
		return "filter service is healthy";
	}
}