package com.ps.soccer.exception;

import java.util.ArrayList;
import java.util.List;

public class GenericInputException extends RuntimeException {

	List<String> messages;

	public GenericInputException() {
		messages = new ArrayList<String>();
	}

	public void add(String message) {
		messages.add("No " + message + " provided(please do verify)");
	}

	public List<String> getMessages() {
		return messages;
	}
}
