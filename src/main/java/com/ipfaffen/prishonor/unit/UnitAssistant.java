package com.ipfaffen.prishonor.unit;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import com.ipfaffen.prishonor.R;
import com.ipfaffen.prishonor.layout.SpriteImageBuilder;

/**
 * @author Isaias Pfaffenseller
 */
public class UnitAssistant extends Unit {

	private int index;
	private Unit parentUnit;

	/**
	 * @param index
	 * @param parentUnit
	 */
	public UnitAssistant(int index, Unit parentUnit) {
		super(SpriteImageBuilder.create(R.image.assistant_sprite).cellSize(24, 24).cellOffset(1, 1).build());

		this.index = index;
		this.parentUnit = parentUnit;
	}

	public void await() {
		spriteAnimation().reset();

		if(direction == null || direction.isDown()) {
			spriteAnimation().add(createSpriteSequence().from(0, 0).to(0, 1));
		}
		else if(direction.isRight()) {
			spriteAnimation().add(createSpriteSequence().from(1, 0).to(1, 1));
		}
		else if(direction.isLeft()) {
			spriteAnimation().add(createSpriteSequence().from(1, 0).to(1, 1)).flipHorizontal();
		}
		else if(direction.isUp()) {
			spriteAnimation().add(createSpriteSequence().from(2, 0).to(2, 1));
		}

		spriteAnimation().duration(R.duration.assistant_await).playIndefinite();
	}

	public void run() {
		spriteAnimation().reset();

		if(direction == null || direction.isDown()) {
			spriteAnimation().add(createSpriteSequence().from(0, 3).to(0, 13));
		}
		else if(direction.isRight()) {
			spriteAnimation().add(createSpriteSequence().from(1, 3).to(1, 13));
		}
		else if(direction.isLeft()) {
			spriteAnimation().add(createSpriteSequence().from(1, 3).to(1, 13)).flipHorizontal();
		}
		else if(direction.isUp()) {
			spriteAnimation().add(createSpriteSequence().from(2, 3).to(2, 13));
		}

		spriteAnimation().duration(R.duration.assistant_run).playIndefinite();
	}

	public void celebrate() {
		spriteAnimation().stop();
		spriteImage.render(3, 0);
	}

	@Override
	public void act(EventHandler<ActionEvent> onActionFinished) {
	}

	@Override
	public void disable() {
	}

	/**
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return
	 */
	public Unit getParentUnit() {
		return parentUnit;
	}
}