package com.ipfaffen.prishonor;

import static com.ipfaffen.prishonor.Game.$;

import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;

import com.ipfaffen.prishonor.element.ElementExplosion;
import com.ipfaffen.prishonor.key.KeyDirection;
import com.ipfaffen.prishonor.layout.ArenaSimpleElement;
import com.ipfaffen.prishonor.layout.SimpleElement;
import com.ipfaffen.prishonor.model.entity.StageObject;
import com.ipfaffen.prishonor.unit.UnitHero;
import com.ipfaffen.prishonor.util.Position;

/**
 * @author Isaias Pfaffenseller
 */
public class GameArena {

	private SimpleElement pane;
	private int cellWidth;
	private int cellHeight;
	private double width;
	private double height;
	private double maxX;
	private double maxY;

	private ElementExplosion explosion;

	/**
	 * Position where the hero will be placed at the start.
	 */
	private Position startPosition;

	/**
	 * Objects of the arena mapped by position.
	 */
	private Map<Position, ArenaSimpleElement> objectMap;

	public GameArena() {
		this.cellWidth = 24;
		this.cellHeight = 24;

		this.width = ($.stage.getLengthX() * cellWidth);
		this.height = ($.stage.getLengthY() * cellHeight);

		this.maxX = (width - cellWidth);
		this.maxY = (height - cellHeight);

		for(int x = 0; x <= maxX; x += cellWidth) {
			for(int y = 0; y <= maxY; y += cellHeight) {
				$.positions.addUnused(new Position(x, y));
			}
		}
	}

	/**
	 * @return
	 */
	public SimpleElement pane() {
		if(pane == null) {
			pane = new SimpleElement(width, height);
			pane.setBackground($.stage.getBackground());
			addObjects();
		}
		return pane;
	}

	private void addObjects() {
		objectMap = new HashMap<Position, ArenaSimpleElement>();

		for(int y = 0; y < $.stage.getLengthY(); y++) {
			for(int x = 0; x < $.stage.getLengthX(); x++) {
				StageObject stageObject = $.stage.getStageObject(x, y);
				if(stageObject != null) {
					ArenaSimpleElement object = new ArenaSimpleElement(cellWidth, cellHeight);
					object.setPosition(new Position((x * cellWidth), (y * cellHeight)));

					if(stageObject.getBackground().getImageName() != null) {
						// Add object background image to element.
						object.add(new ImageView(R.image.mapped(stageObject.getBackground().getImageName())));
					}

					// Set background but style just color.
					object.setBackground(stageObject.getBackground(), true);

					addObject(object);
				}
				else if(startPosition == null) {
					startPosition = new Position((x * cellWidth), (y * cellHeight));
				}
			}
		}
	}

	/**
	 * @param object
	 */
	public void addObject(ArenaSimpleElement object) {
		add(object);
		objectMap.put(object.getPosition(), object);
		$.positions.addObjectUsed(object.getPosition());
	}

	/**
	 * @param position
	 */
	public void removeObject(Position position) {
		remove(objectMap.get(position));
		objectMap.remove(position);
		$.positions.removeObjectUsed(position);
	}

	/**
	 * @param targetPosition
	 * @param onFinished
	 */
	public void destroyObject(Position targetPosition, final EventHandler<ActionEvent> onFinished) {
		final ElementExplosion explosion = getExplosion();
		explosion.setX(targetPosition.x);
		explosion.setY(targetPosition.y);
		explosion.act(onFinished);

		addIfNotIn(explosion);
		removeObject(targetPosition);
	}

	/**
	 * Check if the following positions is within the boundaries of the arena.
	 * 
	 * @param position
	 * @return
	 */
	public boolean isWithinLimits(Position position) {
		return (position.x >= 0 && position.x <= maxX && position.y >= 0 && position.y <= maxY);
	}

	/**
	 * @param position
	 * @return
	 */
	public boolean hasTarget(Position position) {
		return hasTarget(position.x, position.y);
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean hasTarget(double x, double y) {
		double targetX = $.target.element().getPosition().x;
		double targetY = $.target.element().getPosition().y;
		double targetXArea = (targetX + cellWidth);
		double targetYArea = (targetY + cellHeight);

		return (x >= targetX && x < targetXArea && y >= targetY && y < targetYArea);
	}

	/**
	 * @param position
	 * @return
	 */
	public boolean hasCollision(Position position) {
		return hasCollision(position.x, position.y);
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean hasCollision(double x, double y) {
		return $.positions.getHeroUsed().contains(x, y) || $.positions.getObjectUsed().contains(x, y);
	}

	/**
	 * @param position
	 * @return
	 */
	public boolean hasWarp(Position position) {
		return $.positions.getWarpUsed().contains(position);
	}

	/**
	 * Return the position of the next cell based on direction and the given element. Also set x/y of the element when
	 * it's necessary (reached the limits of the arena).
	 * 
	 * @param element
	 * @param direction
	 * @return
	 */
	public Position fixGetPosition(ArenaSimpleElement element, KeyDirection direction) {
		double x = element.getX();
		double y = element.getY();

		if(direction.isRight()) {
			x = (element.getX() + cellWidth);

			if(x > (maxX + cellWidth)) {
				x = 0;
				element.setX(x - cellWidth);
			}
		}
		else if(direction.isLeft()) {
			x = (element.getX() - cellWidth);

			if(x < (0 - cellWidth)) {
				x = maxX;
				element.setX(x + cellWidth);
			}
		}
		else if(direction.isUp()) {
			y = (element.getY() - cellHeight);

			if(y < (0 - cellHeight)) {
				y = maxY;
				element.setY(y + cellHeight);
			}
		}
		else if(direction.isDown()) {
			y = (element.getY() + cellHeight);

			if(y > (maxY + cellHeight)) {
				y = 0;
				element.setY(y - cellHeight);
			}
		}

		return new Position(x, y);
	}

	/**
	 * Return the position of the given destiny element. Also set x/y of the origin element when it's necessary (reached
	 * the limits of the arena).
	 * 
	 * @param origin
	 * @param destiny
	 * @return
	 */
	public Position fixGetPosition(ArenaSimpleElement origin, ArenaSimpleElement destiny) {
		double x = destiny.getX();
		double y = destiny.getY();

		if(x == 0 && origin.getX() == (maxX + cellWidth)) {
			origin.setX(x - cellWidth);
		}
		else if(x == maxX && origin.getX() == (0 - cellWidth)) {
			origin.setX(x + cellWidth);
		}
		else if(y == 0 && origin.getY() == (maxY + cellHeight)) {
			origin.setY(y - cellHeight);
		}
		else if(y == maxY && origin.getY() == (0 - cellHeight)) {
			origin.setY(y + cellHeight);
		}

		return new Position(x, y);
	}

	/**
	 * @param hero
	 */
	public void addHero(UnitHero hero) {
		add(hero);
		hero.await();

		// hero.setPosition($.positions.getUnused().first());
		hero.setPosition(startPosition);

		$.positions.addHeroUsed(hero.getPosition());
	}

	/**
	 * @param element
	 */
	public void add(ArenaSimpleElement element) {
		pane.add(element);
	}

	/**
	 * @param element
	 */
	public void addIfNotIn(ArenaSimpleElement element) {
		if(!$.arena.contains(element)) {
			// New element.
			$.arena.add(element);
		}
	}

	/**
	 * @param element
	 */
	public void remove(ArenaSimpleElement element) {
		pane.remove(element);
	}

	/**
	 * @param element
	 * @return
	 */
	public boolean contains(ArenaSimpleElement element) {
		return pane.contains(element);
	}

	/**
	 * @return
	 */
	public ElementExplosion getExplosion() {
		if(explosion == null) {
			explosion = new ElementExplosion();
		}

		return explosion;
	}

	/**
	 * @return
	 */
	public int getCellWidth() {
		return cellWidth;
	}

	/**
	 * @return
	 */
	public int getCellHeight() {
		return cellHeight;
	}

	/**
	 * @return
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * @return
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @return
	 */
	public double getMaxX() {
		return maxX;
	}

	/**
	 * @return
	 */
	public double getMaxY() {
		return maxY;
	}
}