package com.ipfaffen.prishonor.skill;

/**
 * @author Isaias Pfaffenseller
 */
public enum SkillType {
	
	MAGIC_PROJECTILE(3),
	PORTAL(2);

	private int shots;

	/**
	 * @param shots
	 */
	private SkillType(int shots) {
		this.shots = shots;
	}

	/**
	 * @param skill
	 * @return
	 */
	public boolean is(Skill skill) {
		return (skill == null) ? false : is(skill.getType());
	}

	/**
	 * @param type
	 * @return
	 */
	public boolean is(String type) {
		return code().equals(type);
	}

	/**
	 * @return
	 */
	public String code() {
		return name().toLowerCase();
	}

	/**
	 * @return
	 */
	public int shots() {
		return shots;
	}
}