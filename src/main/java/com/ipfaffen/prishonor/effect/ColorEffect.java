package com.ipfaffen.prishonor.effect;

import javafx.scene.effect.ColorAdjust;

/**
 * @author Isaias Pfaffenseller
 */
public class ColorEffect {

	/**
	 * @return
	 */
	public static ColorAdjust grayTone() {
		ColorAdjust grayTone = new ColorAdjust();
		grayTone.setBrightness(-0.1);
		grayTone.setSaturation(-0.9);
		return grayTone;
	}
}