package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
/**
 * Controller class for the settings screen.
 * Manages user interactions with the settings, such as background and speed selection.
 */
public class Settings {

    // Scene Buttons
    @FXML
    private ImageView backgroundImage;
    @FXML
    private Button leftArrowButton;
    @FXML
    private Button rightArrowButton;
    @FXML
    private Button backButton;
    @FXML
    private Button selectButton;
    @FXML
    private Label chooseBackgroundTitle;

    @FXML
    private Label speedLabel;
    @FXML
    private Button leftSpeedArrowButton;
    @FXML
    private Button rightSpeedArrowButton;
    @FXML
    private Button selectSpeedButton;

    private static int selectedSpeedLevel = 1; // Default speed that the game will be played in
    private static String selectedBackgroundPath = "/UI-background.png";
    private int currentSpeedIndex = 0; // Tracks the speed level
    private int currentBackgroundIndex = 0; // Tracks the background

    private List<String> backgroundImages = Arrays.asList("/UI-background.png", "/UI-background2.png", "/UI-background3.png");
    private List<Integer> speedLevels = Arrays.asList(1, 2, 3); // Speed levels

    // Getters for speed and background
    public static int getSelectedSpeedLevel() {
        return selectedSpeedLevel;
    }

    public static String getSelectedBackgroundPath() {
        return selectedBackgroundPath;
    }

    // Selects the current background based on the user's choice
    @FXML
    private void selectBackground() {
        selectedBackgroundPath = backgroundImages.get(currentBackgroundIndex);
    }

    // Selects the current speed level based on the user's choice
    @FXML
    private void selectSpeedLevel() {
        selectedSpeedLevel = speedLevels.get(currentSpeedIndex);
        speedLabel.setText("Speed: " + selectedSpeedLevel); // Display selected speed
    }

    // Initializes the controller and manages the event handler
    @FXML
    public void initialize() {
        chooseBackgroundTitle.setText("Choose Background");
        chooseBackgroundTitle.getStyleClass().add("background-title");

        updateBackground();
        updateSpeedLevelDisplay();

        // Background selection handlers
        leftArrowButton.setOnAction(event -> navigateBackgrounds(-1));
        rightArrowButton.setOnAction(event -> navigateBackgrounds(1));
        selectButton.setOnAction(event -> selectBackground());
        backButton.setOnAction(event -> goBackToMainMenu());

        // Speed selection handlers
        leftSpeedArrowButton.setOnAction(event -> navigateSpeedLevels(-1));
        rightSpeedArrowButton.setOnAction(event -> navigateSpeedLevels(1));
        selectSpeedButton.setOnAction(event -> selectSpeedLevel());
    }

    // Navigate through the backgrounds
    private void navigateBackgrounds(int direction) {
        currentBackgroundIndex = (currentBackgroundIndex + direction + backgroundImages.size()) % backgroundImages.size();
        updateBackground();
    }

    // Navigate through speed levels
    private void navigateSpeedLevels(int direction) {
        currentSpeedIndex = (currentSpeedIndex + direction + speedLevels.size()) % speedLevels.size();
        updateSpeedLevelDisplay();
    }

    // Updates the speed level display
    private void updateSpeedLevelDisplay() {
        speedLabel.setText("Speed: " + speedLevels.get(currentSpeedIndex));
    }

    // Updates the background
    private void updateBackground() {
        String imagePath = backgroundImages.get(currentBackgroundIndex);
        URL imageUrl = getClass().getResource(imagePath);

        if (imageUrl == null) {
            System.out.println("Image not found: " + imagePath);
            return;
        }

        try {
            Image image = new Image(imageUrl.toExternalForm());
            backgroundImage.setImage(image);
            System.out.println("Loaded image: " + imagePath); // Debugging
        } catch (Exception e) {
            System.out.println("Failed to load image: " + imagePath); // Debugging
            e.printStackTrace();
        }
    }

    // Navigate back to the main menu
    private void goBackToMainMenu() {
        GameScreenFX.showMainMenu();
    }
}

