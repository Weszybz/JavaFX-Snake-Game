package example;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * The GameUtil class provides utility methods for working with images in the game.
 */
public class GameUtil
{
	/**
	 * Retrieves an image form the specified image path.
	 *
	 * @param imagePath The path to the image path
	 * @return The Image object loaded from the specified path.
	 */
	public static Image getImage(String imagePath)
	{
		URL u = GameUtil.class.getClassLoader().getResource(imagePath);
		BufferedImage i = null;
		try
		{
			i = ImageIO.read(u);
		} catch (Exception e)
		{
			System.err.println("\n" + "ERROR : SPECIFIC IMAGE NOT FOUND !\n");
			e.printStackTrace();
		}

		return i;
	}

	/**
	 * Rotates the given BufferedImage by the specified degree.
	 *
	 * @param bufferedImage The BufferedImage to be rotated.
	 * @param degree The degree used to rotate the image.
	 * @return The rotated BufferedImage.
	 */
	public static Image rotateImage(final BufferedImage bufferedImage, final int degree)
	{
	int w = bufferedImage.getWidth();
	int h = bufferedImage.getHeight();
	int t = bufferedImage.getColorModel().getTransparency();

	BufferedImage i;
	Graphics2D graphics2d;

	// Create a new BufferedImage for the rotated image
	(graphics2d = (i = new BufferedImage(w, h, t)).createGraphics()).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

	// Rotate the image
	graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
	graphics2d.drawImage(bufferedImage, 0, 0, null);
	graphics2d.dispose();

	return i;

	}
}
