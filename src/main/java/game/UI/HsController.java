package game.UI;

import game.FileLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Controller for the high scores page.
 */
public class HsController {
    @FXML
    private GridPane board;

    @FXML
    private void initialize() {
        board.getChildren().clear();

        for (int i = 1; i < board.getRowCount(); i++) {
            addScore(i);
        }
    }

    private void addScore(int i) {
        if(MazeApplication.getHighScores() == null || MazeApplication.getHighScores().getScores().length == 0) {
            return;
        }

        if (MazeApplication.getHighScores().getScores()[i - 1] == null
        || MazeApplication.getHighScores().getScores()[i - 1].getName() == null) {
            return;
        }

        var name = new Text(MazeApplication.getHighScores().getScores()[i - 1].getName());
        name.setFont(Font.font("System", 24));

        var time = new Text(DurationFormatUtils.formatDuration(MazeApplication.getHighScores().getScores()[i - 1].getTime(), "mm:ss.SS"));
        time.setFont(Font.font("System", 24));

        board.add(name, 0, i);
        GridPane.setHalignment(name, HPos.CENTER);

        board.add(time, 1, i);
        GridPane.setHalignment(time, HPos.CENTER);
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        MazeApplication.setScene(scene);
    }

    @FXML
    private void handleSaveAs(ActionEvent event) {
        performSaveAs();
    }

    @FXML
    private void handleLoad(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open save file");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            Logger.debug("Opening file {}", file);
            try {
                FileLoader.loadHighScores(file.getPath());

                MazeApplication.setHighScores(FileLoader.getHighScores());

                MazeApplication.getHighScores().getFilePath().set(file.getPath());

                initialize();
            } catch (IOException e) {
                Logger.error("Failed to open file");
            }
        }
    }

    private void performSaveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            Logger.debug("Saving file as {}", file);

            try {
                MazeApplication.getHighScores().saveAs(file.getPath());
            } catch (IOException e) {
                Logger.error(e, "Failed to save file");
            }
        }
    }
}
