package com.ipfaffen.prishonor.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ipfaffen.prishonor.skill.Skill;
import com.ipfaffen.prishonor.skill.SkillType;

/**
 * @author Isaias Pfaffenseller
 */
public class Player implements Serializable {

	private String name;
	private List<Skill> skills;
	private Map<Long, Integer> stageScoreMap;
	
	
	/**
	 * @param name
	 */
	public Player(String name) {
		this.name = name;
		this.stageScoreMap = new LinkedHashMap<Long, Integer>();
	}
	
	public Player() {
		this("default");
	}
	
	public void resetSkills() {
		this.skills = new ArrayList<Skill>() {{
			add(new Skill(SkillType.MAGIC_PROJECTILE));
			add(new Skill(SkillType.PORTAL));
		}};
	}
	
	/**
	 * @param stageId
	 * @param score
	 */
	public void putStageScore(Long stageId, Integer score) {
		Integer stageScore = stageScoreMap.get(stageId);
		if(stageScore == null || score > stageScore) {
			stageScoreMap.put(stageId, score);
		}	
	}
	
	/**
	 * @param stageId
	 * @return
	 */
	public Integer getStageScore(Long stageId) {
		return stageScoreMap.get(stageId);
	}
	
	/**
	 * @return
	 */
	public Long getLastPlayedStageId() {
		if(stageScoreMap.isEmpty()) {
			return 1l;
		}
		Iterator<Long> itr = stageScoreMap.keySet().iterator();
		Long topStageId = itr.next();
		while(itr.hasNext()) {
			Long stageId = itr.next();
			if(stageId > topStageId) {
				topStageId = stageId;
			}
		}
		return topStageId;
	}
	
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return
	 */
	public List<Skill> getSkills() {
		return skills;
	}
	
	/**
	 * @return
	 */
	public Map<Long, Integer> getStageScoreMap() {
		return stageScoreMap;
	}
}