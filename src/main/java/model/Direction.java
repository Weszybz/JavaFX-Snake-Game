package model;

/**
 * Enumeration representing different directions, commonly used for movement or orientation.
 * This enum includes four cardinal directions: UP, DOWN, LEFT, and RIGHT.
 * Each direction can be used to indicate movement or orientation in various applications.
 * For example, in a game, it can represent the direction a character is facing or moving.
 *
 * Usage Example:
 * <pre>
 * {@code
 * Direction direction = Direction.RIGHT;
 * if (direction == Direction.UP) {
 *     // Handle upward movement logic
 * } else if (direction == Direction.DOWN) {
 *     // Handle downward movement logic
 * } else if (direction == Direction.LEFT) {
 *     // Handle leftward movement logic
 * } else if (direction == Direction.RIGHT) {
 *     // Handle rightward movement logic
 * }
 * }
 * </pre>
 */
public enum Direction {
    UP, DOWN, LEFT, RIGHT
}
