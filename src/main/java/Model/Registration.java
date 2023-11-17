package Model;

import Other.Database;
import Other.Hash;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.example.App;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Registration {
    public StringProperty firstnameFieldStyle = new SimpleStringProperty();
    public final StringProperty surnameFieldStyle = new SimpleStringProperty();
    public final StringProperty lastnameFieldStyle = new SimpleStringProperty();
    public final StringProperty usernameFieldStyle = new SimpleStringProperty();
    public final StringProperty passwordFieldStyle = new SimpleStringProperty();
    public final StringProperty firstname = new SimpleStringProperty();
    public final StringProperty surname = new SimpleStringProperty();
    public final StringProperty lastname = new SimpleStringProperty();
    public final StringProperty username = new SimpleStringProperty();
    public final StringProperty password = new SimpleStringProperty();
    public BooleanProperty firstnameEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty surnameEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty lastnameEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty usernameEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty passwordEmpty = new SimpleBooleanProperty(false);
    private final String highlightedStyle = "-fx-border-color: red; -fx-border-width: 2px;";
    private final String defaultStyle = "";
    public void addUser() {
        System.out.println(Hash.toString(Hash.hash("admin")));
        if (firstnameEmpty.get() || surnameEmpty.get() || lastnameEmpty.get() || usernameEmpty.get() || passwordEmpty.get())
            return;
        Database database = Database.getInstance();
        String statement = "insert into people (firstname, surname, lastname, address, phone_number, username, password_hash, role) value (?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(statement)) {
            preparedStatement.setString(1, firstname.get());
            preparedStatement.setString(2, surname.get());
            preparedStatement.setString(3, lastname.get());
            preparedStatement.setString(4, null);
            preparedStatement.setString(5, null);
            preparedStatement.setString(6, username.get());
            preparedStatement.setString(7, Hash.toString(Hash.hash(password.get())));
            preparedStatement.setString(8, "client");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        signIn();
    }

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
    public void signIn() {
        try {
            App.setRoot("authorization");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}