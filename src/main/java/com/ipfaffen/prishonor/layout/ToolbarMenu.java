package com.ipfaffen.prishonor.layout;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import com.ipfaffen.prishonor.R;
import com.ipfaffen.prishonor.effect.ColorEffect;
import com.ipfaffen.prishonor.type.Opacity;

/**
 * @author Isaias Pfaffenseller
 */
public abstract class ToolbarMenu {
	
	private DropShadow dropShadow;
	private ColorAdjust colorAdjust;

	/**
	 * @return
	 */
	public abstract Pane pane();

	/**
	 * @param image
	 * @return
	 */
	protected ImageView createImageView(Image image) {
		return createImageView(image, false);
	}

	/**
	 * @param image
	 * @param addHoverEffect
	 * @return
	 */
	protected ImageView createImageView(Image image, boolean addHoverEffect) {
		final ImageView iv = new ImageView(image);
		iv.setEffect(dropShadow());
		iv.setPreserveRatio(true);
		if(addHoverEffect) {
			iv.setOnMouseEntered(new EventHandler<Event>() {
				public void handle(Event event) {
					iv.setOpacity(Opacity.WELL_VISIBLE);
				};
			});
			iv.setOnMouseExited(new EventHandler<Event>() {
				public void handle(Event event) {
					iv.setOpacity(Opacity.FULLY_VISIBLE);
				};
			});
		}
		return iv;
	}

	/**
	 * @return
	 */
	protected Text createText() {
		Text text = new Text();
		text.setFont(Font.font(R.main.base_font, FontWeight.BOLD, 12));
		text.setFill(Color.web(R.color.base_text));
		text.setEffect(dropShadow());
		text.setTranslateX(-5);
		return text;
	}
	
	/**
	 * @return
	 */
	protected DropShadow dropShadow() {
		if(dropShadow == null) {
			dropShadow = new DropShadow(10, Color.web(R.color.base_text));
		}
		return null;
	}
	
	/**
	 * @return
	 */
	protected ColorAdjust colorAdjust() {
		if(colorAdjust == null) {
			colorAdjust = ColorEffect.grayTone();
			colorAdjust.setInput(dropShadow());
		}
		return colorAdjust;
	}
}