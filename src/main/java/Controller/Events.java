package Controller;

import Other.Database;
import Other.Event;
import Other.SelectedEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.App;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Events{

    @FXML
    private TableView<Event> eventsTable;

    @FXML
    private TableColumn<Event, ImageView> eventPhotoColumn;
    public TableColumn<Event, String> eventDirectionColumn;

    @FXML
    private TableColumn<Event, String> eventNameColumn;

    @FXML
    private TableColumn<Event, String> eventDateColumn;


    public void initialize() {
//        eventPhotoColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        eventDirectionColumn.setCellValueFactory(new PropertyValueFactory<>("field"));
        eventsTable.setOnMouseClicked(event -> {
            Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                System.out.println("Clicked on row: " + selectedEvent.getName());
                SelectedEvent.getInstance().setEvent(selectedEvent);
                try {
                    App.setRoot("DetailedEvent");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        loadEventData();
    }

    private void loadEventData() {
        try {
            Database database = Database.getInstance();
            Connection connection = database.getConnection();
            String query = "SELECT * FROM Event";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Event> eventList = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                int days = resultSet.getInt("days");
                String imagePath = resultSet.getString("photo");
                ImageView photo = new ImageView(new Image(getClass().getResourceAsStream("/events/"+imagePath)));
                int cityId = resultSet.getInt("city_id");
                String field = resultSet.getString("field");
                String description = resultSet.getString("description");


                Event event = new Event(id, name, date, days, cityId, photo, field, description);
                eventList.add(event);
            }
            eventsTable.getItems().addAll(eventList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void openLogin(ActionEvent actionEvent) {
        try {
            App.setRoot("authorization");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
