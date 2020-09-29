package com.ps.soccer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {


	@ExceptionHandler(DataNotFoundException.class)
	protected ResponseEntity<Object> handleDataNotFound(DataNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(exception.getMessage()));
	}

	@ExceptionHandler(GenericInputException.class)
	protected ResponseEntity<Object> handleGenericInputException(GenericInputException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(exception.getMessages()));
	}

	@ExceptionHandler(ServiceNotAvailableException.class)
	protected ResponseEntity<Object> handleServiceNotAvailable(ServiceNotAvailableException exception) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiError(exception.getMessage()));
	}
}