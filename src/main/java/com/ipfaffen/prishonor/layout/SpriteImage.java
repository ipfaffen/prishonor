package com.ipfaffen.prishonor.layout;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

/**
 * @author Isaias Pfaffenseller
 */
public class SpriteImage extends ImageView {

	private double cellWidth;
	private double cellHeight;
	private int cellOffsetX;
	private int cellOffsetY;

	private int rows;
	private int cols;
	private int cellSize;

	private boolean flippedHorizontal;
	private boolean flippedVertical;

	/**
	 * @param image
	 * @param cellWidth
	 * @param cellHeight
	 * @param cellOffsetX
	 * @param cellOffsetY
	 */
	public SpriteImage(Image image, double cellWidth, double cellHeight, int cellOffsetX, int cellOffsetY) {
		super(image);

		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		this.cellOffsetX = cellOffsetX;
		this.cellOffsetY = cellOffsetY;

		rows = new Double(image.getHeight() / (cellHeight + cellOffsetY)).intValue();
		cols = new Double(image.getWidth() / (cellWidth + cellOffsetX)).intValue();
		cellSize = rows * cols;

		// For correct Y rotate.
		setTranslateZ(50);

		// The first viewport is set at 0 index.
		render(0);
	}

	/**
	 * @param index - starts at 0.
	 */
	public SpriteImage render(int index) {
		return render(index, 1, 1);
	}

	/**
	 * @param index - starts at 0.
	 * @param rowSpan
	 * @param colSpan
	 */
	public SpriteImage render(int index, int rowSpan, int colSpan) {
		int row = (index / cols);
		int col = (index % cols);

		return render(row, col, rowSpan, colSpan);
	}

	/**
	 * @param row - starts at 0.
	 * @param col - starts at 0.
	 */
	public SpriteImage render(int row, int col) {
		return render(row, col, 1, 1);
	}

	/**
	 * @param row - starts at 0.
	 * @param col - starts at 0.
	 * @param rowSpan
	 * @param colSpan
	 * @return
	 */
	public SpriteImage render(int row, int col, int rowSpan, int colSpan) {
		double y = row * (cellHeight + cellOffsetX) + cellOffsetY;
		double x = col * (cellWidth + cellOffsetX) + cellOffsetX;

		setViewport(new Rectangle2D(x, y, (cellWidth * colSpan), (cellHeight * rowSpan)));

		return this;
	}

	/**
	 * @param spriteViewport
	 * @return
	 */
	public SpriteImage render(SpriteViewport spriteViewport) {
		return render(spriteViewport.getIndex(), spriteViewport.getRowSpan(), spriteViewport.getColSpan());
	}

	/**
	 * @return
	 */
	public SpriteImage flipHorizontal() {
		setRotationAxis(Rotate.Y_AXIS);
		setRotate(isFlippedHorizontal() ? 0 : 180);

		flippedHorizontal = !isFlippedHorizontal();

		return this;
	}

	/**
	 * @return
	 */
	public SpriteImage flipVertical() {
		setRotationAxis(Rotate.Z_AXIS);
		setRotate(isFlippedVertical() ? 0 : 180);

		flippedVertical = !isFlippedVertical();

		return this;
	}

	/**
	 * @return
	 */
	public SpriteImage reset() {
		if(isFlippedHorizontal()) {
			flipHorizontal();
		}
		if(isFlippedVertical()) {
			flipVertical();
		}

		return this;
	}

	/**
	 * @return
	 */
	public ObjectProperty<Rectangle2D> renderProperty() {
		return viewportProperty();
	}

	/**
	 * @return
	 */
	public double getCellWidth() {
		return cellWidth;
	}

	/**
	 * @return
	 */
	public double getCellHeight() {
		return cellHeight;
	}

	/**
	 * @return
	 */
	public int getCellOffsetX() {
		return cellOffsetX;
	}

	/**
	 * @return
	 */
	public int getCellOffsetY() {
		return cellOffsetY;
	}

	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @return
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * @return
	 */
	public int getCellSize() {
		return cellSize;
	}

	/**
	 * @return
	 */
	public boolean isFlippedHorizontal() {
		return flippedHorizontal;
	}

	/**
	 * @return
	 */
	public boolean isFlippedVertical() {
		return flippedVertical;
	}
}