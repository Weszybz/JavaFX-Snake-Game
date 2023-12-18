package example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @Project Snake
 * @Description Play the game
 * @Author Wesley Agbons
 * @version Not sure

 * The MyFrame class represents the main frame of the Snake game.
 * It extends JPanel and implements KeyListener for user inputs.
 */
public class GameFrame extends JPanel implements KeyListener
{
	/** A unique serial identification number*/
	private static final long serialVersionUID = -3149926831770554380L;
	private String playerName;

	public void setPlayerName(String playerName) {
		this.playerName = playerName;;
	}

	/** The JFrame used to display the game. */
	public JFrame jFrame = new JFrame();

	/** Constructs a new MyFrame and sets the application icon. */
	public GameFrame()
	{
		jFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(GameFrame.class.getResource("snake-logo.png")));
	}

	/**
	 * Loads the game frame
	 * Sets properties
	 * Starts the game loop
	 */
	public void loadFrame()
	{
		/** Prevent the game from flashing or buffering */
		this.setDoubleBuffered(true);
		jFrame.add(this);
		jFrame.addKeyListener(this);

		jFrame.setTitle("Snake Game");
		jFrame.setSize(870, 560);
		jFrame.setLocationRelativeTo(null);
		jFrame.addWindowListener(new WindowAdapter()// Closing
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				super.windowClosing(e);
				System.exit(0);
			}
		});
		jFrame.setVisible(true);
		new GameThread().start();
	}



	/** The MyThread class represents a thread responsible for repainting the game at regular intervals */
	class GameThread extends Thread
	{
		@Override
		public void run()
		{
			super.run();
			while (true)
			{
				repaint();
				try
				{
					sleep(30);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * the MySnake class represents the player-controlled snake in the game.
	 * It implements the movable interface and extends the SnakeObject class.
	 */
	public static class SnakeGame extends SnakeObject implements movable
	{
		// Game variables
		private int speed_XY;
		private static int length;
		private int num; // ?
		public static int score = 0;

		// Static images for the snake head and body
		private static final BufferedImage IMG_SNAKE_HEAD = (BufferedImage) ImageUtil.images.get("snake-head-right");

		// List which stores the body point of the snake
		public static List<Point> bodyPoints = new LinkedList<>();

		// Image for the rotated snake head
		private static BufferedImage newImgSnakeHead;
		boolean up, down, left, right = true;


		/**
		 * Constructs a new MySnake object with the specified initial position.
		 *
		 * @param x The initial x-coordinate.
		 * @param y The initial y-coordinate.
		 */
		public SnakeGame(int x, int y)
		{
			this.isAvailable = true;
			this.x = x;
			this.y = y;
			this.image = ImageUtil.images.get("snake-body");
			this.width = image.getWidth(null);
			this.height = image.getHeight(null);

			this.speed_XY = 5;
			this.length = 1;

			/*
			 * Attention : ?
			 */
			this.num = width / speed_XY;
			newImgSnakeHead = IMG_SNAKE_HEAD;

		}

		/**
		 * Retrieves the current length of the snake.
		 *
		 * @return the length of thr snake.
		 */
		public int getLength()
		{
			return length;
		}

		/**
		 * Changes the length of the snake.
		 *
		 * @param length The new length of the snake.
		 */
		public void changeLength(int length)
		{
			this.length = length;
		}

		/**
		 * Handles key presses to change tge direct of the snake.
		 *
		 * @param e The KeyEvent object representing the key press.
		 */
		public void keyPressed(KeyEvent e)
		{
			// Check the key
			switch (e.getKeyCode())
			{
			case KeyEvent.VK_UP:
				if (!down)
				{
					up = true;
					down = false;
					left = false;
					right = false;

					newImgSnakeHead = (BufferedImage) GameUtil.rotateImage(IMG_SNAKE_HEAD, -90);
				}
				break;

			case KeyEvent.VK_DOWN:
				if (!up)
				{
					up = false;
					down = true;
					left = false;
					right = false;

					newImgSnakeHead = (BufferedImage) GameUtil.rotateImage(IMG_SNAKE_HEAD, 90);
				}
				break;

			case KeyEvent.VK_LEFT:
				if (!right)
				{
					up = false;
					down = false;
					left = true;
					right = false;

					newImgSnakeHead = (BufferedImage) GameUtil.rotateImage(IMG_SNAKE_HEAD, -180);

				}
				break;

			case KeyEvent.VK_RIGHT:
				if (!left)
				{
					up = false;
					down = false;
					left = false;
					right = true;

					newImgSnakeHead = IMG_SNAKE_HEAD;
				}

			default:
				break;
			}
		}


		public void move()
		{
			// Let the snake move right
			if (up)
			{
				y -= speed_XY;
			} else if (down)
			{
				y += speed_XY;
			} else if (left)
			{
				x -= speed_XY;
			} else if (right)
			{
				x += speed_XY;
			}

		}

		@Override
		public void draw(Graphics g)
		{
			outofBounds();
			eatBody();

			bodyPoints.add(new Point(x, y));

			if (bodyPoints.size() == (this.length + 1) * num)
			{
				bodyPoints.remove(0);
			}
			g.drawImage(newImgSnakeHead, x, y, null);
			drawBody(g);

			move();
		}

		public void eatBody()
		{
			for (Point point : bodyPoints)
			{
				for (Point point2 : bodyPoints)
				{
					if (point.equals(point2) && point != point2)
					{
						this.isAvailable = false;
					}
				}
			}
		}

		public void drawBody(Graphics g)
		{
			int length = bodyPoints.size() - 1 - num;

			for (int i = length; i >= num; i -= num)
			{
				Point point = bodyPoints.get(i);
				g.drawImage(this.image, point.x, point.y, null);
			}
		}

		private void outofBounds()
		{
			boolean xOut = (x <= 0 || x >= (870 - width));
			boolean yOut = (y <= 40 || y >= (560 - height));
			if (xOut || yOut)
			{
				isAvailable = false;
			}
		}
	}

	public abstract static class SnakeObject
	{
		int x;
		int y;
		Image image;
		int width;
		int height;

		public boolean isAvailable;


		public abstract void draw(Graphics g);

		public Rectangle getRectangle()
		{
			return new Rectangle(x, y, width, height);
		}
	}

	public void resetGame() {
		// Reset the score and length of the snake to their initial values
		SnakeGame.score = 0; // Access score via the SnakeGame class
		SnakeGame.length = 1; // Access length via the SnakeGame class
		SnakeGame.bodyPoints.clear(); // Access bodyPoints via the SnakeGame class
		// Reset other properties like position, direction, etc.
		// ...
	}

	public void closeFrame() {
		jFrame.dispose();
	}
}
