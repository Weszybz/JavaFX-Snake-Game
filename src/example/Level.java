package example;

import java.awt.*;

public class Level {
    private int snakeSpeed;
    private int numberOfPaddles;
    private Image backgroundImage;

    public Level(int snakeSpeed, int numberOfPaddles, Image backgroundImage) {
        this.snakeSpeed = snakeSpeed;
        this.numberOfPaddles = numberOfPaddles;
        this.backgroundImage = backgroundImage;
    }

    // Getters
    public int getSnakeSpeed() { return snakeSpeed; }
    public int getNumberOfPaddles() { return numberOfPaddles; }
    public Image getBackgroundImage() { return backgroundImage; }
}