package com.ipfaffen.prishonor.layout;

import javafx.scene.image.Image;

/**
 * @author Isaias Pfaffenseller
 */
public class SpriteImageBuilder {

	private Image image;
	private double cellWidth;
	private double cellHeight;
	private int cellOffsetX;
	private int cellOffsetY;

	/**
	 * @return
	 */
	public static SpriteImageBuilder create() {
		return new SpriteImageBuilder();
	}

	/**
	 * @return
	 */
	public static SpriteImageBuilder create(Image image) {
		return new SpriteImageBuilder(image);
	}

	public SpriteImageBuilder() {
	}

	/**
	 * @param image
	 */
	public SpriteImageBuilder(Image image) {
		this.image = image;
	}

	/**
	 * @param image
	 * @return
	 */
	public SpriteImageBuilder image(Image image) {
		this.image = image;
		return this;
	}

	/**
	 * @param cellWidth
	 * @param cellHeight
	 * @return
	 */
	public SpriteImageBuilder cellSize(double cellWidth, double cellHeight) {
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		return this;
	}

	/**
	 * @param cellOffsetX
	 * @param cellOffsetY
	 * @return
	 */
	public SpriteImageBuilder cellOffset(int cellOffsetX, int cellOffsetY) {
		this.cellOffsetX = cellOffsetX;
		this.cellOffsetY = cellOffsetY;
		return this;
	}

	/**
	 * @return
	 */
	public SpriteImage build() {
		return new SpriteImage(image, cellWidth, cellHeight, cellOffsetX, cellOffsetY);
	}
}