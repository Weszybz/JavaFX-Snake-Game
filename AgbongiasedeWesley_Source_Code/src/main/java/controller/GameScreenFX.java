package controller;

import com.sun.javafx.charts.Legend;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * @Project Loading MainMenu
 * @Description This class is responsible for starting the JavaFX application and loading the main menu.
 * It sets up the primary stage, applies CSS styling, and switches scenes.
 * @Author Wesley Agbongiasede - modified
 * @version 1.0
 */
public class GameScreenFX extends javafx.application.Application {

    private static Stage primaryStage;

    /**
     * Starts the JavaFX application and loads the main menu.
     * It sets up the primary stage, applies CSS styling, and switches scenes.
     *
     * @param stage The primary stage of the application.
     * @throws Exception If an error occurs during the application start.
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
     * Returns the primary stage of the application.
     * This allows different parts of the application to access and modify the primary stage.
     *
     * @return The primary stage of the application.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;//Accesses main menu from inside the game
    }
    /**
     * Switches the scene back to the main menu.
     * It loads the main menu FXML file and applies the necessary CSS styling.
     * IOExceptions are caught and printed to the stack trace.
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
     * The main entry point for the JavaFX application.
     * It launches the application with the provided command line arguments.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch();
    }
}

