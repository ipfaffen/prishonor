package com.ipfaffen.prishonor.exception;

/**
 * @author Isaias Pfaffenseller
 */
public class MessageException extends TextException {
	
	private String key;

	/**
	 * @param key
	 */
	public MessageException(String key) {
		this(key, key, null);
	}

	/**
	 * @param key
	 * @param throwable
	 */
	public MessageException(String key, Throwable throwable) {
		this(key, key, throwable);
	}

	/**
	 * @param key
	 * @param message
	 */
	public MessageException(String key, String message) {
		this(key, message, null);
	}

	/**
	 * @param key
	 * @param message
	 * @param throwable
	 */
	public MessageException(String key, String message, Throwable throwable) {
		super(message, throwable);
		this.key = key;
	}

	/**
	 * @return
	 */
	public String getKey() {
		return key;
	}
}