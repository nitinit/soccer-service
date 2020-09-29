package com.ps.soccer.exception;

public class DataNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataNotFoundException() {
		super();
	}

	public DataNotFoundException(final String message) {
		super(message);
	}

	public DataNotFoundException(final String message, final Throwable throwable) {
		super(message, throwable);
	}
}

