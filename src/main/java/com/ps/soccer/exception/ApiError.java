package com.ps.soccer.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {

	List<String> messages;

	public ApiError(String message) {
		messages = new ArrayList<String>();
		messages.add(message);
	}

	public void add(String message) {
		messages.add(message);
	}

}