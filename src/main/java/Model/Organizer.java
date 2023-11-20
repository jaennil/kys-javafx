package Model;

import Other.AuthenticatedUser;
import Other.Database;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import org.example.App;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public class Organizer {
    public StringProperty greeting = new SimpleStringProperty("");
    public StringProperty name = new SimpleStringProperty("");
    public ObjectProperty<Image> image = new SimpleObjectProperty<>();

    public void logOut() {
        try {
            App.setRoot("authorization");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void init() {
        LocalTime currentTime = LocalTime.now();

        greeting.set(getGreeting(currentTime));

        AuthenticatedUser authenticatedUser = AuthenticatedUser.getInstance();
        name.set(authenticatedUser.getFullname());
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        String statement = "SELECT * FROM Organizer WHERE idNumber = ? AND fullname = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, authenticatedUser.getIdNumber());
            preparedStatement.setString(2, authenticatedUser.getFullname());
            ResultSet user = preparedStatement.executeQuery();
            if (user.next()) {
                String photo = user.getString("photo");
                Image image = new Image("/images/organizers/" + photo);
                this.image.set(image);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private String getGreeting(LocalTime currentTime) {
        if (currentTime.isBefore(LocalTime.NOON)) {
            return "Доброе утро!";
        } else if (currentTime.isBefore(LocalTime.of(18, 0))) {
            return "Добрый день!";
        } else {
            return "Добрый вечер!";
        }
    }

    public void myProfile() {
        try {
            App.setRoot("myProfile");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
