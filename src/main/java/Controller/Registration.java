package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class Registration implements Initializable {
    private final Model.Registration model = new Model.Registration();
    public TextField usernameField;
    public TextField passwordField;
    public TextField firstnameField;
    public TextField surnameField;
    public Label surnameEmptyLabel;
    public Label passwordEmptyLabel;
    public TextField lastnameField;
    public Label firstnameEmptyLabel;
    public Label lastnameEmptyLabel;
    public Label usernameEmptyLabel;

    @FXML
    protected void onClickSignUpButton() {
        model.setFirstname(firstnameField.getText());
        model.setSurname(surnameField.getText());
        model.setLastname(lastnameField.getText());
        model.setUsername(usernameField.getText());
        model.setPassword(passwordField.getText());
        model.addUser();
    }

    @FXML
    private void onClickSignInButton() {
        model.signIn();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstnameField.textProperty().addListener((observable, oldValue, newValue) -> model.setFirstname(newValue));
        surnameField.textProperty().addListener((observable, oldValue, newValue) -> model.setSurname(newValue));
        lastnameField.textProperty().addListener((observable, oldValue, newValue) -> model.setLastname(newValue));
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> model.setUsername(newValue));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> model.setPassword(newValue));

        firstnameField.styleProperty().bindBidirectional(model.firstnameFieldStyle);
        surnameField.styleProperty().bindBidirectional(model.surnameFieldStyle);
        lastnameField.styleProperty().bindBidirectional(model.lastnameFieldStyle);
        usernameField.styleProperty().bindBidirectional(model.usernameFieldStyle);
        passwordField.styleProperty().bindBidirectional(model.passwordFieldStyle);

        firstnameEmptyLabel.visibleProperty().bindBidirectional(model.firstnameEmpty);
        surnameEmptyLabel.visibleProperty().bindBidirectional(model.surnameEmpty);
        lastnameEmptyLabel.visibleProperty().bindBidirectional(model.lastnameEmpty);
        usernameEmptyLabel.visibleProperty().bindBidirectional(model.usernameEmpty);
        passwordEmptyLabel.visibleProperty().bindBidirectional(model.passwordEmpty);
    }
}
