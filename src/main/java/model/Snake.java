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
 * The Snake class represents the snake in the game.
 * It is responsible for managing the snake's movement, growth, and collision detection.
 */
public class Snake {

    private List<Objects> body;
    private List<Direction> segmentDirections;
    private LinkedList<Point2D> turnPoints = new LinkedList<>();

    private Image headImage;
    private Image bodyImage;
    private Image originalHeadImage;
    private Direction currentDirection;
    private Direction previousDirection;
    private final int size;
    private final long speed;
    private long lastMoveTime = 0;
    private int score = 0;
    private boolean gameOver = false;


    private Image spriteSheet;
    private Rectangle2D headSprite;
    private Rectangle2D bodySprite;

    // Define constants for the sprite sheet positions
    private static final int SPRITE_SIZE = 64;  // assuming each sprite is 64x64 pixels
    private static final Rectangle2D HEAD_SPRITE = new Rectangle2D(SPRITE_SIZE * 4, 0, SPRITE_SIZE, SPRITE_SIZE);
    private static final Rectangle2D BODY_STRAIGHT_SPRITE = new Rectangle2D(SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
    private static final Rectangle2D BODY_TURN_SPRITE = new Rectangle2D(SPRITE_SIZE * 2, 0, SPRITE_SIZE, SPRITE_SIZE);
    private static final Rectangle2D TAIL_SPRITE = new Rectangle2D(SPRITE_SIZE * 4, SPRITE_SIZE * 2, SPRITE_SIZE, SPRITE_SIZE);

    /**
     * Constructor for Snake.
     * Initializes the snake with the provided parameters.
     *
     * @param initialX            Initial X-coordinate of the snake.
     * @param initialY            Initial Y-coordinate of the snake.
     * @param initialSize         Initial size of the snake.
     * @param segmentSize         Size of each snake segment.
     * @param speed               Movement speed of the snake.
     */
    public Snake(String spriteSheetPath, int initialX, int initialY, int initialSize, int segmentSize, long speed) {
        this.size = segmentSize;
        this.speed = speed;
        this.currentDirection = Direction.RIGHT;
        this.previousDirection = this.currentDirection;

        // Load the sprite sheet
        this.spriteSheet = new Image(spriteSheetPath);

        // Define the individual sprites based on the sprite sheet
        this.headSprite = new Rectangle2D(0, 0, size, size);
        this.bodySprite = new Rectangle2D(size, 0, size, size);
        // ... define other sprites

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
     * Draws the snake on the game canvas.
     *
     */
    private Rectangle2D getHeadSprite() {
        // Use your existing utility to rotate the image based on the direction
        // Here's a pseudocode example, replace with your actual implementation:
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

    private Rectangle2D getTailSprite() {
        // Use your existing utility to rotate the image based on the direction
        // Here's a pseudocode example, replace with your actual implementation:
        switch (currentDirection) {
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
    private Rectangle2D getBodySprite() {
        // Use your existing utility to rotate the image based on the direction
        // Here's a pseudocode example, replace with your actual implementation:
        switch (currentDirection) {
            case UP:
                return new Rectangle2D(SPRITE_SIZE * 2, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
            case DOWN:
                return new Rectangle2D(SPRITE_SIZE * 2, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
            case LEFT:
                return new Rectangle2D(SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
            case RIGHT:
                // No rotation needed
                return new Rectangle2D(SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
            default:
                return new Rectangle2D(SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
        }
    }

    private Rectangle2D getTurnSprite(Direction thisSegmentDirection, Direction nextSegmentDirection) {
        if (thisSegmentDirection == Direction.UP && nextSegmentDirection == Direction.RIGHT) {
            // Curve turning up to right
            return new Rectangle2D(0, 0, SPRITE_SIZE, SPRITE_SIZE);
        } else if (thisSegmentDirection == Direction.UP && nextSegmentDirection == Direction.LEFT) {
            // Curve turning up to left
            return new Rectangle2D(SPRITE_SIZE * 2, 0, SPRITE_SIZE, SPRITE_SIZE);
        } else if (thisSegmentDirection == Direction.LEFT && nextSegmentDirection == Direction.UP) {
            // Curve turning left to up
            return new Rectangle2D(SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
        } else if (thisSegmentDirection == Direction.LEFT && nextSegmentDirection == Direction.DOWN) {
            // Curve turning left to down
            return new Rectangle2D(0, 0, SPRITE_SIZE, SPRITE_SIZE);
        } else if (thisSegmentDirection == Direction.DOWN && nextSegmentDirection == Direction.RIGHT) {
            // Curve turning down to right
            return new Rectangle2D(0, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
        } else if (thisSegmentDirection == Direction.DOWN && nextSegmentDirection == Direction.LEFT) {
            // Curve turning down to left
            return new Rectangle2D(SPRITE_SIZE * 2, SPRITE_SIZE * 2, SPRITE_SIZE, SPRITE_SIZE);
        } else if (thisSegmentDirection == Direction.RIGHT && nextSegmentDirection == Direction.UP) {
            // Curve turning right to up
            return new Rectangle2D(SPRITE_SIZE * 2, SPRITE_SIZE * 2, SPRITE_SIZE, SPRITE_SIZE);
        } else if (thisSegmentDirection == Direction.RIGHT && nextSegmentDirection == Direction.DOWN) {
            // Curve turning right to down
            return new Rectangle2D(SPRITE_SIZE * 2, 0, SPRITE_SIZE, SPRITE_SIZE);
        } else {
            // No turn, use the straight body sprite
            return BODY_STRAIGHT_SPRITE;
        }
    }



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
                spriteToUse = getTailSprite();
            } else {
                // Body of the snake
                Direction thisSegmentDirection = segmentDirections.get(i);
                Direction nextSegmentDirection = segmentDirections.get(i - 1); // Use the direction of the previous segment to determine the turn
                if (thisSegmentDirection != nextSegmentDirection) {
                    // This segment is a turn
                    spriteToUse = getTurnSprite(thisSegmentDirection, nextSegmentDirection);
                } else {
                    // This segment is straight
                    spriteToUse = getBodySprite();
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
     * Moves the snake based on the current time and the snake's speed.
     * Checks for collision with its own body, and if detected, sets the game over flag.
     *
     * @param currentTime Current game time.
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

            // Check if the new head position collides with any body segment
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
     * Checks if the snake is out of bounds.
     *
     * @return true if snake is out of bounds, false otherwise.
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
     * Increases the size of the snake by adding a new segment at its tail.
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
     * Changes the direction of the snake's movement.
     *
     * @param newDirection The new direction for the snake.
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
     */
    private void rotateHeadImage() {
        // Assuming your sprite sheet has the head images in the order: up, right, down, left
        double rotationAngle;
        switch (currentDirection) {
            case UP:
                rotationAngle = 0; // No rotation if the head is already facing up
                headSprite = new Rectangle2D(0 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
                break;
            case RIGHT:
                rotationAngle = 90; // Rotate clockwise by 90 degrees
                headSprite = new Rectangle2D(1 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
                break;
            case DOWN:
                rotationAngle = 180; // Rotate by 180 degrees
                headSprite = new Rectangle2D(2 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
                break;
            case LEFT:
                rotationAngle = 270; // Rotate counter-clockwise by 90 degrees
                headSprite = new Rectangle2D(3 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
                break;
            default:
                return; // Default case to handle any unexpected direction
        }

        // If your sprite sheet does not have separate images for each direction,
        // you can apply a rotation to the headImage using a utility method like so:
        headImage = util.GameUtil.rotateImage(originalHeadImage, rotationAngle);
    }


    /**
     * Checks if the snake is colliding with food.
     *
     * @param food The food object to check collision against.
     * @return true if colliding with food, false otherwise.
     */
    public boolean isCollidingWithFood(model.Food food) {
        Objects head = body.get(0);
        return head.getX() == food.getX() && head.getY() == food.getY();
    }

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
     * Increases the score by a specified increment (10).
     *
     * @param increment The amount by which to increase the score.
     */
    public void increaseScore(int increment) {
        score += increment;
    }

    /**
     * Gets the current score.
     *
     * @return The current score.
     */
    public int getScore() {
        return score;
    }
}