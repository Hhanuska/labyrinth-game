package game;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * Class responsible for interacting with JavaFX.
 */
public class MazeApplication extends Application {

    private static Scene scene;

    private static String name;

    private static HighScores highScores;

    @Override
    public void start(Stage stage) throws IOException {
        MazeRepository.loadLevel();
        MazeRepository.loadHighScores();
        highScores = MazeRepository.getHighScores();

        Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
        stage.setTitle("Maze Game");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene s) {
        scene = s;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String n) {
        name = n;
    }

    public static HighScores getHighScores() {
        return highScores;
    }
}
