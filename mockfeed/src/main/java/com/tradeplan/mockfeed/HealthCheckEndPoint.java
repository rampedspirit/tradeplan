package com.tradeplan.mockfeed;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "mockfeed-health")
public class HealthCheckEndPoint {

	@ReadOperation
	public String check() {
		return "mockfeed service is healthy";
	}
}