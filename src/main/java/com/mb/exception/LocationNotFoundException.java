package com.mb.exception;

public class LocationNotFoundException extends Exception {

	private static final long serialVersionUID = -1113658892473302544L;

	public LocationNotFoundException() {
		super();
	}

	public LocationNotFoundException(String message) {
		super(message);
	}

	public LocationNotFoundException(Throwable cause) {
		super(cause);
	}

	public LocationNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}