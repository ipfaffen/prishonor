package com.ipfaffen.prishonor;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * @author Isaias Pfaffenseller
 */
public final class R {

	public static final Main main = new Main();
	public static final Durations duration = new Durations();
	public static final Colors color = new Colors();
	public static final Images image = new Images();
	public static final Messages message = new Messages();

	public static final class Main {
		public final int game_speed = 10;
		public int move_speed = 6;

		public final int target_max_points = 40;
		public final int target_max_progress = 1000;
		public final int target_bonus_max_points = 140;
		public final int target_bonus_max_progress = 500;
		
		public final String base_font = "Courier New";

		/**
		 * @param move_speed
		 */
		public void setMoveSpeed(int move_speed) {
			this.move_speed = move_speed;
			duration.recalculate();
		}
	}

	public static final class Durations {
		public final Duration lost_assistant_normal_sprite = newDuration(20);
		public final Duration lost_assistant_bonus_sprite = newDuration(250);
		public final Duration lost_assistant_shake = newDuration(20);

		public Duration action_move;

		public Duration hero_run;
		public final Duration hero_await = newDuration(100);
		public final Duration hero_fall = newDuration(20);
		public final Duration hero_cast_spell = newDuration(50);

		public Duration assistant_run;
		public final Duration assistant_await = newDuration(100);

		public final Duration magic_projectile_move = newDuration(8);
		public final Duration magic_projectile_sprite = newDuration(50);
		public final Duration magic_projectile_bright = newDuration(10);

		public final Duration explosion = newDuration(50);

		public final Duration portal_move = newDuration(8);
		public final Duration portal_sprite = newDuration(70);
		public final Duration portal_bright = newDuration(70);

		public Durations() {
			recalculate();
		}

		public void recalculate() {
			action_move = newDuration(2 * main.move_speed);
			hero_run = newDuration(12 * main.move_speed);
			assistant_run = newDuration(10 * main.move_speed);
		}

		/**
		 * @param millis
		 * @return
		 */
		private Duration newDuration(int millis) {
			return Duration.millis(millis * main.game_speed);
		}
	}

	public static final class Colors {
		public final String base_background = "rgb(20,20,20)";
		public final String base_text = "rgb(255,255,255)";
		public final String target_progress_bar = "rgb(151,13,20)";
		public final String target_bonus_progress_bar = "rgb(202,81,0)";
		public final String target_empty_progres_bar = "rgb(50,50,50)";
	}

	public static final class Images {
		public final Image hero_sprite = newImage("hero_sprite_01.png");
		public final Image assistant_sprite = newImage("assistant_sprite_01.png");
		public final Image lost_assistant = newImage("lost_assistant_sprite_01.png");
		public final Image explosion_sprite = newImage("explosion_sprite_01.png");
		public final Image magic_projectile_sprite = newImage("magic_projectile_sprite_01.png");
		
		public final Image magic_projectile = newImage("magic_projectile_01.png");
		public final Image portal_sprite = newImage("portal_sprite_01.png");
		public final Image portal = newImage("portal_01.png");

		public final Image logo = newImage("logo_03.png");
		public final Image target = newImage("target_01.png");
		public final Image target_bonus = newImage("target_bonus_01.png");
		public final Image points = newImage("points_01.png");
		public final Image star = newImage("star_01.png");
		public final Image prev = newImage("prev_01.png");
		public final Image next = newImage("next_01.png");
		public final Image restart = newImage("restart_01.png");
		public final Image close = newImage("close_01.png");
		
		private final Map<String, Image> image_map = new HashMap<String, Image>();
		private final Map<String, URL> url_map = new HashMap<String, URL>();

		/**
		 * @param name
		 * @return
		 */
		private Image newImage(String name) {
			return new Image(getClass().getResourceAsStream(String.format("/resources/images/%s", name)));
		}
		
		/**
		 * @param name
		 * @return
		 */
		public Image mapped(String name) {
			Image image = image_map.get(name);
			if(image == null) {
				image = newImage(String.format("stage/%s", name));
				image_map.put(name, image);
			}
			return image;
		}
		
		/**
		 * @param name
		 * @return
		 */
		public URL url(String name) {
			URL url = url_map.get(name);
			if(url == null) {
				url = getClass().getResource(String.format("/resources/images/stage/%s", name));
				url_map.put(name, url);
			}
			return url;
		}
		
		/**
		 * @param attribute
		 * @return
		 */
		public Image get(String attribute) {
			try {
				return (Image) getClass().getField(attribute).get(this);
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static final class Messages {
		private Properties properties;

		/**
		 * @param key
		 * @return
		 */
		public final String get(String key) {
			return properties().getProperty(key, key);
		}

		/**
		 * @return
		 */
		private final Properties properties() {
			if(properties == null) {
				properties = new Properties();

				try {
					// Load a properties file.
					properties.load(getClass().getResourceAsStream("/resources/messages.properties"));
				}
				catch(IOException e) {
					throw new RuntimeException(e);
				}
			}
			return properties;
		}
	}
}