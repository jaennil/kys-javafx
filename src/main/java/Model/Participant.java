package Model;

import javafx.scene.input.MouseEvent;
import org.example.App;

import java.io.IOException;

public class Participant {
    public void logOut() {
        try {
            App.setRoot("authorization");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
