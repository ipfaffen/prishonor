package com.ipfaffen.prishonor.animation;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.WritableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * @author Isaias Pfaffenseller
 */
public class ActionAnimation {

	private Timeline timeline;
	private Duration duration;
	private List<ActionTarget> targets;
	private ActionTarget mainTarget;

	/**
	 * @return
	 */
	public static ActionAnimation create() {
		return new ActionAnimation();
	}

	public ActionAnimation() {
		timeline = new Timeline();
		targets = new ArrayList<ActionTarget>();
	}

	/**
	 * @return
	 */
	public ActionAnimation reset() {
		timeline.stop();
		timeline.jumpTo(Duration.ZERO);
		timeline.setAutoReverse(false);
		timeline.setCycleCount(1);
		timeline.setDelay(Duration.ZERO);
		timeline.setOnFinished(null);
		timeline.getKeyFrames().clear();

		targets.clear();
		mainTarget = null;
		duration = null;

		return this;
	}

	/**
	 * @param milis
	 * @return
	 */
	public ActionAnimation duration(double milis) {
		return duration(Duration.millis(milis));
	}

	/**
	 * @param duration
	 * @return
	 */
	public ActionAnimation duration(Duration duration) {
		this.duration = duration;
		return this;
	}

	/**
	 * @param delay
	 * @return
	 */
	public ActionAnimation delay(Duration delay) {
		timeline.setDelay(delay);
		return this;
	}

	/**
	 * @param cycles
	 * @return
	 */
	public ActionAnimation cycles(int cycles) {
		timeline.setCycleCount(cycles);
		return this;
	}

	/**
	 * @return
	 */
	public ActionAnimation autoReverse() {
		timeline.setAutoReverse(true);
		return this;
	}

	/**
	 * @param onFinishAction
	 */
	public ActionAnimation onFinished(final ActionAnimation onFinishAction) {
		return onFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onFinishAction.play();
			}
		});
	}

	/**
	 * @param onFinished
	 * @return
	 */
	public ActionAnimation onFinished(EventHandler<ActionEvent> onFinished) {
		timeline.setOnFinished(onFinished);
		return this;
	}

	/**
	 * Add a new target that can have a duration or not. If the target does not have a duration then it will receive the
	 * animation duration.
	 * 
	 * @param target
	 * @return
	 */
	public ActionAnimation add(ActionTarget target) {
		targets.add(target);

		/* If no duration was defined and the target has one then set it. */
		if(target.getDuration() != null && (duration == null || duration.compareTo(target.getDuration()) < 0)) {
			duration = target.getDuration();
		}

		return this;
	}

	/**
	 * Add key value to main target (the one that have the animation duration).
	 * 
	 * @param property
	 * @param targetValue
	 * @return
	 */
	public <T> ActionAnimation add(WritableValue<T> property, T targetValue) {
		getMainTarget().add(property, targetValue);
		return this;
	}

	/**
	 * Add key value to main target (the one that have the animation duration).
	 * 
	 * @param property
	 * @param targetValue
	 * @param interpolator
	 * @return
	 */
	public <T> ActionAnimation add(WritableValue<T> property, T targetValue, Interpolator interpolator) {
		getMainTarget().add(property, targetValue, interpolator);
		return this;
	}

	/**
	 * Play infinitely.
	 */
	public void playIndefinite() {
		play(Timeline.INDEFINITE);
	}

	/**
	 * Play just one time.
	 */
	public void playOne() {
		play(1);
	}

	/**
	 * @param cycleCount
	 */
	public void play(int cycleCount) {
		timeline.setCycleCount(cycleCount);
		play();
	}

	public void play() {
		if(!hasTargets()) {
			return;
		}

		// Add targets to timeline.
		for(ActionTarget target: targets) {
			if(!target.hasKeyValues()) {
				continue;
			}
			if(target.getDuration() == null) {
				// If no duration was defined for target then set it with the duration of animation.
				target.setDuration(duration);
			}
			timeline.getKeyFrames().add(new KeyFrame(target.getDuration(), ("keyFrame" + hashCode()), target.getOnFinished(), target.getKeyValues()));
		}
		timeline.play();
	}

	public void stop() {
		timeline.stop();
	}

	/**
	 * @return
	 */
	public boolean hasTargets() {
		return !targets.isEmpty();
	}

	/**
	 * @return
	 */
	private ActionTarget getMainTarget() {
		if(mainTarget == null) {
			mainTarget = ActionTarget.create();
			targets.add(mainTarget);
		}
		return mainTarget;
	}

	/**
	 * @return
	 */
	protected Duration getDuration() {
		return duration;
	}

	/**
	 * @return
	 */
	protected EventHandler<ActionEvent> getOnFinished() {
		return timeline.getOnFinished();
	}
}