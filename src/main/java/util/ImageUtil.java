package util;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing and retrieving JavaFX images.
 * Used to manage images used in the game, food items and snake images
 * loads and stores the images to be retrieved
 */
public class ImageUtil {
	public static final Map<String, Image> images = new HashMap<>();// A map to hold all images, accessible by their keys.

	static {
		loadImages();
	}
	/**
	 * Loads images into the static map.
	 *
	 */
	private static void loadImages() {
		// Load and store images here
		images.put("snake-head-right", new Image("snake-head-right.png"));
		images.put("snake-body", new Image("snake-body.png"));

		for (int i = 0; i <= 17; i++) {
			images.put(String.valueOf(i), new Image("food-" + i + ".png"));
		}

		images.put("UI-background", new Image("UI-background.png"));
		images.put("sound", new Image("sound.png"));
		images.put("settings", new Image("settings.png"));
		images.put("pause", new Image("pause.png"));
		images.put("help", new Image("help.png"));
	}
	/**
	 * This method allows  an image to be retrieved from the map based on a given key.
	 *
	 * @param key The key associated with the image to be retrieved.
	 * @return The Image object if found, or null if there is no image associated with the key.
	 */
	public static Image getImage(String key) {
		return images.get(key);
	}
}
