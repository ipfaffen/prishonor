package com.ipfaffen.prishonor.animation;

import java.util.ArrayList;
import java.util.List;

import com.ipfaffen.prishonor.layout.SpriteImage;
import com.ipfaffen.prishonor.layout.SpriteViewport;

/**
 * @author Isaias Pfaffenseller
 */
public class SpriteSequence {

	private final SpriteImage spriteImage;

	private int indexFrom = -1;
	private int indexTo = -1;
	private int rowSpan = 1;
	private int colSpan = 1;

	/**
	 * @param spriteImage
	 * @return
	 */
	public static SpriteSequence create(SpriteImage spriteImage) {
		return new SpriteSequence(spriteImage);
	}

	/**
	 * @param spriteImage
	 */
	public SpriteSequence(SpriteImage spriteImage) {
		this.spriteImage = spriteImage;
	}

	/**
	 * @param row
	 * @param col
	 * @return
	 */
	public SpriteSequence from(int row, int col) {
		int index = ((row * spriteImage.getCols()) + col);
		return from(index);
	}

	/**
	 * @param index
	 * @param duration
	 * @return
	 */
	public SpriteSequence from(int index) {
		this.indexFrom = index;
		return this;
	}

	/**
	 * @param row
	 * @param col
	 * @return
	 */
	public SpriteSequence to(int row, int col) {
		int index = ((row * spriteImage.getCols()) + col);
		return to(index);
	}

	/**
	 * @param index
	 * @param duration
	 * @return
	 */
	public SpriteSequence to(int index) {
		this.indexTo = index;
		return this;
	}

	/**
	 * @param rowSpan
	 * @param colSpan
	 * @return
	 */
	public SpriteSequence span(int rowSpan, int colSpan) {
		this.rowSpan = rowSpan;
		this.colSpan = colSpan;
		return this;
	}

	/**
	 * @param rowSpan
	 * @return
	 */
	public SpriteSequence rowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
		return this;
	}

	/**
	 * @param colSpan
	 * @return
	 */
	public SpriteSequence colSpan(int colSpan) {
		this.colSpan = colSpan;
		return this;
	}

	/**
	 * @return
	 */
	public List<SpriteViewport> buildViewports() {
		List<SpriteViewport> viewports = new ArrayList<SpriteViewport>();

		for(int index = indexFrom; index <= indexTo; index += colSpan) {
			viewports.add(new SpriteViewport(index, rowSpan, colSpan));
		}

		return viewports;
	}
}