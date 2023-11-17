package Controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Connecting implements Initializable {
    private final Model.Connecting model = new Model.Connecting();
    @FXML
    private Label connectionProgress;

    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectionProgress.textProperty().bindBidirectional(model.connectionResultProperty());
        delay(100, model::connectToDatabase);
    }
}
