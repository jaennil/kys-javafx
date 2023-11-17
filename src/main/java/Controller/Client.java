package Controller;

import Other.Appointment;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Other.Person;

import java.net.URL;
import java.util.ResourceBundle;

public class Client implements Initializable {

    private static final Model.Client model = new Model.Client();
    public Label welcomeLabel;
    public ComboBox<Person> doctorComboBox;
    public DatePicker datePicker;
    public ComboBox<Other.Pet> petComboBox;
    public TableView<Appointment> appointmentsTableView;
    public TableColumn<Appointment, String> doctorColumn;
    public TableColumn<Appointment, String> dateColumn;
    public TableColumn<Appointment, String> petColumn;

    public void onClickLogOut() {
        model.LogOut();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeLabel.textProperty().bindBidirectional(model.welcomeText);
        petComboBox.setItems(model.pets);
        petComboBox.valueProperty().addListener((observable, oldValue, newValue) -> model.setPet(newValue));
        doctorComboBox.setItems(model.doctors);
        doctorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> model.setDoctor(newValue));
        petColumn.setCellValueFactory(new PropertyValueFactory<>("petName"));
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateString"));
        appointmentsTableView.setItems(model.appointments);
        datePicker.setDayCellFactory(model::disablePastDates);
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> model.setDate(newValue));
    }

    public void onClickSubmit() {
        model.addAppointment();
    }

    public void onClickPetMenuItem() {
        model.setPetView();
    }
}