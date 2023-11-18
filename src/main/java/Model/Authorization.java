package Model;

import Other.AuthenticatedUser;
import Other.Database;
import Other.Hash;
import com.mewebstudio.captcha.Captcha;
import com.mewebstudio.captcha.GeneratedCaptcha;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.util.Duration;
import org.example.App;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authorization {
    public final StringProperty idNumberFieldStyle = new SimpleStringProperty("");
    public final StringProperty passwordFieldStyle = new SimpleStringProperty("");
    public final StringProperty idNumber = new SimpleStringProperty("");
    public final StringProperty password = new SimpleStringProperty("");
    public final StringProperty captchaInputProperty = new SimpleStringProperty("");
    public final StringProperty captchaInfoProperty = new SimpleStringProperty("");
    public BooleanProperty idNumberEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty passwordEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty tooManyAttempts = new SimpleBooleanProperty(false);
    private final String highlightedStyle = "-fx-border-color: red; -fx-border-width: 2px;";
    private final String defaultStyle = "";
    public final ObjectProperty<Image> captchaImage = new SimpleObjectProperty<>();
    private int captchaAttempts = 0;
    private String captchaCode;


    public void highlightIdNumber() {
        String idNumber = this.idNumber.get();
        idNumberFieldStyle.set(idNumber.isBlank() ? highlightedStyle : defaultStyle);
        idNumberEmpty.set(idNumber.isBlank());
    }

    public void highlightPassword() {
        String password = this.password.get();
        passwordFieldStyle.set(password.isBlank() ? highlightedStyle : defaultStyle);
        passwordEmpty.set(password.isBlank());
    }

    private void handleCorrectCaptcha() {
        System.out.println("captcha true");
        if (idNumber.get().isBlank() || password.get().isBlank())
            return;
        System.out.println(idNumber.get());
        Database database = Database.getInstance();
        String hash = Hash.toString(Hash.hash(password.get()));
        String statement = "SELECT * FROM Participants WHERE idNumber = ? AND password = ?";
        Connection connection = database.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, idNumber.get());
            preparedStatement.setString(2, hash);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                showWrongCredentialsDialog();
                return;
            }
            AuthenticatedUser.initFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadCorrespondingView();
    }

    private void handleWrongCaptcha() {
        captchaAttempts++;
        captchaInfoProperty.set("Wrong captcha. Attempts left: " + (3 - captchaAttempts));
        System.out.println("captcha false");
        System.out.println(captchaAttempts);
        if (captchaAttempts == 3) {
            captchaInfoProperty.set("Authorization locked for 10 seconds. Please wait");
            System.out.println("system locked for 10 seconds");
            captchaAttempts = 0;
            tooManyAttempts.set(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
                System.out.println("unlocked");
                tooManyAttempts.set(false);
                captchaInfoProperty.set("");
            }));
            timeline.setCycleCount(1);
            timeline.play();
        }
    }

    public void signIn() {
        String captchaInput = captchaInputProperty.get();
        if (captchaInput == null) {
            handleWrongCaptcha();
            return;
        }
        if (captchaInput.equals(captchaCode)) {
            handleCorrectCaptcha();
            return;
        }
        handleWrongCaptcha();
    }

    private void loadCorrespondingView() {
        AuthenticatedUser authenticatedUser = AuthenticatedUser.getInstance();
        try {
            App.setRoot(authenticatedUser.getRole());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateCaptcha() {
        Captcha captcha = new Captcha();
        captcha.getConfig().setLength(4);
        GeneratedCaptcha generated = captcha.generate();
        BufferedImage buffered = generated.getImage();
        Image image = SwingFXUtils.toFXImage(buffered, null);
        captchaImage.set(image);
        captchaCode = generated.getCode();
        System.out.println(captchaCode);
    }

    public void showWrongCredentialsDialog() {
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setTitle("Authentication");
        dialog.setHeaderText("Failed to authenticated");
        dialog.setContentText("Wrong idNumber or password");
        dialog.showAndWait();
    }

    public void signUp() {
        try {
            App.setRoot("registration");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
