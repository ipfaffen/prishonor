package com.ipfaffen.prishonor;

import static com.ipfaffen.prishonor.Game.$;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import com.ipfaffen.prishonor.key.KeyAction;
import com.ipfaffen.prishonor.key.KeyAttack;
import com.ipfaffen.prishonor.key.KeyDirection;
import com.ipfaffen.prishonor.type.KeyEnum;

/**
 * @author Isaias Pfaffenseller
 */
public class GameKeyboard {

	private List<KeyAction> actionSequence;
	private boolean locked;
	private boolean justEnter;

	public GameKeyboard() {
		actionSequence = new ArrayList<KeyAction>();
	}

	public void startListening() {
		// Add key pressed handler to scene.
		$.scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if(!locked) {
					processKey(keyEvent);
				}
				keyEvent.consume();
			}
		});
	}

	public void stopListening() {
		// Remove key pressed handler from scene.
		$.scene.setOnKeyPressed(null);
	}

	/**
	 * @param keyEvent
	 */
	private void processKey(final KeyEvent keyEvent) {
		if(keyEvent.getCode() == KeyCode.ENTER) {
			onEnterKeyPressed();
		}
		else if(justEnter) {
			// If it is just listening to enter then return.
			return;
		}
		else if(KeyDirection.isDirection(keyEvent.getCode())) {
			actionSequence.add(KeyEnum.get(keyEvent.getCode()).action());
			onDirectionKeyPressed();
		}
		else if(keyEvent.getCode() == KeyCode.TAB) {
			onTabKeyPressed();
		}
		else if(!$.isPlaying) {
			// First key before starting playing must be a direction.
			return;
		}
		else if(keyEvent.getCode() == KeyCode.SPACE) {
			lock();
			actionSequence.clear();
			actionSequence.add(KeyEnum.get(keyEvent.getCode()).action());
			onSpaceKeyPressed();
		}
	}

	private void onDirectionKeyPressed() {
		// If the game is not playing yet start it.
		if(!$.isPlaying) {
			$.play();
		}
	}

	private void onEnterKeyPressed() {
		$.restart();
	}

	private void onSpaceKeyPressed() {
	}

	private void onTabKeyPressed() {
		$.skill.selectNext();
	}

	/**
	 * @return
	 */
	public KeyDirection nextDirection() {
		return nextDirection(false, false, null);
	}

	/**
	 * Consume direction.
	 * 
	 * @param ignoreAlike - it will ignore direction which is equals to the given current one.
	 * @param ignoreReverse - it will ignore direction which is the reverse of the given current one.
	 * @param currentDirection - direction for comparison.
	 * @return
	 */
	public KeyDirection nextDirection(boolean ignoreAlike, boolean ignoreReverse, KeyDirection currentDirection) {
		for(Iterator<KeyAction> iterator = actionSequence.iterator(); iterator.hasNext();) {
			KeyAction action = iterator.next();

			if(action instanceof KeyDirection) {
				KeyDirection direction = (KeyDirection) action;
				if(currentDirection != null) {
					if(currentDirection != null && ((ignoreAlike && currentDirection.equals(direction)) || (ignoreReverse && currentDirection.isReverse(direction)))) {
						iterator.remove();
						continue;
					}
				}
				return direction;
			}

			iterator.remove();
			break;
		}

		return currentDirection;
	}

	/**
	 * Consume attack.
	 * 
	 * @return
	 */
	public KeyAttack nextAttack() {
		for(Iterator<KeyAction> iterator = actionSequence.iterator(); iterator.hasNext();) {
			KeyAction action = iterator.next();

			if(action instanceof KeyAttack) {
				KeyAttack attack = (KeyAttack) action;
				iterator.remove();
				return attack;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public boolean hasDirection() {
		for(KeyAction action: actionSequence) {
			if(action instanceof KeyDirection) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	public boolean hasAttack() {
		for(KeyAction action: actionSequence) {
			if(action instanceof KeyAttack) {
				return true;
			}
		}
		return false;
	}

	public void lock() {
		locked = true;
	}

	public void unlock() {
		locked = false;
	}

	public void enableJustEnter() {
		justEnter = true;
	}

	public void disableJustEnter() {
		justEnter = false;
	}

	/**
	 * @param keySequence
	 */
	public void setKeySequence(String keySequence) {
		actionSequence.addAll(KeyEnum.toKeyActionList(keySequence.replaceFirst("\\[", "").replaceFirst("\\]", "").split(";")));
	}
}