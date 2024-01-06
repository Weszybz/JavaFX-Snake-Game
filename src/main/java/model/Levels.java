package model;

import java.util.ArrayList;
import java.util.List;

public class Levels {
    private List<Paddle> paddles;
    private List<Food> foods;
    private int lastPaddleAddedScore = 0;
    private int lastFoodAddedScore = 0;
    private List<String> foodImageKeys; // List of image keys for different foods

    // Constructor
    public Levels(List<String> foodImageKeys) {
        paddles = new ArrayList<>();
        foods = new ArrayList<>();
        this.foodImageKeys = foodImageKeys; // Initialize with a list of food image keys
    }

    public void addInitialElements(Paddle initialPaddle, Food initialFood) {
        paddles.add(initialPaddle);
        foods.add(initialFood);
    }

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

    public List<Paddle> getPaddles() {
        return paddles;
    }

    public List<Food> getFoods() {
        return foods;
    }
}
