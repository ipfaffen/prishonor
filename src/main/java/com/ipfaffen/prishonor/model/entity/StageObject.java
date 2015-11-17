package com.ipfaffen.prishonor.model.entity;

import java.io.Serializable;

/**
 * @author Isaias Pfaffenseller
 */
public class StageObject implements Serializable {

	private Integer positionX;
	private Integer positionY;
	private Background background;

	/**
	 * @return positionX
	 */
	public Integer getPositionX() {
		return positionX;
	}

	/**
	 * @param positionX
	 */
	public void setPositionX(Integer positionX) {
		this.positionX = positionX;
	}

	/**
	 * @return positionY
	 */
	public Integer getPositionY() {
		return positionY;
	}

	/**
	 * @param positionY
	 */
	public void setPositionY(Integer positionY) {
		this.positionY = positionY;
	}

	/**
	 * @return background
	 */
	public Background getBackground() {
		return background;
	}

	/**
	 * @param background
	 */
	public void setBackground(Background background) {
		this.background = background;
	}
}