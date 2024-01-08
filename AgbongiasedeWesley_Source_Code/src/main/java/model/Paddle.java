package model;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.awt.*;

/**
 * @Project Paddle
 * @Description Represents a paddle in the game. Manages the paddle's movement, position, and rendering.
 * @Author Wesley Agbongiasede - modified
 * @version 1.0
 */
public class Paddle {
    public static final Color INNER_COLOR = Color.GREEN;
    public static final int DEFAULT_MOVE_AMOUNT = 3;
    private final Rectangle paddleFace;
    private Point2D ballPoint; // Current position of the paddle
    private int moveAmount; // How much the paddle should move per action
    private final double min; // minimum x-coordinate of the paddle's movement
    private final double max; // maximum x-coordinate of the paddle's movement

    /**
     * Represents a paddle in the game. Manages the paddle's movement, position, and rendering.
     *
     * @param width The width of the paddle.
     * @param height The height of the paddle.
     * @param gameWidth The width of the game area to constrain the paddle's movement.
     */
    public Paddle(int width, int height, double gameWidth) {
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

    /**
     * Moves the paddle horizontally within the game area. If the paddle reaches the boundary, it reverses its direction.
     * The paddle's position is updated after moving.
     */
    public void move() {
        double newX = ballPoint.getX() + moveAmount;

        if (newX <= min || newX + paddleFace.getWidth() >= max) {
            moveAmount = -moveAmount; // Reverse direction
        }

        ballPoint = new Point2D(newX, ballPoint.getY());
        updatePaddlePosition();
    }

    /**
     * Updates the position of the paddle based on the current position of the ball.
     * The X coordinate of the paddle face is set to the X coordinate of the ball minus half of the paddle face width.
     * The Y coordinate of the paddle face is set to the Y coordinate of the ball.
     */
    private void updatePaddlePosition() {
        paddleFace.setX(ballPoint.getX() - paddleFace.getWidth() / 2);
        paddleFace.setY(ballPoint.getY());
    }

    /**
     * Draws the Paddle on the provided GraphicsContext.
     *
     * @param gc The GraphicsContext on which to draw the Paddle.
     */
    public void draw(GraphicsContext gc) {
        gc.setFill(INNER_COLOR);
        gc.fillRect(paddleFace.getX(), paddleFace.getY(), paddleFace.getWidth(), paddleFace.getHeight());
    }

    /**
     * Retrieves the paddle face rectangle.
     *
     * @return The paddle face rectangle.
     */
    public Rectangle getPaddleFace() {
        return this.paddleFace;
    }
}
