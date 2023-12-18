package example;

/**
 * the Snake class represents the snake in the game.
 * It provides methods to control the snake's movement.
 */
public class Snake {

    /** A unique serial identification number*/
    private static final long serialVersionUID = -3641221053272056036L;


    // TODO: It needs renovation

    /** The current movement state of the snake. */
    public static int moving;

    /**
     * Starts the snake's movement in the specified direction.
     *
     * @param x The direction of the movement. Positive for right, Negative for left and Zero for no movement.
     * @return The current movement state of thåe snake.
     */
    public static int move(int x) {
        moving = x;
        return moving;
    }

    /** Stops the movement of the snake. */
    public static void stop() {
        moving = 0;
    }
}
