package com.ipfaffen.prishonor.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * @author Isaias Pfaffenseller
 */
public class ArenaPosition {

	private DoubleProperty realXProperty;
	private DoubleProperty realYProperty;
	private DoubleProperty layoutXProperty;
	private DoubleProperty layoutYProperty;

	/**
	 * @param x
	 * @param y
	 */
	public ArenaPosition(double x, double y) {
		realXProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				double diff = ((Double) newValue) - ((Double) oldValue);
				setLayoutX(getLayoutX() + diff);
			}
		});
		realYProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				double diff = ((Double) newValue) - ((Double) oldValue);
				setLayoutY(getLayoutY() + diff);
			}
		});

		setRealX(x);
		setRealY(y);
	}

	/**
	 * @return
	 */
	public Position get() {
		return new Position(getRealX(), getRealY());
	}

	/**
	 * @param position
	 */
	public void set(Position position) {
		setRealX(position.x);
		setRealY(position.y);
	}

	/**
	 * @return
	 */
	public DoubleProperty layoutXProperty() {
		if(layoutXProperty == null) {
			layoutXProperty = new SimpleDoubleProperty();
		}
		return layoutXProperty;
	}

	/**
	 * @return
	 */
	public DoubleProperty layoutYProperty() {
		if(layoutYProperty == null) {
			layoutYProperty = new SimpleDoubleProperty();
		}
		return layoutYProperty;
	}

	/**
	 * @return
	 */
	public double getLayoutX() {
		return layoutXProperty().get();
	}

	/**
	 * @param x
	 */
	public void setLayoutX(double x) {
		layoutXProperty().set(x);
	}

	/**
	 * @return
	 */
	public double getLayoutY() {
		return layoutYProperty().get();
	}

	/**
	 * @param y
	 */
	public void setLayoutY(double y) {
		layoutYProperty().set(y);
	}

	/**
	 * @return
	 */
	public DoubleProperty realXProperty() {
		if(realXProperty == null) {
			realXProperty = new SimpleDoubleProperty();
		}
		return realXProperty;
	}

	/**
	 * @return
	 */
	public DoubleProperty realYProperty() {
		if(realYProperty == null) {
			realYProperty = new SimpleDoubleProperty();
		}
		return realYProperty;
	}

	/**
	 * @return
	 */
	public double getRealX() {
		return realXProperty().get();
	}

	/**
	 * @param x
	 */
	public void setRealX(double x) {
		realXProperty().set(x);
	}

	/**
	 * @return
	 */
	public double getRealY() {
		return realYProperty().get();
	}

	/**
	 * @param y
	 */
	public void setRealY(double y) {
		realYProperty().set(y);
	}
}