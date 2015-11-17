package com.ipfaffen.prishonor;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * @author Isaias Pfaffenseller
 */
public class GameProperties {

	private IntegerProperty consumedTargetsProperty;
	private IntegerProperty consumedBonusTargetsProperty;
	private IntegerProperty pointsProperty;
	
	/**
	 * @return
	 */
	public IntegerProperty consumedTargetsProperty() {
		if(consumedTargetsProperty == null) {
			consumedTargetsProperty = new SimpleIntegerProperty(0);
		}
		return consumedTargetsProperty;
	}

	/**
	 * @return
	 */
	public IntegerProperty consumedBonusTargetsProperty() {
		if(consumedBonusTargetsProperty == null) {
			consumedBonusTargetsProperty = new SimpleIntegerProperty(0);
		}
		return consumedBonusTargetsProperty;
	}

	/**
	 * @return
	 */
	public IntegerProperty pointsProperty() {
		if(pointsProperty == null) {
			pointsProperty = new SimpleIntegerProperty(0);
		}
		return pointsProperty;
	}
}