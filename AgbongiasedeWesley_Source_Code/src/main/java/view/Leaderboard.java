package view;

import java.util.*;
import java.nio.file.*;
import java.io.*;

/**
 * @Project Leaderboard
 * @Description Manages both individual player's name and score and the collection of high scores.
 * Responsible for saving, retrieving, and sorting high scores.
 * @Author Wesley Agbongiasede
 * @version 1.0
 */
public class Leaderboard implements Comparable<Leaderboard> {
    private final String name;
    private final int score;
    private static final String HIGH_SCORE_FILE = "leaderboard.txt";
    private static final List<Leaderboard> highScores = new ArrayList<>();

    /**
     * Constructs a new Leaderboard instance with the given name and score.
     *
     * @param name  the name of the player
     * @param score the score of the player
     */
    public Leaderboard(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Returns the name of the player.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the score of the player.
     *
     * @return the score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * Compares this Leaderboard object with the specified Leaderboard object for order.
     * Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * The order is based on the score of the Leaderboard objects, where higher scores are considered greater.
     *
     * @param other the Leaderboard object to be compared
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object
     */
    @Override
    public int compareTo(Leaderboard other) {
        return Integer.compare(other.score, this.score); // Orders the scores.
    }

    /**
     * Saves the high score for a player.
     *
     * @param playerName the name of the player
     * @param score the score of the player
     */
    public static void saveHighScore(String playerName, int score) {
        highScores.add(new Leaderboard(playerName, score));
        Collections.sort(highScores);

        while (highScores.size() > 10) {
            highScores.remove(highScores.size() - 1);
        }

        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(HIGH_SCORE_FILE))) {
            for (Leaderboard record : highScores) {
                bw.write(record.getName() + "," + record.getScore());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving high scores: " + e.getMessage());
        }
    }

    /**
     * Returns a list of high scores in the leaderboard.
     *
     * @return a list of high scores in the leaderboard
     */
    public static List<Leaderboard> getHighScores() {
        return highScores;
    }
}
