package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Authorization implements Initializable {
    private final Model.Authorization model = new Model.Authorization();
    public Label emptyUsernameLabel;
    public Label emptyPasswordLabel;
    public Button signInButton;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    public void onClickSignInButton(ActionEvent actionEvent) {
        model.setUsername(usernameField.getText());
        model.setPassword(passwordField.getText());
        model.signIn();
    }

    public void onClickSignUpButton(MouseEvent mouseEvent) {
        model.signUp();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> model.setUsername(newValue));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> model.setPassword(newValue));

        usernameField.styleProperty().bindBidirectional(model.usernameFieldStyle);
        passwordField.styleProperty().bindBidirectional(model.passwordFieldStyle);

        emptyUsernameLabel.visibleProperty().bindBidirectional(model.usernameEmpty);
        emptyPasswordLabel.visibleProperty().bindBidirectional(model.passwordEmpty);
    }
}
