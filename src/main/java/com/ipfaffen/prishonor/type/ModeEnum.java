package com.ipfaffen.prishonor.type;

/**
 * @author Isaias Pfaffenseller
 */
public enum ModeEnum {

	PLAY,
	DEMO;

	/**
	 * @param code
	 * @return
	 */
	public boolean is(String code) {
		return code().equals(code);
	}

	/**
	 * @return
	 */
	public String code() {
		return name().toLowerCase();
	}
}