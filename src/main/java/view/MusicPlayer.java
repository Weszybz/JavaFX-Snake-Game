package view;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @Project Music Player
 * @Description Handles the playback of game music and sound effects using JavaFX's MediaPlayer. Manages play, stop, pause, and mute functionalities for both background music and specific sound effects like death sound.
 * @Author Wesley Agbongiasede - modified
 * @version 1.0
 */
public class MusicPlayer {
	private MediaPlayer mediaPlayer;
	private MediaPlayer deathSoundPlayer;

	/**
	 * Starts playing the background music.
	 *
	 * This method loads the music file "frogger.mp3" and starts playing it using JavaFX's MediaPlayer class.
	 * The music is played indefinitely in a loop.
	 *
	 * If the music file fails to load or an exception occurs during the process, an error message is printed
	 * and the exception is printed to the standard error stream.
	 *
	 * Example usage:
	 *     MusicPlayer musicPlayer = new MusicPlayer();
	 *     musicPlayer.startMusic();
	 */
	public void startMusic() {try {
		String froggerSound = getClass().getResource("/frogger.mp3").toExternalForm();
		Media media = new Media(froggerSound);
		mediaPlayer = new MediaPlayer(media);
	} catch (Exception e) {
		System.out.println("Error loading frogger sound.");
		e.printStackTrace();
	}
		if (mediaPlayer != null) {
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
			mediaPlayer.play();
		}
	}

	/**
	 * Stops the currently playing music.
	 *
	 * If a MediaPlayer object is currently playing music, this method stops it.
	 *
	 * Example usage:
	 *     MusicPlayer musicPlayer = new MusicPlayer();
	 *     musicPlayer.stopMusic();
	 */
	public void stopMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}

	/**
	 * Pauses the currently playing music.
	 *
	 * If a MediaPlayer object is currently playing music, this method pauses it.
	 */
	public void pauseMusic() {
		if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
			mediaPlayer.pause();
		}
	}

	/**
	 * Resumes the currently paused music.
	 *
	 * If a MediaPlayer object is currently playing music and it is in a paused state,
	 * this method resumes the music by calling the play() method on the MediaPlayer object.
	 *
	 * Example usage:
	 *     MusicPlayer musicPlayer = new MusicPlayer();
	 *     musicPlayer.resumeMusic();
	 */
	public void resumeMusic() {
		if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
			mediaPlayer.play();
		}
	}

	/**
	 * Mutes the currently playing music.
	 *
	 * This method checks if a MediaPlayer object is currently playing music.
	 * If it is, this method sets the mute property of the MediaPlayer object to true, which mutes the music.
	 *
	 * Example usage:
	 *
	 * MusicPlayer musicPlayer = new MusicPlayer();
	 * musicPlayer.muteMusic();
	 */
	public void muteMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.setMute(true);
		}
	}

	/**
	 * Unmutes the currently playing music.
	 *
	 * This method checks if a MediaPlayer object is currently playing music.
	 * If it is, this method sets the mute property of the MediaPlayer object to false, which unmutes the music.
	 *
	 * Example usage:
	 *     MusicPlayer musicPlayer = new MusicPlayer();
	 *     musicPlayer.unmuteMusic();
	 */
	public void unmuteMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.setMute(false);
		}
	}

	/**
	 * Initializes the death sound.
	 *
	 * This method loads the death sound file "death.mp3" using the getResource() method of the Class class.
	 * The death sound file is located in the resources folder within the classpath.
	 *
	 * Example usage:
	 *     MusicPlayer musicPlayer = new MusicPlayer();
	 *     musicPlayer.initializeDeathSound();
	 */
	public void initializeDeathSound() {
		try {
			String deathSound = getClass().getResource("/death.mp3").toExternalForm();
			Media deathPlayer = new Media(deathSound);
			deathSoundPlayer = new MediaPlayer(deathPlayer);
		} catch (Exception e) {
			System.out.println("Error loading death sound.");
			e.printStackTrace();
		}
	}

	/**
	 * Plays the death sound if it is initialized.
	 *
	 * This method checks if the death sound player is not null. If it is not null, it stops the current playback and starts playing the death sound.
	 * This method does not return any value.
	 *
	 * Example usage:
	 *     musicPlayer.playDeathSound();
	 */
	public void playDeathSound() {
		if (deathSoundPlayer != null) {
			deathSoundPlayer.stop();
			deathSoundPlayer.play();
		}
	}
}
