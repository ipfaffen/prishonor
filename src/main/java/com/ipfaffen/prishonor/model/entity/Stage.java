package com.ipfaffen.prishonor.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Isaias Pfaffenseller
 */
public class Stage implements Serializable {
	
	private Long id;
	private String name;
	private Integer lengthX;
	private Integer lengthY;
	private Integer bronzeMedalPoints;
	private Integer silverMedalPoints;
	private Integer goldMedalPoints;
	private Background background;
	private List<StageObject> stageObjects;

	/**
	 * @param positionX
	 * @param positionY
	 * @return
	 */
	public StageObject getStageObject(int positionX, int positionY) {
		for(StageObject stageObject: getStageObjects()) {
			if(stageObject.getPositionX() == positionX && stageObject.getPositionY() == positionY) {
				return stageObject;
			}
		}
		return null;
	}

	/**
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return lengthX
	 */
	public Integer getLengthX() {
		return lengthX;
	}

	/**
	 * @param lengthX
	 */
	public void setLengthX(Integer lengthX) {
		this.lengthX = lengthX;
	}

	/**
	 * @return lengthY
	 */
	public Integer getLengthY() {
		return lengthY;
	}

	/**
	 * @param lengthY
	 */
	public void setLengthY(Integer lengthY) {
		this.lengthY = lengthY;
	}

	/**
	 * @return bronzeMedalPoints
	 */
	public Integer getBronzeMedalPoints() {
		return bronzeMedalPoints;
	}

	/**
	 * @param bronzeMedalPoints
	 */
	public void setBronzeMedalPoints(Integer bronzeMedalPoints) {
		this.bronzeMedalPoints = bronzeMedalPoints;
	}

	/**
	 * @return silverMedalPoints
	 */
	public Integer getSilverMedalPoints() {
		return silverMedalPoints;
	}

	/**
	 * @param silverMedalPoints
	 */
	public void setSilverMedalPoints(Integer silverMedalPoints) {
		this.silverMedalPoints = silverMedalPoints;
	}

	/**
	 * @return goldMedalPoints
	 */
	public Integer getGoldMedalPoints() {
		return goldMedalPoints;
	}

	/**
	 * @param goldMedalPoints
	 */
	public void setGoldMedalPoints(Integer goldMedalPoints) {
		this.goldMedalPoints = goldMedalPoints;
	}

	/**
	 * @return background
	 */
	public Background getBackground() {
		return background;
	}

	/**
	 * @param background
	 */
	public void setBackground(Background background) {
		this.background = background;
	}

	/**
	 * @return
	 */
	public List<StageObject> getStageObjects() {
		if(stageObjects == null) {
			stageObjects = new ArrayList<StageObject>();
		}
		return stageObjects;
	}

	/**
	 * @param stageObjects
	 */
	public void setStageObjects(List<StageObject> stageObjects) {
		this.stageObjects = stageObjects;
	}
}