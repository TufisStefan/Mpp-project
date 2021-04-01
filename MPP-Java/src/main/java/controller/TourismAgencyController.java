package controller;

import domain.Excursion;
import domain.Reservation;
import domain.validators.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.ServicesException;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TourismAgencyController extends IController{

    ObservableList<Excursion> modelExcursion = FXCollections.observableArrayList();
    ObservableList<String> modelStartTime = FXCollections.observableArrayList();
    ObservableList<String> modelEndTime = FXCollections.observableArrayList();

    @FXML
    TableView<Excursion> excursionsTableView;
    @FXML
    TableColumn<Excursion, String> objectiveTableColumn;
    @FXML
    TableColumn<Excursion, String> companyTableColumn;
    @FXML
    TableColumn<Excursion, Float> priceTableColumn;
    @FXML
    TableColumn<Excursion, Long> seatsTableColumn;
    @FXML
    TableColumn<Excursion, LocalTime> departureTableColumn;
    @FXML
    Button logoutButton;
    @FXML
    Button filterButton;
    @FXML
    TextField objectiveTextField;
    @FXML
    TextField nameTextField;
    @FXML
    TextField phoneTextField;
    @FXML
    Spinner<Integer> ticketsSpinner;
    @FXML
    Button addReservationButton;
    @FXML
    ComboBox<String> startTimeCombo;
    @FXML
    ComboBox<String> endTimeCombo;

    @FXML
    public void initialize() {

        updateTable(services.findAllExcursions());


        objectiveTableColumn.setCellValueFactory(new PropertyValueFactory<>("objective"));
        companyTableColumn.setCellValueFactory(new PropertyValueFactory<>("company"));
        priceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        departureTableColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        seatsTableColumn.setCellValueFactory(new PropertyValueFactory<>("seats"));

        excursionsTableView.setRowFactory(x -> new TableRow<>(){
            @Override
            protected void updateItem(Excursion excursion, boolean empty) {
                super.updateItem(excursion, empty);
                if (excursion == null) {
                    setStyle("");
                    return;
                }
                if (excursion.getSeats() == 0) {
                    setStyle("-fx-background-color: red");
                }
                else {
                    setStyle("");
                }
            }
        });

        modelStartTime.clear();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j += 15) {
                String hour = i / 10 == 0 ? "0" + i : "" + i;
                String minute = j / 10 == 0 ? "0" + j : "" + j;
                modelStartTime.add(hour + ":" + minute + ":00");
            }
        }
        startTimeCombo.setItems(modelStartTime);
        startTimeCombo.setVisibleRowCount(3);
        startTimeCombo.getSelectionModel().select(0);

        modelEndTime.clear();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j += 15) {
                String hour = i / 10 == 0 ? "0" + i : "" + i;
                String minute = j / 10 == 0 ? "0" + j : "" + j;
                modelEndTime.add(hour + ":" + minute + ":00");
            }
        }
        endTimeCombo.setItems(modelStartTime);
        endTimeCombo.setVisibleRowCount(3);
        endTimeCombo.getSelectionModel().select(0);

        ticketsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));

    }

    private void updateTable(List<Excursion> allExcursions) {
        modelExcursion.clear();
        modelExcursion.setAll(allExcursions);
        excursionsTableView.setItems(modelExcursion);
    }

    public void handleLogout(MouseEvent mouseEvent) throws IOException {
        Stage mainStage = (Stage) logoutButton.getScene().getWindow();
        mainStage.close();

        FXMLLoader loader = new FXMLLoader();
        Stage loginStage = new Stage();
        loader.setLocation(this.getClass().getResource("/views/LoginWindow.fxml"));
        AnchorPane root = loader.load();
        IController controller = loader.getController();
        controller.setService(services);

        loginStage.setScene(new Scene(root));
        loginStage.setTitle("Login");
        loginStage.show();
    }

    public void handleFilter(MouseEvent mouseEvent) {
        String objective  = objectiveTextField.getText();
        LocalTime startTime = LocalTime.parse(startTimeCombo.getSelectionModel().getSelectedItem(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime endTime = LocalTime.parse(endTimeCombo.getSelectionModel().getSelectedItem(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        List<Excursion> filteredExcursions = services.findFilteredExcursions(objective, startTime, endTime);
        updateTable(filteredExcursions);
    }

    public void handleAddReservation(MouseEvent mouseEvent) {

        Excursion excursion = excursionsTableView.getSelectionModel().getSelectedItem();
        if(excursion == null){
            new Alert(Alert.AlertType.ERROR, "No excursion selected!").show();
        }
        String name = nameTextField.getText();
        Long phone = Long.parseLong(phoneTextField.getText());
        Long tickets = Long.valueOf(ticketsSpinner.getValue());
        try{
            services.addReservation(name, phone, tickets, excursion);
            updateTable(services.findAllExcursions());
        }
        catch (ServicesException ex){
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }
}
