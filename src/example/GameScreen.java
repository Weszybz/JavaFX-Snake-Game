package example;

import java.awt.*;
import java.awt.event.*;
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
	private MusicPlayer musicPlayer = new MusicPlayer("src/example/death.mp3", false);;
	private boolean isMusicOn = true; // Flag to track music state
	/** The background image for the game. */
	public Image background = ImageUtil.images.get("UI-background");
	/** The image displayed when the game is over */
	public Image failImage = ImageUtil.images.get("game-scene-01");
	private String playerName;
	private static Leaderboard leaderboard;
	private boolean gameEnded = false; // Flag to indicate if the game has ended
	private boolean isPaused = false; // Flag to track pause state
	private JButton pauseButton;
	private Image pauseIcon = ImageUtil.images.get("pause");


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

		// Create the pause button
		pauseButton = new JButton(new ImageIcon(pauseIcon));
		pauseButton.setPreferredSize(new Dimension(32, 32));
		pauseButton.setContentAreaFilled(false);
		pauseButton.setBorder(null);
		JPanel buttonPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				// Draw the pause icon on the panel
				g.drawImage(pauseIcon, 0, 11,null);
			}
		};
		buttonPanel.setPreferredSize(new Dimension(32, 32));
		buttonPanel.setOpaque(false);

// Add an ActionListener to the buttonPanel to handle the button click event
		buttonPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				togglePause();
				// Handle the button click event here
				// You can toggle pause or perform any other action
			}
		});

// Add the buttonPanel to the JFrame
		jFrame.add(buttonPanel, BorderLayout.EAST);
		//jFrame.revalidate();
		//jFrame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));


		//jFrame.setVisible(true);
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
			if (!snakeGame.isAvailable && !gameEnded && snakeGame.isPaused == false) {
				gameEnded = true; // Set the flag to true to indicate the game has ended
				g.drawImage(failImage, 0, 0, null);
				onGameEnd();
				if (MainMenu.musicPlayer != null) {
					MainMenu.musicPlayer.resetMusic();
				}
				musicPlayer.play();

			}

		}

		if (!snakeGame.isPaused) {
			// Regular game rendering
			// ... (your game rendering logic)
		} else {
			drawPauseScreen(g); // Draw pause screen when game is paused
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

		int scoreX = getWidth() - 20 -  g.getFontMetrics().stringWidth("SCORE : " + snakeGame.score);

		g.drawString("Player: " + playerName, 20,40);
		g.drawString("SCORE : " + snakeGame.score, scoreX, 40);
	}

	public void toggleMusic() {
		if (isMusicOn) {
			musicPlayer.stopMusic(); // Stop playing music
		} else {
			musicPlayer.start(); // Start playing music
		}
		isMusicOn = !isMusicOn;
	}

	public void togglePause() {
		snakeGame.isPaused = !snakeGame.isPaused;

		if (snakeGame.isPaused) {
			// Pause the game and display the pause menu
			pauseGame();
		} else {
			// Resume the game
			resumeGame();
		}
	}

	// Method to pause the game (you can implement this)
	private void pauseGame() {
		snakeGame.isPaused = true;
	}

	// Method to resume the game (you can implement this)
	private void resumeGame() {
		snakeGame.isPaused = false;
		snakeGame.resetUpdateCounter(); // Reset the counter when game is resumed
	}


	// Method to display the pause menu
	private void drawPauseScreen(Graphics g) {
		// Drawing a simple pause overlay
		g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 48));
		g.drawString("Game Paused", getWidth() / 2 - 150, getHeight() / 2);
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
		};
		leaderboardPanel.setLayout(new BorderLayout()); // Use BorderLayout to add the "Play Again" button


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
		leaderboardPanel.add(leaderboardEntriesPanel, BorderLayout.CENTER);
		leaderboardPanel.add(playAgainButton, BorderLayout.SOUTH);

		// Add the leaderboard panel to the leaderboard frame
		leaderboardFrame.getContentPane().add(leaderboardPanel);

		// Set the size and visibility of the leaderboard frame
		leaderboardFrame.setSize(870, 560);; // Adjust the size as needed
		leaderboardFrame.setLocationRelativeTo(null); // Center on the screen
		leaderboardFrame.setVisible(true);

		closeFrame();
	}

	public MusicPlayer getMusicPlayer() {
		return musicPlayer;
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

//		MusicPlayer musicPlayer = new MusicPlayer("src/example/frogger.mp3");
//		musicPlayer.setLoop(true);
//		musicPlayer.play();
//		musicPlayer.stopMusic();

	}
}
