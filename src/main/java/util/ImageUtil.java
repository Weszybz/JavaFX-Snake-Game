package util;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project Image Utilization
 * @Description Utility class for managing and retrieving JavaFX images. Used to manage images used in the game, including food items and snake images. It loads and stores the images to be efficiently retrieved.
 * @Author Wesley Agbongiasede - modified
 * @version 1.0
 */
public class ImageUtil {
	/**
	 * A map to hold all images, accessible by their keys.
	 *
	 * The key is a string that represents the image. The value is the Image object.
	 */
	public static final Map<String, Image> images = new HashMap<>();// A map to hold all images, accessible by their keys.

	static {
		loadImages();
	}

	/**
	 * Load and store images used in the game.
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
	 * Retrieves an Image object associated with the given key.
	 *
	 * @param key The key representing the image.
	 * @return The Image object associated with the key.
	 */
	public static Image getImage(String key) {
		return images.get(key);
	}
}
