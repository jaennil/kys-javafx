package Controller;
import Other.AuthenticatedUser;
import Other.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public class Organizer {

    @FXML
    private Label greetingLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView userPhoto;
    Model.Organizer model = new Model.Organizer();

    public void initialize() {
        model.init();
        greetingLabel.textProperty().bindBidirectional(model.greeting);
        userPhoto.imageProperty().bindBidirectional(model.image);
        nameLabel.textProperty().bindBidirectional(model.name);
    }

    public void logOut(MouseEvent mouseEvent) {
        model.logOut();
    }

    public void myProfile(ActionEvent actionEvent) {
        model.myProfile();
    }

    public void events(ActionEvent actionEvent) {
    }

    public void participants(ActionEvent actionEvent) {
    }

    public void jury(ActionEvent actionEvent) {
    }
}
