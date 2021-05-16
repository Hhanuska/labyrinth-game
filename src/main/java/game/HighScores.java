package game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

/**
 * Class for handling high scores.
 */
@Data
public class HighScores {
    private int MAX_LENGTH = 5;

    private HighScore[] scores;

    /**
     * Attempt to add a score to the high scores.
     * If the score is worse than all of the high scores and the list is full, nothing happens
     *
     * @param hs {@link HighScore}
     */
    public void addScore(HighScore hs) {
        if (scores.length == 0) {
            scores = new HighScore[MAX_LENGTH];
        }

        boolean found = false;
        boolean added = false;

        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == null || scores[i].getName() == null) {
                break;
            }

            if (scores[i].getName().equals(hs.getName())) {
                found = true;

                if (scores[i].getTime() > hs.getTime()) {
                    scores[i].setTime(hs.getTime());

                    added = true;
                }

                break;
            }
        }

        if (!found) {
            int replaceIndex = getIndexToReplace();

            if ((scores[replaceIndex] == null || scores[replaceIndex].getName() == null)|| scores[replaceIndex].getTime() > hs.getTime()) {
                scores[replaceIndex] = new HighScore(hs.getName(), hs.getTime());

                added = true;
            }
        }

        if (added) {
            sortByTime();
        }
    }

    private int getIndexToReplace() {
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == null || scores[i].getTime() == null) {
                return i;
            }
        }

        return MAX_LENGTH - 1;
    }

    private void sortByTime() {
        for (int i = 0; i < scores.length - 1; i++) {
            if (scores[i] == null || scores[i].getTime() == null) {
                break;
            }

            for (int j = i + 1; j < scores.length; j++) {
                if (scores[j] == null || scores[j].getTime() == null) {
                    break;
                }

                if (scores[i].getTime() > scores[j].getTime()) {
                    var temp = scores[i];
                    scores[i] = scores[j];
                    scores[j] = temp;
                }
            }
        }
    }
}
