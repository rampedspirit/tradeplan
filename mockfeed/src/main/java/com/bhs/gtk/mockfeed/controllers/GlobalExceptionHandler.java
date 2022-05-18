package com.bhs.gtk.mockfeed.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bhs.gtk.mockfeed.exceptions.TokenException;
import com.bhs.gtk.mockfeed.model.TokenError;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { TokenException.class })
	protected ResponseEntity<Object> handleTokenException(TokenException ex, WebRequest request) {
		TokenError error = new TokenError();
		error.setError(ex.getError());
		error.setErrorDescription(ex.getErrorDescription());
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
}
