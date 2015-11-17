package com.ipfaffen.prishonor.layout;

import javafx.beans.property.DoubleProperty;

import com.ipfaffen.prishonor.model.entity.Background;
import com.ipfaffen.prishonor.util.Position;

/**
 * @author Isaias Pfaffenseller
 */
public class ArenaSimpleElement extends SimpleElement {

	/**
	 * @param width
	 * @param height
	 */
	public ArenaSimpleElement(double width, double height) {
		super(width, height);
	}

	/**
	 * @param width
	 * @param height
	 * @param background
	 */
	public ArenaSimpleElement(double width, double height, Background background) {
		super(width, height, background);
	}

	/**
	 * @param x
	 */
	public void setX(double x) {
		setLayoutX(x);
	}

	/**
	 * @return
	 */
	public double getX() {
		return getLayoutX();
	}

	/**
	 * @param y
	 */
	public void setY(double y) {
		setLayoutY(y);
	}

	/**
	 * @return
	 */
	public double getY() {
		return getLayoutY();
	}

	/**
	 * @param position
	 */
	public void setPosition(Position position) {
		setPosition(position.x, position.y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public void setPosition(double x, double y) {
		setX(x);
		setY(y);
	}

	/**
	 * @return
	 */
	public Position getPosition() {
		return new Position(getX(), getY());
	}

	/**
	 * @return
	 */
	public DoubleProperty xProperty() {
		return layoutXProperty();
	}

	/**
	 * @return
	 */
	public DoubleProperty yProperty() {
		return layoutYProperty();
	}
}