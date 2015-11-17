package com.ipfaffen.prishonor.key;

import javafx.scene.input.KeyCode;

import com.ipfaffen.prishonor.type.KeyEnum;

/**
 * @author Isaias Pfaffenseller
 */
public class KeyDirection extends KeyAction {

	/**
	 * @param keyCode
	 */
	public KeyDirection(KeyCode keyCode) {
		super(keyCode);
	}

	/**
	 * @return
	 */
	public boolean isRight() {
		return (keyCode == KeyCode.RIGHT);
	}

	/**
	 * @return
	 */
	public boolean isLeft() {
		return (keyCode == KeyCode.LEFT);
	}

	/**
	 * @return
	 */
	public boolean isUp() {
		return (keyCode == KeyCode.UP);
	}

	/**
	 * @return
	 */
	public boolean isDown() {
		return (keyCode == KeyCode.DOWN);
	}

	/**
	 * @return
	 */
	public KeyDirection reversed() {
		if(isRight()) {
			return (KeyDirection) KeyEnum.LEFT.action();
		}
		else if(isLeft()) {
			return (KeyDirection) KeyEnum.RIGHT.action();
		}
		else if(isUp()) {
			return (KeyDirection) KeyEnum.DOWN.action();
		}
		else if(isDown()) {
			return (KeyDirection) KeyEnum.UP.action();
		}
		return null;
	}

	/**
	 * @param direction
	 * @return
	 */
	public boolean isReverse(KeyDirection direction) {
		return ((isRight() && direction.isLeft()) || (isLeft() && direction.isRight()) || (isUp() && direction.isDown()) || (isDown() && direction.isUp()));
	}

	/**
	 * @param keyCode
	 * @return
	 */
	public static boolean isDirection(KeyCode keyCode) {
		return (KeyEnum.LEFT.is(keyCode) || KeyEnum.RIGHT.is(keyCode) || KeyEnum.DOWN.is(keyCode) || KeyEnum.UP.is(keyCode));
	}
}