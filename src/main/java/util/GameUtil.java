package util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.SnapshotParameters;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * Utility class for game operations, image loading + rotation in JavaFX.
 */
public class GameUtil {
	public static final int CELL_SIZE = 20; // The size of each cell in the grid
	public static final int WIDTH = 870;    // Width of the game area (canvas)
	public static final int HEIGHT = 560;   // Height of the game area (canvas)



	/**
	 * Rotates an image to a specified angle.
	 * Used in rotating the head image of the snake based on its current direction.
	 * @param image The image to rotate.
	 * @param angle The angle of rotation in degrees.
	 * @return A new Image object representing the rotated image.
	 */
	public static Image rotateImage(Image image, double angle) {
		ImageView imageView = new ImageView(image);
		imageView.setRotate(angle);

		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		return imageView.snapshot(params, null);
	}


}
