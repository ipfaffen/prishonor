package com.ipfaffen.prishonor.layout;

/**
 * @author Isaias Pfaffenseller
 */
public class SpriteViewport {

	private int index;
	private int rowSpan;
	private int colSpan;

	/**
	 * @param index
	 */
	public SpriteViewport(int index) {
		this(index, 1, 1);
	}

	/**
	 * @param index
	 * @param rowSpan
	 * @param colSpan
	 */
	public SpriteViewport(int index, int rowSpan, int colSpan) {
		this.index = index;
		this.rowSpan = rowSpan;
		this.colSpan = colSpan;
	}

	/**
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return
	 */
	public int getRowSpan() {
		return rowSpan;
	}

	/**
	 * @return
	 */
	public int getColSpan() {
		return colSpan;
	}
}