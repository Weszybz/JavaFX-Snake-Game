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
	public Player player;
	private boolean loop;
	private boolean stopped;
	public boolean isPlaying;

	/**
	 * Constructs a new MusicPlayer with the specified filename
	 *
	 * @param filename The filename of the music file to be played.
	 */
	public MusicPlayer(String filename)
	{
		this.filename = filename;
		this.loop = true;
		this.stopped = false;
		this.isPlaying = false;
	}

	public void setLoop(boolean loop){
		this.loop = loop;
	}

	public void stopMusic() {
		stopped = true;
		if (player != null){
			player.close();
			isPlaying = false;
		}
	}

	public void toggle() {
		if (!stopped) {
			stopMusic();
		} else {
			play();
		}
	}

	/**
	 * Initiates the music playback in a separate thread.
	 */
	public void play()
	{
		new Thread()
		{
			@Override
			public void run()
			{
				do {
					try {
						// Open a buffered input stream and create a player for the specified music file.
						//BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(filename));
						stopped = false; // Reset the stopped flag
						player = new Player(new BufferedInputStream(new FileInputStream(filename)));
						player.play();

					} catch (Exception e) {
						System.out.println(e);
					}
				} while (loop & !stopped);
			}
		}.start();
	}


	/**
	 * Static method to simplify the process of creating and playing a MusicPlayer instance.
	 *
	 * @param filename The filename of the music file to be played.
	 */
	public static void getMusicPlay(String filename)
	{
		MusicPlayer musicPlayer = new MusicPlayer(filename);
		musicPlayer.play();
	}
}
