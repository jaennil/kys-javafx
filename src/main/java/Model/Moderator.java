package Model;

import org.example.App;

import java.io.IOException;

public class Moderator {
    public void logOut() {
        try {
            App.setRoot("authorization");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
