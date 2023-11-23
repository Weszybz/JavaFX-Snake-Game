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
    public static final int DEF_MOVE_AMOUNT = 5;

    /** The rectangle representing the face of the paddle. */
    private Rectangle paddleFace;
    /** The point representing the current position of the ball. */
    private Point ballPoint;
    /** The movement amount of the paddle. */
    private int moveAmount;
    /** The minimum x-coordinate of the paddle's movement. */
    private int min;
    /** The maximum x-coordinate of the paddle's movement. */
    private int max;


    /**
     * Constructs a new Paddle with the specified ball point, width, height and container rectangle.
     * @param ballPoint The point representing the current position of the ball.
     * @param width The width of the paddle.
     * @param height The height of the paddle.
     * @param container The rectangle representing the container for the paddle's movement.
     */
    public Paddle(Point ballPoint, int width, int height, Rectangle container) {
        this.ballPoint = ballPoint;
        moveAmount = 0;
        paddleFace = makeRectangle(width, height);
        min = container.x + (width / 2);
        max = min + container.width - width;

    }

    /**
     * Creates a rectangle based on the current ball point, width, and the height.
     *
     * @param width The width of the rectangle.
     * @param height the height of the rectangle.
     * @return A rectangle based on the specified parameters.
     */
    public Rectangle makeRectangle(int width,int height){
        Point p = new Point((int)(ballPoint.getX() - (width / 2)),(int)ballPoint.getY());
        return  new Rectangle(p,new Dimension(width,height));
    }

    /** Moves the paddle based on teh current move amount */
    public void move(){
        double x = ballPoint.getX() + moveAmount;
        if(x < min || x > max)
            return;
        ballPoint.setLocation(x,ballPoint.getY());
        paddleFace.setLocation(ballPoint.x - (int) paddleFace.getWidth()/2,ballPoint.y);
    }

    /** Starts left movement of the paddle */
    public void moveLeft(){
        moveAmount = -DEF_MOVE_AMOUNT;
    }

    /** Starts right movement of the paddle */
    public void movRight(){
        moveAmount = DEF_MOVE_AMOUNT;
    }

    /** Stops the movement of the paddle. */
    public void stop(){
        moveAmount = 0;
    }

    /** Gets the shape representing the face of the paddle.
     *
     * @return The shape representing the face of the paddle,
     */
    public Shape getPaddleFace(){
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
}
