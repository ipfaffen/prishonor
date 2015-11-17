package com.ipfaffen.prishonor;

import static com.ipfaffen.prishonor.Game.$;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;

import com.ipfaffen.prishonor.skill.Skill;

/**
 * @author Isaias Pfaffenseller
 */
public class GameSkill {

	private List<Skill> skills;
	private Map<String, Skill> skillMap;

	private int selectedIndex;
	private ObjectProperty<Skill> selectedProperty;
	private IntegerProperty selectedUsageLeftProperty;
	private StringProperty selectedLegendProperty;
	private ObjectProperty<Image> selectedImageProperty;

	public GameSkill() {
		skills = new ArrayList<Skill>();
		skillMap = new HashMap<String, Skill>();
		selectedProperty = new SimpleObjectProperty<Skill>();

		// Reset player skills.
		$.player.resetSkills();
		
		for(Skill skill: $.player.getSkills()) {
			if(selectedProperty.get() == null) {
				selectedProperty.addListener(new ChangeListener<Skill>() {
					@Override
					public void changed(ObservableValue<? extends Skill> observable, Skill oldValue, Skill newValue) {
						// Change selected skill information.
						selectedUsageLeftProperty().set(newValue.getUsageLeft());
						selectedLegendProperty().set(R.message.get(newValue.getType()));
						selectedImageProperty().set(R.image.get(newValue.getType()));
					}
				});

				// Initialize selected property with first skill in order.
				selectedProperty.set(skill);
			}

			skills.add(skill);
			skillMap.put(skill.getType(), skill);
		}
	}

	public void selectNext() {
		if(!hasSkill()) {
			return;
		}

		selectedIndex += 1;
		if(selectedIndex >= skills.size()) {
			selectedIndex = 0;
		}
		selectedProperty.set(skills.get(selectedIndex));
	}

	public void selectPrevious() {
		if(!hasSkill()) {
			return;
		}

		selectedIndex -= 1;
		if(selectedIndex < 0) {
			selectedIndex = (skills.size() - 1);
		}
		selectedProperty.set(skills.get(selectedIndex));
	}

	/**
	 * @return
	 */
	public Skill selected() {
		return selectedProperty.get();
	}

	/**
	 * @param skillType
	 * @return
	 */
	public void consumeUsage(String skillType) {
		Skill skill = skillMap.get(skillType);
		skill.setUsageLeft(skill.getUsageLeft() - 1);

		selectedUsageLeftProperty().set(skill.getUsageLeft());
	}

	/**
	 * @return
	 */
	public boolean hasSkill() {
		return !skills.isEmpty();
	}

	/**
	 * @return
	 */
	public IntegerProperty selectedUsageLeftProperty() {
		if(selectedUsageLeftProperty == null) {
			selectedUsageLeftProperty = new SimpleIntegerProperty();
		}
		return selectedUsageLeftProperty;
	}

	/**
	 * @return
	 */
	public StringProperty selectedLegendProperty() {
		if(selectedLegendProperty == null) {
			selectedLegendProperty = new SimpleStringProperty();
		}
		return selectedLegendProperty;
	}

	/**
	 * @return
	 */
	public ObjectProperty<Image> selectedImageProperty() {
		if(selectedImageProperty == null) {
			selectedImageProperty = new SimpleObjectProperty<Image>();
		}
		return selectedImageProperty;
	}
}