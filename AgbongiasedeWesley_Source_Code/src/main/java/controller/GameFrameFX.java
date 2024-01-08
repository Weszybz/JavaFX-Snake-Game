package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.Leaderboard;

import java.io.IOException;

/**
 * @Project Launch MainMenu and Game
 * @Description The controller for the main game menu. Manages interactions, initializes components, and handles navigation between game parts.
 * @Author Wesley Agbongiasede - modified
 * @version 1.0
 */
public class GameFrameFX {
    @FXML
    private BorderPane rootPane; // root pane background image.
    @FXML
    private Button startGameButton;
    @FXML
    private Button exitGameButton;
    @FXML
    private Button leaderboardButton;
    @FXML
    private Button showOptionsButton;
    @FXML
    private Button helpButton;
    @FXML
    private TextField playerNameInput;
    private Stage stage; // primary stage

    /**
     * Sets the main stage of the application.
     *
     * @param stage The main stage of the application.
     */
    public void setMainStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It sets up action handlers for menu buttons
     * and initializes the background and other UI components of the main menu.
     */
    public void initialize() {
        startGameButton.setOnAction(event -> startGame());
        leaderboardButton.setOnAction(event -> showLeaderboard());
        showOptionsButton.setOnAction(event -> showOptions());
        exitGameButton.setOnAction(event -> exitGame());
        helpButton.setOnAction(event -> showHelp());

        Image leaderboardImage = new Image(getClass().getResource("/leaderboard.png").toExternalForm());
        ImageView leaderboardImageView = new ImageView(leaderboardImage);

        Image SettingImage = new Image(getClass().getResource("/settings.png").toExternalForm());
        ImageView SettingImageView = new ImageView(SettingImage);

        Image helpImage = new Image(getClass().getResource("/help.png").toExternalForm());
        ImageView helpView = new ImageView(helpImage);

        // Sets background image
        Image backgroundImage = new Image(getClass().getResource("/UI-background.png").toExternalForm());
        BackgroundImage bgImage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));

        // Set the background on the root pane
        rootPane.setBackground(new Background(bgImage));
        leaderboardButton.setGraphic(leaderboardImageView);
        showOptionsButton.setGraphic(SettingImageView);
        helpButton.setGraphic(helpView);
    }
    @FXML
    /**
     * Displays the leaderboard in a new stage.
     * The leaderboard shows high scores and player rankings.
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
    }
    /**
     * Starts the game by initializing the game stage and setting up necessary configurations.
     * Reads the player's name and passes it to the game stage.
     */
    @FXML
    private void startGame() { try {
        final String currentName = playerNameInput.getText(); // Copy to a final variable
        GameSnakeFX game = new GameSnakeFX(currentName); // Pass player name to the game stage
        Stage primaryStage = GameScreenFX.getPrimaryStage();
        game.start(primaryStage); // Start the game using the existing stage.
        game.applySelectedBackground(); // Apply the selected background
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
    /**
     * Displays the options screen for the game. Allows players to adjust settings like
     * difficulty and sound. Loads the options FXML file and sets it on the current stage.
     */
    @FXML
    private void showOptions() {
        try {
            // Load options FXML or creates the scene here
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Settings.fxml"));
            Parent optionsRoot = loader.load();

            // Set the scene on the current stage
            Scene optionsScene = new Scene(optionsRoot, 870, 560);
            Stage stage = GameScreenFX.getPrimaryStage();
            stage.setScene(optionsScene);
            // Load CSS
            optionsScene.getStylesheets().add(getClass().getResource("/Settings.css").toExternalForm()); //

            // Shows the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Displays help information in a new stage. Includes game rules,
     * controls, and objectives to assist players in understanding how to play the game.
     */
    @FXML
    private void showHelp() {
        Stage helpStage = new Stage();
        helpStage.setTitle("Game Help");

        // Instructions text or content
        Text instructions = new Text(
                "Objective:\n" +
                        "Eat food to grow. Avoid walls, your tail, and paddles.\n\n" +
                        "Controls:\n" +
                        "- Arrow Keys: Move snake.\n\n" +
                        "Rules:\n" +
                        "- Eating food grows the snake and scores points.\n" +
                        "- Game ends by hitting walls, snake's tail, or paddles.\n" +
                        "- New paddles appear every 50 points; new food every 30 points.\n\n" +
                        "Tips:\n" +
                        "- Plan your moves.\n" +
                        "- Watch for new obstacles.\n\n" +
                        "Scoring:\n" +
                        "Points increase with food eaten.\n\n" +
                        "Pause/Resume:\n" +
                        "Pause and resume anytime with the 'Pause' button."
        );


        // Optionally, style the instructions or add more components as needed
        instructions.getStyleClass().add("help-text");

        // Create layout and add the instructions
        VBox layout = new VBox(10);
        layout.getChildren().add(instructions);
        layout.setAlignment(Pos.CENTER);

        // Create scene and set the stage
        Scene scene = new Scene(layout, 500, 400); // Adjust size as needed
        helpStage.setScene(scene);

        helpStage.show(); // Display the help window
    }
    /**
     * Closes the game application. Ensures a graceful shutdown of the application
     * by closing the primary stage.
     */
    @FXML
    private void exitGame() {

        // Close the application
        Stage primaryStage = GameScreenFX.getPrimaryStage();
        if (primaryStage != null) {
            primaryStage.close();
        }
    }
}


