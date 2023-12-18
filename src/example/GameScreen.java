package example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private static Leaderboard leaderboard;
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

		// Create a new JFrame for the leaderboard
		JFrame leaderboardFrame = new JFrame("Leaderboard");
		leaderboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the leaderboard frame

		// Create a JPanel for the leaderboard and set its layout
		JPanel leaderboardPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// Draw the background image here
				g.drawImage(failImage, 0, 0, getWidth(), getHeight(), null);
			}
			@Override
			public boolean isOpaque() {
				return false; // Set the panel as not opaque
			}
		};
		leaderboardPanel.setLayout(new BorderLayout()); // Use BorderLayout to add the "Play Again" button

		// Create a background component for the background image
		JComponent backgroundComponent = new JComponent() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
			}
		};

		// Add the background component to the NORTH position (above the leaderboard entries)
		leaderboardPanel.add(backgroundComponent, BorderLayout.NORTH); // Background image

		// Create a JPanel for the leaderboard entries
		JPanel leaderboardEntriesPanel = new JPanel(new GridLayout(0, 1));
		leaderboardEntriesPanel.setOpaque(false);

		// Add leaderboard data (player names and scores) as JLabels or other components to this panel
		for (Leaderboard.PlayerScore score : leaderboard.getScores()) {
			JLabel label = new JLabel(score.toString());
			label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			label.setForeground(Color.WHITE);
			leaderboardEntriesPanel.add(label);
		}

		// Add the leaderboard entries panel to the center of leaderboardPanel
		leaderboardPanel.add(leaderboardEntriesPanel, BorderLayout.CENTER);

		// Create a "Play Again" button and add it to the SOUTH position
		JButton playAgainButton = new JButton("Play Again");
		playAgainButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Handle the "Play Again" action here
				resetGame(); // Reset the game
				playerName = "PlayerName";;
				leaderboardFrame.dispose(); // Close the leaderboard frame
				MainMenu mainMenu = new MainMenu();
			}
		});

		// Add components to leaderboardPanel
		leaderboardPanel.add(backgroundComponent, BorderLayout.CENTER); // Background image
		leaderboardPanel.add(leaderboardEntriesPanel, BorderLayout.CENTER);
		leaderboardPanel.add(playAgainButton, BorderLayout.SOUTH);

		// Add the leaderboard panel to the leaderboard frame
		leaderboardFrame.getContentPane().add(leaderboardPanel);

		// Set the size and visibility of the leaderboard frame
		leaderboardFrame.setSize(400, 400); // Adjust the size as needed
		leaderboardFrame.setLocationRelativeTo(null); // Center on the screen
		leaderboardFrame.setVisible(true);

		closeFrame();
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
