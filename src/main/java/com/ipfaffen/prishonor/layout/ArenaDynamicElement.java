package com.ipfaffen.prishonor.layout;

import javafx.beans.property.DoubleProperty;

import com.ipfaffen.prishonor.model.entity.Background;
import com.ipfaffen.prishonor.util.ArenaPosition;
import com.ipfaffen.prishonor.util.Position;
import com.ipfaffen.prishonor.util.WeakBinder;

/**
 * @author Isaias Pfaffenseller
 */
public class ArenaDynamicElement extends ArenaSimpleElement {
	
	private ArenaPosition arenaPosition;

	/**
	 * @param width
	 * @param height
	 */
	public ArenaDynamicElement(double width, double height) {
		this(width, height, null);
	}

	/**
	 * @param width
	 * @param height
	 * @param background
	 */
	public ArenaDynamicElement(double width, double height, Background background) {
		super(width, height, background);

		arenaPosition = new ArenaPosition(0d, 0d);

		// Bind layout x and y with position.
		WeakBinder.get().bind(super.xProperty(), arenaPosition.layoutXProperty());
		WeakBinder.get().bind(super.yProperty(), arenaPosition.layoutYProperty());
	}

	/**
	 * @return
	 */
	public ArenaPosition getArenaPosition() {
		return arenaPosition;
	}

	@Override
	public void setPosition(Position position) {
		arenaPosition.set(position);
	}

	@Override
	public Position getPosition() {
		return arenaPosition.get();
	}

	@Override
	public double getX() {
		return arenaPosition.getRealX();
	}

	@Override
	public void setX(double x) {
		arenaPosition.setRealX(x);
	}

	@Override
	public double getY() {
		return arenaPosition.getRealY();
	}

	@Override
	public void setY(double y) {
		arenaPosition.setRealY(y);
	}

	@Override
	public DoubleProperty xProperty() {
		return arenaPosition.realXProperty();
	}

	@Override
	public DoubleProperty yProperty() {
		return arenaPosition.realYProperty();
	}
}