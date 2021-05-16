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

    @Override
    public void start(Stage stage) throws IOException {
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
}
