package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class Authorization implements Initializable {
    private final Model.Authorization model = new Model.Authorization();
    public Label emptyIdNumberLabel;
    public Label emptyPasswordLabel;
    public Button signInButton;
    public TextField captchaField;
    public Label captchaInfoLabel;
    @FXML
    private TextField idNumberField;
    @FXML
    private TextField passwordField;
    @FXML
    private ImageView imageView;

    public void onClickSignInButton(ActionEvent actionEvent) {
        model.signIn();
    }

    public void onClickSignUpButton(MouseEvent mouseEvent) {
        model.signUp();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model.generateCaptcha();
        idNumberField.textProperty().bindBidirectional(model.idNumber);
        passwordField.textProperty().bindBidirectional(model.password);
        idNumberField.textProperty().addListener((observable, oldValue, newValue) -> model.highlightIdNumber());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> model.highlightPassword());
        captchaField.textProperty().bindBidirectional(model.captchaInputProperty);
        captchaInfoLabel.textProperty().bindBidirectional(model.captchaInfoProperty);

        signInButton.disableProperty().bindBidirectional(model.tooManyAttempts);

        idNumberField.styleProperty().bindBidirectional(model.idNumberFieldStyle);
        passwordField.styleProperty().bindBidirectional(model.passwordFieldStyle);

        imageView.imageProperty().bindBidirectional(model.captchaImage);

        emptyIdNumberLabel.visibleProperty().bindBidirectional(model.idNumberEmpty);
        emptyPasswordLabel.visibleProperty().bindBidirectional(model.passwordEmpty);
    }
}
