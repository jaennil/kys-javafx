package Model;

import Other.AuthenticatedUser;
import Other.Breed;
import Other.Database;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.App;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pet {
    public final StringProperty nameFieldStyle = new SimpleStringProperty();
    public final StringProperty breedComboBoxStyle = new SimpleStringProperty();
    private final String highlightedStyle = "-fx-border-color: red; -fx-border-width: 2px;";
    private final String defaultStyle = "";
    public AuthenticatedUser authenticatedUser = AuthenticatedUser.getInstance();
    public StringProperty welcomeText = new SimpleStringProperty("Welcome, " + authenticatedUser.getWelcomeName());
    public StringProperty name = new SimpleStringProperty();
    public BooleanProperty nameEmpty = new SimpleBooleanProperty(false);
    public BooleanProperty breedEmpty = new SimpleBooleanProperty(false);
    public ObjectProperty<Breed> breed = new SimpleObjectProperty<>();
    public ObservableList<Other.Pet> pets;
    public ObservableList<Breed> breeds;
    Database database = Database.getInstance();
    Connection connection = database.getConnection();

    public Pet() {
        pets = FXCollections.observableArrayList();
        breeds = FXCollections.observableArrayList();
        readBreedsFromDatabase();
        readPetsFromDatabase();
    }

    public void setName(String name) {
        this.name.set(name);
        nameFieldStyle.set(name.isBlank() ? highlightedStyle : defaultStyle);
        nameEmpty.set(name.isBlank());
    }

    public void setBreed(Breed breed) {
        this.breed.set(breed);
        breedComboBoxStyle.set(breed == null ? highlightedStyle : defaultStyle);
        breedEmpty.set(breed == null);
    }

    private void readBreedsFromDatabase() {
        String statement = "select * from breeds";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Breed breed = Breed.fromResultSet(resultSet);
                breeds.add(breed);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void readPetsFromDatabase() {
        pets.clear();
        String statement = "select * from pets where owner_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, authenticatedUser.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Other.Pet pet = Other.Pet.fromResultSet(resultSet);
                pets.add(pet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPet() {
        if (breed.get() == null || name.get().isBlank())
            return;
        String statement = "insert into pets (name, breed_id, owner_id) value (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, name.get());
            preparedStatement.setInt(2, breed.get().getId());
            preparedStatement.setInt(3, authenticatedUser.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        readPetsFromDatabase();
    }

    public void setAppointmentView() {
        try {
            App.setRoot("client");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logOut() {
        try {
            App.setRoot("authorization");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePet(Other.Pet pet) {
        String statement = "delete from pets where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, pet.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        pets.remove(pet);


    }

    public void updatePet(Other.Pet pet) {
        String statement = "update pets set name = ?, breed_id = ? where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, name.get());
            preparedStatement.setInt(2, breed.get().getId());
            preparedStatement.setInt(3, pet.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        readPetsFromDatabase();
    }
}