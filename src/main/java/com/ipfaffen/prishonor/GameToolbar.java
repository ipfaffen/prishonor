package com.ipfaffen.prishonor;

import static com.ipfaffen.prishonor.Game.$;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import com.ipfaffen.prishonor.layout.ToolbarMenu;

/**
 * @author Isaias Pfaffenseller
 */
public class GameToolbar extends ToolbarMenu {

	private double contentHeight;

	private Pane pane;
	private BorderPane content;

	private ImageView logoImage;
	private Text titleText;
	private ImageView exitImage;
	
	public GameToolbar() {
		contentHeight = 32;
	}

	@Override
	public Pane pane() {
		if(pane == null) {
			pane = new VBox();
			pane.setPrefHeight(getHeight());
			pane.getChildren().add(content());
		}
		return pane;
	}
	
	/**
	 * @return
	 */
	public BorderPane content() {
		if(content == null) {
			HBox leftPane = new HBox(12);
			leftPane.setAlignment(Pos.CENTER_LEFT);
			leftPane.getChildren().addAll(logoImage(), titleText());

			HBox rightPane = new HBox();
			rightPane.getChildren().addAll(exitImage());

			content = new BorderPane();
			content.setPrefHeight(contentHeight);
			content.setPadding(new Insets(2, 8, 1, 8));
			content.setStyle(String.format("-fx-background-color:%s;", R.color.base_background));
			content.setLeft(leftPane);
			content.setRight(rightPane);
		}
		return content;
	}

	/**
	 * @return
	 */
	public ImageView logoImage() {
		if(logoImage == null) {
			logoImage = createImageView(R.image.logo);
			logoImage.setFitHeight(16);
		}
		return logoImage;
	}

	/**
	 * @return
	 */
	public Text titleText() {
		if(titleText == null) {
			titleText = createText();
			titleText.setText(String.format("Prishonor :: %s", $.stage.getName()));
		}
		return titleText;
	}

	/**
	 * @return
	 */
	public ImageView exitImage() {
		if(exitImage == null) {
			exitImage = createImageView(R.image.close, true);
			exitImage.setFitHeight(15);
			exitImage.setTranslateY(7);
			exitImage.setCursor(Cursor.HAND);
			exitImage.setPickOnBounds(true);
			exitImage.setOnMouseClicked(new EventHandler<Event>() {
				public void handle(Event event) {
					Platform.exit();
				};
			});
		}
		return exitImage;
	}
	
	/**
	 * @return
	 */
	public double getHeight() {
		return contentHeight;
	}
}