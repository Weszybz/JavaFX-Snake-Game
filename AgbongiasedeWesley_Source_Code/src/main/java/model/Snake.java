package model;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;
import java.util.List;

/**
 * @Project Snake Game
 * @Description The Snake class represents the snake in the game. It is responsible for managing the snake's movement, growth, and collision detection.
 * @Author Wesley Agbongiasede - modified
 * @version 1.0
 */
public class Snake {
    private final List<Objects> body;
    private final List<Direction> segmentDirections;
    private final LinkedList<Point2D> turnPoints = new LinkedList<>();
    private Direction currentDirection;
    private Direction previousDirection;
    private final int size;
    private final long speed;
    private long lastMoveTime = 0;
    private int score = 0;
    private boolean gameOver = false;
    private final Image spriteSheet;
    private static final int SPRITE_SIZE = 64;  // assuming each sprite is 64x64 pixels
    private static final Rectangle2D BODY_STRAIGHT_SPRITE = new Rectangle2D(SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);


    /**
     * Constructor for Snake. Initializes the snake with the provided parameters.
     *
     * @param spriteSheetPath Path to the sprite sheet image.
     * @param initialX        Initial X-coordinate of the snake.
     * @param initialY        Initial Y-coordinate of the snake.
     * @param initialSize     Initial size of the snake.
     * @param segmentSize     Size of each snake segment.
     * @param speed           Movement speed of the snake.
     */
    public Snake(String spriteSheetPath, int initialX, int initialY, int initialSize, int segmentSize, long speed) {
        this.size = segmentSize;
        this.speed = speed;
        this.currentDirection = Direction.RIGHT;
        this.previousDirection = this.currentDirection;

        // Load the sprite sheet
        this.spriteSheet = new Image(spriteSheetPath);

        // Initialize the snake's body
        this.body = new LinkedList<>();
        for (int i = 0; i < initialSize; i++) {
            this.body.add(new Objects(initialX - i, initialY));
        }

        segmentDirections = new LinkedList<>();
        for (int i = 0; i < initialSize; i++) {
            segmentDirections.add(Direction.RIGHT); // Assuming snake initially moves right
        }
    }

    /**
     * Returns the sprite representing the head of the snake based on the current direction.
     *
     * @return The sprite representing the head of the snake.
     */
    private Rectangle2D getHeadSprite() {
        switch (currentDirection) {
            case UP:
                return new Rectangle2D(SPRITE_SIZE * 3, 0, SPRITE_SIZE, SPRITE_SIZE);
            case DOWN:
                return new Rectangle2D(SPRITE_SIZE * 4, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
            case LEFT:
                return new Rectangle2D(SPRITE_SIZE * 3, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
            case RIGHT:
                // No rotation needed
                return new Rectangle2D(SPRITE_SIZE * 4, 0, SPRITE_SIZE, SPRITE_SIZE);
            default:
                return new Rectangle2D(SPRITE_SIZE * 4, 0, SPRITE_SIZE, SPRITE_SIZE);
        }
    }

    /**
     * Returns the sprite representing the tail of the snake based on the given direction.
     *
     * @param direction The direction of the next segment from the tail.
     * @return The sprite representing the tail of the snake.
     */
    private Rectangle2D getTailSprite(Direction direction) {
        switch (direction) {
            case UP:
                return new Rectangle2D(SPRITE_SIZE * 3, SPRITE_SIZE * 2, SPRITE_SIZE, SPRITE_SIZE);
            case DOWN:
                return new Rectangle2D(SPRITE_SIZE * 4, SPRITE_SIZE * 3, SPRITE_SIZE, SPRITE_SIZE);
            case LEFT:
                return new Rectangle2D(SPRITE_SIZE * 3, SPRITE_SIZE * 3, SPRITE_SIZE, SPRITE_SIZE);
            case RIGHT:
                // No rotation needed
                return new Rectangle2D(SPRITE_SIZE * 4, SPRITE_SIZE * 2, SPRITE_SIZE, SPRITE_SIZE);
            default:
                return new Rectangle2D(SPRITE_SIZE * 4, SPRITE_SIZE * 2, SPRITE_SIZE, SPRITE_SIZE);
        }
    }

    /**
     * Returns the sprite representing a segment of the snake's body based on the given direction.
     *
     * @param direction The direction of the segment.
     * @return The sprite representing the segment of the snake's body.
     */
    private Rectangle2D getBodySprite(Direction direction) {
        switch (direction) {
            case UP:
            case DOWN:
                return new Rectangle2D(SPRITE_SIZE * 2, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
            case RIGHT:
            case LEFT:
            default:
                return new Rectangle2D(SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
        }
    }

    /**
     * Returns the sprite representing a segment of the snake's body when there is a turn in direction between this segment and the next segment.
     *
     * @param thisSegmentDirection The direction of this segment.
     * @param nextSegmentDirection The direction of the next segment.
     * @return The sprite representing the segment of the snake's body for a turn.
     */
    private Rectangle2D getTurnSprite(Direction thisSegmentDirection, Direction nextSegmentDirection) {
        if ((thisSegmentDirection == Direction.UP && nextSegmentDirection == Direction.RIGHT)|| (thisSegmentDirection == Direction.LEFT && nextSegmentDirection == Direction.DOWN)) {
            // Curve turning up to right // Curve turning left to down
            return new Rectangle2D(0, 0, SPRITE_SIZE, SPRITE_SIZE);
        } else if ((thisSegmentDirection == Direction.UP && nextSegmentDirection == Direction.LEFT) || (thisSegmentDirection == Direction.RIGHT && nextSegmentDirection == Direction.DOWN)) {
            // Curve turning up to left // Curve turning right to down
            return new Rectangle2D(SPRITE_SIZE * 2, 0, SPRITE_SIZE, SPRITE_SIZE);
        } else if ((thisSegmentDirection == Direction.LEFT && nextSegmentDirection == Direction.UP) || (thisSegmentDirection == Direction.DOWN && nextSegmentDirection == Direction.RIGHT)) {
            // Curve turning left to up // Curve turning down to right
            return new Rectangle2D(0, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
        } else if ((thisSegmentDirection == Direction.DOWN && nextSegmentDirection == Direction.LEFT) ||(thisSegmentDirection == Direction.RIGHT && nextSegmentDirection == Direction.UP) ) {
            // Curve turning down to left // Curve turning right to up
            return new Rectangle2D(SPRITE_SIZE * 2, SPRITE_SIZE * 2, SPRITE_SIZE, SPRITE_SIZE);
        } else {
            // No turn, use the straight body sprite
            return BODY_STRAIGHT_SPRITE;
        }
    }

    /**
     * Draws the snake on the provided GraphicsContext.
     *
     * @param gc The GraphicsContext on which to draw the snake.
     */
    public void draw(GraphicsContext gc) {
        for (int i = 0; i < body.size(); i++) {
            Objects segment = body.get(i);
            // Determine the sprite to use
            Rectangle2D spriteToUse;

            // Draw the segment using the appropriate sprite
            if (i == 0) {
                // Head of the snake
                spriteToUse = getHeadSprite();
            } else if (i == body.size() - 1) {
                // Tail of the snake
                Direction nextSegmentDirection = segmentDirections.get(i - 1);
                spriteToUse = getTailSprite(nextSegmentDirection);
            } else {
                // Body of the snake
                Direction thisSegmentDirection = segmentDirections.get(i);
                Direction nextSegmentDirection = segmentDirections.get(i - 1);
                if (thisSegmentDirection != nextSegmentDirection) {
                    // This segment is a turn
                    spriteToUse = getTurnSprite(thisSegmentDirection, nextSegmentDirection);
                } else {
                    // This segment is straight
                    spriteToUse = getBodySprite(nextSegmentDirection);
                }
            }

            // Draw the segment using the appropriate sprite
            gc.drawImage(
                    spriteSheet,
                    spriteToUse.getMinX(), spriteToUse.getMinY(),
                    spriteToUse.getWidth(), spriteToUse.getHeight(),
                    segment.getX() * size, segment.getY() * size,
                    size, size // Scale sprite to the desired size
            );
        }
    }

    /**
     * Move the snake's head to a new position based on the current direction.
     * If the current time minus the last move time is greater than or equal to the speed,
     * update the snake's position and check for collisions with the body.
     * If a collision occurs, set the game over flag to true.
     *
     * @param currentTime The current time in milliseconds.
     */
    public void move(long currentTime) {
        if (currentTime - lastMoveTime >= speed) {
            Objects head = body.get(0);
            Objects newHead = new Objects(head.getX(), head.getY());

            switch (currentDirection) {
                case UP: newHead.setY(head.getY() - 1); break;
                case DOWN: newHead.setY(head.getY() + 1); break;
                case LEFT: newHead.setX(head.getX() - 1); break;
                case RIGHT: newHead.setX(head.getX() + 1); break;
            }

            // Check if the new head position collides with body
            for (int i = 0; i < body.size(); i++) {
                if (newHead.equals(body.get(i))) {
                    gameOver = true;
                    return;
                }
            }

            // When the head turns, add the point to the turnPoints list
            if (currentDirection != previousDirection) {
                turnPoints.add(new Point2D(head.getX(), head.getY()));
                previousDirection = currentDirection; // Don't forget to update the previousDirection
            }

            // Move the snake forward by adding the new head and removing the last segment
            body.add(0, newHead);
            body.remove(body.size() - 1);

            // Update the segment directions to propagate the turn through the body
            for (int i = segmentDirections.size() - 1; i > 0; i--) {
                segmentDirections.set(i, segmentDirections.get(i - 1));
            }
            segmentDirections.set(0, currentDirection);

            lastMoveTime = currentTime;
        }
    }

    /**
     * Checks if the head of the snake is out of bounds.
     * The head is considered out of bounds if its x-coordinate is less than 0 or greater than or equal to the width of the game divided by the cell size,
     * or if its y-coordinate is less than 0 or greater than or equal to the height of the game divided by the cell size.
     *
     * @return true if the head is out of bounds, false otherwise.
     */
    public boolean isOutOfBounds() {
        Objects head = body.get(0);
        boolean xOut = head.getX() < 0 || head.getX() >= util.GameUtil.WIDTH / util.GameUtil.CELL_SIZE;
        boolean yOut = head.getY() < 0 || head.getY() >= util.GameUtil.HEIGHT / util.GameUtil.CELL_SIZE;
        return xOut || yOut;
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Adds a new segment to the snake's body and updates the segment directions.
     * The new segment is placed at the position of the current tail and continues in the same direction as the last segment.
     */
    public void grow() {
        Objects tail = body.get(body.size() - 1);
        // Add a new segment at the position of the current tail (it will move in the next update)
        body.add(new Objects(tail.getX(), tail.getY()));

        // It's crucial to also add the direction of the last segment to the segmentDirections list
        // The new segment will initially continue in the same direction as the last segment
        Direction tailDirection = segmentDirections.get(segmentDirections.size() - 1);
        segmentDirections.add(tailDirection);
    }

    /**
     * Changes the direction of the snake.
     *
     * @param newDirection The new direction to change to.
     */
    public void changeDirection(Direction newDirection) {
        if ((currentDirection == Direction.UP && newDirection != Direction.DOWN) ||
                (currentDirection == Direction.DOWN && newDirection != Direction.UP) ||
                (currentDirection == Direction.LEFT && newDirection != Direction.RIGHT) ||
                (currentDirection == Direction.RIGHT && newDirection != Direction.LEFT)) {
            currentDirection = newDirection;
            rotateHeadImage();
        }
    }

    /**
     * Rotates the head image of the snake based on its current direction.
     * The head image is selected from a sprite sheet, with each direction having its own image.
     *
     * @return The rectangle representing the rotated head image of the snake.
     */
    private Rectangle2D rotateHeadImage() {
        // Assuming your sprite sheet has the head images in the order: up, right, down, left
        switch (currentDirection) {
            case UP:
                return new Rectangle2D(0, 0, SPRITE_SIZE, SPRITE_SIZE);
            case RIGHT:
                return new Rectangle2D(SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
            case DOWN:
                return new Rectangle2D(2 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
            case LEFT:
                return new Rectangle2D(3 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
            default:
                return new Rectangle2D(SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
        }
    }

    /**
     * Checks if the snake's head is colliding with the given food.
     *
     * @param food The food object to check collision with.
     * @return true if the head of the snake is colliding with the food, false otherwise.
     */
    public boolean isCollidingWithFood(model.Food food) {
        Objects head = body.get(0);
        return head.getX() == food.getX() && head.getY() == food.getY();
    }

    /**
     * Checks if the head of the snake is colliding with the paddle.
     *
     * @param paddle The paddle object to check collision with.
     * @return true if the head of the snake is colliding with the paddle, false otherwise.
     */
    public boolean checkCollisionWithPaddle(Paddle paddle) {
        Objects head = body.get(0); // Assuming this gives you the head of the snake
        Rectangle2D headRect = new Rectangle2D(
                head.getX() * util.GameUtil.CELL_SIZE,
                head.getY() * util.GameUtil.CELL_SIZE,
                util.GameUtil.CELL_SIZE,
                util.GameUtil.CELL_SIZE
        );

        Rectangle paddleFace = paddle.getPaddleFace();

        // Convert Bounds to Rectangle2D for intersection check
        Bounds bounds = paddleFace.getBoundsInLocal();
        Rectangle2D paddleRect = new Rectangle2D(
                bounds.getMinX(),
                bounds.getMinY(),
                bounds.getWidth(),
                bounds.getHeight()
        );

        return headRect.intersects(paddleRect);
    }

    /**
     * Increases the score of the snake by the specified amount.
     *
     * @param increment The amount by which to increase the score.
     */
    public void increaseScore(int increment) {
        score += increment;
    }

    /**
     * Returns the current score of the snake.
     *
     * @return The score of the snake.
     */
    public int getScore() {
        return score;
    }
}