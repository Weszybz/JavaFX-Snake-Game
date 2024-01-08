package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * @Project Settings
 * @Description Controller class for the settings screen. Manages user interactions with the settings, such as background and speed selection.
 * @Author Wesley Agbongiasede
 * @version 1.0
 */
public class Settings {
    @FXML
    private BorderPane rootPane;
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
    private Label chooseBackgroundTitle;
    @FXML
    private Label speedLabel;
    @FXML
    private Slider speedSlider;
    @FXML
    private Button confirmButton;
    private static int selectedSpeedLevel = 1; // Default speed that the game will be played in
    private static String selectedBackgroundPath = "/UI-background.png";
    private int currentSpeedIndex = 0; // Tracks the speed level
    private int currentBackgroundIndex = 0; // Tracks the background
    private final List<String> backgroundImages = Arrays.asList("/UI-background.png", "/UI-background2.png", "/UI-background3.png");
    private final List<Integer> speedLevels = Arrays.asList(1, 2, 3); // Speed levels

    /**
     * Retrieves the selected speed level from the settings.
     *
     * @return The selected speed level.
     */
    public static int getSelectedSpeedLevel() {
        return selectedSpeedLevel;
    }

    /**
     * Retrieves the selected background path from the settings.
     *
     * @return The selected background path.
     */
    public static String getSelectedBackgroundPath() {
        return selectedBackgroundPath;
    }

    /**
     * Updates the selectedBackgroundPath with the path of the selected background image.
     */
    @FXML
    private void selectBackground() {
        selectedBackgroundPath = backgroundImages.get(currentBackgroundIndex);
    }

    /**
     * Selects a speed level from the available speed levels.
     * This method updates the selectedSpeedLevel field with the selected speed level
     * and displays the selected speed level on the speedLabel.
     */
    @FXML
    private void selectSpeedLevel() {
        selectedSpeedLevel = speedLevels.get(currentSpeedIndex);
        speedLabel.setText("Speed: " + selectedSpeedLevel); // Display selected speed
    }

    /**
     * Handles the confirm action in the Settings class.
     * This method is called when the confirmButton is clicked in the settings window.
     * It performs the following actions:
     * 1. Calls the selectBackground method to handle the background selection.
     * 2. Calls the selectSpeedLevel method to handle the speed selection.
     * 3. Calls the goBackToMainMenu method to close the settings window or transition to the main menu.
     */
    @FXML
    private void handleConfirmAction() {
        selectBackground();
        selectSpeedLevel();
        goBackToMainMenu(); // or another appropriate method
    }

    /**
     * Initializes the settings window. This method is called when the settings window is loaded.
     */
    @FXML
    public void initialize() {
        chooseBackgroundTitle.setText("Choose Background");
        chooseBackgroundTitle.getStyleClass().add("background-title");

        Image backImage = new Image(getClass().getResource("/back.png").toExternalForm());
        ImageView backImageView = new ImageView(backImage);

        Image leftImage = new Image(getClass().getResource("/chevron-left.png").toExternalForm());
        ImageView leftImageView = new ImageView(leftImage);

        Image rightImage = new Image(getClass().getResource("/chevron-right.png").toExternalForm());
        ImageView rightImageView = new ImageView(rightImage);

        // Sets background image
        Image backgroundImage = new Image(getClass().getResource("/UI-background.png").toExternalForm());
        BackgroundImage bgImage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));

        // Set the background on the root pane
        rootPane.setBackground(new Background(bgImage));

        updateBackground();
        updateSpeedLevelDisplay();

        // Background selection handlers
        leftArrowButton.setOnAction(event -> navigateBackgrounds(-1));
        leftArrowButton.setGraphic(leftImageView);
        rightArrowButton.setOnAction(event -> navigateBackgrounds(1));
        rightArrowButton.setGraphic(rightImageView);
        backButton.setOnAction(event -> goBackToMainMenu());
        backButton.setGraphic(backImageView);


        // Initialize the slider
        speedSlider.setMin(1);
        speedSlider.setMax(speedLevels.size()); // Assuming speedLevels is a List<Integer>
        speedSlider.setValue(currentSpeedIndex);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setSnapToTicks(true); // Makes the slider snap to the nearest tick
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);

        // Update the speed label and selected speed whenever the slider value changes
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Round the double value to the nearest integer
            currentSpeedIndex = (int) Math.round(newValue.doubleValue()) - 1;
            updateSpeedLevelDisplay(); // Update the display label
            selectSpeedLevel(); // Update the selected speed level
        });

        confirmButton.setOnAction(event -> handleConfirmAction());

    }

    /**
     * Navigates through the background images in the settings.
     *
     * @param direction the direction to navigate (1 for next background, -1 for previous background)
     */
    private void navigateBackgrounds(int direction) {
        currentBackgroundIndex = (currentBackgroundIndex + direction + backgroundImages.size()) % backgroundImages.size();
        updateBackground();
    }

    /**
     * Navigates through the speed levels.
     * The speed level index is updated based on the given direction value.
     *
     * @param direction the direction to navigate (1 for next speed level, -1 for previous speed level)
     */
    private void navigateSpeedLevels(int direction) {
        currentSpeedIndex = (currentSpeedIndex + direction + speedLevels.size()) % speedLevels.size();
        updateSpeedLevelDisplay();
    }

    /**
     * Updates the display of the speed level.
     * This method sets the text of the speedLabel to the selected speed level.
     */
    private void updateSpeedLevelDisplay() {
        speedLabel.setText("Speed: " + speedLevels.get(currentSpeedIndex));
    }

    /**
     * Updates the background image of the settings window.
     * If the image is not found, a message is printed to the console.
     */
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates back to the main menu by switching the scene to the main menu screen.
     */
    private void goBackToMainMenu() {
        GameScreenFX.showMainMenu();
    }
}

