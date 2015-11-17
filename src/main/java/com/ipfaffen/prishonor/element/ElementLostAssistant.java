package com.ipfaffen.prishonor.element;

import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.Effect;
import javafx.scene.effect.SepiaTone;

import com.ipfaffen.prishonor.R;
import com.ipfaffen.prishonor.layout.SpriteImageBuilder;

/**
 * @author Isaias Pfaffenseller
 */
public class ElementLostAssistant extends Element {

	private RotateTransition shakeAnimation;
	private boolean bonus;

	public ElementLostAssistant() {
		super(SpriteImageBuilder.create(R.image.lost_assistant).cellSize(24, 24).cellOffset(1, 1).build());
	}

	@Override
	protected void configure() {
	}

	@Override
	public void act(EventHandler<ActionEvent> onActionFinished) {
		if(bonus) {
			showBonus();
		}
		else {
			showNormal();
		}
	}

	public void enableBonus() {
		bonus = true;
	}

	public void disableBonus() {
		bonus = false;
	}

	private void showNormal() {
		spriteAnimation().reset().add(createSpriteSequence().from(0).to(2)).duration(R.duration.lost_assistant_normal_sprite).autoReverse();
		spriteAnimation().playIndefinite();
		shakeAnimation().play();
		setSepiaTone(0);
	}

	private void showBonus() {
		spriteAnimation().reset().add(createSpriteSequence().from(3).to(5)).duration(R.duration.lost_assistant_bonus_sprite).autoReverse();
		spriteAnimation().playIndefinite();
		shakeAnimation().play();
		setSepiaTone(0);
	}

	public void disable() {
		spriteAnimation().stop();
		shakeAnimation().stop();
	}

	/**
	 * @param sepiaToneLevel
	 */
	@SuppressWarnings("unused")
	public void setSepiaTone(double sepiaToneLevel) {
		// For some reason in some computers setting the SepiaTone effect hide the pane.
		if(true) {
			return;
		}

		Effect effect = getEffect();
		if(effect == null) {
			setEffect(new SepiaTone(sepiaToneLevel));
		}
		else if(effect instanceof SepiaTone) {
			((SepiaTone) effect).setLevel(sepiaToneLevel);
		}
	}

	/**
	 * @return
	 */
	private RotateTransition shakeAnimation() {
		if(shakeAnimation == null) {
			shakeAnimation = new RotateTransition(R.duration.lost_assistant_shake, this);
			shakeAnimation.setFromAngle(-2);
			shakeAnimation.setToAngle(2);
			shakeAnimation.setCycleCount(Timeline.INDEFINITE);
			shakeAnimation.setAutoReverse(true);
		}
		return shakeAnimation;
	}

	/**
	 * @return
	 */
	public boolean isBonus() {
		return bonus;
	}
}