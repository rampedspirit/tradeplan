package com.bhs.gtk.mockfeed.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.bhs.gtk.mockfeed.api.AdminApi;
import com.bhs.gtk.mockfeed.model.CreateUserRequest;
import com.bhs.gtk.mockfeed.service.StockService;
import com.bhs.gtk.mockfeed.service.UserService;

@Controller
@CrossOrigin
public class AdminApiController implements AdminApi {

	@Autowired
	private StockService adminService;

	@Autowired
	private UserService userService;

	@Override
	public ResponseEntity<Void> createUser(@Valid CreateUserRequest createUserRequest) {
		userService.createUser(createUserRequest.getName(), createUserRequest.getPassword());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> updateAllSymbols(@NotNull @Valid String exchange, @NotNull @Valid String url) {
		adminService.updateAllSymbols(exchange, url);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
