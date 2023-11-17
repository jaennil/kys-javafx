package Model;

import Other.Pet;
import Other.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import org.example.App;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class Client {
    private final AuthenticatedUser authenticatedUser = AuthenticatedUser.getInstance();
    public StringProperty welcomeText = new SimpleStringProperty("Welcome, " + authenticatedUser.getWelcomeName());
    public ObjectProperty<Other.Pet> pet = new SimpleObjectProperty<>();
    public ObjectProperty<Person> doctor = new SimpleObjectProperty<>();
    public ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    public ObservableList<Appointment> appointments;
    public ObservableList<Person> doctors;
    public ObservableList<Other.Pet> pets;
    Database database = Database.getInstance();
    Connection connection = database.getConnection();
    public Client() {
        appointments = FXCollections.observableArrayList();
        doctors = FXCollections.observableArrayList();
        pets = FXCollections.observableArrayList();
        readDoctorsFromDatabase();
        readPetsFromDatabase();
        readAppointmentsFromDatabase();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public void setPet(Pet pet) {
        this.pet.set(pet);
    }

    public void setDoctor(Person doctor) {
        this.doctor.set(doctor);
    }

    private void readPetsFromDatabase() {
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

    private void readDoctorsFromDatabase() {
        String statement = "select * from people where role = 'doctor'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                doctors.add(Person.fromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void readAppointmentsFromDatabase() {
        String statement = "select * from appointments where person_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, authenticatedUser.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                appointments.add(Appointment.fromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DateCell disablePastDates(DatePicker datePicker) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        };
    }

    public void LogOut() {
        try {
            App.setRoot("authorization");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addAppointment() {
        String statement = "insert into appointments (pet_id, person_id, doctor_id, date) value (?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, pet.get().getId());
            preparedStatement.setInt(2, authenticatedUser.getId());
            preparedStatement.setInt(3, doctor.get().getId());
            preparedStatement.setDate(4, Date.valueOf(date.get()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        appointments.clear();
        readAppointmentsFromDatabase();
    }

    public void setPetView() {
        try {
            App.setRoot("pet");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

