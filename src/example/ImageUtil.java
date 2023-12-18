package example;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

/**
 * The ImageUtil class provides a utility for managing and retrieving images used in the game.
 */
public class ImageUtil
{
	/** A map to store images with corresponding names. */
	public static Map<String, Image> images = new HashMap<>();

	static
	{
		// Initialize the map with various images used in the game.

		// Snake images
		images.put("snake-head-right", GameUtil.getImage("example/snake-head-right.png"));
		images.put("snake-body", GameUtil.getImage("example/snake-body.png"));

		// Obstacles images
		for (int i = 0; i <= 17; i++) {
			images.put(String.valueOf(i), GameUtil.getImage("example/food-" + i + ".png"));
		}

		// User interface and scene background images
		images.put("UI-background", GameUtil.getImage("example/UI-background.png"));
		images.put("game-scene-01", GameUtil.getImage("example/game-scene-01.jpg"));
		images.put("sound", GameUtil.getImage("example/sound.png"));
		images.put("settings", GameUtil.getImage("example/settings.png"));
		images.put("pause", GameUtil.getImage("example/pause.png"));
	}
}
