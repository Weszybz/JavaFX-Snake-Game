package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Random;

/**
 * Represents a food object in the Snake game.
 * The food is responsible for generating random images and relocating to new positions on the canvas.
 */
public class Food {
	private Image image;
	private int x, y; // Position of the food on the canvas

	private List<String> imageKeys;// List of image keys for different foods
	private Random random = new Random();

	/**
	 * Constructor for Food.
	 * Initialises the food with a list of image keys.
	 * @param imageKeys List of keys representing different food images.
	 */
	public Food(List<String> imageKeys) {
		this.imageKeys = imageKeys;
		relocateAndChangeImage();
	}
	/**
	 * Generates random image from list of image keys.
	 * Relocates food to a new random position on the canvas.
	 */
	public void relocateAndChangeImage() {
		//Generates a new image and relocates it after previous one is eaten
		String key = imageKeys.get(random.nextInt(imageKeys.size()));
		image = util.ImageUtil.getImage(key);
		x = random.nextInt(util.GameUtil.WIDTH / util.GameUtil.CELL_SIZE);
		y = random.nextInt(util.GameUtil.HEIGHT / util.GameUtil.CELL_SIZE);
	}
	/**
	 * Draws the food item on the game canvas.
	 *
	 * @param gc GraphicsContext for drawing the food.
	 */
	public void draw(GraphicsContext gc) {
		gc.drawImage(image, x * util.GameUtil.CELL_SIZE, y * util.GameUtil.CELL_SIZE, util.GameUtil.CELL_SIZE, util.GameUtil.CELL_SIZE);
	}

	// Getters and setters for position of food so x and y coordinates for the foods position
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

