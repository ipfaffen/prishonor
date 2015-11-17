package com.ipfaffen.prishonor.storage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.ipfaffen.prishonor.player.Player;

/**
 * @author Isaias Pfaffenseller
 */
public class Storage {

	/**
	 * @return
	 */
	public static Player loadPlayer() {
		Player player;
		try {
			FileInputStream fin = new FileInputStream(folder().concat("\\player.ph"));
			ObjectInputStream ois = new ObjectInputStream(fin);
			player = (Player) ois.readObject();
			ois.close();
		}
		catch(Exception ex) {
			player = new Player();
		}
		return player;
	}
	
	/**
	 * @param player
	 */
	public static void savePlayer(Player player) {
		try {
			FileOutputStream fout = new FileOutputStream(folder().concat("\\player.ph"));
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(player);
			oos.close();
		}
		catch(Exception ex) {
			// Do nothing...
		}
	}
	
	/**
	 * @return
	 */
	public static String folder() {
		return System.getProperty("user.home");
	}
}