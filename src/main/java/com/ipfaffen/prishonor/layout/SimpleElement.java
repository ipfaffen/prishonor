package com.ipfaffen.prishonor.layout;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import com.ipfaffen.prishonor.R;
import com.ipfaffen.prishonor.model.entity.Background;

/**
 * @author Isaias Pfaffenseller
 */
public class SimpleElement extends Pane {
	
	private Background background;

	/**
	 * @param width
	 * @param height
	 */
	public SimpleElement(double width, double height) {
		super();
		setPrefSize(width, height);
	}

	/**
	 * @param width
	 * @param height
	 * @param background
	 */
	public SimpleElement(double width, double height, Background background) {
		this(width, height);

		if(background != null) {
			setBackground(background);
		}
	}

	/**
	 * @param node
	 */
	public void add(Node node) {
		getChildren().add(node);
	}

	/**
	 * @param node
	 */
	public void remove(Node node) {
		getChildren().remove(node);
	}

	/**
	 * @param node
	 * @return
	 */
	public boolean contains(Node node) {
		return getChildren().contains(node);
	}

	/**
	 * @param background
	 */
	public void setBackground(Background background) {
		setBackground(background, false);
	}

	/**
	 * @param background
	 * @param ignoreImage
	 */
	public void setBackground(Background background, boolean ignoreImage) {
		this.background = background;
		
		StringBuilder style = new StringBuilder();
		if(!ignoreImage && background.getImageName() != null) {
			style.append(String.format("-fx-background-image: url('%s');", R.image.url(background.getImageName()).toExternalForm()));
		}
		if(background.getColor() != null) {
			style.append(String.format("-fx-background-color: %s;", background.getColor()));
		}

		setStyle(style.toString());
	}

	/**
	 * @return
	 */
	public Background getBackground() {
		return background;
	}
}