package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Random;

/**
 * @Project Food
 * @Description Represents the food entities in the game. Responsible for managing the food's appearance, location, and rendering on the canvas.
 * @Author Wesley Agbongiasede - modified
 * @version 1.0
 */
public class Food {
	private Image image;
	private int x, y; // Position of the food on the canvas
	private final List<String> imageKeys;// List of image keys for different foods
	private final Random random = new Random();

	/**
	 * Constructor for Food. Initializes the food with a list of image keys.
	 *
	 * @param imageKeys List of keys representing different food images.
	 */
	public Food(List<String> imageKeys) {
		this.imageKeys = imageKeys;
		relocateAndChangeImage();
	}

	/**
	 * Relocates the current food item and changes its image.
	 * Generates a new image and random coordinates for the food item.
	 */
	public void relocateAndChangeImage() {
		String key = imageKeys.get(random.nextInt(imageKeys.size()));
		image = util.ImageUtil.getImage(key);
		x = random.nextInt(util.GameUtil.WIDTH / util.GameUtil.CELL_SIZE);
		y = random.nextInt(util.GameUtil.HEIGHT / util.GameUtil.CELL_SIZE);
	}

	/**
	 * Draws the food item on the canvas using the provided GraphicsContext object.
	 *
	 * @param gc The GraphicsContext object used to draw the food.
	 */
	public void draw(GraphicsContext gc) {
		gc.drawImage(image, x * util.GameUtil.CELL_SIZE, y * util.GameUtil.CELL_SIZE, util.GameUtil.CELL_SIZE, util.GameUtil.CELL_SIZE);
	}

	/**
	 * Gets the X coordinate of the food's position.
	 *
	 * @return X coordinate of the food.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the Y coordinate of the food's position.
	 *
	 * @return Y coordinate of the food.
	 */
	public int getY() {
		return y;
	}


}

