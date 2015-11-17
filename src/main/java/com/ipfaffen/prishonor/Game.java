package com.ipfaffen.prishonor;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipfaffen.prishonor.model.entity.Stage;
import com.ipfaffen.prishonor.player.Player;
import com.ipfaffen.prishonor.storage.Storage;
import com.ipfaffen.prishonor.util.Chronometer;
import com.ipfaffen.prishonor.util.WeakBinder;

/**
 * @author Isaias Pfaffenseller
 */
public class Game {

	/**
	 * Hold the current game instance.
	 */
	public static Game $;

	protected Group root;
	protected Scene scene;
	
	protected Map<Long, Stage> stagesMap;
	
	protected Player player;
	protected Stage stage;

	public GameEnding ending;
	public GameLocker locker;
	public GameStatus status;
	public GameProperties properties;
	public GameToolbar toolbar;
	public GameMenu menu;
	public GameUnit unit;
	public GamePositions positions;
	public GameArena arena;
	public GameSkill skill;
	public GameKeyboard keyboard;
	public GameTarget target;

	protected Chronometer chronometer;
	protected boolean isPlaying;
	
	public Game(Player player) {
		// Set instance to static holder.
		$ = this;

		// TODO This is not good. The scene size should be calculated based on arena and other panes.
		double sceneWidth = 600;
		double sceneHeight = 430;
		
		root = new Group();
		scene = new Scene(root, sceneWidth, sceneHeight, Color.web(R.color.base_background));
		locker = new GameLocker();
		
		initialize(player);
	}

	/**
	 * @param player
	 */
	protected final void initialize(Player player) {
		this.player = player;

		run(new GameTask(){
			@Override
			protected Void call() {
				runInitialize();
				return null;
			}
			
			@Override
			protected void succeeded() {
				if(locker.lockedError) {
					return;
				}
				
				// Unlock loading and lock unfocused.
				locker.unlockLoading();
				locker.lockUnfocused();
			}
		});
	}

	private void runInitialize() {
		stagesMap = buildStagesMap();
		
		// Prepare game with last stage played. In first access it will be the first stage.
		prepare(player.getLastPlayedStageId());

		ending = new GameEnding();
		status = new GameStatus();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					start();
				}
				catch(Throwable e) {
					// Show error.
					locker.lockError(e);
				}
			}
		});
	}

	/**
	 * @param stageId
	 */
	private final void prepare(Long stageId) {
		stage = stagesMap.get(stageId);
		
		// Unbind all.
		WeakBinder.get().unbindAll();

		positions = new GamePositions();
		properties = new GameProperties();
		arena = new GameArena();
		target = new GameTarget();
		unit = new GameUnit();
		skill = new GameSkill();
		keyboard = new GameKeyboard();
		toolbar = new GameToolbar();
		menu = new GameMenu();
		chronometer = new Chronometer();

		final BorderPane contentPane = new BorderPane();
		contentPane.setCenter(arena.pane());
		contentPane.setBottom(menu.pane());
		contentPane.setTop(toolbar.pane());

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				setAll(contentPane);
			}
		});
	}

	private void start() {
		// Add hero to arena.
		arena.addHero(unit.hero());

		// Add target to arena.
		arena.add(target.element());

		// Start target turn.
		target.startTurn();

		// Pause target turn.
		target.pauseTurn();

		// Start listening to keyboard key press.
		keyboard.startListening();
	}

	private void end() {
		// Listen only to enter key press.
		keyboard.enableJustEnter();

		// Stop chronometer.
		chronometer.stop();

		// Disable hero and all its dependencies.
		unit.hero().disable();

		// Stop target turn.
		target.stopTurn();

		isPlaying = false;
	}
	
	/**
	 * Called when hero do the first move.
	 */
	protected void play() {
		isPlaying = true;

		// Start chronometer.
		chronometer.start();

		// Continue target turn.
		target.continueTurn();

		// Start action.
		unit.hero().act();
	}

	/**
	 * Called when the game is over.
	 */
	public void gameOver() {
		end();
		run(new GameTask(){
			@Override
			protected Void call() {
				saveScore();
				return null;
			}
			
			@Override
			protected void succeeded() {
				// Unlock screen.
				locker.unlockLoading();

				// Show closing.
				ending.showClosing();
			}

			@Override
			protected void failed() {
				// Unlock screen.
				locker.unlockLoading();

				// Show error.
				status.showError(getException());
			}
		});
	}

	private void saveScore() {
		player.putStageScore(stage.getId(), properties.pointsProperty().get());
		
		// Save player for later access.
		Storage.savePlayer(player);
	}

	protected void restart() {
		end();
		run(new GameTask(){
			@Override
			protected Void call() throws Exception {
				runRestart();
				return null;
			}
		});
	}

	private void runRestart() {
		prepare(stage.getId());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				start();
			}
		});
	}
	
	protected void prevStage() {
		end();
		run(new GameTask(){
			@Override
			protected Void call() {
				runPrevStage();
				return null;
			}
		});
	}

	private void runPrevStage() {
		prepare(stage.getId() - 1);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				start();
			}
		});
	}

	protected void nextStage() {
		end();
		run(new GameTask(){
			@Override
			protected Void call() {
				runNextStage();
				return null;
			}
		});
	}

	private void runNextStage() {
		prepare(stage.getId() + 1);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				start();
			}
		});
	}
	
	/**
	 * @return
	 */
	private Map<Long, Stage> buildStagesMap() {
		Map<Long, Stage> stagesMap = new HashMap<Long, Stage>();
		for(Stage stage: loadStages()) {
			stagesMap.put(stage.getId(), stage);
		}		
		return stagesMap;
	}
	
	/**
	 * @return
	 */
	private List<Stage> loadStages() {
		List<Stage> stages;
		try {
			InputStream jsonIS = getClass().getResourceAsStream("/resources/json/stages.json");
			stages = new ObjectMapper().readValue(jsonIS, new TypeReference<List<Stage>>(){});
		}
		catch(Exception e) {
			throw new RuntimeException("Could not load stages from json.", e);
		}
		return stages;
	}
	
	/**
	 * @return
	 */
	protected boolean isFirstStage() {
		return (stage.getId() == 1);
	}
	
	/**
	 * @return
	 */
	protected boolean isLastStage() {
		return (stage.getId() == stagesMap.size());
	}
	
	/**
	 * @return
	 */
	protected boolean isAlreadyBeatStage() {
		Integer score = player.getStageScore(stage.getId());
		if(score != null && score >= stage.getBronzeMedalPoints()) {
			// Player already beat this stage.
			return true;
		}
		return false;
	}
	
	/**
	 * @param node
	 */
	protected void add(Node node) {
		root.getChildren().add(node);
	}

	/**
	 * @param node
	 */
	protected void remove(Node node) {
		root.getChildren().remove(node);
	}

	/**
	 * @param nodes
	 */
	protected void setAll(Node... nodes) {
		root.getChildren().clear();
		root.getChildren().setAll(nodes);
	}
	
	/**
	 * @param task
	 */
	protected void run(GameTask task) {
		new Thread(task).start();
	}

	/**
	 * @author Isaias Pfaffenseller
	 */
	protected abstract class GameTask extends Task<Void> {
		@Override
		protected abstract Void call() throws Exception;

		@Override
		protected void running() {
			// Lock screen.
			locker.lockLoading(R.message.get("info.pleaseWait"));
		}

		@Override
		protected void succeeded() {
			// Unlock loading and lock unfocused.
			locker.unlockLoading();
		}

		@Override
		protected void failed() {
			// Show error.
			locker.lockError(getException());
		}		
	}
}