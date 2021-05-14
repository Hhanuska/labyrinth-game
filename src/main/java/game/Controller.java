package game;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import org.tinylog.Logger;

import javafx.scene.input.KeyEvent;

public class Controller {
    @FXML
    private GridPane board;

    private final Maze model = new MazeRepository().getLevel();

    @FXML
    private void initialize() {
        Logger.info("Initializing UI...");

        model.initialize();

        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                drawWalls(i, j);

                createBall(i, j);

                drawFinish(i, j);
            }
        }

        board.setOnKeyPressed(this::handleKeyPress);
    }

    private void drawWalls(int i, int j) {
        int strokeWidth = 2;

        if ((model.getCells()[i][j].get() & 0b1) > 0) {
            var piece = new Line(0, 0, 100, 0);
            piece.setStrokeWidth(strokeWidth);
            board.add(piece, j, i);
            GridPane.setValignment(piece, VPos.TOP);
        }

        if ((model.getCells()[i][j].get() & 0b10) > 0) {
            var piece = new Line(0, 0, 0, 100);
            piece.setStrokeWidth(strokeWidth);
            board.add(piece, j, i);
            GridPane.setHalignment(piece, HPos.RIGHT);
        }

        if ((model.getCells()[i][j].get() & 0b100) > 0) {
            var piece = new Line(0, 0, 100, 0);
            piece.setStrokeWidth(strokeWidth);
            board.add(piece, j, i);
            GridPane.setValignment(piece, VPos.BOTTOM);
        }

        if ((model.getCells()[i][j].get() & 0b1000) > 0) {
            var piece = new Line(0, 0, 0, 100);
            piece.setStrokeWidth(strokeWidth);
            board.add(piece, j, i);
            GridPane.setHalignment(piece, HPos.LEFT);
        }
    }

    private void createBall(int i, int j) {
        var piece = new Circle(40);
        piece.fillProperty().bind(Bindings.when((model.getCells()[i][j].greaterThanOrEqualTo(0b10000)
                .and(model.getCells()[i][j].lessThan(0b100000))
                .or(model.getCells()[i][j].greaterThanOrEqualTo(0b110000))))
                .then(Color.BLUE)
                .otherwise(Color.TRANSPARENT)
        );
        board.add(piece, j, i);
        GridPane.setValignment(piece, VPos.CENTER);
        GridPane.setHalignment(piece, HPos.CENTER);
    }

    private void drawFinish(int i, int j) {
        if (model.getCells()[i][j].get() < 0b100000) {
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
    }
}
