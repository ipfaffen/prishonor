package com.ipfaffen.prishonor.element;

import static com.ipfaffen.prishonor.Game.$;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import com.ipfaffen.prishonor.R;
import com.ipfaffen.prishonor.animation.ActionAnimation;
import com.ipfaffen.prishonor.layout.SpriteImageBuilder;
import com.ipfaffen.prishonor.util.Position;

/**
 * @author Isaias Pfaffenseller
 */
public class ElementMagicProjectile extends Element {

	private EventHandler<ActionEvent> onMoveFinished;

	private ActionAnimation brightnessAnimation;
	private DropShadow dropShadow;

	public ElementMagicProjectile() {
		super(SpriteImageBuilder.create(R.image.magic_projectile_sprite).cellSize(24, 24).cellOffset(1, 1).build());
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

		spriteAnimation().reset();

		if(direction.isDown()) {
			spriteAnimation().add(createSpriteSequence().from(0, 0).to(0, 2).rowSpan(2));
		}
		else if(direction.isRight()) {
			spriteAnimation().add(createSpriteSequence().from(2, 0).to(2, 4).colSpan(2));
		}
		else if(direction.isLeft()) {
			spriteAnimation().add(createSpriteSequence().from(2, 0).to(2, 4).colSpan(2)).flipHorizontal();
		}
		else if(direction.isUp()) {
			spriteAnimation().add(createSpriteSequence().from(3, 0).to(3, 2).rowSpan(2));
		}

		spriteAnimation().duration(R.duration.magic_projectile_sprite).playIndefinite();

		dropShadow.setRadius(2);
		dropShadow.setColor(Color.rgb(255, 255, 250, 0.2));

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
			hit(targetPosition);
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

		ActionAnimation animation = actionAnimation().reset().duration(R.duration.magic_projectile_move).onFinished(onMoveFinished);
		animation.add(xProperty(), targetPosition.x);
		animation.add(yProperty(), targetPosition.y);
		animation.playOne();
	}

	/**
	 * @param targetPosition
	 */
	private void hit(Position targetPosition) {
		disable();
		$.arena.destroyObject(targetPosition, onActionFinished);
	}

	/**
	 * @return
	 */
	private ActionAnimation brightnessAnimation() {
		if(brightnessAnimation == null) {
			brightnessAnimation = ActionAnimation.create().duration(R.duration.magic_projectile_bright).autoReverse();
			brightnessAnimation.add(dropShadow.radiusProperty(), 8);
			brightnessAnimation.add(dropShadow.colorProperty(), Color.rgb(255, 255, 220, 0.4));
		}
		return brightnessAnimation;
	}
}