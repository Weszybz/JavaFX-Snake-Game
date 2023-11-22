package example;

import java.awt.Graphics;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.Image;


/**
 * The Food class represents the food object that the snake can eat in the game.
 * It extends the SnakeObject class from the MyFrame class.
 */
public class Food extends MyFrame.SnakeObject
{
	/** A unique serial identification*/
	private static final long serialVersionUID = -3641221053272056036L;


	/**
	 * Constructs a new Food object.
	 * Intiialises the food properties
	 */
	public Food()	{
		this.l = true;

		// Set the image based on a random number
		this.i = ImageUtil.images.get(String.valueOf(new Random().nextInt(10)));

		this.w = i.getWidth(null);
		this.h = i.getHeight(null);

		// Randomly set the position within the game window.
		this.x = (int) (Math.random() * (870 - w + 10));
		this.y = (int) (Math.random() * (560 - h - 40));
	}

	/**
	 * Checks if the food has been eaten by the snake and updates the game state accordingly.
	 *
	 * @param mySnake represents the snake in the game.
	 */
	public void eaten(MyFrame.MySnake mySnake)	{
		// Mark the food as eaten, increase the snake length, and update the score.
		if (mySnake.getRectangle().intersects(this.getRectangle()) && l && mySnake.l)		{
			this.l = false;
			mySnake.changeLength(mySnake.getLength() + 1);
			mySnake.score += 521;
		}
	}

	/**
	 * Overrides the draw method to render the food on the screen.
	 * @param g The Graphics object used for drawing.
	 */
	@Override
	public void draw(Graphics g)
	{
		g.drawImage(i, x, y, null);
	}
}
