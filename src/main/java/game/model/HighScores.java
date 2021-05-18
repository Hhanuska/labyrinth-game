package game.model;

import game.UI.MazeApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class for handling high scores.
 */
@Data
@NoArgsConstructor
public class HighScores {
    private int MAX_LENGTH = 5;

    private HighScore[] scores;

    private StringProperty filePath = new SimpleStringProperty(null);

    /**
     * Attempt to add a score to the high scores.
     * If the score is worse than all of the high scores and the list is full, nothing happens
     *
     * @param hs {@link HighScore}
     */
    public void addScore(HighScore hs) {
        if (scores == null || scores.length == 0) {
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

    public void save() throws IOException {
        if (filePath.get() == null) {
            throw new IllegalStateException();
        }

        saveAs(filePath.get());
    }

    public void saveAs(String filePath) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("scores", scoresToJson());

        Files.writeString(Path.of(filePath), obj.toJSONString());

        this.filePath.set(filePath);
    }

    private JSONArray scoresToJson() {
        JSONArray arr = new JSONArray();

        for (int i = 0; i < scores.length; i++) {
            JSONObject obj = new JSONObject();
            if (scores[i] != null) {
                obj.put("name", scores[i].getName());
                obj.put("time", scores[i].getTime());
            }

            arr.add(obj);
        }

        return arr;
    }
}
