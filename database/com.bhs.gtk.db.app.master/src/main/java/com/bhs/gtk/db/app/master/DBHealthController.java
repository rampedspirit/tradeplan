package com.bhs.gtk.db.app.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DBHealthController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/health")
	public void healthCheck() {
		jdbcTemplate.queryForObject("SELECT version()", String.class);
	}
}
