package Model;

import Other.AuthenticatedUser;
import Other.Database;
import Other.Hash;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.example.App;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Admin {
    public ObservableList<String> roles = FXCollections.observableArrayList("employee", "doctor");
    public StringProperty firstnameFieldStyle = new SimpleStringProperty();
    public final StringProperty surnameFieldStyle = new SimpleStringProperty();
    public final StringProperty lastnameFieldStyle = new SimpleStringProperty();
    public final StringProperty usernameFieldStyle = new SimpleStringProperty();
    public final StringProperty passwordFieldStyle = new SimpleStringProperty();
    public final StringProperty numberFieldStyle = new SimpleStringProperty();
    public final StringProperty addressFieldStyle = new SimpleStringProperty();
    public final StringProperty roleComboBoxStyle = new SimpleStringProperty();
    public final StringProperty firstname = new SimpleStringProperty();
    public final StringProperty surname = new SimpleStringProperty();
    public final StringProperty lastname = new SimpleStringProperty();
    public final StringProperty username = new SimpleStringProperty();
    public final StringProperty password = new SimpleStringProperty();
    public final StringProperty number = new SimpleStringProperty();
    public final StringProperty address = new SimpleStringProperty();
    public final StringProperty role = new SimpleStringProperty();
    public BooleanProperty firstnameEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty surnameEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty lastnameEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty usernameEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty numberEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty addressEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty passwordEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty roleEmpty = new SimpleBooleanProperty(false);
    public AuthenticatedUser authenticatedUser = AuthenticatedUser.getInstance();
    public StringProperty welcomeText = new SimpleStringProperty("Welcome, " + authenticatedUser.getWelcomeName());
    private final String highlightedStyle = "-fx-border-color: red; -fx-border-width: 2px;";
    private final String defaultStyle = "";

    public void setFirstname(String string) {
        this.firstname.set(string);
        firstnameFieldStyle.set(string.isBlank() ? highlightedStyle : defaultStyle);
        firstnameEmpty.set(string.isBlank());
    }

    public void setSurname(String string) {
        this.surname.set(string);
        surnameFieldStyle.set(string.isBlank() ? highlightedStyle : defaultStyle);
        surnameEmpty.set(string.isBlank());
    }

    public void setLastname(String string) {
        this.lastname.set(string);
        lastnameFieldStyle.set(string.isBlank() ? highlightedStyle : defaultStyle);
        lastnameEmpty.set(string.isBlank());
    }

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

    public void setRole(String string) {
        this.role.set(string);
        roleComboBoxStyle.set(string == null ? highlightedStyle : defaultStyle);
        roleEmpty.set(string == null);
    }
    public void setNumber(String string) {
        this.number.set(string);
        numberFieldStyle.set(string == null ? highlightedStyle : defaultStyle);
        numberEmpty.set(string == null);
    }
    public void setAddress(String string) {
        this.address.set(string);
        addressFieldStyle.set(string == null ? highlightedStyle : defaultStyle);
        addressEmpty.set(string == null);
    }

    public void showInformationDialog() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Adding employee");
        dialog.setHeaderText("Successfully added employee");
        dialog.setContentText(surname.get() + " " + firstname.get() + " " + lastname.get());
        dialog.showAndWait();
    }

    private void nullValues() {
        firstname.set("");
        surname.set("");
        lastname.set("");
        username.set("");
        password.set("");
        number.set("");
        address.set("");
        role.set("");
    }

    public void addUser() {
        if (firstnameEmpty.get() || surnameEmpty.get() || lastnameEmpty.get() || usernameEmpty.get() || passwordEmpty.get() || roleEmpty.get())
            return;
        Database database = Database.getInstance();
        String statement = "insert into people (firstname, surname, lastname, address, phone_number, username, password_hash, role) value (?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(statement)) {
            preparedStatement.setString(1, firstname.get());
            preparedStatement.setString(2, surname.get());
            preparedStatement.setString(3, lastname.get());
            preparedStatement.setString(4, address.get());
            preparedStatement.setString(5, number.get());
            preparedStatement.setString(6, username.get());
            preparedStatement.setString(7, Hash.toString(Hash.hash(password.get())));
            preparedStatement.setString(8, role.get());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        showInformationDialog();
        nullValues();
    }

    public void logOut() {
        try {
            App.setRoot("authorization");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
