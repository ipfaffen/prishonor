package com.ipfaffen.prishonor.key;

import javafx.scene.input.KeyCode;

/**
 * @author Isaias Pfaffenseller
 */
public class KeyAction implements Comparable<KeyAction> {
	
	protected KeyCode keyCode;

	/**
	 * @param keyCode
	 */
	public KeyAction(KeyCode keyCode) {
		this.keyCode = keyCode;
	}

	@Override
	public int compareTo(KeyAction direction) {
		return keyCode().compareTo(direction.keyCode());
	}

	/**
	 * @param direction
	 * @return
	 */
	public boolean equals(KeyAction direction) {
		return keyCode() == direction.keyCode();
	}

	/**
	 * @return
	 */
	public KeyCode keyCode() {
		return keyCode;
	}
}