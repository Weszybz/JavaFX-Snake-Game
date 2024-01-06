package view;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

/**
 * The MusicPlayer class handles the playback of background music in the application.
 * It utilizes JavaFX's MediaPlayer to manage music playback, allowing control over play, pause, stop, and mute functionality.
 */
public class MusicPlayer {

	private MediaPlayer mediaPlayer; // Media player which will control playback

	/**
	 * Constructor for MusicPlayer.
	 * Initializes the MusicPlayer with the specified music file.
	 *
	 * @param filePath The file path to the music file.
	 */
	public MusicPlayer(String filePath) {
		try {
			// Creates a media object from the file path and initializes the media player
			Media media = new Media(new File(filePath).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			// Sets the music to loop continuously
			mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO)); // Loop the music
		} catch (Exception e) {
			System.err.println("Error: Music file not found");
			e.printStackTrace();
		}
	}

	/**
	 * Plays the loaded music.
	 */
	public void playMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.play();
		}
	}

	/**
	 * Stops the music playback.
	 */
	public void stopMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}

	/**
	 * Pauses the music playback.
	 */
	public void pauseMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.pause();
		}
	}

	/**
	 * Resumes the paused music playback.
	 */
	public void resumeMusic() {
		if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
			mediaPlayer.play();
		}
	}

	/**
	 * Mutes or unmutes the music based on the provided flag.
	 *
	 * @param mute True to mute the music, false to unmute.
	 */
	public void muteMusic(boolean mute) {
		if (mediaPlayer != null) {
			mediaPlayer.setMute(mute);
		}
	}
}
