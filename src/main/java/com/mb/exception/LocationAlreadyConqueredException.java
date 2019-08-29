package com.mb.exception;

public class LocationAlreadyConqueredException extends Exception {

	private static final long serialVersionUID = 7464195268238037022L;

	public LocationAlreadyConqueredException() {
		super();
	}

	public LocationAlreadyConqueredException(String message) {
		super(message);
	}

	public LocationAlreadyConqueredException(Throwable cause) {
		super(cause);
	}

	public LocationAlreadyConqueredException(String message, Throwable cause) {
		super(message, cause);
	}
}