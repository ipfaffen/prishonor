package com.ipfaffen.prishonor;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import com.ipfaffen.prishonor.exception.MessageException;
import com.ipfaffen.prishonor.exception.TextException;
import com.ipfaffen.prishonor.storage.Storage;

/**
 * @author Isaias Pfaffenseller
 */
public class Main extends Application {

	private static double fxStageOffsetX = 0;
	private static double fxStageOffsetY = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(final javafx.stage.Stage fxStage) {
		final Game game = new Game(Storage.loadPlayer());

		// Lock and unlock based on focus.
		fxStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					game.locker.unlockUnfocused();
				}
				else {
					game.locker.lockUnfocused();
				}
			}
		});
		
		// Configure scene as draggable.
		game.scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                fxStageOffsetX = fxStage.getX() - event.getScreenX();
                fxStageOffsetY = fxStage.getY() - event.getScreenY();
            }
        });
		game.scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	fxStage.setX(event.getScreenX() + fxStageOffsetX);
                fxStage.setY(event.getScreenY() + fxStageOffsetY);
            }
        });

		fxStage.setScene(game.scene);
		fxStage.setTitle("Prishonor");
		fxStage.initStyle(StageStyle.UNDECORATED);
		fxStage.setResizable(false);
		fxStage.show();
	}
}

/**
 * @author Isaias Pfaffenseller
 */
class Status {

	private Group root;
	private Scene scene;

	private StackPane pane;
	private Text errorText;

	/**
	 * @param sceneWidth
	 * @param sceneHeight
	 * @param throwable
	 */
	public Status(double sceneWidth, double sceneHeight, Throwable throwable) {
		root = new Group();
		scene = new Scene(root, sceneWidth, sceneHeight, Color.BLACK);

		try {
			throw throwable;
		}
		catch(MessageException e) {
			showError(e);
		}
		catch(TextException e) {
			showError(e);
		}
		catch(Throwable e) {
			e.printStackTrace();
			showError(new MessageException("error.generic", throwable));
		}
	}

	/**
	 * @return
	 */
	private StackPane pane() {
		if(pane == null) {
			pane = new StackPane();
			pane.prefHeightProperty().bind(scene.heightProperty());
			pane.prefWidthProperty().bind(scene.widthProperty());
			pane.setStyle("-fx-background-color:rgba(0,0,0,0.8);");
			pane.setAlignment(Pos.CENTER);
			pane.getChildren().add(errorText());
		}

		return pane;
	}

	/**
	 * @param me
	 */
	public void showError(MessageException me) {
		showError(R.message.get(me.getKey()));
	}

	/**
	 * @param te
	 */
	public void showError(TextException te) {
		showError(te.getMessage());
	}

	/**
	 * @param errorMessage
	 */
	public void showError(String errorMessage) {
		errorText().setText(errorMessage);
		root.getChildren().add(pane());
	}

	/**
	 * @return
	 */
	public Text errorText() {
		if(errorText == null) {
			errorText = new Text();
			errorText.setFont(Font.font(R.main.base_font, FontWeight.BOLD, 16));
			errorText.setFill(Color.rgb(185, 185, 185));
		}
		return errorText;
	}
}