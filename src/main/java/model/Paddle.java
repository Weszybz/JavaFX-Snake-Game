package model;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class Paddle {

    // Constants for the paddle
    public static final Color BORDER_COLOR = Color.GREEN.darker().darker();
    public static final Color INNER_COLOR = Color.GREEN;
    public static final int DEFAULT_MOVE_AMOUNT = 3;

    // Paddle properties
    private Rectangle paddleFace; // Now using Rectangle
    private Point2D ballPoint;    // Current position of the paddle
    private int moveAmount;       // How much the paddle should move per action
    private double min; // minimum x-coordinate of the paddle's movement
    private double max; // maximum x-coordinate of the paddle's movement
    private boolean isAvailable;  // Is the paddle active

    // Constructor
    public Paddle(int width, int height, double gameWidth) {
        // ... initialization ...
        // Generate a random x-coordinate within the bounds of the container
        int x = (int) (Math.random() * (870 - width + 10));

        // Generate a random y-coordinate within the bounds of the container
        int y = (int) (Math.random() * (560 - height - 40));

        // Set the paddle's initial position
        this.ballPoint = new Point2D(x, y);

        // Initialize paddleFace as Rectangle
        this.paddleFace = new Rectangle(width, height);
        this.paddleFace.setFill(INNER_COLOR); // Set color if needed

        // Set the initial position of the paddle
        this.min = 40;
        this.max = gameWidth + 40;
        this.moveAmount = DEFAULT_MOVE_AMOUNT;

        updatePaddlePosition();
    }

    // Moves the paddle left or right
    public void move() {
        double newX = ballPoint.getX() + moveAmount;

        if (newX <= min || newX + paddleFace.getWidth() >= max) {
            moveAmount = -moveAmount; // Reverse direction
        }

        ballPoint = new Point2D(newX, ballPoint.getY());
        updatePaddlePosition();
    }


    // Update paddle position
    private void updatePaddlePosition() {
        paddleFace.setX(ballPoint.getX() - paddleFace.getWidth() / 2);
        paddleFace.setY(ballPoint.getY());
    }

    // Start moving the paddle left
    public void moveLeft() {
        moveAmount = -DEFAULT_MOVE_AMOUNT;
    }

    // Start moving the paddle right
    public void moveRight() {
        moveAmount = DEFAULT_MOVE_AMOUNT;
    }

    // Stop moving the paddle
    public void stop() {
        moveAmount = 0;
    }

    // Resume movement with the last known direction
    public void resume() {
        // Implement logic to resume movement
    }

    // Draw the paddle on the canvas
    public void draw(GraphicsContext gc) {
        gc.setFill(INNER_COLOR);
        gc.fillRect(paddleFace.getX(), paddleFace.getY(), paddleFace.getWidth(), paddleFace.getHeight());
    }

    // Getters and Setters
    public Rectangle getPaddleFace() {
        return this.paddleFace;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // Check if the paddle collides with another object like the snake
    public boolean checkCollision(Rectangle other) {
        return paddleFace.intersects(other.getBoundsInLocal());
    }
}
