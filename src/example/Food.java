package example;

import java.awt.Graphics;
import java.util.Random;


/**
 * The Food class represents the food object that the snake can eat in the game.
 * It extends the SnakeObject class from the MyFrame class.
 */
public class Food extends GameFrame.SnakeObject
{
	/** A unique serial identification*/
	private static final long serialVersionUID = -3641221053272056036L;


	/**
	 * Constructs a new Food object.
	 * Intiialises the food properties
	 */
	public Food()	{
		this.isAvailable = true;

		// Set the image based on a random number
		this.image = ImageUtil.images.get(String.valueOf(new Random().nextInt(10)));

		this.width = image.getWidth(null);
		this.height = image.getHeight(null);

		// Randomly set the position within the game window.
		this.x = (int) (Math.random() * (870 - width + 10));
		this.y = (int) (Math.random() * (560 - height - 40));
	}

	/**
	 * Checks if the food has been eaten by the snake and updates the game state accordingly.
	 *
	 * @param snakeGame represents the snake in the game.
	 */
	public void eaten(GameFrame.SnakeGame snakeGame)	{
		// Mark the food as eaten, increase the snake length, and update the score.
		if (snakeGame.getRectangle().intersects(this.getRectangle()) && isAvailable && snakeGame.isAvailable)		{
			this.isAvailable = false;
			snakeGame.changeLength(snakeGame.getLength() + 1);
			snakeGame.score += 521;
		}
	}

	/**
	 * Overrides the draw method to render the food on the screen.
	 * @param g The Graphics object used for drawing.
	 */
	@Override
	public void draw(Graphics g)
	{
		g.drawImage(image, x, y, null);
	}
}
