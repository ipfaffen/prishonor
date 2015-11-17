package com.ipfaffen.prishonor;

import com.ipfaffen.prishonor.unit.UnitHero;

/**
 * @author Isaias Pfaffenseller
 */
public class GameUnit {

	private UnitHero hero;

	/**
	 * @return
	 */
	public UnitHero hero() {
		if(hero == null) {
			hero = new UnitHero();
		}
		return hero;
	}
}