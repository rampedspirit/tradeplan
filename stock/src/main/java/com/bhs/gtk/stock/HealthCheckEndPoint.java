package com.bhs.gtk.stock;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "stock-health")
public class HealthCheckEndPoint {

	@ReadOperation
	public String check() {
		return "stock service is healthy";
	}
}