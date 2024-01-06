package model;

/**
 * The Objects class represents objects in the game with x and y coordinates.
 * These objects can be used to represent points or positions in a two-dimensional space.
 */
public class Objects {

    public int x; // Holds the x coordinate
    public int y; // Holds the y coordinate

    /**
     * Constructs a new Objects instance with specified x and y coordinates.
     *
     * @param x The x coordinate of the object.
     * @param y The y coordinate of the object.
     */
    public Objects(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters and setters

    /**
     * Gets the x coordinate of the object.
     *
     * @return The x coordinate of the object.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the object.
     *
     * @return The y coordinate of the object.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the x coordinate of the object.
     *
     * @param x The new x coordinate for the object.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y coordinate of the object.
     *
     * @param y The new y coordinate for the object.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Compares this object to the specified object for equality.
     * The result is true if and only if the argument is not null and is an Objects instance
     * with the same x and y coordinates.
     *
     * @param obj The object to compare this Objects against.
     * @return true if the given object represents an equivalent Objects instance, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Objects objects = (Objects) obj;
        return x == objects.x && y == objects.y;
    }

    /**
     * Returns a hash code value for the object.
     * This method is supported for the benefit of hash tables.
     *
     * @return A hash code value for this Objects instance.
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }
}
