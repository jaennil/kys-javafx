package Controller;

import Other.Hash;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class Authorization implements Initializable {
    private final Model.Authorization model = new Model.Authorization();
    public Label emptyIdNumberLabel;
    public Label emptyPasswordLabel;
    public Button signInButton;
    public TextField captchaField;
    public Label captchaInfoLabel;
    public CheckBox rememberMeCheckbox;
    @FXML
    private TextField idNumberField;
    @FXML
    private TextField passwordField;
    @FXML
    private ImageView imageView;

    public void onClickSignInButton() {
        model.signIn();
    }

    public void onClickSignUpButton() {
        model.signUp();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model.handleRememberedUser();
        model.generateCaptcha();
        idNumberField.textProperty().bindBidirectional(model.idNumber);
        passwordField.textProperty().bindBidirectional(model.password);
        idNumberField.textProperty().addListener((observable, oldValue, newValue) -> model.handleIdNumberHighlight());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> model.handlePasswordHighlight());
        captchaField.textProperty().bindBidirectional(model.captchaInputProperty);
        captchaInfoLabel.textProperty().bindBidirectional(model.captchaInfoProperty);

        rememberMeCheckbox.selectedProperty().bindBidirectional(model.rememberMe);

        signInButton.disableProperty().bindBidirectional(model.tooManyAttempts);

        idNumberField.styleProperty().bindBidirectional(model.idNumberFieldStyle);
        passwordField.styleProperty().bindBidirectional(model.passwordFieldStyle);

        imageView.imageProperty().bindBidirectional(model.captchaImage);

        emptyIdNumberLabel.visibleProperty().bindBidirectional(model.idNumberEmpty);
        emptyPasswordLabel.visibleProperty().bindBidirectional(model.passwordEmpty);
    }
}
