package view;

import controller.GameScreenFX;
import controller.Leaderboard;
import controller.Settings;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import model.Direction;
import model.Paddle;

/**
 * Main class for the snake game application.
 * Sets up the stage and scene for the game, handles user interactions, and controls the game states.
 */
public class GameSnake extends Application {
	private static final int WIDTH = 870;
	private static final int HEIGHT = 560;
	private model.Snake snake;
	private model.Food food;
	private AnimationTimer gameLoop;
	private Pane root; // Declare root at the class level
	private Canvas canvas;
	private Scene scene;
	private MediaPlayer mediaPlayer;
	private MediaPlayer deathSoundPlayer;
	private String playerName;
	private Stage primaryStage;
	private StackPane layeredPane; // New StackPane to layer the pause screen
	private Rectangle pauseOverlay; // Using Rectangle for the semi-transparent layer
	private model.Paddle paddle;

	Image muteImage = new Image(getClass().getResource("/sound.png").toExternalForm());
	ImageView muteView = new ImageView(muteImage);
	Image unmuteImage = new Image(getClass().getResource("/nosound.png").toExternalForm());
	ImageView unmuteView = new ImageView(unmuteImage);
	Image pauseImage = new Image(getClass().getResource("/pause.png").toExternalForm());
	ImageView pauseView = new ImageView(pauseImage);

	public GameSnake(String playerName) {
		this.playerName = playerName; // Initialize player name
	}

	@Override
	public void start(Stage primaryStage) {
		/**
		 * The main entry point for the application.
		 * Launches the JavaFX application.
		 *
		 * @param args Command line arguments passed to the application.
		 */
		// Create the root pane
		root = new Pane();
		layeredPane = new StackPane();
		this.primaryStage = primaryStage;

		//Applies the chosen background to the game scene
		applySelectedBackground();

		// Create and add the canvas for drawing game elements
		canvas = new Canvas(WIDTH, HEIGHT);
		canvas.setFocusTraversable(true);
		root.getChildren().add(canvas); // Add canvas over the background image

		// Initialize the pauseOverlay but don't add to layeredPane yet
		pauseOverlay = new Rectangle(WIDTH, HEIGHT, new Color(0, 0, 0, 0.5)); // Semi-transparent black
		pauseOverlay.setVisible(false); // Initially not visible

		// Layer everything together
		//layeredPane.getChildren().addAll(root, pauseOverlay); // Stack the pauseOverlay on top

		// Set up the scene with layeredPane as the root
		scene = new Scene(layeredPane, WIDTH, HEIGHT);
		primaryStage.setTitle("Snake Game");
		primaryStage.setScene(scene);
		primaryStage.show();

		scene.getStylesheets().add(getClass().getResource("/menu.css").toExternalForm());

		primaryStage.setResizable(false); // Stops resizing of the scene
		primaryStage.setMaximized(false); // Window remains the same size
		canvas.widthProperty().bind(scene.widthProperty());
		canvas.heightProperty().bind(scene.heightProperty());
		startMusic();
		// Initializes game components and event handlers
		initializeGame(canvas);
		setupEventHandlers(scene);

		// Initialize the death sound
		initializeDeathSound();


		// Starts the game loop
		startGameLoop(canvas.getGraphicsContext2D());

		// Create and configure the pause button
		Button pauseButton = new Button();
		pauseButton.setGraphic(pauseView);

		// Create and configure the mute button
		Button muteButton = new Button();
		muteButton.setGraphic(muteView);

		// Create an HBox for buttons with spacing between them
		HBox buttonContainer = new HBox(10); // 10px spacing between buttons
		StackPane.setAlignment(pauseButton, Pos.TOP_RIGHT);
		StackPane.setAlignment(muteButton, Pos.TOP_RIGHT);
		buttonContainer.getChildren().addAll(muteButton, pauseButton);

		// Position the HBox within the StackPane using alignment (top-right)
		StackPane.setAlignment(buttonContainer, Pos.TOP_RIGHT);
		StackPane.setMargin(buttonContainer, new Insets(10, 10, 0, WIDTH)); // Adjust top and right padding

		// Add children to the layeredPane
		layeredPane.getChildren().addAll(root, pauseOverlay, buttonContainer);

		pauseButton.setOnAction(event -> togglePause());

		muteButton.setOnAction(event -> {
			// Mute logic
			if (muteView.equals(muteButton.getGraphic())) {
				muteMusic();
				muteButton.setGraphic(unmuteView);
			} else {
				unmuteMusic();
				muteButton.setGraphic(muteView);
			}
			canvas.requestFocus();
		});

		primaryStage.show();
		canvas.requestFocus();
	}

	public void applySelectedBackground() {
		/**
		 * Applies selected background image to the game scene.
		 * loads a default background if none is selected.
		 */
		String backgroundPath = Settings.getSelectedBackgroundPath();
		// Check if a background has been selected; if not, set a default background
		if (backgroundPath == null || backgroundPath.isEmpty()) {
			backgroundPath = "/UI-background.png"; //
		}
		Image backgroundImage = new Image(getClass().getResource(backgroundPath).toExternalForm());
		ImageView backgroundView = new ImageView(backgroundImage);
		backgroundView.setFitWidth(WIDTH);
		backgroundView.setFitHeight(HEIGHT);
		root.getChildren().add(0, backgroundView); // Add as the first child of root

	}

	private void startGameLoop(GraphicsContext gc) {
		gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				// Clear the canvas
				gc.clearRect(0, 0, WIDTH, HEIGHT);

				// Move the paddle and draw it
				if (paddle != null) {
					paddle.move();
					paddle.draw(gc);
				}

				// Move the snake and check for collisions
				snake.move(now);
				snake.draw(gc);

				// Draw the food
				food.draw(gc);

				// Draw the score
				drawScore(gc);

				// Check if the snake is out of bounds or if the game is over
				if (snake.isOutOfBounds() || snake.isGameOver()) {
					stop(); // Stop the AnimationTimer
					showGameOverMenu(); // Show game over menu
				}else {
					// Game continues
					checkForFoodConsumption();
					gc.clearRect(0, 0, WIDTH, HEIGHT);
					if (paddle != null) {
						paddle.move();
						paddle.draw(gc);
					}
					snake.draw(gc);
					food.draw(gc);
					drawScore(gc); // Draw the score
				}
				// Optionally: Check for collisions between the snake and the paddle
				if (snake.checkCollisionWithPaddle(paddle)) {
					stop(); // Stop the AnimationTimer
					showGameOverMenu();
				}
			}
		};
		gameLoop.start();
	}


	private void initializeGame(Canvas canvas) {
		/**
		 * Initialises game settings
		 * Sets up the game components with the snake body and head and all the food items.
		 *Shows the different setting for the snake speed.
		 *
		 * @param canvas The canvas element on which the game is drawn.
		 */
		// Initialize  snake and food
		int speedLevel = Settings.getSelectedSpeedLevel();
		long speed;// = 100_000_000;
		switch (speedLevel) {
			case 2: speed = 50_000_000; break;
			case 3: speed = 25_000_000; break;
			case 1:
			default: speed =100_000_000; break;
		}

		snake = new model.Snake("snake-head-right", "snake-body", 5, 5, 3, 20, speed);

		List<String> foodImageKeys = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16");
		food = new model.Food(foodImageKeys); // Initialize food with multiple images

		// Initialize the paddle
		double paddleWidth = 100; // Example width
		double paddleHeight = 20; // Example height
		double gameWidth = WIDTH; // Use the game's width for the paddle's movement range

		// Create a new Paddle instance with the specified dimensions and game width
		paddle = new Paddle((int)paddleWidth, (int)paddleHeight, gameWidth);



	}

	private void togglePause() {
		if (gameLoop != null) {
			if (!pauseOverlay.isVisible()) {
				gameLoop.stop(); // Stop the game loop
				pauseOverlay.setVisible(true); // Show the overlay
				pauseMusic(); // Assuming this method is defined to pause the music
			} else {
				gameLoop.start(); // Start the game loop
				pauseOverlay.setVisible(false); // Hide the overlay
				resumeMusic(); // Assuming this method is defined to resume the music
			}
		}
		canvas.requestFocus(); // Request focus back to the canvas if needed
	}

	//starts the music when the game starts
	private void startMusic() {
		/**
		 * Starts music when the game starts
		 */
		String musicFile = "src/main/resources/frogger.mp3";
		Media sound = new Media(new File(musicFile).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // loop indefinitely
		mediaPlayer.play();
	}

	//stops music
	private void stopMusic() {
		/**
		 * Stops music when the game ends, user goes to main menu or exits the game.
		 */
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}
	//pauses the music
	private void pauseMusic() {
		/**
		 * Pauses the music when the user presses pause
		 *
		 */
		if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
			mediaPlayer.pause();
		}
	}
	// resumes the music
	private void resumeMusic() {
		/**
		 * Resumes the music when the user resumes the game.
		 */
		if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
			mediaPlayer.play();
		}
	}
	//mutes the music
	private void muteMusic() {
		/**
		 * Mutes the music when user presses mute
		 */
		if (mediaPlayer != null) {
			mediaPlayer.setMute(true);
		}
	}
	//music is no longer muted
	private void unmuteMusic() {
		/**
		 * Plays the music when the user presses the button
		 */
		if (mediaPlayer != null) {
			mediaPlayer.setMute(false);
		}
	}

	private void initializeDeathSound() {
		try {
			String deathSoundPath = getClass().getResource("/death.mp3").toExternalForm();
			Media deathSound = new Media(deathSoundPath);
			deathSoundPlayer = new MediaPlayer(deathSound);
		} catch (NullPointerException e) {
			System.out.println("Error loading death sound.");
			// Handle exception, e.g., sound file not found
		}
	}

	private void playDeathSound() {
		if (deathSoundPlayer != null) {
			deathSoundPlayer.stop(); // Stop the sound if it's already playing
			deathSoundPlayer.play();
		}
	}

	/**
	 * Event Handler for game scene used for handling the snake direction.
	 * @param scene which is the main scene of the snake game.
	 */
	private void setupEventHandlers(Scene scene) {
		scene.setOnKeyPressed(event -> {
			switch (event.getCode()) {
				case UP:
					snake.changeDirection(Direction.UP);
					break;
				case DOWN:
					snake.changeDirection(Direction.DOWN);
					break;
				case LEFT:
					snake.changeDirection(Direction.LEFT);
					break;
				case RIGHT:
					snake.changeDirection(Direction.RIGHT);
					break;
			}
		});
	}

	/**
	 * Handles the snake food consumption.
	 * If snake consumes food, snake grows in size, score increases by 10 and new food item is relocated on map
	 */
	private void checkForFoodConsumption() {
		if (snake.isCollidingWithFood(food)) {
			snake.grow();//Snake grows in size
			snake.increaseScore(10);//If the food is eaten by the snake score increases
			food.relocateAndChangeImage();// New food item generated and relocated elsewhere
		}
	}

	/**
	 * This method draws the current score onto the game canvas.
	 * @param gc which is the GraphicsContext for drawing on the canvas.
	 */
	private void drawScore(GraphicsContext gc) {
		gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));
		gc.setFill(Color.WHITE);
		gc.fillText("SCORE : " + snake.getScore(), 20, 40);
	}
	/**
	 * Displays pause menu options.
	 * This includes buttons to resume, restart, or return to the main menu.
	 */
	private void showPauseOptions() {
		pauseMusic();
		// menu options when pause button is pressed
		VBox pauseMenu = new VBox(10);
		pauseMenu.getStyleClass().add("menu-overlay");//Design of the menu

		Button resumeButton = new Button("Resume");
		resumeButton.getStyleClass().add("menu-button");//Design of the button

		Button restartButton = new Button("Restart");
		restartButton.getStyleClass().add("menu-button");//Design of the button

		Button mainMenuButton = new Button("Main Menu");
		mainMenuButton.getStyleClass().add("menu-button");//Design of the button

		pauseMenu.getChildren().addAll(resumeButton, restartButton, mainMenuButton);
		pauseMenu.setLayoutX(WIDTH / 2 - 50);//position of pause button
		pauseMenu.setLayoutY(HEIGHT / 2 - 50);//position of pause button
		root.getChildren().add(pauseMenu);

		// Implement button functionalities
		resumeButton.setOnAction(event -> {
			root.getChildren().remove(pauseMenu);
			gameLoop.start();
			resumeMusic();
			canvas.requestFocus();

		});
		//Restart button functionality
		restartButton.setOnAction(event -> {
			root.getChildren().remove(pauseMenu);
			restartGame();
		});
		//Main menu button functionality
		mainMenuButton.setOnAction(event -> {
			GameScreenFX.showMainMenu();
		});
	}

	/**
	 * Method to restart the game.
	 * Starts by stopping the music and clearing canvas
	 *  Game is reinitialized reintroduces all the game elements.
	 */
	private void restartGame() {
		stopMusic();
		// Clear all elements from the canvas
		root.getChildren().clear();
		applySelectedBackground();
		canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);


		// Game components are being re-initialized
		initializeGame(canvas);

		setupEventHandlers(scene); // Need to make sure that scene is being accessed

		// Ensure the canvas has focus
		canvas.requestFocus();

		// Restart the game loop
		startGameLoop(canvas.getGraphicsContext2D());
		Button pauseButton = new Button();//Pause button
		pauseButton.setGraphic(pauseView);
		pauseButton.setLayoutX(WIDTH - 50);
		pauseButton.setLayoutY(10);

		Button muteButton = new Button();//mute button
		muteButton.setGraphic(muteView);
		muteButton.setLayoutX(WIDTH - 100); // position next to the pause button
		muteButton.setLayoutY(10);

		root.getChildren().addAll(pauseButton, muteButton);

		// Event handler for the pause button
		pauseButton.setOnAction(event -> {
			if (gameLoop != null) {
				gameLoop.stop();
			}
			showPauseOptions();
		});
		muteButton.setOnAction(event -> {
			// Mute logic
			if (muteView.equals(muteButton.getGraphic())) {
				muteMusic();
				muteButton.setGraphic(unmuteView);
			} else {
				unmuteMusic();
				muteButton.setGraphic(muteView);
			}
			canvas.requestFocus();
		});

		startMusic();
	}
	/**
	 * Shows game over menu.
	 * Options to restart the game, return to the main menu, or exit the game.
	 */
	private void showGameOverMenu() {
		VBox gameOverMenu = new VBox(10);
		gameOverMenu.getStyleClass().add("menu-overlay");//Css design
		gameOverMenu.setAlignment(Pos.CENTER);

		Button restartButton = new Button("Restart Game");
		restartButton.getStyleClass().add("menu-button");//Css design
		Button mainMenuButton = new Button("Main Menu");
		mainMenuButton.getStyleClass().add("menu-button");//Css design
		Button exitButton = new Button("Exit Game");
		exitButton.getStyleClass().add("menu-button");//Css design

		gameOverMenu.getChildren().addAll(restartButton, mainMenuButton, exitButton);
		gameOverMenu.setLayoutX(WIDTH / 2 - 50); //Position of button
		gameOverMenu.setLayoutY(HEIGHT / 2 - 50);

		// Event handler for the restart button
		restartButton.setOnAction(event -> {
			restartGame();
		});
		// Event handler for the main menu button
		mainMenuButton.setOnAction(event -> {
			GameScreenFX.showMainMenu();
		});
		// Event handler for the exit button
		exitButton.setOnAction(event -> {
			Platform.exit();
		});

		root.getChildren().add(gameOverMenu);
		endGame(playerName);
	}
	/**
	 * Handles game over function.
	 * Prompts player to enter their name so their score can be put onto the leaderboard, shows the leaderboard.
	 */
	private void endGame(String playerName) {
		playDeathSound();
		stopMusic();
		Platform.runLater(() -> {
			final String finalPlayerName; // Declare a final variable to use in lambda
			if (playerName != null && !playerName.trim().isEmpty()) {
				finalPlayerName = playerName; // Use the provided name if it's valid
			} else {
				finalPlayerName = "Anonymous"; // Use a default name if none was provided
			}

			int currentScore = snake.getScore(); // Assuming you have a way to get the current score
			Leaderboard.saveHighScore(finalPlayerName, currentScore); // Update leaderboard with player name and score
			showLeaderboard(); // Shows the leaderboard
		});
	}
	/**
	 * Displays the leaderboard in a new stage.
	 * Shows a list of high scores and associated player names.
	 */
	private void showLeaderboard() {
		//New stage made for leaderboard
		Stage leaderboardStage = new Stage();
		leaderboardStage.setTitle("Leaderboard");

		StackPane layout = new StackPane();

		if (layout == null) {
			System.out.println("Layout is null. Please check your FXML file.");
			return;
		}

		// Load the background image
		Image backgroundImage = new Image(getClass().getResource("/endGame.jpg").toExternalForm());
		BackgroundImage bgImage = new BackgroundImage(backgroundImage,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
		layout.setBackground(new Background(bgImage));
		if (backgroundImage.isError()) {
			System.out.println("Error loading background image.");
			return;
		}

		// Title
		Text title = new Text("Leaderboard:");
		title.getStyleClass().add("titleLabel"); // Applying title style
		StackPane.setAlignment(title, Pos.TOP_CENTER);

		// List for displaying scores
		VBox scoreList = new VBox(10);
		scoreList.setAlignment(Pos.CENTER);
		for (Leaderboard record : Leaderboard.getHighScores()) {
			Text scoreText = new Text(record.getName() + ": " + record.getScore());
			scoreText.getStyleClass().add("scoreText"); // Applying score style
			scoreList.getChildren().add(scoreText);
		}

		// Adding components to layout, puts the title and score list on leaderboard stage
		layout.getChildren().addAll(title, scoreList);
		// Scene setup
		Scene scene = new Scene(layout,870, 560); // Size of the leaderboard
		// CSS Style for Leaderboard
		scene.getStylesheets().add(getClass().getResource("/leaderboard.css").toExternalForm());
		leaderboardStage.setScene(scene);
		leaderboardStage.show(); // Display the leaderboard
		//showGameOverMenu();
	}
	/**
	 * Called when the application stops.
	 * Stops the background music and handles any necessary cleanup.
	 */

	@Override
	public void stop() {
		// This is called when the application is closed
		stopMusic(); // Ensure music is stopped when application exits
	}
	/**
	 * The main entry point for the application.
	 * Launches the JavaFX application.
	 *
	 * @param args Command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		launch(args); // Starts the JavaFX application
	}

}


