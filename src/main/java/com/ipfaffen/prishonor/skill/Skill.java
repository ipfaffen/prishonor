package com.ipfaffen.prishonor.skill;

import java.io.Serializable;

/**
 * @author Isaias Pfaffenseller
 */
public class Skill implements Serializable {

	private String type;
	private Integer usageLeft;
	
	/**
	 * @param type
	 */
	public Skill(SkillType type) {
		this.type = type.code();
		this.usageLeft = type.shots();
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return usageLeft
	 */
	public Integer getUsageLeft() {
		return usageLeft;
	}

	/**
	 * @param usageLeft
	 */
	public void setUsageLeft(Integer usageLeft) {
		this.usageLeft = usageLeft;
	}

	/**
	 * @return
	 */
	public boolean hasUsageLeft() {
		return usageLeft > 0;
	}
}