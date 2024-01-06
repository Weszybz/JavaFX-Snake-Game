package model;

import javafx.geometry.Bounds;
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
    private Image headImage;
    private Image bodyImage;
    private Image originalHeadImage;
    private Direction currentDirection;
    private final int size;
    private final long speed;
    private long lastMoveTime = 0;
    private int score = 0;
    private boolean gameOver = false;

    /**
     * Constructor for Snake.
     * Initializes the snake with the provided parameters.
     *
     * @param headImagePath       Path to the image for the snake's head.
     * @param bodyImagePathKey    Key to retrieve the body image from the image utility.
     * @param initialX            Initial X-coordinate of the snake.
     * @param initialY            Initial Y-coordinate of the snake.
     * @param initialSize         Initial size of the snake.
     * @param segmentSize         Size of each snake segment.
     * @param speed               Movement speed of the snake.
     */
    public Snake(String headImagePath, String bodyImagePathKey, int initialX, int initialY, int initialSize, int segmentSize, long speed) {
        originalHeadImage = util.ImageUtil.getImage("snake-head-right");
        headImage = util.ImageUtil.getImage("snake-head-right");
        bodyImage = util.ImageUtil.getImage("snake-body");

        this.size = segmentSize;
        this.speed = speed;
        currentDirection = Direction.RIGHT;

        body = new LinkedList<>();
        body.add(new Objects(initialX, initialY));
    }

    /**
     * Draws the snake on the game canvas.
     *
     * @param gc GraphicsContext for drawing the snake.
     */
    public void draw(GraphicsContext gc) {
        Objects head = body.get(0);
        gc.drawImage(headImage, head.getX() * util.GameUtil.CELL_SIZE, head.getY() * util.GameUtil.CELL_SIZE, util.GameUtil.CELL_SIZE, util.GameUtil.CELL_SIZE);

        for (int i = 1; i < body.size(); i++) {
            Objects segment = body.get(i);
            gc.drawImage(bodyImage, segment.getX() * util.GameUtil.CELL_SIZE, segment.getY() * util.GameUtil.CELL_SIZE, util.GameUtil.CELL_SIZE, util.GameUtil.CELL_SIZE);
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

            for (int i = 1; i < body.size(); i++) {
                if (newHead.equals(body.get(i))) {
                    gameOver = true;
                    return;
                }
            }

            body.add(0, newHead);
            body.remove(body.size() - 1);
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
        body.add(new Objects(tail.getX(), tail.getY()));
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
        switch (currentDirection) {
            case UP:
                headImage = util.GameUtil.rotateImage(originalHeadImage, -90);
                break;
            case DOWN:
                headImage = util.GameUtil.rotateImage(originalHeadImage, 90);
                break;
            case LEFT:
                headImage = util.GameUtil.rotateImage(originalHeadImage, 180);
                break;
            case RIGHT:
                headImage = originalHeadImage;
                break;
        }
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
