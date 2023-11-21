package Controller;

import Other.UnmaskedPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    public PasswordField reEnterPasswordField;
    public PasswordField passwordField;
    public Label countryLabel;
    public Label phoneNumberLabel;
    public Label emailLabel;
    public CheckBox visiblePasswordCheckbox;
    public ImageView image;
    public Button okButton;

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
        passwordField.textProperty().bindBidirectional(model.password);
        passwordField.promptTextProperty().bindBidirectional(model.passwordPrompt);
        reEnterPasswordField.textProperty().bindBidirectional(model.reEnterPassword);
        reEnterPasswordField.promptTextProperty().bindBidirectional(model.reEnterPasswordPrompt);
        visiblePasswordCheckbox.selectedProperty().addListener(event -> {
            if (visiblePasswordCheckbox.isSelected()) {
                model.passwordPrompt.set(passwordField.getText());
                passwordField.clear();
                model.reEnterPasswordPrompt.set(reEnterPasswordField.getText());
                reEnterPasswordField.clear();
            } else {
                if (!model.passwordPrompt.get().isEmpty()) {
                    passwordField.setText(model.passwordPrompt.get());
                }
                reEnterPasswordField.setText(model.reEnterPasswordPrompt.get());
            }
        });

    }

    public void handleOkButton(ActionEvent actionEvent) {
        model.handleOkButton();
    }
}
