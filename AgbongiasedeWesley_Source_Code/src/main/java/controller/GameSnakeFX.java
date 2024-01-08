package controller;

import view.Leaderboard;
import javafx.application.Application;
import javafx.geometry.Insets;
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
import model.Food;
import model.Levels;
import model.Paddle;
import view.MusicPlayer;

/**
 * @Project Snake Game
 * @Description Main class for the snake game application. Sets up the stage and scene for the game, handles user interactions, and controls the game states.
 * @Author Wesley Agbongiasede - modified
 * @version 1.0
 */
public class GameSnakeFX extends Application {
	private static final int WIDTH = 870;
	private static final int HEIGHT = 560;
	private model.Snake snake;
	private model.Food food;
	private AnimationTimer gameLoop;
	private Pane root; // Declare root at the class level
	private Canvas canvas;
	private Scene scene;
	private MusicPlayer musicPlayer;
	private final String playerName;
	private Stage primaryStage;
	private StackPane layeredPane;
	private Rectangle pauseOverlay;
	private model.Paddle paddle;
	private Levels levels;
	Image muteImage = new Image(getClass().getResource("/sound.png").toExternalForm());
	ImageView muteView = new ImageView(muteImage);
	Image unmuteImage = new Image(getClass().getResource("/nosound.png").toExternalForm());
	ImageView unmuteView = new ImageView(unmuteImage);
	Image pauseImage = new Image(getClass().getResource("/pause.png").toExternalForm());
	ImageView pauseView = new ImageView(pauseImage);

	/**
	 * Represents a game instance of the Snake game implemented using JavaFX.
	 *
	 * Usage example:
	 * GameSnakeFX game = new GameSnakeFX(playerName);
	 * Stage primaryStage = GameScreenFX.getPrimaryStage();
	 * game.start(primaryStage);
	 * game.applySelectedBackground();
	 */
	public GameSnakeFX(String playerName) {
		this.playerName = playerName; // Initialize player name
	}

	/**
	 * The main entry point for the application.
	 * Launches the JavaFX application.
	 *
	 * @param primaryStage The primary stage of the JavaFX application.
	 */
	@Override
	public void start(Stage primaryStage) {
		// Create the root pane
		root = new Pane();
		layeredPane = new StackPane();
		this.primaryStage = primaryStage;
		this.musicPlayer = new MusicPlayer();

		//Applies the chosen background to the game scene
		applySelectedBackground();

		// Create and add the canvas for drawing game elements
		canvas = new Canvas(WIDTH, HEIGHT);
		canvas.setFocusTraversable(true);
		root.getChildren().add(canvas); // Add canvas over the background image

		// Initialize the pauseOverlay but don't add to layeredPane yet
		pauseOverlay = new Rectangle(WIDTH, HEIGHT, new Color(0, 0, 0, 0.5)); // Semi-transparent black
		pauseOverlay.setVisible(false); // Initially not visible

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
		musicPlayer.startMusic();
		// Initializes game components and event handlers
		initializeGame(canvas);
		setupEventHandlers(scene);

		// Initialize the death sound
		musicPlayer.initializeDeathSound();

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
				musicPlayer.muteMusic();
				muteButton.setGraphic(unmuteView);
			} else {
				musicPlayer.unmuteMusic();
				muteButton.setGraphic(muteView);
			}
			canvas.requestFocus();
		});

		primaryStage.show();
		canvas.requestFocus();
	}

	/**
	 * Applies selected background image to the game scene.
	 * If no background is selected, a default background is loaded.
	 */
	public void applySelectedBackground() {
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

	/**
	 * Starts the game loop that continuously updates and renders the game.
	 * This method is responsible for moving and drawing the paddles, moving and checking collisions of the snake, handling food consumption, and updating the score and level.
	 *
	 * @param gc The GraphicsContext used for drawing on the canvas.
	 */
	private void startGameLoop(GraphicsContext gc) {
		gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				// Clear the canvas
				gc.clearRect(0, 0, WIDTH, HEIGHT);

				// Move and draw paddles
				for (Paddle paddle : levels.getPaddles()) {
					if (snake.checkCollisionWithPaddle(paddle)) {
						stop(); // Stop the AnimationTimer
						showGameOverMenu();
					}
					paddle.move();
					paddle.draw(gc);
				}

				// Move the snake and check for collisions
				snake.move(now);
				snake.draw(gc);

				// Draw foods
				for (Food food : levels.getFoods()) {
					if (snake.isCollidingWithFood(food)) {
						snake.grow(); // Snake grows in size
						snake.increaseScore(10); // Increase score
						food.relocateAndChangeImage(); // Relocate and change the food image
					}
					food.draw(gc); // Assuming Food class has draw method
				}

				// Draw the score
				drawScore(gc);

				// Check if the snake is out of bounds or if the game is over
				if (snake.isOutOfBounds() || snake.isGameOver()) {
					stop(); // Stop the AnimationTimer
					showGameOverMenu(); // Show game over menu
				}else {
					// Game continues
					checkForFoodConsumption();
					// After checking for food consumption or score update
					levels.updateLevel(snake.getScore());
					gc.clearRect(0, 0, WIDTH, HEIGHT);
					// Move and draw paddles
					for (Paddle paddle : levels.getPaddles()) {
						if (snake.checkCollisionWithPaddle(paddle)) {
							stop(); // Stop the AnimationTimer
							showGameOverMenu();
						}
						paddle.move();
						paddle.draw(gc);
					}
					snake.draw(gc);
					// Draw foods
					for (Food food : levels.getFoods()) {
						if (snake.isCollidingWithFood(food)) {
							snake.grow(); // Snake grows in size
							snake.increaseScore(10); // Increase score
							food.relocateAndChangeImage(); // Relocate and change the food image
						}
						food.draw(gc); // Assuming Food class has draw method
					}
					drawScore(gc); // Draw the score
				}
				if (snake.checkCollisionWithPaddle(paddle)) {
					stop(); // Stop the AnimationTimer
					showGameOverMenu();
				}
			}
		};
		gameLoop.start();
	}

	/**
	 * Initializes the game settings and components.
	 * Sets up the snake body and head, the food items, and the paddle.
	 * Shows the different settings for the snake speed.
	 *
	 * @param canvas The canvas element on which the game is drawn.
	 */
	private void initializeGame(Canvas canvas) {
		// Initialize  snake and food
		int speedLevel = Settings.getSelectedSpeedLevel();
		long speed = determineSpeedBasedOnLevel(speedLevel);

		// Initialize the snake with the sprite sheet path
		String spriteSheetPath = "/snake-graphics.png"; // The path to your sprite sheet
		int initialX = 5; // Initial X position of the snake
		int initialY = 5; // Initial Y position of the snake
		int initialSize = 3; // Initial size of the snake
		int segmentSize = 20; // Size of each segment of the snak

		snake = new model.Snake(spriteSheetPath, initialX, initialY, initialSize, segmentSize, speed);

		List<String> foodImageKeys = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16");
		food = new model.Food(foodImageKeys); // Initialize food with multiple images

		// Initialize the paddle
		double paddleWidth = 100; // Example width
		double paddleHeight = 20; // Example height
		double gameWidth = WIDTH; // Use the game's width for the paddle's movement range

		// Create a new Paddle instance with the specified dimensions and game width
		paddle = new Paddle((int)paddleWidth, (int)paddleHeight, gameWidth);

		levels = new Levels(foodImageKeys);
		levels.addInitialElements(paddle, food);

	}

	/**
	 * Determines the speed of the game based on the level.
	 *
	 * @param level The level of the game. It can be 1, 2, or 3.
	 * @return The speed of the game in nanoseconds.
	 */
	private long determineSpeedBasedOnLevel(int level) {
		switch (level) {
			case 2: return 50_000_000;
			case 3: return 25_000_000;
			case 1:
			default: return 100_000_000;
		}
	}

	/**
	 * Toggles the pause state of the game. If the game is not paused, it will pause the game by stopping the game loop, showing the pause overlay, and pausing the music. If the game
	 * is already paused, it will resume the game by starting the game loop, hiding the pause overlay, and resuming the music.
	 */
	private void togglePause() {
		if (gameLoop != null) {
			if (!pauseOverlay.isVisible()) {
				gameLoop.stop(); // Stop the game loop
				pauseOverlay.setVisible(true); // Show the overlay
				musicPlayer.pauseMusic(); // Assuming this method is defined to pause the music
			} else {
				gameLoop.start(); // Start the game loop
				pauseOverlay.setVisible(false); // Hide the overlay
				musicPlayer.resumeMusic(); // Assuming this method is defined to resume the music
			}
		}
		canvas.requestFocus(); // Request focus back to the canvas if needed
	}

	/**
	 * Sets up the event handlers for the given scene.
	 *
	 * @param scene The scene for which event handlers are set up.
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
	 * Checks if the snake is colliding with food. If a collision occurs, the snake grows in size, the score increases by 10, and a new food item is generated and relocated elsewhere
	 *.
	 */
	private void checkForFoodConsumption() {
		if (snake.isCollidingWithFood(food)) {
			snake.grow();//Snake grows in size
			snake.increaseScore(10);//If the food is eaten by the snake score increases
			food.relocateAndChangeImage();// New food item generated and relocated elsewhere
		}
	}

	/**
	 * Draws the score on the canvas.
	 *
	 * @param gc The GraphicsContext used for drawing on the canvas.
	 */
	private void drawScore(GraphicsContext gc) {
		gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));
		gc.setFill(Color.WHITE);
		gc.fillText("SCORE : " + snake.getScore(), 20, 40);
	}

	/**
	 * Displays the pause options menu when the pause button is pressed.
	 * The menu includes options to resume the game, restart the game, and return to the main menu.
	 */
	private void showPauseOptions() {
		musicPlayer.pauseMusic();
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
			musicPlayer.resumeMusic();
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
	 * Restarts the game by clearing the canvas, re-initializing game components,
	 * setting up event handlers, starting the game loop, and adding the pause and mute buttons.
	 * It also starts the background music.
	 */
	private void restartGame() {
		musicPlayer.stopMusic();
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
				musicPlayer.muteMusic();
				muteButton.setGraphic(unmuteView);
			} else {
				musicPlayer.unmuteMusic();
				muteButton.setGraphic(muteView);
			}
			canvas.requestFocus();
		});

		musicPlayer.startMusic();
	}

	/**
	 * Displays the game over menu when the game ends.
	 * The menu includes options to restart the game, go to the main menu, and exit the game.
	 * It also prompts the player to enter their name to record their score on the leaderboard.
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
	 * Ends the game by performing the necessary actions such as playing the death sound, stopping the music, saving the player's score to the leaderboard, and displaying the leaderboard
	 *.
	 *
	 * @param playerName The name of the player. If null or empty, a default name "Anonymous" will be used.
	 */
	private void endGame(String playerName) {
		musicPlayer.playDeathSound();
		musicPlayer.stopMusic();
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
	 * Shows the leaderboard window.
	 * This method creates a new stage for the leaderboard and sets its title.
	 * It then loads the leaderboard layout and sets it as the background of the stage.
	 * The leaderboard scores are retrieved from the Leaderboard class and displayed in a VBox.
	 * A scene is created with the leaderboard layout and set to the stage.
	 * The CSS style for the leaderboard is applied to the scene.
	 * Finally, the stage is shown to display the leaderboard.
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
	 * Stops the music when the game ends, user goes to main menu or exits the game.
	 */
	@Override
	public void stop() {
		// This is called when the application is closed
		musicPlayer.stopMusic(); // Ensure music is stopped when application exits
	}

	/**
	 * The main entry point for the application. Launches the JavaFX application.
	 *
	 * @param args The command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		launch(args); // Starts the JavaFX application
	}

}


