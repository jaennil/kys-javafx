package Controller;

import javafx.scene.input.MouseEvent;
import org.example.App;

import java.io.IOException;

public class Moderator {
    private final Model.Moderator model = new Model.Moderator();
    public void logOut(MouseEvent event) {
        model.logOut();
    }
}
