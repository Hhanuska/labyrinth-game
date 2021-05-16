package game;

import com.fasterxml.jackson.databind.util.JSONPObject;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.tinylog.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import javafx.scene.input.KeyEvent;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Class responsible for game controls.
 */
public class GameController {
    @FXML
    private GridPane board;

    @FXML
    private Label stopwatchLabel;

    private final Maze model = MazeRepository.getLevel();

    private final Stopwatch stopwatch = new Stopwatch();

    private FileWriter file;

    @FXML
    private void initialize() {
        Logger.info("Initializing UI...");

        model.restart();
        model.initialize();

        for (int i = 1; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                drawWalls(i, j);

                createBall(i, j);

                drawFinish(i, j);
            }
        }

        board.setOnKeyPressed(this::handleKeyPress);

        stopwatchLabel.textProperty().bind(stopwatch.mmssSSProperty());
        stopwatch.start();
    }

    private void drawWalls(int i, int j) {
        int strokeWidth = 2;

        if ((model.getCells()[i-1][j].get() & 0b1) > 0) {
            var piece = new Line(0, 0, 100, 0);
            piece.setStrokeWidth(strokeWidth);
            board.add(piece, j, i);
            GridPane.setValignment(piece, VPos.TOP);
        }

        if ((model.getCells()[i-1][j].get() & 0b10) > 0) {
            var piece = new Line(0, 0, 0, 100);
            piece.setStrokeWidth(strokeWidth);
            board.add(piece, j, i);
            GridPane.setHalignment(piece, HPos.RIGHT);
        }

        if ((model.getCells()[i-1][j].get() & 0b100) > 0) {
            var piece = new Line(0, 0, 100, 0);
            piece.setStrokeWidth(strokeWidth);
            board.add(piece, j, i);
            GridPane.setValignment(piece, VPos.BOTTOM);
        }

        if ((model.getCells()[i-1][j].get() & 0b1000) > 0) {
            var piece = new Line(0, 0, 0, 100);
            piece.setStrokeWidth(strokeWidth);
            board.add(piece, j, i);
            GridPane.setHalignment(piece, HPos.LEFT);
        }
    }

    private void createBall(int i, int j) {
        var piece = new Circle(40);
        piece.fillProperty().bind(Bindings.when((model.getCells()[i-1][j].greaterThanOrEqualTo(0b10000)
                .and(model.getCells()[i-1][j].lessThan(0b100000))
                .or(model.getCells()[i-1][j].greaterThanOrEqualTo(0b110000))))
                .then(Color.BLUE)
                .otherwise(Color.TRANSPARENT)
        );
        board.add(piece, j, i);
        GridPane.setValignment(piece, VPos.CENTER);
        GridPane.setHalignment(piece, HPos.CENTER);
    }

    private void drawFinish(int i, int j) {
        if (model.getCells()[i-1][j].get() < 0b100000) {
            return;
        }

        var piece = new Text("FINISH");
        board.add(piece, j, i);
        GridPane.setHalignment(piece, HPos.CENTER);
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        KeyCode code = event.getCode();

        switch (code) {
            case DOWN -> model.moveRecursive(Direction.DOWN);
            case UP -> model.moveRecursive(Direction.UP);
            case LEFT -> model.moveRecursive(Direction.LEFT);
            case RIGHT -> model.moveRecursive(Direction.RIGHT);
        }

        checkFinished();
    }

    @FXML
    private void handleRestart(ActionEvent event) {
        model.restart();
        stopwatch.stop();
        stopwatch.reset();

        initialize();

        MazeApplication.getScene().getRoot().requestFocus();
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        MazeApplication.setScene(scene);
    }

    private void checkFinished() {
        if (!model.isFinished()) {
            return;
        }

        stopwatch.stop();

        Logger.info(
                "Game finished by {} in {}",
                MazeApplication.getName(),
                stopwatch.mmssSSProperty().getValue()
        );

        MazeApplication.getHighScores().addScore(new HighScore(MazeApplication.getName(), stopwatch.millisProperty().get()));

        try {
            file = new FileWriter("target/classes/game/highscores.json");

            JSONObject obj = new JSONObject();
            obj.put("scores", scoresToJson());

            file.write(obj.toJSONString());
        } catch (IOException e) {
            Logger.error(e);
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                Logger.error(e);
            }
        }
    }

    private JSONArray scoresToJson() {
        JSONArray arr = new JSONArray();

        for (int i = 0; i < MazeApplication.getHighScores().getScores().length; i++) {
            JSONObject obj = new JSONObject();
            if (MazeApplication.getHighScores().getScores()[i] != null) {
                obj.put("name", MazeApplication.getHighScores().getScores()[i].getName());
                obj.put("time", MazeApplication.getHighScores().getScores()[i].getTime());
            }

            arr.add(obj);
        }

        return arr;
    }
}
