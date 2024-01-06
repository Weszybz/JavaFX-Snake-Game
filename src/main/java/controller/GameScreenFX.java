package controller;

import com.sun.javafx.charts.Legend;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * This class is responsible for starting the application and loading the main menu.
 */
public class GameScreenFX extends javafx.application.Application {

    private static Stage primaryStage;

    /**
     * Starts application from main menu.
     * Loads main menu FXML, applies CSS styling, sets up the primary stage.
     *
     * @param stage Primary stage for this application.
     * @throws Exception If FXML loading fails.
     */
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));//Loads FXML file
        Parent root = loader.load();

        Scene scene = new Scene(root, 870, 560);
        scene.getStylesheets().add(getClass().getResource("/menu.css").toExternalForm());

        // Sets scene on the stage
        stage.setScene(scene);
        stage.setTitle("Main Menu");

        // Access controller for scene transitions
        GameFrameFX controller = loader.getController();
        controller.setMainStage(primaryStage);
        stage.setResizable(false);// Disable resizing
        stage.setMaximized(false);// Minimise window

        primaryStage.setScene(scene);// Set scene on stage
        primaryStage.show();//Shows stage

    }
    /**
     * Returns primary stage of application.
     * Allows the application to access primary stage.
     * @return The primary stage of the application.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;//Accesses main menu from inside the game
    }
    /**
     * Switches scene back to main menu.
     * Loads main menu FXML, applies css styling.
     * Handles IOExceptions.
     */
    public static void showMainMenu() {
        try {
            //FXML File loader
            FXMLLoader loader = new FXMLLoader(GameScreenFX.class.getResource("/MainMenu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 870, 560);
            scene.getStylesheets().add(GameScreenFX.class.getResource("/menu.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Main entry point for the application.
     * Launches the JavaFX application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch();
    }
}

