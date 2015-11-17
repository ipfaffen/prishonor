package com.ipfaffen.prishonor.util;

import javafx.util.Duration;

/**
 * @author Isaias Pfaffenseller
 */
public class Chronometer {
	
	private Long start;
	private Long stop;

	/**
	 * @return
	 */
	public Long start() {
		start = System.currentTimeMillis();
		return start;
	}

	/**
	 * @return
	 */
	public Long stop() {
		stop = System.currentTimeMillis();
		return stop;
	}

	/**
	 * @return
	 */
	public Duration getDuration() {
		return Duration.millis(stop - start);
	}
}
