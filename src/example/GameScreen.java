package example;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.*;


/**
 * 
 * @Project Snake
 * @Description Play the game
 * @Author Wesley Agbons
 * @version Not sure
 */


/**
 * The Play class represents the main game class
 * It extends from MyFrame
 * It contains the game logic, rendering, and user input handling.
 */
public class GameScreen extends GameFrame
{
	/** A unique serial identification number*/
	private static final long serialVersionUID = -3641221053272056036L;
/** The snake object representing the player. */
	public SnakeGame snakeGame = new SnakeGame(100, 100);// x , y
	/** The food object that the snake can eat */
	public Food food = new Food();

	/** The background image for the game. */
	public Image background = ImageUtil.images.get("UI-background");
	/** The image displayed when the game is over */
	public Image failImage = ImageUtil.images.get("game-scene-01");
	private String playerName;
	private static Leaderboard leaderboard = new Leaderboard();
	private boolean gameEnded = false; // Flag to indicate if the game has ended


	/**
	 * Constructor for GameScreen.
	 * @param playerName The name of the player.
	 */
	public GameScreen(String playerName) {
		this.playerName = playerName;
		//this.leaderboard = new Leaderboard();
		if (leaderboard == null) {
			leaderboard = new Leaderboard(); // Initialize the leaderboard only once
		}
		loadFrame();
	}

	/**
	 * Override the keyPressed method to handle keyboard input.
	 *
	 * @param e The KeyEvent object representing the key press.
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{
		super.keyPressed(e);
		snakeGame.keyPressed(e);
	}

	/**
	 * Overrides the paint method to render game elements on the screen.
	 *
	 * @param g The Graphics object used for drawing.
	 */
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		g.drawImage(background, 0, 0, null);

		// Determine the state of the game
		if (snakeGame.isAvailable)
		{
			// Draw the snake
			snakeGame.draw(g);
			if (food.isAvailable)
			{
				// Draw and check for the eaten food
				food.draw(g);
				food.eaten(snakeGame);
			} else
			{
				// If no food, create a new one
				food = new Food();
			}
		} else
		{
			// If the snake is not alive, show the failure image
			if (!snakeGame.isAvailable && !gameEnded) {
				gameEnded = true; // Set the flag to true to indicate the game has ended
				g.drawImage(failImage, 0, 0, null);
				onGameEnd();
			}

		}
		// Draw the score on the screen
		drawScore(g);
	}
	/**
	 * Draw the current score on the screen.
	 *
	 * @param g The Graphics object used for drawing.
	 */
	public void drawScore(Graphics g)
	{
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		g.setColor(Color.MAGENTA);
		g.drawString("SCORE : " + snakeGame.score, 20, 40);
	}

	/**
	 * Method to be called when the game ends.
	 */
	private void onGameEnd() {
		leaderboard.addScore(playerName, snakeGame.score);
		JOptionPane.showMessageDialog(this, leaderboard.getFormattedScores(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);

		// Ask the player if they want to play again
		int response = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (response == JOptionPane.YES_OPTION) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					GameScreen.this.closeFrame(); // Close the current game window
					// Reset the game state by calling resetGame
					resetGame(); // This calls the resetGame method of the GameFrame class
					new GameScreen(playerName).loadFrame(); // Start a new game
				}
			});
		} else {
			GameScreen.this.closeFrame(); // Close the game window
			resetGame(); // This calls the resetGame method of the GameFrame class
			MainMenu mainMenu = new MainMenu();
		}
	}


	/**
	 * The main method to the start the game. Creates an instance of the Play class and loads the frame.
	 *
	 * @param args The command-line arguments (unused).
	 */
	public static void main(String[] args)
	{
		// Create an instance of the Play class and load the frame
		new GameScreen("PlayerName").loadFrame();
		//Start playing background music
		MusicPlayer.getMusicPlay("src/example/frogger.mp3");

	}
}
