package example;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Leaderboard {
    private List<PlayerScore> scores = new ArrayList<>();
    private final String leaderboardFileName = "leaderboard.txt"; // Name of the file to store scores

    public Leaderboard() {
        loadScoresFromFile(); // Load scores from file when the Leaderboard is created

        // Register a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Code to execute when the program is force quit
            emptyLeaderboardFile();
        }));
    }

    public void addScore(String name, int score) {
        scores.add(new PlayerScore(name, score));
        Collections.sort(scores);
        updateRankings(); // Update rankings after sorting
        saveScoresToFile(); // Save scores to file after adding a new score
    }

    public String getFormattedScores() {
        StringBuilder sb = new StringBuilder();
        for (PlayerScore score : scores) {
            sb.append(score).append("\n");
        }
        return sb.toString();
    }

    // Load scores from a file
    private void loadScoresFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(leaderboardFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Use regular expression to extract score and rank
                String pattern = "(\\d+)\\s+\\(Rank:\\s+(\\d+)\\)";
                Pattern r = Pattern.compile(pattern);
                Matcher matcher = r.matcher(line);

                if (matcher.find()) {
                    int score = Integer.parseInt(matcher.group(1));
                    int rank = Integer.parseInt(matcher.group(2));
                    scores.add(new PlayerScore("Unknown", score)); // You can set the name to "Unknown" or any default value
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save scores to a file
    private void saveScoresToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(leaderboardFileName))) {
            for (PlayerScore score : scores) {
                writer.write(score.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update the rankings based on the current order of scores
    private void updateRankings() {
        int rank = 1;
        for (PlayerScore score : scores) {
            score.setRank(rank++);
        }
    }

    public List<PlayerScore> getScores() {
        return scores;
    }

    private void emptyLeaderboardFile() {
        try (PrintWriter writer = new PrintWriter(leaderboardFileName)) {
            writer.print(""); // Clear the file content
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static class PlayerScore implements Comparable<PlayerScore> {
        String name;
        int score;
        int rank; // Add a rank field

        PlayerScore(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public int compareTo(PlayerScore o) {
            return Integer.compare(o.score, this.score); // Sort in descending order
        }

        @Override
        public String toString() {
            return name + " - " + score + " (Rank: " + rank + ")"; // Include the rank in the toString
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }
}
