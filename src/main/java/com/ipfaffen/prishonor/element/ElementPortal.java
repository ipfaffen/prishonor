package com.ipfaffen.prishonor.element;

import static com.ipfaffen.prishonor.Game.$;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import com.ipfaffen.prishonor.R;
import com.ipfaffen.prishonor.animation.ActionAnimation;
import com.ipfaffen.prishonor.animation.SpriteAnimation;
import com.ipfaffen.prishonor.layout.SpriteImageBuilder;
import com.ipfaffen.prishonor.util.Position;

/**
 * @author Isaias Pfaffenseller
 */
public class ElementPortal extends Element {

	private EventHandler<ActionEvent> onMoveFinished;

	private ActionAnimation brightnessAnimation;
	private DropShadow dropShadow;

	private ElementPortal destiny;

	public ElementPortal() {
		super(SpriteImageBuilder.create(R.image.portal_sprite).cellSize(24, 24).cellOffset(1, 1).build());
	}

	@Override
	protected void configure() {
		// Invisble at first.
		setVisible(false);

		dropShadow = new DropShadow();
		dropShadow.setSpread(1);
		dropShadow.setBlurType(BlurType.GAUSSIAN);

		// Brightness effect.
		spriteImage.setEffect(dropShadow);
	}

	@Override
	public void act(final EventHandler<ActionEvent> onActionFinished) {
		this.onActionFinished = onActionFinished;

		decreaseSize();

		SpriteAnimation animation = spriteAnimation().reset().duration(R.duration.portal_sprite);
		animation.add(createSpriteSequence().from(0, 0).to(0, 5));
		animation.playIndefinite();

		dropShadow.setRadius(0);
		dropShadow.setColor(Color.rgb(255, 255, 255, 0.8));

		brightnessAnimation().playIndefinite();

		move();
	}

	@Override
	public void disable() {
		setVisible(false);

		brightnessAnimation().stop();
		spriteAnimation().stop();

		// Not necessary and make no difference at the moment, but it could make sense in the future.
		actionAnimation().stop();
	}

	private void move() {
		Position targetPosition = $.arena.fixGetPosition(this, direction);

		if($.arena.isWithinLimits(targetPosition) && $.arena.hasCollision(targetPosition)) {
			open(getPosition());
			return;
		}

		if(onMoveFinished == null) {
			onMoveFinished = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if(!isVisible()) {
						// Only make visible after first move.
						setVisible(true);
					}
					move();
				}
			};
		}

		ActionAnimation animation = actionAnimation().reset().duration(R.duration.portal_move).onFinished(onMoveFinished);
		animation.add(xProperty(), targetPosition.x);
		animation.add(yProperty(), targetPosition.y);
		animation.playOne();
	}

	private void decreaseSize() {
		spriteImage.setPreserveRatio(true);
		spriteImage.setFitHeight(12);
		spriteImage.yProperty().set(spriteImage.yProperty().get() + 6);
		spriteImage.xProperty().set(spriteImage.xProperty().get() + 6);
	}

	private void increaseSize() {
		spriteImage.setFitHeight(24);
		spriteImage.yProperty().set(spriteImage.yProperty().get() - 6);
		spriteImage.xProperty().set(spriteImage.xProperty().get() - 6);
	}

	/**
	 * @param targetPosition
	 */
	private void open(Position targetPosition) {
		increaseSize();
		onActionFinished.handle(null);

		// Reverse direction.
		setDirection(getDirection().reversed());

		// Hold position.
		$.positions.addWarpUsed(targetPosition);
	}

	/**
	 * @return
	 */
	private ActionAnimation brightnessAnimation() {
		if(brightnessAnimation == null) {
			brightnessAnimation = ActionAnimation.create().duration(R.duration.portal_bright).autoReverse();
			brightnessAnimation.add(dropShadow.radiusProperty(), 2);
			brightnessAnimation.add(dropShadow.colorProperty(), Color.rgb(255, 255, 255, 0.4));
		}
		return brightnessAnimation;
	}

	/**
	 * @return
	 */
	public ElementPortal getDestiny() {
		return destiny;
	}

	/**
	 * @param destiny
	 */
	public void setDestiny(ElementPortal destiny) {
		this.destiny = destiny;
	}
}