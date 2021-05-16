package game;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class MenuController {
    @FXML
    private GridPane menu;

    @FXML
    private void handleStart(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ui.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        MazeApplication.setScene(scene);
        scene.getRoot().requestFocus();
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Logger.info("Exiting...");

        Platform.exit();
    }
}
