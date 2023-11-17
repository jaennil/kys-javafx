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
    public Label emptyIdNumberLabel;
    public Label emptyPasswordLabel;
    public Button signInButton;
    @FXML
    private TextField idNumberField;
    @FXML
    private TextField passwordField;

    public void onClickSignInButton(ActionEvent actionEvent) {
        model.setIdNumber(idNumberField.getText());
        model.setPassword(passwordField.getText());
        model.signIn();
    }

    public void onClickSignUpButton(MouseEvent mouseEvent) {
        model.signUp();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idNumberField.textProperty().addListener((observable, oldValue, newValue) -> model.setIdNumber(newValue));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> model.setPassword(newValue));

        idNumberField.styleProperty().bindBidirectional(model.idNumberFieldStyle);
        passwordField.styleProperty().bindBidirectional(model.passwordFieldStyle);

        emptyIdNumberLabel.visibleProperty().bindBidirectional(model.idNumberEmpty);
        emptyPasswordLabel.visibleProperty().bindBidirectional(model.passwordEmpty);
    }
}
