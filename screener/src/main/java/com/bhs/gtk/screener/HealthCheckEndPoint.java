package com.bhs.gtk.screener;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "screener-health")
public class HealthCheckEndPoint {

	@ReadOperation
	public String check() {
		return "screener service is healthy";
	}
}