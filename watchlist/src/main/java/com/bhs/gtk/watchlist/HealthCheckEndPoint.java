package com.bhs.gtk.watchlist;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "watchlist-health")
public class HealthCheckEndPoint {

	@ReadOperation
	public String check() {
		return "watchlist service is healthy";
	}
}