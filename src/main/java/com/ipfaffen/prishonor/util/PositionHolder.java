package com.ipfaffen.prishonor.util;

import java.util.TreeSet;

/**
 * @author Isaias Pfaffenseller
 */
@SuppressWarnings("serial")
public class PositionHolder extends TreeSet<Position> {

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean add(double x, double y) {
		return super.add(new Position(x, y));
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean remove(double x, double y) {
		return super.remove(new Position(x, y));
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean contains(double x, double y) {
		return super.contains(new Position(x, y));
	}

	/**
	 * @return
	 */
	public Position random() {
		if(size() <= 0) {
			return null;
		}

		int randomIndex = (int) (Math.random() * size());
		return (Position) toArray()[randomIndex];
	}
}