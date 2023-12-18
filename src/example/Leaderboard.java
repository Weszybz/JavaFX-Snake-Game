package example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leaderboard {
    private List<PlayerScore> scores = new ArrayList<>();

    public void addScore(String name, int score) {
        scores.add(new PlayerScore(name, score));
        Collections.sort(scores);
    }

    public String getFormattedScores() {
        StringBuilder sb = new StringBuilder();
        for (PlayerScore score : scores) {
            sb.append(score).append("\n");
        }
        return sb.toString();
    }

    private static class PlayerScore implements Comparable<PlayerScore> {
        String name;
        int score;

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
            return name + " - " + score;
        }
    }
}
