package com.ipfaffen.prishonor;

import static com.ipfaffen.prishonor.Game.$;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import com.ipfaffen.prishonor.layout.ToolbarMenu;
import com.ipfaffen.prishonor.type.Opacity;
import com.ipfaffen.prishonor.util.WeakBinder;

/**
 * @author Isaias Pfaffenseller
 */
public class GameMenu extends ToolbarMenu {

	private double divisorHeight;
	private double contentHeight;

	private Pane pane;
	private BorderPane content;
	private Pane divisor;
	private Pane progressBar;

	private ImageView pointsImage;
	private Text pointsText;
	private ImageView consumedTargetImage;
	private Text consumedTargetText;
	private ImageView consumedBonusTargetImage;
	private Text consumedBonusTargetText;
	private ImageView selectedSkillImage;
	private Text selectedSkillUsageLeftText;
	private Text selectedSkillLegendText;
	private ImageView prevStageImage;
	private ImageView nextStageImage;
	private ImageView restartImage;

	private TranslateTransition receivePointsAnimation;

	public GameMenu() {
		divisorHeight = 5;
		contentHeight = 32;
	}
	
	@Override
	public Pane pane() {
		if(pane == null) {
			pane = new VBox();
			pane.setPrefHeight(getHeight());
			pane.getChildren().add(divisor());
			pane.getChildren().add(content());
		}
		return pane;
	}
	
	/**
	 * @return
	 */
	public BorderPane content() {
		if(content == null) {
			HBox leftPane = new HBox(10);
			leftPane.setAlignment(Pos.CENTER_LEFT);
			leftPane.getChildren().addAll(pointsImage(), pointsText());
			leftPane.getChildren().addAll(consumedTargetImage(), consumedTargetText());
			leftPane.getChildren().addAll(consumedBonusTargetImage(), consumedBonusTargetText());

			if($.skill.hasSkill()) {
				leftPane.getChildren().addAll(selectedSkillImage(), selectedSkillUsageLeftText(), selectedSkillLegendText());
			}

			HBox rightPane = new HBox(15);
			rightPane.getChildren().addAll(prevStageImage(), nextStageImage(), restartImage());

			content = new BorderPane();
			content.setPrefHeight(contentHeight);
			content.setPadding(new Insets(4, 8, 1, 8));
			content.setStyle(String.format("-fx-background-color:%s;", R.color.base_background));
			content.setLeft(leftPane);
			content.setRight(rightPane);
		}
		return content;
	}

	/**
	 * @return
	 */
	public Pane divisor() {
		if(divisor == null) {
			divisor = new Pane();
			divisor.setPrefHeight(divisorHeight);
			divisor.setStyle(String.format("-fx-background-color:%s;", R.color.target_empty_progres_bar));
			divisor.getChildren().add(progressBar());
		}
		return divisor;
	}

	/**
	 * @return
	 */
	public Pane progressBar() {
		if(progressBar == null) {
			progressBar = new Pane();
			progressBar.setPrefHeight(divisorHeight);
			progressBar.setStyle("-fx-background-color:black;");
		}
		return progressBar;
	}
	
	/**
	 * @param points
	 */
	public void arrangeOptions(int points) {
		double opacity = Opacity.FULLY_VISIBLE;
		consumedTargetImage().setOpacity(opacity);
		consumedTargetText().setOpacity(opacity);
		consumedBonusTargetImage().setOpacity(opacity);
		consumedBonusTargetText().setOpacity(opacity);

		opacity = Opacity.BARELY_VISIBLE;
		selectedSkillImage().setOpacity(opacity);
		selectedSkillUsageLeftText().setOpacity(opacity);
		selectedSkillLegendText().setOpacity(opacity);

		if(nextStageImage().isDisable() && !$.isLastStage() && points >= $.stage.getBronzeMedalPoints()) {
			enable(nextStageImage());
		}		
	}

	/**
	 * @return
	 */
	public ImageView pointsImage() {
		if(pointsImage == null) {
			pointsImage = createImageView(R.image.points);
			pointsImage.setTranslateY(-1.5);
			pointsImage.setFitHeight(14);
		}
		return pointsImage;
	}

	/**
	 * @return
	 */
	public Text pointsText() {
		if(pointsText == null) {
			pointsText = createText();
			pointsText.setFont(Font.font(R.main.base_font, FontWeight.BOLD, 20));
			WeakBinder.get().bind(pointsText.textProperty(), $.properties.pointsProperty().asString());
		}
		return pointsText;
	}

	/**
	 * @return
	 */
	public ImageView consumedTargetImage() {
		if(consumedTargetImage == null) {
			consumedTargetImage = createImageView(R.image.target);
			consumedTargetImage.setOpacity(Opacity.BARELY_VISIBLE);
			consumedTargetImage.setFitHeight(14);
		}
		return consumedTargetImage;
	}

	/**
	 * @return
	 */
	public Text consumedTargetText() {
		if(consumedTargetText == null) {
			consumedTargetText = createText();
			consumedTargetText.setOpacity(Opacity.BARELY_VISIBLE);
			WeakBinder.get().bind(consumedTargetText.textProperty(), $.properties.consumedTargetsProperty().asString());
		}
		return consumedTargetText;
	}

	/**
	 * @return
	 */
	public ImageView consumedBonusTargetImage() {
		if(consumedBonusTargetImage == null) {
			consumedBonusTargetImage = createImageView(R.image.target_bonus);
			consumedBonusTargetImage.setOpacity(Opacity.BARELY_VISIBLE);
			consumedBonusTargetImage.setFitHeight(14);
		}
		return consumedBonusTargetImage;
	}

	/**
	 * @return
	 */
	public Text consumedBonusTargetText() {
		if(consumedBonusTargetText == null) {
			consumedBonusTargetText = createText();
			consumedBonusTargetText.setOpacity(Opacity.BARELY_VISIBLE);
			WeakBinder.get().bind(consumedBonusTargetText.textProperty(), $.properties.consumedBonusTargetsProperty().asString());
		}
		return consumedBonusTargetText;
	}

	/**
	 * @return
	 */
	public ImageView selectedSkillImage() {
		if(selectedSkillImage == null) {
			selectedSkillImage = createImageView(R.image.points);
			selectedSkillImage.setFitHeight(20);
			selectedSkillImage.setTranslateX(30);
			WeakBinder.get().bind(selectedSkillImage.imageProperty(), $.skill.selectedImageProperty());
		}
		return selectedSkillImage;
	}

	/**
	 * @return
	 */
	public Text selectedSkillUsageLeftText() {
		if(selectedSkillUsageLeftText == null) {
			selectedSkillUsageLeftText = createText();
			selectedSkillUsageLeftText.setTranslateX(30);
			WeakBinder.get().bind(selectedSkillUsageLeftText.textProperty(), Bindings.format("x%s", $.skill.selectedUsageLeftProperty().asString()));
		}
		return selectedSkillUsageLeftText;
	}

	/**
	 * @return
	 */
	public Text selectedSkillLegendText() {
		if(selectedSkillLegendText == null) {
			selectedSkillLegendText = createText();
			selectedSkillLegendText.setTranslateX(30);
			WeakBinder.get().bind(selectedSkillLegendText.textProperty(), $.skill.selectedLegendProperty());
		}
		return selectedSkillLegendText;
	}

	/**
	 * @return
	 */
	public ImageView prevStageImage() {
		if(prevStageImage == null) {
			prevStageImage = createImageView(R.image.prev, true);
			prevStageImage.setFitHeight(20);
			prevStageImage.setTranslateY(2);
			prevStageImage.setCursor(Cursor.HAND);
			prevStageImage.setPickOnBounds(true);
			prevStageImage.setOnMouseClicked(new EventHandler<Event>() {
				public void handle(Event event) {
					$.prevStage();
				};
			});
			if($.isFirstStage()) {
				disable(prevStageImage);
			}
		}
		return prevStageImage;
	}

	/**
	 * @return
	 */
	public ImageView nextStageImage() {
		if(nextStageImage == null) {
			nextStageImage = createImageView(R.image.next, true);
			nextStageImage.setFitHeight(20);
			nextStageImage.setTranslateY(2);
			nextStageImage.setCursor(Cursor.HAND);
			nextStageImage.setPickOnBounds(true);
			nextStageImage.setOnMouseClicked(new EventHandler<Event>() {
				public void handle(Event event) {
					$.nextStage();
				};
			});
			if(!$.isAlreadyBeatStage()) {
				disable(nextStageImage);
			}
		}
		return nextStageImage;
	}
	
	/**
	 * @return
	 */
	public ImageView restartImage() {
		if(restartImage == null) {
			restartImage = createImageView(R.image.restart, true);
			restartImage.setFitHeight(20);
			restartImage.setTranslateY(2);
			restartImage.setCursor(Cursor.HAND);
			restartImage.setPickOnBounds(true);
			restartImage.setOnMouseClicked(new EventHandler<Event>() {
				public void handle(Event event) {
					$.restart();
				};
			});
		}
		return restartImage;
	}

	/**
	 * @param node
	 */
	public void disable(Node node) {
		node.setDisable(true);
		node.setOpacity(Opacity.BARELY_VISIBLE);
	}
	
	/**
	 * @param node
	 */
	public void enable(Node node) {
		node.setDisable(false);
		node.setOpacity(Opacity.FULLY_VISIBLE);
	}
	
	public void receivePoints() {
		receivePointsAnimation().playFromStart();
	}

	/**
	 * @return
	 */
	public TranslateTransition receivePointsAnimation() {
		if(receivePointsAnimation == null) {
			receivePointsAnimation = new TranslateTransition(Duration.millis(100), pointsText());
			receivePointsAnimation.setCycleCount(2);
			receivePointsAnimation.setAutoReverse(true);
			receivePointsAnimation.setFromY(pointsText().getTranslateY());
			receivePointsAnimation.setToY(2);
		}
		return receivePointsAnimation;
	}
	
	/**
	 * @return
	 */
	public double getHeight() {
		return (contentHeight + divisorHeight);
	}
}