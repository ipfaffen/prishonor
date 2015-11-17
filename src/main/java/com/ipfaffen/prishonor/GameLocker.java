package com.ipfaffen.prishonor;

import static com.ipfaffen.prishonor.Game.$;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import com.ipfaffen.prishonor.animation.ActionAnimation;
import com.ipfaffen.prishonor.type.Opacity;

/**
 * @author Isaias Pfaffenseller
 */
public class GameLocker {

	private StackPane pane;
	private Text lockText;
	private StringProperty lockMessageProperty;
	private Timeline dotAnimation;
	private ActionAnimation loadingAnimation;

	private boolean lockedLoading;
	private boolean lockedUnfocused;
	
	public boolean lockedError;

	public GameLocker() {
		lockMessageProperty = new SimpleStringProperty();
	}

	/**
	 * @return
	 */
	private StackPane pane() {
		if(pane == null) {
			pane = new StackPane();
			pane.setPrefSize($.scene.getWidth(), $.scene.getHeight());
			pane.setStyle("-fx-background-color:rgba(0,0,0,0.8);");
			pane.getChildren().add(lockText());
		}
		pane.toFront();
		return pane;
	}

	public void lockLoading() {
		lockLoading(R.message.get("info.loading"));
	}

	/**
	 * @param loadingMessage
	 */
	public void lockLoading(String loadingMessage) {
		if(lockedUnfocused) {
			unlockUnfocused();
		}

		if(!lockedLoading) {
			$.add(pane());

			lockText().setOpacity(Opacity.FULLY_VISIBLE);
			loadingAnimation().play();
		}

		setLockMessage(loadingMessage);
		lockedLoading = true;
	}

	public void unlockLoading() {
		if(lockedLoading) {
			$.remove(pane());
			loadingAnimation().stop();
			lockedLoading = false;
		}
	}

	/**
	 * @param exception
	 */
	public void lockError(Throwable exception) {
		exception.printStackTrace();
		unlockLoading();

		$.add(pane());

		lockText().setOpacity(Opacity.FULLY_VISIBLE);
		setLockMessage(R.message.get("error.generic"));

		lockedLoading = true;
		lockedError = true;
	}

	private final EventHandler<MouseEvent> unlockOnMouseClickHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			unlockUnfocused();
		}
	};

	public void lockUnfocused() {
		if(lockedLoading || lockedUnfocused) {
			return;
		}

		StackPane pane = pane();
		pane.setOnMouseClicked(unlockOnMouseClickHandler);

		$.add(pane);
		$.keyboard.lock();

		setLockMessage(R.message.get("info.clickToFocus"));

		lockText().setOpacity(Opacity.FULLY_VISIBLE);
		loadingAnimation().play();

		lockedUnfocused = true;
	}

	public void unlockUnfocused() {
		if(lockedLoading || !lockedUnfocused) {
			return;
		}

		StackPane pane = pane();
		pane.setOnMouseClicked(null);

		$.remove(pane());
		$.keyboard.unlock();

		loadingAnimation().stop();

		lockedUnfocused = false;
	}

	/**
	 * @param lockMessage
	 */
	public void setLockMessage(String lockMessage) {
		lockMessageProperty.set(lockMessage);
	}

	/**
	 * @return
	 */
	public String getLockMessage() {
		return lockMessageProperty.get();
	}

	/**
	 * @return
	 */
	public Text lockText() {
		if(lockText == null) {
			lockText = new Text();
			lockText.setFont(Font.font(R.main.base_font, FontWeight.BOLD, 16));
			lockText.setFill(Color.web(R.color.base_text));
			lockText.textProperty().bind(lockMessageProperty);
		}
		return lockText;
	}

	/**
	 * @return
	 */
	private ActionAnimation loadingAnimation() {
		if(loadingAnimation == null) {
			loadingAnimation = ActionAnimation.create().add(lockText.opacityProperty(), Opacity.BARELY_VISIBLE).duration(1000).autoReverse().cycles(Timeline.INDEFINITE);
		}
		return loadingAnimation;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unused")
	private Timeline dotAnimation() {
		if(dotAnimation == null) {
			dotAnimation = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Run this every 500 milliseconds.
					String message = getLockMessage();
					if(message.endsWith("...")) {
						lockMessageProperty.set(message.replace("...", "."));
					}
					else if(message.endsWith("..")) {
						lockMessageProperty.set(message.replace("..", "..."));
					}
					else if(message.endsWith(".")) {
						lockMessageProperty.set(message.replace(".", ".."));
					}
				}
			}));
			dotAnimation.setCycleCount(Timeline.INDEFINITE);
		}
		return dotAnimation;
	}
}