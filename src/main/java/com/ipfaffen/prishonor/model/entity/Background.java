package com.ipfaffen.prishonor.model.entity;

import java.io.Serializable;

/**
 * @author Isaias Pfaffenseller
 */
public class Background implements Serializable {

	private String color;
	private String imageName;

	/**
	 * @return color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * @param imageName
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}