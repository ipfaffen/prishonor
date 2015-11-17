package com.ipfaffen.prishonor.layout;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;

import com.ipfaffen.prishonor.animation.ActionAnimation;
import com.ipfaffen.prishonor.animation.SpriteAnimation;
import com.ipfaffen.prishonor.animation.SpriteSequence;
import com.ipfaffen.prishonor.key.KeyDirection;

/**
 * @author Isaias Pfaffenseller
 */
public abstract class SpriteElement extends ArenaDynamicElement {

	protected SpriteImage spriteImage;
	private SpriteAnimation spriteAnimation;

	private ActionAnimation actionAnimation;
	protected EventHandler<ActionEvent> onActionFinished;

	protected KeyDirection direction;

	/**
	 * @param spriteImage
	 */
	public SpriteElement(SpriteImage spriteImage) {
		// Start with size of a sprite image cell.
		super(spriteImage.getCellWidth(), spriteImage.getCellHeight());

		this.spriteImage = spriteImage;

		installRenderListener();
		configure();

		add(spriteImage);
	}

	private void installRenderListener() {
		// Set pane size based on sprite image viewport size, also fix position if necessary.
		spriteImage.renderProperty().addListener(new ChangeListener<Rectangle2D>() {
			@Override
			public void changed(ObservableValue<? extends Rectangle2D> observable, Rectangle2D oldValue, Rectangle2D newValue) {
				setPrefSize(newValue.getWidth(), newValue.getHeight());

				/*
				 * If the rendered image (viewport) width is larger then one cell so fix x position, centralizing
				 * image on the cell. Otherwise just set layout x with the real x.
				 */
				if(newValue.getWidth() > getSpriteImage().getCellWidth()) {
					double x = getArenaPosition().getRealX() - ((newValue.getWidth() / 2) - (getSpriteImage().getCellWidth() / 2));
					getArenaPosition().setLayoutX(x);
				}
				else {
					getArenaPosition().setLayoutX(getArenaPosition().getRealX());
				}

				/*
				 * If the rendered image (viewport) height is larger then one cell so fix y position, centralizing
				 * image on the cell. Otherwise just set layout y with the real y.
				 */
				if(newValue.getHeight() > getSpriteImage().getCellHeight()) {
					double y = getArenaPosition().getRealY() - ((newValue.getHeight() / 2) - (getSpriteImage().getCellHeight() / 2));
					getArenaPosition().setLayoutY(y);
				}
				else {
					getArenaPosition().setLayoutY(getArenaPosition().getRealY());
				}
			}
		});
	}

	protected void configure() {
	}

	public final void act() {
		act(null);
	}

	/**
	 * @param onActionFinished
	 */
	public abstract void act(final EventHandler<ActionEvent> onActionFinished);

	public abstract void disable();

	/**
	 * @return
	 */
	public final SpriteAnimation spriteAnimation() {
		if(spriteAnimation == null) {
			spriteAnimation = new SpriteAnimation(spriteImage);
		}
		return spriteAnimation;
	}

	/**
	 * @return
	 */
	protected ActionAnimation actionAnimation() {
		if(actionAnimation == null) {
			actionAnimation = ActionAnimation.create();
		}
		return actionAnimation;
	}

	/**
	 * @return
	 */
	public final SpriteSequence createSpriteSequence() {
		return SpriteSequence.create(spriteImage);
	}

	/**
	 * @return
	 */
	public final SpriteImage getSpriteImage() {
		return spriteImage;
	}

	/**
	 * @return
	 */
	public final KeyDirection getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 */
	public final void setDirection(KeyDirection direction) {
		this.direction = direction;
	}
}