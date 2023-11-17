package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Other.Breed;

import java.net.URL;
import java.util.ResourceBundle;

public class Pet implements Initializable {
    public Label nameEmptyLabel;
    public Label breedEmptyLabel;
    Model.Pet model = new Model.Pet();
    public TextField nameTextField;
    public Label welcomeLabel;
    public ComboBox<Breed> breedComboBox;
    public TableView<Other.Pet> tableView;
    public TableColumn<Other.Pet, String> nameColumn;
    public TableColumn<Other.Pet, String> breedColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeLabel.textProperty().bindBidirectional(model.welcomeText);

        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> model.setName(newValue));
        nameTextField.styleProperty().bindBidirectional(model.nameFieldStyle);
        nameEmptyLabel.visibleProperty().bindBidirectional(model.nameEmpty);

        breedComboBox.valueProperty().addListener((observable, oldValue, newValue) -> model.setBreed(newValue));
        breedComboBox.styleProperty().bindBidirectional(model.breedComboBoxStyle);
        breedEmptyLabel.visibleProperty().bindBidirectional(model.breedEmpty);

        breedComboBox.setItems(model.breeds);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        breedColumn.setCellValueFactory(new PropertyValueFactory<>("breedName"));
        tableView.setItems(model.pets);
    }

    public void onClickAppointmentView() {
        model.setAppointmentView();
    }

    public void onClickLogOut() {
        model.logOut();
    }

    public void onClickSubmit() {
        model.setName(nameTextField.getText());
        model.setBreed(breedComboBox.getValue());
        model.addPet();
    }

    public void onClickDelete() {
        Other.Pet pet = tableView.getSelectionModel().getSelectedItem();
        model.deletePet(pet);
    }

    public void onClickUpdate() {
        Other.Pet pet = tableView.getSelectionModel().getSelectedItem();
        model.updatePet(pet);
    }
}
