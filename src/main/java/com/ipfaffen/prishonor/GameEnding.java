package com.ipfaffen.prishonor;

import static com.ipfaffen.prishonor.Game.$;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import com.ipfaffen.prishonor.animation.ActionAnimation;
import com.ipfaffen.prishonor.animation.ActionSequence;
import com.ipfaffen.prishonor.effect.ColorEffect;

/**
 * @author Isaias Pfaffenseller
 */
public class GameEnding {

	private ImageView starOneImage;
	private ImageView starTwoImage;
	private ImageView starThreeImage;
	private Text messageText;

	/**
	 * @return
	 */
	private StackPane pane() {
		starOneImage = null;
		starTwoImage = null;
		starThreeImage = null;
		messageText = null;

		HBox starPane = new HBox(20);
		starPane.setPrefWidth($.scene.getWidth());
		starPane.setAlignment(Pos.CENTER);
		starPane.getChildren().add(starOneImage());
		starPane.getChildren().add(starTwoImage());
		starPane.getChildren().add(starThreeImage());
		
		VBox vbox = new VBox(30);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(starPane, messageText());

		StackPane pane = new StackPane();
		pane.setPrefSize($.scene.getWidth(), $.arena.getHeight());
		pane.setTranslateY($.toolbar.getHeight());
		pane.setStyle("-fx-background-color:rgba(0,0,0,0.8);");
		pane.getChildren().add(vbox);
		return pane;
	}

	public void showClosing() {
		$.add(pane());
		playStarAnimation();
	}

	private void playStarAnimation() {
		ActionSequence shineStarsSequence = ActionSequence.create().betweenDelay(Duration.millis(200));

		IntegerProperty pointsProperty = $.properties.pointsProperty();
		final int points = pointsProperty.get();
		pointsProperty.set(0);

		if(points >= $.stage.getBronzeMedalPoints()) {
			shineStarsSequence.add(createShineStarAnimation(starOneImage()));
			if(points >= $.stage.getSilverMedalPoints()) {
				shineStarsSequence.add(createShineStarAnimation(starTwoImage()));
				if(points >= $.stage.getGoldMedalPoints()) {
					shineStarsSequence.add(createShineStarAnimation(starThreeImage()));
				}
			}
		}

		Duration showPointsDuration = shineStarsSequence.getDuration().add(Duration.millis(600));
		ActionAnimation.create().duration(showPointsDuration).add(pointsProperty, points).onFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				// After animation arrange menu options based on gained points.
				$.menu.arrangeOptions(points);
				
				if(points >= $.stage.getBronzeMedalPoints()) {
					if($.isLastStage()) {
						// Did minimum points in the last stage. Game beat.
						messageText().setText(R.message.get("info.gameBeat"));
					}
					else {
						// Did minimum points. Ready for next stage.
						messageText().setText(R.message.get("info.readyForNextStage"));
					}
				}
				else {
					// Did not minimum points. Not ready for next stage.
					messageText().setText(R.message.get("info.notReadyForNextStage"));
				}
			}
		}).playOne();
		shineStarsSequence.play();
	}

	/**
	 * @param node
	 * @return
	 */
	private ActionAnimation createShineStarAnimation(Node node) {
		ColorAdjust colorAdjust = (ColorAdjust) node.getEffect();
		DropShadow dropShadow = (DropShadow) colorAdjust.getInput();

		ActionAnimation brightAction = ActionAnimation.create().duration(600).cycles(2).autoReverse();
		brightAction.add(dropShadow.radiusProperty(), 50);

		ActionAnimation colorAction = ActionAnimation.create().duration(600);
		colorAction.add(colorAdjust.brightnessProperty(), 0);
		colorAction.add(colorAdjust.saturationProperty(), 0);
		colorAction.onFinished(brightAction);
		return colorAction;
	}

	/**
	 * @return
	 */
	private ImageView createStarImage() {
		ColorAdjust colorAdjust = ColorEffect.grayTone();
		colorAdjust.setInput(new DropShadow(10, Color.rgb(250, 209, 37)));

		ImageView starImage = new ImageView(R.image.star);
		starImage.setEffect(colorAdjust);
		return starImage;
	}

	/**
	 * @return
	 */
	public ImageView starOneImage() {
		if(starOneImage == null) {
			starOneImage = createStarImage();
		}
		return starOneImage;
	}

	/**
	 * @return
	 */
	public ImageView starTwoImage() {
		if(starTwoImage == null) {
			starTwoImage = createStarImage();
		}
		return starTwoImage;
	}

	/**
	 * @return
	 */
	public ImageView starThreeImage() {
		if(starThreeImage == null) {
			starThreeImage = createStarImage();
		}
		return starThreeImage;
	}
	
	/**
	 * @return
	 */
	public Text messageText() {
		if(messageText == null) {
			messageText = new Text(R.message.get("info.countingScore"));
			messageText.setFont(Font.font(R.main.base_font, FontWeight.BOLD, 12));
			messageText.setFill(Color.web(R.color.base_text));
		}
		return messageText;
	}
}