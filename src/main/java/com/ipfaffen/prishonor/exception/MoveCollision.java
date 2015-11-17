package com.ipfaffen.prishonor.exception;

import com.ipfaffen.prishonor.util.Position;

/**
 * @author Isaias Pfaffenseller
 */
public class MoveCollision extends RuntimeException {

	private Position collisionPosition;

	/**
	 * @param collisionPosition
	 */
	public MoveCollision(Position collisionPosition) {
		super();
		this.collisionPosition = collisionPosition;
	}

	/**
	 * @return
	 */
	public Position getCollisionPosition() {
		return collisionPosition;
	}
}