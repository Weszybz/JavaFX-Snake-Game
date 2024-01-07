package view;

import java.util.*;
import java.nio.file.*;
import java.io.*;

/**
 * Manages both individual player's name and score and the collection of high scores.
 * Responsible for saving, retrieving, and sorting high scores.
 */
public class Leaderboard implements Comparable<Leaderboard> {
    private String name; // name of the player
    private int score; // score of the player

    private static final String HIGH_SCORE_FILE = "leaderboard.txt"; // file for high scores
    private static List<Leaderboard> highScores = new ArrayList<>(); // list to manage player scores

    public Leaderboard(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Leaderboard other) {
        return Integer.compare(other.score, this.score); // Orders the scores.
    }

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

    public static List<Leaderboard> getHighScores() {
        return highScores;
    }
}
