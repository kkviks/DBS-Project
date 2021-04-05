package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.Room;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReceptionController implements Initializable {

    @FXML
    private VBox pnItems = null, pnItems1 = null;
    @FXML
    private Button btnOverview;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnStaff;

    @FXML
    private Button btnCheckout;

    @FXML
    private Button btnNewBooking;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnSignout;

    //panels

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlNewBooking;

    @FXML
    private Pane pnlStaff;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlCheckout;

    @FXML
    ToggleGroup toggleStaff;

    @FXML
    ToggleButton totalStaff;

    @FXML
    ToggleButton presentStaff;

    @FXML
    ToggleButton absentStaff;

    @FXML
    Label lblSelectedStaff;

    //tableView
    @FXML
    private TableView<Room> roomTableView;

    @FXML
    private TableColumn<Room, Integer> roomNumCol;
    @FXML
    private TableColumn<Room, String> roomTypeCol;
    @FXML
    private TableColumn<Room, Integer> roomBedsCol;
    @FXML
    private TableColumn<Room, Integer> roomPriceCol;
    @FXML
    private TableColumn<Room, String> roomAvailabilityCol;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //updateOverviewTable("Total");
        roomNumCol.setCellValueFactory(new PropertyValueFactory<Room,Integer>("roomNum"));
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<Room,String>("roomType"));
        roomBedsCol.setCellValueFactory(new PropertyValueFactory<Room,Integer>("roomBeds"));
        roomPriceCol.setCellValueFactory(new PropertyValueFactory<Room,Integer>("roomPrice"));
        roomAvailabilityCol.setCellValueFactory(new PropertyValueFactory<Room,String>("roomAvailability"));

        //Data
        ObservableList<Room> roomList = FXCollections.observableArrayList();
        for(int i=0;i<100;i++)
         roomList.add(new Room(1,"type1",2, 2000, "Yes"));

        roomTableView.setItems(roomList);

        staffTableShowTemp();
        hideAll();
        showOnly("Overview");

        //Toggle Staff
        lblSelectedStaff.setText("Present");
    }

    private void updateOverviewTable(String total) {
    }

    private void staffTableShowTemp() {
        Node[] nodes = new Node[10];
        for (int i = 0; i < nodes.length; i++) {
            try {

                final int j = i;
                nodes[i] = FXMLLoader.load(getClass().getResource("Item.fxml"));
                //give the items some effect


                nodes[i].setOnMouseEntered(event -> {
                    nodes[j].setStyle("-fx-background-color : #0A0E3F");
                });
                nodes[i].setOnMouseExited(event -> {
                    nodes[j].setStyle("-fx-background-color : #02030A");
                });
                pnItems1.getChildren().add(nodes[i]);
                //pnItems1.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void hideAll() {

        pnlOverview.setStyle("-fx-background-color : #02030A");
        pnlNewBooking.setStyle("-fx-background-color : #02030A");
        pnlStaff.setStyle("-fx-background-color : #02030A");
        pnlCustomer.setStyle("-fx-background-color : #02030A");
        pnlOrders.setStyle("-fx-background-color : #02030A");
        pnlCheckout.setStyle("-fx-background-color : #02030A");

        pnlOverview.setVisible(false);
        pnlNewBooking.setVisible(false);
        pnlStaff.setVisible(false);
        pnlCustomer.setVisible(false);
        pnlOrders.setVisible(false);
        pnlCheckout.setVisible(false);
    }

    private void showOnly(String panel) {

        switch (panel) {
            case "Overview":
                pnlOverview.toFront();
                pnlOverview.setVisible(true);
                break;
            case "New Booking":
                pnlNewBooking.toFront();
                pnlNewBooking.setVisible(true);
                pnlNewBooking.setStyle("-fx-background-color : #02030A");
                break;
            case "Staff":
                pnlStaff.toFront();
                pnlStaff.setVisible(true);
                pnlStaff.setStyle("-fx-background-color : #02030A");
                break;
            case "Customers":
                pnlCustomer.toFront();
                pnlCustomer.setVisible(true);
                pnlCustomer.setStyle("-fx-background-color : #02030A");
                break;
            case "Orders":
                pnlOrders.toFront();
                pnlOrders.setVisible(true);
                pnlOrders.setStyle("-fx-background-color : #02030A");
                break;
            case "Checkout":
                pnlCheckout.toFront();
                pnlCheckout.setVisible(true);
                pnlCheckout.setStyle("-fx-background-color : #02030A");
                break;
            default: // Do nothing
        }
    }


    public void handleClicks(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnOverview) {
            showOnly("Overview");
        }

        if (actionEvent.getSource() == btnNewBooking) {
            showOnly("New Booking");
        }

        if (actionEvent.getSource() == btnStaff) {
            showOnly("Staff");
        }

        if (actionEvent.getSource() == btnCustomers) {
            showOnly("Customers");
        }

        if (actionEvent.getSource() == btnOrders) {
            showOnly("Orders");
        }

        if (actionEvent.getSource() == btnCheckout) {
            showOnly("Checkout");
        }

        if (actionEvent.getSource() == btnSignout) {
            Platform.exit();
            System.exit(0);
        }
    }

    public void handleToggle(ActionEvent actionEvent) {
        System.out.println(actionEvent.getSource().toString());

        if (actionEvent.getSource() == totalStaff) {
            lblSelectedStaff.setText("Total Staff");
        }

        if (actionEvent.getSource() == presentStaff) {
            lblSelectedStaff.setText("Present");
        }

        if (actionEvent.getSource() == absentStaff) {
            lblSelectedStaff.setText("Absent");
        }
    }
}
