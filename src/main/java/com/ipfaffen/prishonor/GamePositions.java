package com.ipfaffen.prishonor;

import com.ipfaffen.prishonor.util.Position;
import com.ipfaffen.prishonor.util.PositionHolder;

/**
 * @author Isaias Pfaffenseller
 */
public class GamePositions {

	private PositionHolder unused;
	private PositionHolder heroUsed;
	private PositionHolder objectUsed;
	private PositionHolder warpUsed;

	public GamePositions() {
		unused = new PositionHolder();
		heroUsed = new PositionHolder();
		objectUsed = new PositionHolder();
		warpUsed = new PositionHolder();
	}

	/**
	 * @param position
	 */
	public void addUnused(Position position) {
		unused.add(position);
	}

	/**
	 * @param position
	 */
	public void addHeroUsed(Position position) {
		unused.remove(position);
		heroUsed.add(position);
	}

	public void clearHeroUsed() {
		unused.addAll(heroUsed);
		heroUsed.clear();
	}

	/**
	 * @param position
	 */
	public void addObjectUsed(Position position) {
		unused.remove(position);
		objectUsed.add(position);
	}

	/**
	 * @param position
	 */
	public void removeObjectUsed(Position position) {
		objectUsed.remove(position);
		unused.add(position);
	}

	/**
	 * @param position
	 */
	public void addWarpUsed(Position position) {
		unused.remove(position);
		warpUsed.add(position);
	}

	/**
	 * @return
	 */
	public PositionHolder getUnused() {
		return unused;
	}

	/**
	 * @return
	 */
	public PositionHolder getHeroUsed() {
		return heroUsed;
	}

	/**
	 * @return
	 */
	public PositionHolder getObjectUsed() {
		return objectUsed;
	}

	/**
	 * @return
	 */
	public PositionHolder getWarpUsed() {
		return warpUsed;
	}
}