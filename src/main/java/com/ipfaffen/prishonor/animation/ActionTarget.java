package com.ipfaffen.prishonor.animation;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.KeyValue;
import javafx.beans.value.WritableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * @author Isaias Pfaffenseller
 */
public class ActionTarget {

	private Duration duration;
	private EventHandler<ActionEvent> onFinished;
	private List<KeyValue> keyValues;

	/**
	 * @return
	 */
	public static ActionTarget create() {
		return new ActionTarget();
	}

	public ActionTarget() {
		keyValues = new ArrayList<KeyValue>();
	}

	/**
	 * @param milis
	 * @return
	 */
	public ActionTarget duration(double milis) {
		return duration(Duration.millis(milis));
	}

	/**
	 * @param duration
	 * @return
	 */
	public ActionTarget duration(Duration duration) {
		this.duration = duration;
		return this;
	}

	/**
	 * @param onFinished
	 * @return
	 */
	public ActionTarget onFinished(EventHandler<ActionEvent> onFinished) {
		this.onFinished = onFinished;
		return this;
	}

	/**
	 * @param property
	 * @param targetValue
	 * @return
	 */
	public <T> ActionTarget add(WritableValue<T> property, T targetValue) {
		add(new KeyValue(property, targetValue));
		return this;
	}

	/**
	 * @param property
	 * @param targetValue
	 * @param interpolator
	 * @return
	 */
	public <T> ActionTarget add(WritableValue<T> property, T targetValue, Interpolator interpolator) {
		add(new KeyValue(property, targetValue, interpolator));
		return this;
	}

	/**
	 * @param keyValue
	 * @return
	 */
	public ActionTarget add(KeyValue keyValue) {
		keyValues.add(keyValue);
		return this;
	}

	/**
	 * @return
	 */
	public ActionTarget clear() {
		keyValues.clear();
		return this;
	}

	/**
	 * @return
	 */
	public boolean hasKeyValues() {
		return !keyValues.isEmpty();
	}

	/**
	 * @param duration
	 */
	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	/**
	 * @return
	 */
	public Duration getDuration() {
		return duration;
	}

	/**
	 * @return
	 */
	public EventHandler<ActionEvent> getOnFinished() {
		return onFinished;
	}

	/**
	 * @return
	 */
	public List<KeyValue> getKeyValues() {
		return keyValues;
	}
}