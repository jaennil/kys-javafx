package Controller;

import javafx.scene.input.MouseEvent;
import org.example.App;

import java.io.IOException;

public class Participant {
    private final Model.Participant model = new Model.Participant();
    public void logOut(MouseEvent event) {
        model.logOut();
    }
}
