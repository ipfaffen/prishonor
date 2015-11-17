package com.ipfaffen.prishonor.exception;

/**
 * @author Isaias Pfaffenseller
 */
public class TextException extends RuntimeException {

	/**
	 * @param message
	 */
	public TextException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public TextException(String message, Throwable throwable) {
		super(message, throwable);
	}
}