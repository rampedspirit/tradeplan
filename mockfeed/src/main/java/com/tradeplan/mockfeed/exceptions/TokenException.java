package com.tradeplan.mockfeed.exceptions;

public class TokenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String error;
	private final String errorDescription;

	public TokenException(String errorDescription) {
		this.error = "invalid_grant";
		this.errorDescription = errorDescription;
	}

	public String getError() {
		return error;
	}

	public String getErrorDescription() {
		return errorDescription;
	}
}
