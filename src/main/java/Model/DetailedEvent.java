package Model;

import Other.Database;
import Other.Event;
import Other.SelectedEvent;
import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetailedEvent {
    public StringProperty name = new SimpleStringProperty("");
    public StringProperty description = new SimpleStringProperty("");
    public StringProperty organizer = new SimpleStringProperty("");
    public StringProperty city = new SimpleStringProperty("");
    public StringProperty date = new SimpleStringProperty("");
    public final ObjectProperty<Image> image = new SimpleObjectProperty<>();

    public void init() {
        Event event = SelectedEvent.getInstance().getEvent();
        name.set(event.getName());
        date.set(event.getDate());
        image.set(event.getPhoto().getImage());
        description.set(event.getDescription());
        Database database = Database.getInstance();
        String statement = "SELECT * FROM City WHERE id = ?";
        Connection connection = database.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, event.getCityId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                city.set(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//        organizer.set(event.getName());
    }
}
