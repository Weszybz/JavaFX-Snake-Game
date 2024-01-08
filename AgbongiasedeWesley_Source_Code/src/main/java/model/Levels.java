package model;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project Levels
 * @Description Manages the different levels of the game including the paddles and foods. Handles level progression and element addition based on the score.
 * @Author Wesley Agbongiasede
 * @version 1.0
 */
public class Levels {
    private final List<Paddle> paddles;
    private final List<Food> foods;
    private int lastPaddleAddedScore = 0;
    private int lastFoodAddedScore = 0;
    private final List<String> foodImageKeys; // List of image keys for different foods

    /**
     * Manages the different levels of the game including the paddles and foods.
     * Handles level progression and element addition based on the score.
     *
     * @param foodImageKeys List of keys representing different food images.
     */
    public Levels(List<String> foodImageKeys) {
        paddles = new ArrayList<>();
        foods = new ArrayList<>();
        this.foodImageKeys = foodImageKeys; // Initialize with a list of food image keys
    }

    /**
     * Adds the initial paddle and food elements to the game.
     *
     * @param initialPaddle The initial paddle element to be added.
     * @param initialFood The initial food element to be added.
     */
    public void addInitialElements(Paddle initialPaddle, Food initialFood) {
        paddles.add(initialPaddle);
        foods.add(initialFood);
    }

    /**
     * Updates the level of the game based on the player's score. Adds new paddles and food items
     * based on specific score thresholds.
     *
     * @param score The current score of the player.
     */
    public void updateLevel(int score) {
        // Handle adding new paddles every 50 points
        if (score >= 50 && score % 50 == 0 && score > lastPaddleAddedScore) {
            paddles.add(new Paddle(100, 20, 870)); // Modify with actual game width and paddle dimensions
            lastPaddleAddedScore = score;
        }

        // Handle adding new food every 30 points
        if (score >= 30 && score % 30 == 0 && score > lastFoodAddedScore) {
            foods.add(new Food(foodImageKeys)); // Use the same foodImageKeys for new food
            lastFoodAddedScore = score;
        }
    }

    /**
     * Returns the list of paddles in the game.
     *
     * @return The list of paddles.
     */
    public List<Paddle> getPaddles() {
        return paddles;
    }

    /**
     * Returns the list of foods in the game.
     *
     * @return The list of foods.
     */
    public List<Food> getFoods() {
        return foods;
    }
}
