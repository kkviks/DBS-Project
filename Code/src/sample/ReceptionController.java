package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import models.Room;
import utils.ConnectionUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
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
    Label lblSelectedStaff, lblServerStatus;

    @FXML
    TextField filterRoomTable;

    @FXML
    Label lblRoomsTotal, lblRoomsOccupied, lblRoomsAvailable;

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

    //SQL setup
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Database Connectivity
        con = ConnectionUtil.conDB();

        //Initialize panels
        hideAll();
        setupOverview();
        staffTableShowTemp();
        showOnly("Overview");

        //Toggle Staff
        lblSelectedStaff.setText("Present");
    }

    private void setupOverview() {
        setupOverviewHeader();
        setupOverviewTable();
    }

    private void setupOverviewHeader() {
        //Get room numbers
        int numRoomsTotal = getRoomNums("Total");
        int numRoomAvailable = getRoomNums("Available");
        int numRoomsOccupied = getRoomNums("Occupied");

        //Set label text values
        lblRoomsTotal.setText(String.valueOf(numRoomsTotal));
        lblRoomsOccupied.setText(String.valueOf(numRoomsOccupied));
        lblRoomsAvailable.setText(String.valueOf(numRoomAvailable));

        //Server Status
        if (con == null) {
            lblServerStatus.setTextFill(Color.TOMATO);
            lblServerStatus.setText("Not OK");
            return;
        } else {
            lblServerStatus.setTextFill(Color.GREEN);
            lblServerStatus.setText("OK");
        }
    }

    private int getRoomNums(String whatRooms) {

        int count = 0;
        String query ="";

        switch (whatRooms)
        {
            case "Total":
                query = "SELECT COUNT(*) AS VAL FROM ROOM";
                break;
            case "Available":
                query = "SELECT COUNT(*) AS VAL FROM ROOM WHERE Availability=1;";
                break;
            case "Occupied":
                query ="SELECT COUNT(*) AS VAL FROM room WHERE Availability=0";
        }

        try
        {
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                count = resultSet.getInt("VAL");
            }

        }catch (SQLException ex) {
            System.err.println(ex.getMessage());
            lblServerStatus.setText("Not OK");
        }

        return count;
    }

    private void setupOverviewTable() {

        //Set Cell and Property Value Factory for the table
        roomNumCol.setCellValueFactory(new PropertyValueFactory<Room,Integer>("roomNum"));
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<Room,String>("roomType"));
        roomBedsCol.setCellValueFactory(new PropertyValueFactory<Room,Integer>("roomBeds"));
        roomPriceCol.setCellValueFactory(new PropertyValueFactory<Room,Integer>("roomPrice"));
        roomAvailabilityCol.setCellValueFactory(new PropertyValueFactory<Room,String>("roomAvailability"));

        // 0. Initialize the columns.
        //ObservableList<Room> roomList = FXCollections.observableArrayList();
        ObservableList<Room> roomList = fetchOverviewData();


        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Room> roomFilteredList = new FilteredList<>(roomList,p->true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterRoomTable.textProperty().addListener((observable, oldValue, newValue) -> {
            roomFilteredList.setPredicate(room -> {

                //If filter is empty display all data
                if(newValue==null || newValue.isEmpty())
                {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase().trim();

                //Column-wise filtering logic
                if(room.getRoomType().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                else if(room.getRoomAvailability().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                else if(String.valueOf(room.getRoomBeds()).toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                else if(String.valueOf(room.getRoomPrice()).contains(lowerCaseFilter))
                {
                    return true;
                }
                else if(String.valueOf(room.getRoomNum()).toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                return false;
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Room> roomSortedList = new SortedList<>(roomFilteredList);

        //4. Bind the Sortedlist comparator to the TableView comparator.
        //Otherwise, sorting the TableView would have no effect.
        roomSortedList.comparatorProperty().bind(roomTableView.comparatorProperty());

        //Add sorted ( and filtered ) data to the table.
        roomTableView.setItems(roomSortedList);

        //If we have no data to show
        roomTableView.setPlaceholder(new Label("No search results"));
    }

    private ObservableList<Room> fetchOverviewData() {

        ObservableList<Room> roomsList = FXCollections.observableArrayList();

        try
        {
            //Quering data for room items
            String query = "SELECT Room_No,Room_Type, Availability , Beds_Num, Price " +
                    "FROM room,room_type " +
                    "WHERE room.room_type=room_type.Type " +
                    "ORDER BY room.Availability DESC, " +
                    "Room_Type.Price DESC, " +
                    "room.Room_No ASC;";

            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            //Iterating over data and adding to rooms observable list
            while(resultSet.next())
            {
                //Extract data from a particular row
                int roomNum = resultSet.getInt("Room_No");
                String roomType = resultSet.getString("Room_Type");
                int numBed = resultSet.getInt("Beds_Num");
                int price = resultSet.getInt("Price");
                String roomAvailability = resultSet.getInt("Availability")==1?"Available": "Taken";

                //Add data to roomList
                roomsList.add(new Room(roomNum, roomType,numBed,price,roomAvailability));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return roomsList;
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
