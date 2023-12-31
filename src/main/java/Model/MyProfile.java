package Model;

import Other.AuthenticatedUser;
import Other.Database;
import Other.Hash;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class MyProfile {
    public final StringProperty fullname = new SimpleStringProperty("");
    public final StringProperty gender = new SimpleStringProperty("");
    public final StringProperty dob = new SimpleStringProperty("");
    public final StringProperty idNumber = new SimpleStringProperty("");
    public final StringProperty country = new SimpleStringProperty("");
    public final StringProperty phoneNumber = new SimpleStringProperty("");
    public final StringProperty email = new SimpleStringProperty("");
    public final StringProperty password = new SimpleStringProperty("");
    public final StringProperty passwordPrompt = new SimpleStringProperty("");
    public final StringProperty reEnterPassword = new SimpleStringProperty("");
    public final StringProperty reEnterPasswordPrompt = new SimpleStringProperty("");
    public final ObjectProperty<Image> image = new SimpleObjectProperty<>();

    public void init() {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        AuthenticatedUser authenticatedUser = AuthenticatedUser.getInstance();
        String statement = "SELECT * FROM Organizer WHERE idNumber = ? AND fullname = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, authenticatedUser.getIdNumber());
            preparedStatement.setString(2, authenticatedUser.getFullname());
            ResultSet user = preparedStatement.executeQuery();
            if (user.next()) {
                fullname.set(user.getString("fullname"));
                phoneNumber.set(user.getString("phone_number"));
                email.set(user.getString("email"));
                image.set(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/organizers/" + user.getString("photo")))));
                dob.set(user.getString("birth_date"));
                idNumber.set(user.getString("idNumber"));
                String genderStatement = "SELECT * FROM Sex WHERE id = ?";
                PreparedStatement genderPreparedStatement = connection.prepareStatement(genderStatement);
                genderPreparedStatement.setInt(1, user.getInt("sex_id"));
                ResultSet genderRS = genderPreparedStatement.executeQuery();
                if (genderRS.next()) {
                    gender.set(genderRS.getString("name"));
                }
                String countryStatement = "SELECT * FROM Country WHERE code = ?";
                PreparedStatement countryPreparedStatement = connection.prepareStatement(countryStatement);
                countryPreparedStatement.setInt(1, user.getInt("country_id"));
                ResultSet countryRS = countryPreparedStatement.executeQuery();
                if (countryRS.next()) {
                    country.set(countryRS.getString("name"));
                } else {
                    country.set("Не указана");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleOkButton() {
        System.out.println("password" + password.get());
        System.out.println("passwordPrompt" + passwordPrompt.get());
        String actualPassword = password.get().isEmpty() ? passwordPrompt.get() : password.get();
        String actualReEnterPassword = reEnterPassword.get().isEmpty() ? reEnterPasswordPrompt.get() : reEnterPassword.get();
        if (actualPassword.isEmpty()) {
            System.out.println("password is empty");
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("Change password");
            dialog.setHeaderText("Failed to change password");
            dialog.setContentText("Password is empty");
            dialog.showAndWait();
            return;
        }
        if (actualPassword.equals(actualReEnterPassword)) {
            Database database = Database.getInstance();
            Connection connection = database.getConnection();
            AuthenticatedUser authenticatedUser = AuthenticatedUser.getInstance();
            String passwordHash = Hash.toString(Hash.hash(actualPassword));
            String statement = "UPDATE Organizer SET password_hash = ? WHERE idNumber = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, passwordHash);
                preparedStatement.setString(2, authenticatedUser.getIdNumber());
                int result = preparedStatement.executeUpdate();
                if (result == 1) {
                    System.out.println("successfully changed password");
                    password.set("");
                    reEnterPassword.set("");
                } else {
                    Alert dialog = new Alert(Alert.AlertType.ERROR);
                    dialog.setTitle("Change password");
                    dialog.setHeaderText("Failed to change password");
                    dialog.setContentText("User not founded in database");
                    dialog.showAndWait();
                }
                System.out.println("update password result: " + result);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("Change password");
            dialog.setHeaderText("Failed to change password");
            dialog.setContentText("Passwords doesn't match");
            dialog.showAndWait();
            System.out.println("passwords dont match");
        }
    }
}
