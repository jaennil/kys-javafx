package Controller;

import Other.Appointment;
import Other.Breed;
import Other.Database;
import Other.Person;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.example.App;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Employee implements Initializable {
    private final Model.Employee model = new Model.Employee();
    public Label welcomeLabel;
    public Label welcomeLabel1;
    public Label welcomeLabel2;
    public TextField phoneNumberField;
    public Label firstnameEmptyLabel;
    public TextField firstnameField;
    public Label surnameEmptyLabel;
    public TextField surnameField;
    public Label lastnameEmptyLabel;
    public TextField lastnameField;
    public Label phoneNumberEmptyLabel;
    public ComboBox<Person> personComboBoxInAddPet;
    public ComboBox<Breed> breedComboBox;
    public TextField nameTextField;
    public TableView<Other.Pet> tableView;
    public TableColumn<Other.Pet, String> nameColumn;
    public TableColumn<Other.Pet, String> breedColumn;
    public DatePicker datePicker;
    public ComboBox<Person> personComboBox;
    public ComboBox<Other.Pet> petComboBox;
    public ComboBox<Person> doctorComboBox;
    public TableView<Appointment> appointmentsTableView;
    public TableColumn<Appointment, String> petColumn;
    public TableColumn<Appointment, String> doctorColumn;
    public TableColumn<Appointment, String> dateColumn;
    public TabPane tabPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
                if (newTab != null) {
                    model.pets.clear();
                    if (personComboBox.getValue() != null) {
                        model.getPersonPets(personComboBox.getValue());
                        model.getPersonPets(personComboBox.getValue());
                    }
                }
            });
            initWelcomeLabels();
            initPersonComboBox();
            initBreedComboBox();
            initDoctorComboBox();
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            breedColumn.setCellValueFactory(new PropertyValueFactory<>("breedName"));

            petColumn.setCellValueFactory(new PropertyValueFactory<>("petName"));
            doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateString"));
            datePicker.setDayCellFactory(this::disablePastDates);
            tableView.setItems(model.pets);
            appointmentsTableView.setItems(model.appointments);
            personComboBoxInAddPet.valueProperty().addListener((observable, oldValue, newValue) -> {
                model.pets.clear();
                model.getPersonPets(personComboBoxInAddPet.getValue());
            });
            personComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                model.appointments.clear();
                model.pets.clear();
                petComboBox.setItems(model.getPersonPets(newValue));
                model.getPersonAppointments(newValue);
            });
            datePicker.setDayCellFactory(this::disablePastDates);
        });
    }

    private void initDoctorComboBox() {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        String statement = "select * from people where role = 'doctor'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Person doctor = Person.fromResultSet(resultSet);
                doctorComboBox.getItems().add(doctor);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private DateCell disablePastDates(DatePicker datePicker) {
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

    private void initWelcomeLabels() {
        welcomeLabel.setText("Welcome, " + model.authenticatedUser.getWelcomeName());
        welcomeLabel1.setText("Welcome, " + model.authenticatedUser.getWelcomeName());
        welcomeLabel2.setText("Welcome, " + model.authenticatedUser.getWelcomeName());

    }

    private void initPersonComboBox() {
        personComboBoxInAddPet.setItems(model.persons);
        personComboBox.setItems(model.persons);
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        String statement = "select * from people where role = 'client'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Person person = Person.fromResultSet(resultSet);
                model.persons.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initBreedComboBox() {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        String statement = "select * from breeds";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Breed breed = Breed.fromResultSet(resultSet);
                breedComboBox.getItems().add(breed);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void logOut(MouseEvent mouseEvent) {
        try {
            App.setRoot("authorization");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPerson(ActionEvent actionEvent) {
        String firstname = firstnameField.getText();
        String surname = surnameField.getText();
        String lastname = lastnameField.getText();
        String phoneNumber = phoneNumberField.getText();
        boolean firstnameIsBlank = firstname.isBlank();
        boolean surnameIsBlank = surname.isBlank();
        boolean lastnameIsBlank = lastname.isBlank();
        boolean phoneNumberIsBlank = phoneNumber.isBlank();
        firstnameField.setStyle(firstnameIsBlank ? "-fx-border-color: red;" : "");
        surnameField.setStyle(surnameIsBlank ? "-fx-border-color: red;" : "");
        lastnameField.setStyle(lastnameIsBlank ? "-fx-border-color: red;" : "");
        phoneNumberField.setStyle(phoneNumberIsBlank ? "-fx-border-color: red;" : "");
        firstnameEmptyLabel.setVisible(firstnameIsBlank);
        surnameEmptyLabel.setVisible(surnameIsBlank);
        lastnameEmptyLabel.setVisible(lastnameIsBlank);
        phoneNumberEmptyLabel.setVisible(phoneNumberIsBlank);
        if (firstnameIsBlank || surnameIsBlank || lastnameIsBlank || phoneNumberIsBlank) {
            return;
        }
        model.addUser(firstname, surname, lastname, phoneNumber, "client");
        System.out.println("user successfully added");
    }

    public void onClickSubmit(ActionEvent actionEvent) {
        Person person1 = personComboBoxInAddPet.getValue();
        String petName = nameTextField.getText();
        Breed breed = breedComboBox.getValue();
        model.addPet(person1, petName, breed);
    }

    public void addAppointment(ActionEvent actionEvent) {
        LocalDate date = datePicker.getValue();
        Person person1 = personComboBox.getValue();
        Other.Pet pet = petComboBox.getValue();
        Person doctor = doctorComboBox.getValue();
        Appointment appointment = new Appointment(pet.getId(), person1.getId(), doctor.getId(), Date.valueOf(date));
        model.addAppointment(appointment);
    }

    public void setAuthenticatedUser(Person user) {
    }
}
