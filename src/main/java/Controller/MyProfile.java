package Controller;

import Other.UnmaskedPasswordField;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MyProfile implements Initializable {
    private final Model.MyProfile model = new Model.MyProfile();
    public Label fullnameLabel;
    public Label genderLabel;
    public Label dobLabel;
    public Label idNumberLabel;
    public UnmaskedPasswordField reEnterPasswordField;
    public UnmaskedPasswordField passwordField;
    public Label countryLabel;
    public Label phoneNumberLabel;
    public Label emailLabel;
    public CheckBox visiblePasswordCheckbox;
    public ImageView image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.init();
        fullnameLabel.textProperty().bindBidirectional(model.fullname);
        genderLabel.textProperty().bindBidirectional(model.gender);
        dobLabel.textProperty().bindBidirectional(model.dob);
        idNumberLabel.textProperty().bindBidirectional(model.idNumber);
        countryLabel.textProperty().bindBidirectional(model.country);
        phoneNumberLabel.textProperty().bindBidirectional(model.phoneNumber);
        emailLabel.textProperty().bindBidirectional(model.email);
        image.imageProperty().bindBidirectional(model.image);
        visiblePasswordCheckbox.selectedProperty().addListener(event -> {
            passwordField.toggleMask();
            System.out.println("visible password checkbox toggled");
        });
    }
}
