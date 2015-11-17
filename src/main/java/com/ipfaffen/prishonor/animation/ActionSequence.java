package com.ipfaffen.prishonor.animation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import com.ipfaffen.prishonor.util.DataList;

/**
 * @author Isaias Pfaffenseller
 */
public class ActionSequence {
	
	/**
	 * Sum of durations of all actions.
	 */
	private Duration duration;

	/**
	 * Initial delay.
	 */
	private Duration initialDelay;

	/**
	 * Delay between actions.
	 */
	private Duration betweenDelay;

	/**
	 * Called after finish all actions.
	 */
	private EventHandler<ActionEvent> onFinished;

	/**
	 * This actions will be played in sequence.
	 */
	private DataList<ActionAnimation> actions;

	/**
	 * @return
	 */
	public static ActionSequence create() {
		return new ActionSequence();
	}

	public ActionSequence() {
		duration = Duration.ZERO;
		initialDelay = Duration.ZERO;
		betweenDelay = Duration.ZERO;
		actions = new DataList<ActionAnimation>();
	}

	/**
	 * @param delay
	 * @return
	 */
	public ActionSequence delay(Duration delay) {
		initialDelay = delay;
		betweenDelay = delay;
		return this;
	}

	/**
	 * @param initialDelay
	 * @return
	 */
	public ActionSequence initialDelay(Duration initialDelay) {
		this.initialDelay = initialDelay;
		return this;
	}

	/**
	 * @param betweenDelay
	 * @return
	 */
	public ActionSequence betweenDelay(Duration betweenDelay) {
		this.betweenDelay = betweenDelay;
		return this;
	}

	/**
	 * @param action
	 * @return
	 */
	public ActionSequence add(ActionAnimation action) {
		actions.add(action);
		duration = duration.add(action.getDuration());
		return this;
	}

	/**
	 * @param actions
	 * @return
	 */
	public ActionSequence add(ActionAnimation... actions) {
		for(ActionAnimation action: actions) {
			add(action);
		}
		return this;
	}

	/**
	 * @param onFinishAction
	 */
	public ActionSequence onFinished(final ActionAnimation onFinishAction) {
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
	public ActionSequence onFinished(EventHandler<ActionEvent> onFinished) {
		this.onFinished = onFinished;
		return this;
	}

	/**
	 * @param delay
	 */
	public void play(Duration delay) {
		for(int i = 0; i < actions.size(); i++) {
			ActionAnimation previousAction = (i > 0) ? actions.get(i - 1) : null;
			ActionAnimation action = actions.get(i);

			/*
			 * As this is a sequence animation then add the duration of previous turn as delay of current turn. Also add
			 * the between delay.
			 */
			if(previousAction != null) {
				delay = delay.add(previousAction.getDuration()).add(betweenDelay);
			}

			/*
			 * If it is the last turn then append to it the sequence finish handler
			 */
			if(action.equals(actions.last()) && onFinished != null) {
				appendOnFinishedHandlerToAction(action);
			}

			action.delay(delay).play();
		}
	}

	public void play() {
		play(initialDelay);
	}

	/**
	 * @param action
	 */
	public void appendOnFinishedHandlerToAction(ActionAnimation action) {
		final EventHandler<ActionEvent> onActionFinished = action.getOnFinished();

		/*
		 * If action has a "on finished handler" then append the sequence "on finished handler" to it. Otherwise just
		 * set it with the sequence "on finished handler".
		 */
		if(onActionFinished != null) {
			action.onFinished(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					onActionFinished.handle(event);
					onFinished.handle(event);
				}
			});
		}
		else {
			action.onFinished(onFinished);
		}
	}

	/**
	 * @return
	 */
	public Duration getDuration() {
		return duration;
	}
}