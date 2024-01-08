package model;

/**
 * @Project Objects
 * @Description Represents generic objects in the game with x and y coordinates. Provides basic functionalities like setting and getting coordinates, and comparing object positions.
 * @Author Wesley Agbongiasede
 * @version 1.0
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
     * Compares this Objects instance with the specified Object for equality.
     * Returns true if the Object is an instance of Objects class, and both the x and y coordinates match; false otherwise.
     *
     * @param obj The Object to compare with this Objects instance.
     * @return true if the objects are equal (same x and y coordinates), false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Objects objects = (Objects) obj;
        return x == objects.x && y == objects.y;
    }

    /**
     * Calculates the hash code value for this Objects instance.
     * The hash code is calculated based on the x and y coordinates of the object.
     *
     * @return The hash code value for this Objects instance.
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }
}
