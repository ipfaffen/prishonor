package com.ipfaffen.prishonor;

import static com.ipfaffen.prishonor.Game.$;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * @author Isaias Pfaffenseller
 */
public class GameStatus {

	private StackPane pane;
	private Text errorText;

	/**
	 * @return
	 */
	private StackPane pane() {
		if(pane == null) {
			pane = new StackPane();
			pane.setPrefSize($.arena.getWidth(), $.arena.getHeight());
			pane.setStyle("-fx-background-color:rgba(0, 0, 0, 0.8);");
			pane.getChildren().add(errorText());
		}

		return pane;
	}

	/**
	 * @param throwable
	 */
	public void showError(Throwable throwable) {
		showError(throwable.getMessage());
	}

	/**
	 * @param errorMessage
	 */
	public void showError(String errorMessage) {
		errorText().setText(errorMessage);
		$.add(pane());
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