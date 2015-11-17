package com.ipfaffen.prishonor.unit;

import static com.ipfaffen.prishonor.Game.$;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import com.ipfaffen.prishonor.R;
import com.ipfaffen.prishonor.animation.ActionAnimation;
import com.ipfaffen.prishonor.element.ElementMagicProjectile;
import com.ipfaffen.prishonor.element.ElementPortal;
import com.ipfaffen.prishonor.exception.MoveCollision;
import com.ipfaffen.prishonor.key.KeyDirection;
import com.ipfaffen.prishonor.layout.SpriteImageBuilder;
import com.ipfaffen.prishonor.skill.Skill;
import com.ipfaffen.prishonor.skill.SkillType;
import com.ipfaffen.prishonor.util.DataList;
import com.ipfaffen.prishonor.util.Position;
import com.ipfaffen.prishonor.util.ReversedIterator;

/**
 * @author Isaias Pfaffenseller
 */
public class UnitHero extends Unit {

	private DataList<UnitAssistant> assistants;

	private EventHandler<ActionEvent> onMoveFinished;
	private EventHandler<ActionEvent> onCastSpellFinished;
	private EventHandler<ActionEvent> onCastMagicProjectileFinished;
	private EventHandler<ActionEvent> onCastPortalFinished;

	private ElementMagicProjectile magicProjectile;
	private DataList<ElementPortal> portals;

	public UnitHero() {
		super(SpriteImageBuilder.create(R.image.hero_sprite).cellSize(24, 24).cellOffset(1, 1).build());
	}

	@Override
	protected void configure() {
		assistants = new DataList<UnitAssistant>();
	}

	public void addAssistant() {
		int index = 0;
		Unit parentUnit = this;

		if(!assistants.isEmpty()) {
			index = assistants.size();
			parentUnit = assistants.last();
		}

		assistants.add(new UnitAssistant(index, parentUnit));
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

		spriteAnimation().duration(R.duration.hero_await).playIndefinite();
	}

	public void run() {
		spriteAnimation().reset();

		if(direction.isDown()) {
			spriteAnimation().add(createSpriteSequence().from(0, 3).to(0, 12));
		}
		else if(direction.isRight()) {
			spriteAnimation().add(createSpriteSequence().from(1, 3).to(1, 12));
		}
		else if(direction.isLeft()) {
			spriteAnimation().add(createSpriteSequence().from(1, 3).to(1, 12)).flipHorizontal();
		}
		else if(direction.isUp()) {
			spriteAnimation().add(createSpriteSequence().from(2, 3).to(2, 12));
		}

		spriteAnimation().duration(R.duration.hero_run).playIndefinite();
	}

	public void fall() {
		spriteAnimation().reset();

		if(direction.isDown()) {
			spriteAnimation().add(createSpriteSequence().from(0, 13).to(0, 15));
		}
		else if(direction.isRight()) {
			spriteAnimation().add(createSpriteSequence().from(1, 13).to(1, 15));
		}
		else if(direction.isLeft()) {
			spriteAnimation().add(createSpriteSequence().from(1, 13).to(1, 15)).flipHorizontal();
		}
		else if(direction.isUp()) {
			spriteAnimation().add(createSpriteSequence().from(2, 13).to(2, 15));
		}

		spriteAnimation().duration(R.duration.hero_fall).playOne();

		// Play celebrate animation for assistants.
		for(UnitAssistant assistant: ReversedIterator.get(assistants)) {
			assistant.celebrate();
		}
	}

	@Override
	public void act(final EventHandler<ActionEvent> onActionFinished) {
		actionAnimation().reset();

		if($.keyboard.hasAttack()) {
			$.keyboard.nextAttack();

			Skill selectedSkill = $.skill.selected();
			if(selectedSkill.hasUsageLeft()) {
				if(SkillType.MAGIC_PROJECTILE.is(selectedSkill)) {
					castMagicProjectile();
				}
				else if(SkillType.PORTAL.is(selectedSkill)) {
					castPortal();
				}
			}
			else {
				// Unlock back the keyboard.
				$.keyboard.unlock();
				act();
			}
		}
		else {
			KeyDirection direction = getDirection();

			if($.keyboard.hasDirection()) {
				// It is only allowed a direction change when hero is within arena's limits.
				if($.arena.isWithinLimits(getPosition())) {
					direction = $.keyboard.nextDirection(true, true, direction);
				}
			}

			try {
				// Move hero.
				move(direction);
			}
			catch(MoveCollision mc) {
				// Play fall animation.
				fall();

				$.gameOver();
			}
		}
	}

	@Override
	public void disable() {
		magicProjectile().disable();

		for(ElementPortal portal: portals()) {
			portal.disable();
		}

		actionAnimation().stop();
	}

	/**
	 * Move hero based on direction.
	 * 
	 * @param direction
	 * @throws MoveCollision
	 */
	public void move(KeyDirection direction) throws MoveCollision {
		$.positions.clearHeroUsed();

		// Add assistants move properties to action target.
		moveAssistants();

		Position targetPosition;

		if($.arena.hasWarp(getPosition())) {
			targetPosition = enterPortal(getPosition(), this);
		}
		else {
			targetPosition = $.arena.fixGetPosition(this, direction);
		}

		boolean directionChange = (getDirection() != direction);
		if(directionChange) {
			setDirection(direction);
		}

		if($.arena.isWithinLimits(targetPosition)) {
			if($.arena.hasCollision(targetPosition) || $.positions.getUnused().isEmpty()) {
				// If has collision ahead or there is no more unused positions left.
				throw new MoveCollision(targetPosition);
			}

			$.positions.addHeroUsed(targetPosition);

			if($.arena.hasTarget(targetPosition)) {
				$.target.consumeTarget();
			}
		}

		ActionAnimation animation = actionAnimation();
		animation.add(xProperty(), targetPosition.x);
		animation.add(yProperty(), targetPosition.y);

		// If direction changed then start run sprite animation.
		if(directionChange) {
			run();
		}

		if(onMoveFinished == null) {
			onMoveFinished = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					act();
				}
			};
		}

		animation.duration(R.duration.action_move).onFinished(onMoveFinished).playOne();
	}

	/**
	 * @param actionTarget
	 */
	private void moveAssistants() {
		for(UnitAssistant assistant: ReversedIterator.get(assistants)) {
			Unit parent = assistant.getParentUnit();

			if(!$.arena.contains(assistant)) {
				// New member.
				assistant.setPosition(parent.getPosition());

				$.arena.add(assistant);
				$.positions.addHeroUsed(parent.getPosition());
			}
			else {
				Position targetPosition;

				if($.arena.hasWarp(assistant.getPosition())) {
					targetPosition = enterPortal(assistant.getPosition(), assistant);
				}
				else {
					targetPosition = $.arena.fixGetPosition(assistant, parent);
				}

				if($.arena.isWithinLimits(targetPosition)) {
					$.positions.addHeroUsed(targetPosition);
				}

				ActionAnimation animation = actionAnimation();
				animation.add(assistant.xProperty(), targetPosition.x);
				animation.add(assistant.yProperty(), targetPosition.y);
			}

			// If parent member direction is different then change sprite animation and set current direction.
			if(assistant.getDirection() != parent.getDirection()) {
				assistant.setDirection(parent.getDirection());
				assistant.run();
			}
		}
	}

	public void castSpell(EventHandler<ActionEvent> onFinished) {
		spriteAnimation().reset();

		if(direction.isDown()) {
			spriteAnimation().add(createSpriteSequence().from(4, 0).to(4, 6).rowSpan(2));
		}
		else if(direction.isRight()) {
			spriteAnimation().add(createSpriteSequence().from(6, 0).to(6, 10).span(2, 2));
		}
		else if(direction.isLeft()) {
			spriteAnimation().add(createSpriteSequence().from(6, 0).to(6, 10).span(2, 2)).flipHorizontal();
		}
		else if(direction.isUp()) {
			spriteAnimation().add(createSpriteSequence().from(4, 7).to(4, 13).rowSpan(2));
		}

		for(UnitAssistant assistant: ReversedIterator.get(assistants)) {
			assistant.await();
		}

		if(onCastSpellFinished == null) {
			onCastSpellFinished = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					run();
					for(UnitAssistant assistant: ReversedIterator.get(assistants)) {
						assistant.run();
					}

					act();
				}
			};
		}

		spriteAnimation().duration(R.duration.hero_cast_spell).onFinished(onFinished).playOne();
	}

	public void castMagicProjectile() {
		if(onCastMagicProjectileFinished == null) {
			onCastMagicProjectileFinished = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					await();

					Position startPosition = getPosition();

					ElementMagicProjectile magicProjectile = magicProjectile();
					magicProjectile.setDirection(direction);
					magicProjectile.setPosition(startPosition);

					$.arena.addIfNotIn(magicProjectile);

					magicProjectile.act(onCastSpellFinished);

					// Consume one usage of magic projectile.
					$.skill.consumeUsage(SkillType.MAGIC_PROJECTILE.code());

					// Unlock back the keyboard.
					$.keyboard.unlock();
				}
			};
		}

		castSpell(onCastMagicProjectileFinished);
	}

	public void castPortal() {
		if(onCastPortalFinished == null) {
			onCastPortalFinished = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					await();

					Position startPosition = getPosition();

					ElementPortal portal = new ElementPortal();
					portal.setDirection(direction);
					portal.setPosition(startPosition);

					$.arena.addIfNotIn(portal);

					portal.act(onCastSpellFinished);

					// Consume one usage of portal.
					$.skill.consumeUsage(SkillType.PORTAL.code());

					// Unlock back the keyboard.
					$.keyboard.unlock();

					ElementPortal openedPortal = portals().first();
					if(openedPortal != null) {
						// If already has an opened portal then set destiny of both.
						portal.setDestiny(openedPortal);
						openedPortal.setDestiny(portal);
					}

					portals().add(portal);
				}
			};
		}

		castSpell(onCastPortalFinished);
	}

	/**
	 * @param enterPosition
	 * @param unit
	 * @return
	 */
	private Position enterPortal(Position enterPosition, Unit unit) {
		Position outPosition = null;
		for(ElementPortal element: portals()) {
			if(element.getPosition().equals(enterPosition) && element.getDestiny() != null) {
				outPosition = element.getDestiny().getPosition();
				element.toFront();
				element.getDestiny().toFront();
				break;
			}
		}

		if(outPosition == null) {
			throw new MoveCollision(enterPosition);
		}

		unit.setPosition(outPosition);

		return $.arena.fixGetPosition(unit, unit.getDirection());
	}

	/**
	 * @return
	 */
	private ElementMagicProjectile magicProjectile() {
		if(magicProjectile == null) {
			magicProjectile = new ElementMagicProjectile();
		}
		return magicProjectile;
	}

	/**
	 * @return
	 */
	private DataList<ElementPortal> portals() {
		if(portals == null) {
			portals = new DataList<ElementPortal>();
		}
		return portals;
	}

	/**
	 * @return
	 */
	public List<UnitAssistant> getAssistants() {
		return assistants;
	}
}