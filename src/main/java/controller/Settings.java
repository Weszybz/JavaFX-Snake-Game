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
 * Controller class for the settings screen.
 * Manages user interactions with the settings, such as background and speed selection.
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

    @FXML
    private void handleConfirmAction() {
        // Handle the background selection
        selectBackground();

        // Handle the speed selection
        selectSpeedLevel();

        // Code to close the settings window or transition to the main menu
        goBackToMainMenu(); // or another appropriate method
    }

    // Initializes the controller and manages the event handler
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
            //System.out.println("Loaded image: " + imagePath); // Debugging
        } catch (Exception e) {
            //System.out.println("Failed to load image: " + imagePath); // Debugging
            e.printStackTrace();
        }
    }

    // Navigate back to the main menu
    private void goBackToMainMenu() {
        GameScreenFX.showMainMenu();
    }
}

