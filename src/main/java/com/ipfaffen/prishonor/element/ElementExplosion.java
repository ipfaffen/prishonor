package com.ipfaffen.prishonor.element;

import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import com.ipfaffen.prishonor.R;
import com.ipfaffen.prishonor.animation.ActionAnimation;
import com.ipfaffen.prishonor.animation.SpriteAnimation;
import com.ipfaffen.prishonor.layout.SpriteImageBuilder;

/**
 * @author Isaias Pfaffenseller
 */
public class ElementExplosion extends Element {

	private ActionAnimation brightnessAnimation;
	private DropShadow dropShadow;

	public ElementExplosion() {
		super(SpriteImageBuilder.create(R.image.explosion_sprite).cellSize(24, 24).cellOffset(1, 1).build());
	}

	@Override
	protected void configure() {
		dropShadow = new DropShadow();
		dropShadow.setSpread(1);
		dropShadow.setBlurType(BlurType.GAUSSIAN);

		// Brightness effect.
		spriteImage.setEffect(dropShadow);
	}

	@Override
	public void act(final EventHandler<ActionEvent> onActionFinished) {
		setVisible(true);
		toFront();

		dropShadow.setRadius(5);
		dropShadow.setColor(Color.rgb(255, 255, 220, 0.4));

		brightnessAnimation().playOne();

		// Once the explosion animation is finished disable it and call action finished handler.
		final EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				disable();
				onActionFinished.handle(event);
			}
		};

		SpriteAnimation animation = spriteAnimation().reset().duration(R.duration.explosion).onFinished(onFinished);
		animation.add(createSpriteSequence().from(0, 0).to(0, 9).span(2, 2));
		animation.playOne();
	}

	@Override
	public void disable() {
		setVisible(false);

		brightnessAnimation().stop();
		spriteAnimation().stop();
	}

	/**
	 * @return
	 */
	private ActionAnimation brightnessAnimation() {
		if(brightnessAnimation == null) {
			brightnessAnimation = ActionAnimation.create().duration(R.duration.explosion);
			brightnessAnimation.add(dropShadow.radiusProperty(), 40, Interpolator.EASE_OUT);
			brightnessAnimation.add(dropShadow.colorProperty(), Color.rgb(255, 255, 250, 0));
		}
		return brightnessAnimation;
	}
}