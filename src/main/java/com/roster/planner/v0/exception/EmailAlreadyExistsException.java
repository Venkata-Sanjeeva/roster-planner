package com.roster.planner.v0.exception;

public class EmailAlreadyExistsException extends RuntimeException {
	public EmailAlreadyExistsException(String msg) {
		super(msg);
	}
}