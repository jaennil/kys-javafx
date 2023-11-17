package Model;

import Other.AuthenticatedUser;
import Other.Database;
import Other.Hash;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import org.example.App;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authorization {
    public final StringProperty usernameFieldStyle = new SimpleStringProperty();
    public final StringProperty passwordFieldStyle = new SimpleStringProperty();
    public final StringProperty username = new SimpleStringProperty();
    public final StringProperty password = new SimpleStringProperty();
    public BooleanProperty usernameEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty passwordEmpty = new SimpleBooleanProperty(false);
    private final String highlightedStyle = "-fx-border-color: red; -fx-border-width: 2px;";
    private final String defaultStyle = "";

    public void setUsername(String string) {
        this.username.set(string);
        usernameFieldStyle.set(string.isBlank() ? highlightedStyle : defaultStyle);
        usernameEmpty.set(string.isBlank());
    }

    public void setPassword(String string) {
        this.password.set(string);
        passwordFieldStyle.set(string.isBlank() ? highlightedStyle : defaultStyle);
        passwordEmpty.set(string.isBlank());
    }

    public void signIn() {
        if (usernameEmpty.get() || passwordEmpty.get())
            return;
        Database database = Database.getInstance();
        String hash = Hash.toString(Hash.hash(password.get()));
        String statement = "SELECT * FROM people WHERE username = ? AND password_hash = ?";
        Connection connection = database.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, username.get());
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

    private void loadCorrespondingView() {
        AuthenticatedUser authenticatedUser = AuthenticatedUser.getInstance();
        try {
            App.setRoot(authenticatedUser.getRole());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showWrongCredentialsDialog() {
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setTitle("Authentication");
        dialog.setHeaderText("Failed to authenticated");
        dialog.setContentText("Wrong username or password");
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
