package Controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.example.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DetailedEvent implements Initializable {
    private final Model.DetailedEvent model = new Model.DetailedEvent();
    public Label name;
    public ImageView image;
    public Label date;
    public Label city;
    public Label organizer;
    public Label description;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.init();
        name.textProperty().bindBidirectional(model.name);
        date.textProperty().bindBidirectional(model.date);
        city.textProperty().bindBidirectional(model.city);
        organizer.textProperty().bindBidirectional(model.organizer);
        description.textProperty().bindBidirectional(model.description);
        image.imageProperty().bindBidirectional(model.image);
    }

    public void back(MouseEvent mouseEvent) {
        try {
            App.setRoot("Events");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
