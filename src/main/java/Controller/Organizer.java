package Controller;

import javafx.scene.input.MouseEvent;
import org.example.App;

import java.io.IOException;

public class Organizer {
    private final Model.Organizer model = new Model.Organizer();
    public void logOut(MouseEvent event) {
        model.logOut();
    }
}
