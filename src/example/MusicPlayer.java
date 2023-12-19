package example;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Random;
import java.awt.Graphics2D;

import javazoom.jl.player.Player;

/**
 * The MusicPlayer class provides functionality for playing music in the game.
 * It extends the Thread class to allow for concurrent music playback.
 */
public class MusicPlayer extends Thread
{
	/** The filename of the music file to be played. */
	private String filename;
	/** The player object responsible for playing th music. */
	private Player player;
	private final Object lock = new Object();
	private boolean loop;
	private boolean stopped;
	public boolean isPlaying;

	/**
	 * Constructs a new MusicPlayer with the specified filename
	 *
	 * @param filename The filename of the music file to be played.
	 */
	public MusicPlayer(String filename, boolean loop)
	{
		this.filename = filename;
		this.loop = loop;
		this.stopped = false;
		this.isPlaying = false;
	}

	public void setLoop(boolean loop){
		this.loop = loop;
	}

	public void stopMusic() {
		synchronized (lock) {
			stopped = true;
			if (player != null) {
				player.close();
				isPlaying = false;
			}
		}
	}

	public void toggle() {
		synchronized (lock) {
			if (!isPlaying) {
				play();
			} else {
				stopMusic();
				isPlaying = false;
			}
		}
	}

	/**
	 * Initiates the music playback in a separate thread.
	 */
	public void play()
	{
		synchronized (lock) {
			if (isPlaying) {
				return; // Already playing
			}
			isPlaying = true;
			stopped = false;
			}
		new Thread()
		{
			@Override
			public void run()
			{
				do {
					try {
						// Open a buffered input stream and create a player for the specified music file.
						//BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(filename));

						player = new Player(new BufferedInputStream(new FileInputStream(filename)));
						player.play();

					} catch (Exception e) {
						System.out.println("Error playing music file: " + e);
					}
				} while (loop & !stopped);
				isPlaying = false;
			}
		}.start();
	}


	/**
	 * Static method to simplify the process of creating and playing a MusicPlayer instance.
	 *
	 * @param filename The filename of the music file to be played.
	 */
	public void resetMusic() {
		synchronized (lock) {
			if (player != null) {
				player.close();  // Stop current playback
			}
			isPlaying = false;
			stopped = true; // Ensure that the current play loop stops

			// Reset the player for future use
			player = null;
		}
	}
}
