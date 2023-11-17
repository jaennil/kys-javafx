package Model;

import Other.*;
import Other.Pet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee {
    public ObservableList<Person> persons;
    public ObservableList<Other.Pet> pets;
    public ObservableList<Appointment> appointments;
    public AuthenticatedUser authenticatedUser = AuthenticatedUser.getInstance();

    public Employee() {
        persons = FXCollections.observableArrayList();
        pets = FXCollections.observableArrayList();
        appointments = FXCollections.observableArrayList();
    }
    public void addUser(String firstname, String surname, String lastname, String phoneNumber, String role) {
        Person person = new Person(firstname, lastname, surname, null, phoneNumber, null, role);
        persons.add(person);
        Database db = Database.getInstance();
        String statement = "insert into people (firstname, surname, lastname, address, phone_number, username, password_hash, role) value (?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = db.getConnection().prepareStatement(statement)) {
            preparedStatement.setString(1, firstname);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, lastname);
            preparedStatement.setString(4, null);
            preparedStatement.setString(5, phoneNumber);
            preparedStatement.setString(6, null);
            preparedStatement.setString(7, null);
            preparedStatement.setString(8, role);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPet(Person person, String petName, Breed breed) {
        pets.add(new Other.Pet(petName, breed.getId(), person.getId()));
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        String statement = "insert into pets (name, breed_id, owner_id) value (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, petName);
            preparedStatement.setInt(2, breed.getId());
            preparedStatement.setInt(3, person.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Pet> getPersonPets(Person owner) {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        String statement = "select * from pets where owner_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, owner.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                pets.add(Pet.fromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pets;
    }

    public void addAppointment(Appointment appointment) {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        String statement = "insert into appointments (pet_id, person_id, doctor_id, date) value (?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, appointment.getPetId());
            preparedStatement.setInt(2, appointment.getPersonId());
            preparedStatement.setInt(3, appointment.getDoctorId());
            preparedStatement.setDate(4, appointment.getDate());
            preparedStatement.executeUpdate();
            appointments.add(appointment);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Appointment> getPersonAppointments(Person person) {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        String statement = "select * from appointments where person_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, person.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                appointments.add(Appointment.fromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appointments;
    }
}
