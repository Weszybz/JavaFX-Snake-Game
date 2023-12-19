package example;
import java.awt.*;

/**
 * the Paddle class represents the paddle in the game.
 * It defines the paddle's appearance, movement, and the interaction with the ball.
 * */
public class Paddle {

    /** The border color for the paddle. */
    public static final Color BORDER_COLOR = Color.GREEN.darker().darker();
    /** The inner color for the paddle. */
    public static final Color INNER_COLOR = Color.GREEN;

    /** The default movement amount of the paddle */
    public static final int DEFAULT_MOVE_AMOUNT = 3;

    /** The rectangle representing the face of the paddle. */
    private Rectangle paddleFace;
    /** The point representing the current position of the ball. */
    private Point ballPoint;
    /** The movement amount of the paddle. */
    private int moveAmount;
    /** The last movement before the game was paused. */
    private int lastMoveAmount;
    /** The minimum x-coordinate of the paddle's movement. */
    private int min;
    /** The maximum x-coordinate of the paddle's movement. */
    private int max;
    private boolean isAvailable;


    /**
     * Constructs a new Paddle with the specified ball point, width, height and container rectangle.
     * @param width The width of the paddle.
     * @param height The height of the paddle.
     * @param container The rectangle representing the container for the paddle's movement.
     */
    public Paddle(int width, int height, Rectangle container) {
        // Generate a random x-coordinate within the bounds of the container
        int x = (int) (Math.random() * (870 - width + 10));

        // Generate a random y-coordinate within the bounds of the container
        int y = (int) (Math.random() * (560 - height - 40));

        // Set the paddle's initial position
        this.ballPoint = new Point(x, y);

        // Initialize other paddle properties
        this.moveAmount = DEFAULT_MOVE_AMOUNT;
        this.paddleFace = makeRectangle(width, height);
        this.min = container.x + 40;
        this.max = container.x + container.width;
        this.isAvailable = true;
    }

    /**
     * Creates a rectangle based on the current ball point, width, and the height.
     *
     * @param width The width of the rectangle.
     * @param height the height of the rectangle.
     * @return A rectangle based on the specified parameters.
     */
    private Rectangle makeRectangle(int width,int height){
        Point p = new Point((int)(ballPoint.getX() - (width / 2)),(int)ballPoint.getY());
        return  new Rectangle(p,new Dimension(width,height));
    }

    /** Moves the paddle based on the current move amount */
    public void move(){
        double x = ballPoint.getX() + moveAmount;
        if(x <= min){
            moveRight();
        } else if (x >= max - paddleFace.width) {
            moveLeft();
        }
        ballPoint.setLocation(x,ballPoint.getY());
        paddleFace.setLocation(ballPoint.x - (int) paddleFace.getWidth()/2,ballPoint.y);
    }

    /** Starts left movement of the paddle */
    public void moveLeft(){
        moveAmount = -DEFAULT_MOVE_AMOUNT;
    }

    /** Starts right movement of the paddle */
    public void moveRight(){
        moveAmount = DEFAULT_MOVE_AMOUNT;
    }

    /** Stops the movement of the paddle. */
    public void stop(){
        lastMoveAmount = moveAmount;
        moveAmount = 0;
    }

    public void resume() {
        moveAmount = lastMoveAmount;
    }

    /**
     * Gets the shape representing the face of the paddle.
     *
     * @return The shape representing the face of the paddle,
     */
    public Rectangle getPaddleFace(){
        return paddleFace;
    }

    /**
     * Moves the paddle to the specified point.
     *
     * @param p The point to which the paddle should be moved.
     */
    public void moveTo(Point p){
        ballPoint.setLocation(p);
        paddleFace.setLocation(ballPoint.x - (int) paddleFace.getWidth()/2,ballPoint.y);
    }

    /**
     * Returns the availability of the paddle.
     *
     * @return true if the paddle is available, false otherwise.
     */

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Checks if the snake has collided with the paddle.
     *
     * @param snakeGame The rectangle representing the snake's head.
     */
    public void checkCollision(GameFrame.SnakeGame snakeGame) {
        Rectangle snakeHead = snakeGame.getRectangle(); // Get snake's head rectangle
        if (snakeHead.intersects(paddleFace)) {
            snakeGame.isAvailable = false;
        }
    }

    /**
     * Draws the paddle on the given Graphics context.
     *
     * @param g The Graphics object to draw on.
     */
    public void draw(Graphics g) {
        // Set the color for the paddle's interior
        g.setColor(INNER_COLOR);
        g.fillRect(paddleFace.x, paddleFace.y, paddleFace.width, paddleFace.height);

        // Set the color for the paddle's border
        g.setColor(BORDER_COLOR);
        g.drawRect(paddleFace.x, paddleFace.y, paddleFace.width, paddleFace.height);
    }


}
