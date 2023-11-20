package Controller;

import javafx.scene.input.MouseEvent;
import org.example.App;

import java.io.IOException;

public class Jury {
    private final Model.Jury model = new Model.Jury();
    public void logOut(MouseEvent event) {
        model.logOut();
    }
}
