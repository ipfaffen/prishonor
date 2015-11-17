package com.ipfaffen.prishonor.type;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;

import com.ipfaffen.prishonor.key.KeyAction;
import com.ipfaffen.prishonor.key.KeyAttack;
import com.ipfaffen.prishonor.key.KeyDirection;
import com.ipfaffen.prishonor.key.KeyTab;

/**
 * @author Isaias Pfaffenseller
 */
public enum KeyEnum {

	UP(1, new KeyDirection(KeyCode.UP)),
	RIGHT(2, new KeyDirection(KeyCode.RIGHT)),
	DOWN(3, new KeyDirection(KeyCode.DOWN)),
	LEFT(4, new KeyDirection(KeyCode.LEFT)),
	TAB(5, new KeyTab(KeyCode.TAB)),
	SPACE(6, new KeyAttack(KeyCode.SPACE));

	private KeyAction action;
	private Integer number;

	/**
	 * @param number
	 */
	private KeyEnum(Integer number, KeyAction keyAction) {
		this.action = keyAction;
		this.number = number;
	}

	/**
	 * @param keyCode
	 * @return
	 */
	public boolean is(KeyCode keyCode) {
		return code() == keyCode;
	}

	/**
	 * @param keyNumber
	 * @return
	 */
	public boolean is(Integer keyNumber) {
		return number() == keyNumber;
	}

	/**
	 * @return
	 */
	public KeyAction action() {
		return action;
	}

	/**
	 * @return
	 */
	public KeyCode code() {
		return action.keyCode();
	}

	/**
	 * @return
	 */
	public Integer number() {
		return number;
	}

	/**
	 * @param name
	 * @return
	 */
	public static KeyEnum get(String name) {
		return valueOf(name.toUpperCase());
	}

	/**
	 * @param keyCode
	 * @return
	 */
	public static KeyEnum get(KeyCode keyCode) {
		return get(keyCode.getName());
	}

	/**
	 * @param keyNumber
	 * @return
	 */
	public static KeyEnum get(Integer keyNumber) {
		for(KeyEnum value: values()) {
			if(value.is(keyNumber)) {
				return value;
			}
		}
		return null;
	}

	/**
	 * @param keyNumbers
	 * @return
	 */
	public static List<KeyCode> toKeyCodeList(List<Integer> keyNumbers) {
		List<KeyCode> kc = new ArrayList<KeyCode>();
		for(Integer keyNumber: keyNumbers) {
			kc.add(get(keyNumber).code());
		}
		return kc;
	}

	/**
	 * @param keyNumbers
	 * @return
	 */
	public static List<KeyCode> toKeyCodeList(String[] keyNumbers) {
		List<KeyCode> kc = new ArrayList<KeyCode>();
		for(String keyNumber: keyNumbers) {
			kc.add(get(Integer.valueOf(keyNumber)).code());
		}
		return kc;
	}

	/**
	 * @param keyNumbers
	 * @return
	 */
	public static List<KeyAction> toKeyActionList(String[] keyNumbers) {
		List<KeyAction> ka = new ArrayList<KeyAction>();
		for(String keyNumber: keyNumbers) {
			ka.add(get(Integer.valueOf(keyNumber)).action());
		}
		return ka;
	}
}