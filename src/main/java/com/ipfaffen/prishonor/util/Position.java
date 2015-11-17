package com.ipfaffen.prishonor.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Isaias Pfaffenseller
 */
public class Position implements Comparable<Position> {

	public double x;
	public double y;

	/**
	 * @param x
	 * @param y
	 */
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(Position position) {
		if(position.x == x) {
			if(position.y == y) {
				return 0;
			}
			else if(position.y > y) {
				return -1;
			}

			return 1;
		}
		else if(position.x > x) {
			return -1;
		}

		return 1;
	}

	@Override
	public boolean equals(Object object) {
		Position position = (Position) object;
		return (x == position.x && y == position.y);
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(String.format("%s%s", x, y).replaceAll("\\.", ""));
	}

	@Override
	public String toString() {
		return String.format("%sx%s", x, y);
	}

	/**
	 * @param positions
	 * @return
	 */
	public static List<Position> toPositionList(String[] positionArray) {
		List<Position> positionsList = new ArrayList<Position>();
		for(String position: positionArray) {
			String[] xy = position.split("x");
			positionsList.add(new Position(Double.valueOf(xy[0]), Double.valueOf(xy[1])));
		}
		return positionsList;
	}
}