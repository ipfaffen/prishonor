package com.ipfaffen.prishonor;

import static com.ipfaffen.prishonor.Game.$;

import java.math.BigDecimal;

import javafx.animation.AnimationTimer;

import com.ipfaffen.prishonor.element.ElementLostAssistant;

/**
 * @author Isaias Pfaffenseller
 */
public class GameTarget {

	private ElementLostAssistant lostAssistant;

	private AnimationTimer turnTimer;
	private int turnCurrentProgress;

	/**
	 * @return
	 */
	public ElementLostAssistant element() {
		if(lostAssistant == null) {
			lostAssistant = new ElementLostAssistant();
		}

		return lostAssistant;
	}

	/**
	 * @return
	 */
	private AnimationTimer turnTimer() {
		if(turnTimer == null) {
			turnTimer = new AnimationTimer() {
				@Override
				public void handle(long l) {
					handleTurn();
				}
			};
		}

		return turnTimer;
	}

	public void startTurn() {
		turnCurrentProgress = -1;

		int consumedTargets = $.properties.consumedTargetsProperty().get();
		if(!lostAssistant.isBonus() && consumedTargets > 0 && (consumedTargets % 5) == 0) {
			activateBonus();
		}
		else {
			activateNormal();
		}

		changePosition();
		turnTimer().start();
	}

	public void stopTurn() {
		lostAssistant.disable();
		turnTimer().stop();

		setTurnProgress(0);
	}

	public void pauseTurn() {
		turnTimer().stop();
	}

	public void continueTurn() {
		turnTimer().start();
	}

	private void handleTurn() {
		int maxProgress = (lostAssistant.isBonus()) ? R.main.target_bonus_max_progress : R.main.target_max_progress;

		if(turnCurrentProgress >= maxProgress) {
			if(lostAssistant.isBonus()) {
				stopTurn();
				startTurn();
			}
			else {
				// Play fall animation.
				$.unit.hero().fall();
				$.gameOver();
			}

			return;
		}

		turnCurrentProgress += 1;

		double width = $.arena.getWidth();
		setTurnProgress(width - ((turnCurrentProgress * width) / maxProgress));

		// Rotten effect.
		lostAssistant.setSepiaTone((turnCurrentProgress * 1d) / maxProgress);
	}

	public void consumeTarget() {
		stopTurn();
		increasePoints();

		$.unit.hero().addAssistant();

		if($.positions.getUnused().isEmpty()) {
			// If there is no unused position left then game is over.
			element().setVisible(false);
		}
		else {
			// Otherwise start a new target turn.
			startTurn();
		}
	}

	public void increasePoints() {
		BigDecimal maxProgress = BigDecimal.valueOf((lostAssistant.isBonus()) ? R.main.target_bonus_max_progress : R.main.target_max_progress);
		BigDecimal maxPoints = BigDecimal.valueOf((lostAssistant.isBonus()) ? R.main.target_bonus_max_points : R.main.target_max_points);
		BigDecimal balancedProgress = maxProgress.subtract(BigDecimal.valueOf(turnCurrentProgress));

		int gainedPoints = balancedProgress.multiply(maxPoints).divide(maxProgress, BigDecimal.ROUND_FLOOR).intValue();
		$.properties.pointsProperty().set($.properties.pointsProperty().get() + gainedPoints);

		if(lostAssistant.isBonus()) {
			$.properties.consumedBonusTargetsProperty().set($.properties.consumedBonusTargetsProperty().get() + 1);
		}
		else {
			$.properties.consumedTargetsProperty().set($.properties.consumedTargetsProperty().get() + 1);
		}

		$.menu.receivePoints();
	}

	public void changePosition() {
		// Place target in a random unused position.
		lostAssistant.setPosition($.positions.getUnused().random());
	}

	public void activateNormal() {
		lostAssistant.disableBonus();
		lostAssistant.act();
		setTurnProgressColor(R.color.target_progress_bar);
	}

	public void activateBonus() {
		lostAssistant.enableBonus();
		lostAssistant.act();
		setTurnProgressColor(R.color.target_bonus_progress_bar);
	}

	/**
	 * @param progress
	 */
	private void setTurnProgress(double progress) {
		$.menu.progressBar().setPrefWidth(progress);
	}

	/**
	 * @param color
	 */
	private void setTurnProgressColor(String color) {
		$.menu.progressBar().setStyle(String.format("-fx-background-color:%s;", color));
	}
}