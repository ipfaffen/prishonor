package com.ipfaffen.prishonor.animation;

import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import com.ipfaffen.prishonor.layout.SpriteImage;
import com.ipfaffen.prishonor.layout.SpriteViewport;
import com.ipfaffen.prishonor.util.DataList;

/**
 * @author Isaias Pfaffenseller
 */
public class SpriteAnimation extends Transition {

	private final SpriteImage spriteImage;

	private DataList<SpriteViewport> viewportSequence;
	private int lastSequenceIndex = -1;

	/**
	 * @param spriteImage
	 */
	public SpriteAnimation(SpriteImage spriteImage) {
		this.spriteImage = spriteImage;
		this.viewportSequence = new DataList<SpriteViewport>();

		setInterpolator(Interpolator.LINEAR);
	}

	/**
	 * @param indexFrom
	 * @param indexTo
	 * @param duration
	 * @return
	 */
	public SpriteAnimation reset() {
		stop();
		setAutoReverse(false);
		setOnFinished(null);

		spriteImage.reset();
		viewportSequence.clear();
		lastSequenceIndex = -1;

		return this;
	}

	/**
	 * @param spriteSequence
	 * @return
	 */
	public SpriteAnimation add(SpriteSequence spriteSequence) {
		return add(spriteSequence.buildViewports());
	}

	/**
	 * @param viewports
	 * @return
	 */
	public SpriteAnimation add(List<SpriteViewport> viewports) {
		viewportSequence.addAll(viewports);
		return this;
	}

	/**
	 * @param viewport
	 * @return
	 */
	public SpriteAnimation add(SpriteViewport viewport) {
		viewportSequence.add(viewport);
		return this;
	}

	/**
	 * @param milis
	 * @return
	 */
	public SpriteAnimation duration(double milis) {
		return duration(Duration.millis(milis));
	}

	/**
	 * @param duration
	 * @return
	 */
	public SpriteAnimation duration(Duration duration) {
		setCycleDuration(duration);
		return this;
	}

	/**
	 * @param cycles
	 * @return
	 */
	public SpriteAnimation cycles(int cycles) {
		setCycleCount(cycles);
		return this;
	}

	/**
	 * @param onFinished
	 * @return
	 */
	public SpriteAnimation onFinished(EventHandler<ActionEvent> handler) {
		setOnFinished(handler);
		return this;
	}

	/**
	 * @return
	 */
	public SpriteAnimation autoReverse() {
		setAutoReverse(true);
		return this;
	}

	/**
	 * @return
	 */
	public SpriteAnimation flipHorizontal() {
		spriteImage.flipHorizontal();
		return this;
	}

	/**
	 * @return
	 */
	public SpriteAnimation flipVertical() {
		spriteImage.flipVertical();
		return this;
	}

	@Override
	protected void interpolate(double fraction) {
		int sequenceIndex = Math.min((int) Math.floor(fraction * (viewportSequence.size() - 1)), viewportSequence.size());
		if(sequenceIndex != lastSequenceIndex) {
			spriteImage.render(viewportSequence.get(sequenceIndex));
			lastSequenceIndex = sequenceIndex;
		}
	}

	/**
	 * Play infinitely.
	 */
	public void playIndefinite() {
		play(INDEFINITE);
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
		setCycleCount(cycleCount);
		play();
	}

	public void play() {
		/*
		 * If it has more then one cycle then repeat the last viewport (increasing the size of the list), otherwise it
		 * don't work properly. The start of next cycle is so fast that the last index is replaced for the first making
		 * impossible to see.
		 */
		if(getCycleCount() == INDEFINITE || getCycleCount() > 1) {
			viewportSequence.add(viewportSequence.last());
		}

		super.play();
	}
}