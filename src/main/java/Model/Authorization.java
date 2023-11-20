package Model;

import Other.AuthenticatedUser;
import Other.Database;
import Other.Hash;
import com.mewebstudio.captcha.Captcha;
import com.mewebstudio.captcha.GeneratedCaptcha;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import java.util.prefs.Preferences;


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
    public BooleanProperty rememberMe = new SimpleBooleanProperty(false);
    private final String highlightedStyle = "-fx-border-color: red; -fx-border-width: 2px;";
    private final String defaultStyle = "";
    public final ObjectProperty<Image> captchaImage = new SimpleObjectProperty<>();
    private int captchaAttempts = 0;
    private String captchaCode;


    public void handleIdNumberHighlight() {
        String idNumber = this.idNumber.get();
        idNumberFieldStyle.set(idNumber.isBlank() ? highlightedStyle : defaultStyle);
        idNumberEmpty.set(idNumber.isBlank());
    }

    public void handlePasswordHighlight() {
        String password = this.password.get();
        passwordFieldStyle.set(password.isBlank() ? highlightedStyle : defaultStyle);
        passwordEmpty.set(password.isBlank());
    }

    private void handleCorrectCaptcha() {
        System.out.println("captcha is correct");
        captchaInfoProperty.set("");

        String idNumber = this.idNumber.get();
        String password = this.password.get();

        if (idNumber.isBlank()) {
            idNumberEmpty.set(true);
            return;
        }

        if (password.isBlank()) {
            passwordEmpty.set(true);
            return;
        }
        String passwordHash = Hash.toString(Hash.hash(password));
        boolean userAuthenticated = tryAuthenticateUser(idNumber, passwordHash);
        if (userAuthenticated) {
            System.out.println("authorized successfully");
            handleRememberMe();
            loadCorrespondingView();
            return;
        }
        showWrongCredentialsDialog();
    }

    boolean tryAuthenticateUser(String idNumber, String passwordHash) {
        System.out.println(passwordHash);

        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        String[] tables = {"Participant", "Organizer", "Jury", "Moderator"};
        for (String table : tables) {
            String statement = "SELECT * FROM " + table +  " WHERE idNumber = ? AND password_hash = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setString(1, idNumber);
                preparedStatement.setString(2, passwordHash);
                ResultSet user = preparedStatement.executeQuery();
                if (user.next()) {
                    AuthenticatedUser.initFromResultSet(user, table);
                    return true;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
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
        generateCaptcha();
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
        System.out.println("loading corresponding view");
        AuthenticatedUser authenticatedUser = AuthenticatedUser.getInstance();
        Platform.runLater(() -> {
            try {
                App.setRoot(authenticatedUser.getRole());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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

    public void handleRememberedUser() {
        Preferences pref = Preferences.userRoot();
        String idNumber = pref.get("idNumber", "");
        String passwordHash = pref.get("passwordHash", "");
        if (idNumber.isEmpty()) {
            System.out.println("idNumber is empty in handleRememberedUser");
            return;
        }
        if (passwordHash.isEmpty()) {
            System.out.println("password is empty in handleRememberedUser");
            return;
        }
        System.out.println(idNumber);
        System.out.println(passwordHash);
        System.out.println("trying to find remembered user in database");
        boolean userAuthenticated = tryAuthenticateUser(idNumber, passwordHash);
        if (userAuthenticated) {
            System.out.println("authorized successfully");
            loadCorrespondingView();
            return;
        }
        pref.remove("idNumber");
        pref.remove("passwordHash");
        showWrongCredentialsDialog();
    }

    public void handleRememberMe() {
        Preferences pref = Preferences.userRoot();
        String idNumber = this.idNumber.get();
        String password = this.password.get();
        if (rememberMe.get()) {
            if (idNumber.isEmpty()) {
                return;
            }
            if (password.isEmpty()) {
                return;
            }
            pref.put("idNumber", idNumber);
            String hash = Hash.toString(Hash.hash(password));
            pref.put("passwordHash", hash);
        }
    }
}
