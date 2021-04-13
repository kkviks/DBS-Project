package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import models.Customer;
import models.Orders;
import models.Room;
import models.Staff;
import utils.ConnectionUtil;
import utils.DateManipulation;
import utils.Pair;
import utils.Queries;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceptionController implements Initializable {

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
    Pane pnlOverview;

    @FXML
    Pane pnlNewBooking;

    @FXML
    Pane pnlStaff;

    @FXML
    Pane pnlCustomer;

    @FXML
    Pane pnlOrders;

    @FXML
    Pane pnlCheckout;

    @FXML
    Label lblServerStatus;

    @FXML
    TextField filterRoomTable, filterStaffTable;

    @FXML
    Label lblRoomsTotal, lblRoomsOccupied, lblRoomsAvailable;

    @FXML
    Label lblStaffTotal, lblStaffPresent, lblStaffAbsent;

    @FXML
    Label lblServerStatusStaff;

    @FXML
    Label lblServerStatusOrder;

    //New Booking elements
    private boolean isRoomAvailabe = false;
    @FXML
    TextField txtRoomNB;

    @FXML
    Label lblRoomStatusNB, lblRoomType, lblRoomTypePrice,lblServiceTypePrice, lblTotalRoomPrice;

    @FXML
    ComboBox cbServiceType, cbMartialStatus;

    @FXML
    Button btnBookRoom,btnResetBooking,btnResetBooking1;

    @FXML
    TextField txtFirstName, txtLastName, txtEmail, txtPhone, txtUIDAI, txtPassport, txtProfession, txtAddress;

    @FXML
    DatePicker dpArrival, dpDeparture;

    @FXML
    Spinner spinnerOccupants;

    @FXML
    ProgressBar progressBar;


    //tableView
    @FXML
    TableView<Room> roomTableView;

    @FXML
    TableView<Staff> staffTableView;

    @FXML
    TableView<Customer> customerTableView;

    @FXML
    TableView<Orders> ordersTableView;

    @FXML
    TableColumn<Room, Integer> roomNumCol;
    @FXML
    TableColumn<Room, Integer> roomBedsCol;
    @FXML
    TableColumn<Room, Integer> roomPriceCol;
    @FXML
    TableColumn<Room, String> roomTypeCol;
    @FXML
    TableColumn<Room, String> roomAvailabilityCol;

    @FXML
    TableColumn<Staff, String> staffShiftCol;
    @FXML
    TableColumn<Staff, String> staffNameCol;
    @FXML
    TableColumn<Staff, String> staffDesignationCol;
    @FXML
    TableColumn<Staff, String> staffPhoneCol;
    @FXML
    TableColumn<Staff, String> staffAvailabilityCol;

    //For customer table
    @FXML
    TableColumn<Customer, Integer> customerRoomCol;
    @FXML
    TableColumn<Customer, String> customerNameCol;
    @FXML
    TableColumn<Customer, String> customerServiceTypeCol;
    @FXML
    TableColumn<Customer, Integer> customerOccupantsCol;
    @FXML
    TableColumn<Customer, String> customerArrivalCol;
    @FXML
    TableColumn<Customer, Integer> customerAmountDueCol;
    @FXML
    TableColumn<Customer, String> customerRequestsCol;

    //For Orders table
    @FXML
    TableColumn<Orders, Integer> orderRoomCol;
    @FXML
    TableColumn<Orders, String> orderRequestsCol;
    @FXML
    TableColumn<Orders, String> orderInventoriesCol;
    @FXML
    TableColumn<Orders, Integer> orderChargesCol;
    @FXML
    TableColumn<Orders, String> orderTimeCol;
    @FXML
    TableColumn<Orders, String> orderBellboyCol;


    @FXML
    TextField filterCustomerTable;

    @FXML
    TextField filterOrderTable;

    @FXML
    Label lblServerStatusCustomer, lblVisitorCount, lblCustomerCount, lblAmountDueCount;

    //Checkout
    @FXML
    TextField txtRoomCheckout;

    @FXML
    Label lblRoomStatusCheckout;

    @FXML
    Button btnFinalCheckout;

    @FXML
    ProgressBar progressBarCheckout;

    @FXML
    Label lblName,lblEmail, lblPhone, lblOccupants, lblCheckin, lblCheckout;

    @FXML
    Label lblRoomNumCheckOut,lblRoomPriceCheckOut, lblDuration, lblDurationPrice, lblService, lblServicePrice, lblTotalRoomPriceCheckout;

    @FXML
    TextField txtDiscount;

    @FXML
    Label lblSubtotal, lblAdditionalCharges, lblTax;

    @FXML
    Label lblTotalCheckout, lblPaid, lblDue;

    @FXML
    TextField txtCash, txtReferenceCard, txtReferenceUPI;

    @FXML
    Button btnReceiveCash, btnReceiveUPI, btnReceiveCard, btnShowQR;

    private final double TAX = 0.18;

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
        setupStaff();
        setupOrders();
        setupCustomer();
        showOnly("Overview");

    }

    private void setupStaff() {
        setupStaffHeader();
        setupStaffTable();
    }

    private void setupStaffTable() {
        //Set Cell and Property Value Factory for the table
        staffNameCol.setCellValueFactory(new PropertyValueFactory<Staff, String>("staffName"));
        staffDesignationCol.setCellValueFactory(new PropertyValueFactory<Staff, String>("staffDesignation"));
        staffPhoneCol.setCellValueFactory(new PropertyValueFactory<Staff, String>("staffPhone"));
        staffShiftCol.setCellValueFactory(new PropertyValueFactory<Staff, String>("staffShift"));
        staffAvailabilityCol.setCellValueFactory(new PropertyValueFactory<Staff, String>("staffAttendance"));

        // 0. Initialize the columns.
        //ObservableList<Staff> roomList = FXCollections.observableArrayList();
        ObservableList<Staff> staffList = fetchStaffData();


        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Staff> staffFilteredList = new FilteredList<>(staffList, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterStaffTable.textProperty().addListener((observable, oldValue, newValue) -> {
            staffFilteredList.setPredicate(staff -> {

                //If filter is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase().trim();
                //Column-wise filtering logic
                if (staff.getStaffName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (staff.getStaffDesignation().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (staff.getStaffShift().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (staff.getStaffAttendance().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (staff.getStaffPhone().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Staff> staffSortedList = new SortedList<>(staffFilteredList);

        //4. Bind the Sortedlist comparator to the TableView comparator.
        //Otherwise, sorting the TableView would have no effect.
        staffSortedList.comparatorProperty().bind(staffTableView.comparatorProperty());

        //Add sorted ( and filtered ) data to the table.
        staffTableView.setItems(staffSortedList);

        //If we have no data to show
        staffTableView.setPlaceholder(new Label("No search results"));
    }

    private ObservableList<Staff> fetchStaffData() {
        ObservableList<Staff> stafflist = FXCollections.observableArrayList();

        try {
            //Quering data for staff items
            String query = "SELECT CONCAT(Employee.FirstName, ' ',Employee.LastName) AS Name, Designation, Phone, Shift, isPresent AS Availability " +
                    "FROM Employee, Attendance " +
                    "WHERE Employee.E_ID=Attendance.E_ID;";

            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            //Iterating over data and adding to observable list
            while (resultSet.next()) {
                //Extract data from a particular row
                String staffName = resultSet.getString("Name");
                String staffDesignation = resultSet.getString("Designation");
                String staffPhone = resultSet.getString("Phone");
                String staffShift = resultSet.getString("Shift");
                String staffAvailability = resultSet.getInt("Availability") == 1 ? "Present" : "Absent";

                //Add data to stafflist
                stafflist.add(new Staff(staffName, staffDesignation, staffPhone, staffShift, staffAvailability));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stafflist;
    }

    private void setupStaffHeader() {
        //Get room numbers
        int numStaffTotal = getStaffNums("Total");
        int numStaffPresent = getStaffNums("Present");
        int numStaffAbsent = getStaffNums("Absent");

        //Set label text values
        lblStaffTotal.setText(String.valueOf(numStaffTotal));
        lblStaffPresent.setText(String.valueOf(numStaffPresent));
        lblStaffAbsent.setText(String.valueOf(numStaffAbsent));

        //Server Status
        if (con == null) {
            lblServerStatusStaff.setTextFill(Color.TOMATO);
            lblServerStatusStaff.setText("Not OK");
            return;
        } else {
            lblServerStatusStaff.setTextFill(Color.GREEN);
            lblServerStatusStaff.setText("OK");
        }

    }

    private int getStaffNums(String whatStaff) {

        int count = 0;
        String query = "";

        switch (whatStaff) {
            case "Total":
                query = "SELECT COUNT(*) AS VAL FROM Employee, Attendance WHERE Employee.E_ID=Attendance.E_ID";
                break;
            case "Present":
                query = "SELECT COUNT(*) AS VAL FROM Employee, Attendance WHERE Employee.E_ID=Attendance.E_ID AND isPresent=1";
                break;
            case "Absent":
                query = "SELECT COUNT(*) AS VAL FROM Employee, Attendance WHERE Employee.E_ID=Attendance.E_ID AND isPresent=0";
        }

        try {
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("VAL");
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            lblServerStatusStaff.setText("Not OK");
        }

        return count;
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
        String query = "";

        switch (whatRooms) {
            case "Total":
                query = "SELECT COUNT(*) AS VAL FROM ROOM";
                break;
            case "Available":
                query = "SELECT COUNT(*) AS VAL FROM ROOM WHERE Availability=1;";
                break;
            case "Occupied":
                query = "SELECT COUNT(*) AS VAL FROM room WHERE Availability=0";
        }

        try {
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("VAL");
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            lblServerStatus.setText("Not OK");
        }

        return count;
    }

    private void setupOverviewTable() {

        //Set Cell and Property Value Factory for the table
        roomNumCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("roomNum"));
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<Room, String>("roomType"));
        roomBedsCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("roomBeds"));
        roomPriceCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("roomPrice"));
        roomAvailabilityCol.setCellValueFactory(new PropertyValueFactory<Room, String>("roomAvailability"));

        // 0. Initialize the columns.
        //ObservableList<Room> roomList = FXCollections.observableArrayList();
        ObservableList<Room> roomList = fetchOverviewData();


        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Room> roomFilteredList = new FilteredList<>(roomList, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterRoomTable.textProperty().addListener((observable, oldValue, newValue) -> {
            roomFilteredList.setPredicate(room -> {

                //If filter is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase().trim();

                //Column-wise filtering logic
                if (room.getRoomType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (room.getRoomAvailability().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(room.getRoomBeds()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(room.getRoomPrice()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(room.getRoomNum()).toLowerCase().contains(lowerCaseFilter)) {
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

        try {
            //Querying data for room items
            String query = "SELECT Room_No,Room_Type, Availability , Beds_Num, Price " +
                    "FROM room,room_type " +
                    "WHERE room.room_type=room_type.Type " +
                    "ORDER BY room.Availability DESC, " +
                    "Room_Type.Price DESC, " +
                    "room.Room_No ASC;";

            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            //Iterating over data and adding to rooms observable list
            while (resultSet.next()) {
                //Extract data from a particular row
                int roomNum = resultSet.getInt("Room_No");
                String roomType = resultSet.getString("Room_Type");
                int numBed = resultSet.getInt("Beds_Num");
                int price = resultSet.getInt("Price");
                String roomAvailability = resultSet.getInt("Availability") == 1 ? "Available" : "Taken";

                //Add data to roomList
                roomsList.add(new Room(roomNum, roomType, numBed, price, roomAvailability));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomsList;
    }

    private void setupCustomer() {
        setupCustomerHeader();
        setupCustomerTable();
    }

    private void setupCustomerHeader() {
        //Get room numbers
        int numCustomerVisitor = getCustomerNums("Visitor");
        int numCustomerCustomer = getCustomerNums("Customer");
        int numCustomerAmountDue = getCustomerNums("Amount Due");

        //Set label text values
        lblVisitorCount.setText(String.valueOf(numCustomerVisitor));
        lblCustomerCount.setText(String.valueOf(numCustomerCustomer));
        lblAmountDueCount.setText("₹ " + String.valueOf(numCustomerAmountDue));

        //Server Status
        if (con == null) {
            lblServerStatusCustomer.setTextFill(Color.TOMATO);
            lblServerStatusCustomer.setText("Not OK");
            return;
        } else {
            lblServerStatusCustomer.setTextFill(Color.GREEN);
            lblServerStatusCustomer.setText("OK");
        }

    }

    private int getCustomerNums(String whatCustomers) {

        int count = 0;
        String query = "";

        switch (whatCustomers) {
            case "Visitor":
                query = "SELECT COUNT(*) AS VAL FROM VISITOR;";
                break;
            case "Customer":
                query = "SELECT COUNT(*) AS VAL FROM CUSTOMER;";
                break;
            case "Amount Due":
                query = "SELECT SUM(DUE) AS VAL FROM BILL,CUSTOMER WHERE CUSTOMER.BILL_ID=BILL.BILL_ID;";
        }

        try {
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("VAL");
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            lblServerStatus.setText("Not OK");
        }

        return count;
    }

    private void setupCustomerTable() {

        //Set Cell and Property Value Factory for the table
        customerRoomCol.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerRoomNo"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        customerServiceTypeCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerServiceType"));
        customerOccupantsCol.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerOccupants"));
        customerArrivalCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerArrivalTime"));
        customerAmountDueCol.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerAmountDue"));
        customerRequestsCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerSpecialRequest"));

        // 0. Initialize the columns.
        //ObservableList<Customer> customerList = FXCollections.observableArrayList();
        ObservableList<Customer> customerList = fetchCustomerData();


        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Customer> customerFilteredList = new FilteredList<>(customerList, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterCustomerTable.textProperty().addListener((observable, oldValue, newValue) -> {
            customerFilteredList.setPredicate(customer -> {

                //If filter is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase().trim();

                //Column-wise filtering logic
                if (String.valueOf(customer.getCustomerRoomNo()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getCustomerName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getCustomerServiceType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(customer.getCustomerOccupants()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getCustomerArrivalTime().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(customer.getCustomerAmountDue()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getCustomerSpecialRequest().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Customer> customerSortedList = new SortedList<>(customerFilteredList);

        //4. Bind the Sortedlist comparator to the TableView comparator.
        //Otherwise, sorting the TableView would have no effect.
        customerSortedList.comparatorProperty().bind(customerTableView.comparatorProperty());

        //Add sorted ( and filtered ) data to the table.
        customerTableView.setItems(customerSortedList);

        //If we have no data to show
        customerTableView.setPlaceholder(new Label("No search results"));


    }

    private ObservableList<Customer> fetchCustomerData() {

        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try {
            //Querying data for room items
            String query = "SELECT Room_No , CONCAT(Visitor.FirstName, ' ',Visitor.LastName) AS Name,Service_Type,Occupants_Num, Arrival, Special_Requests,Due " +
                    "FROM Customer,Visitor,Bill " +
                    "WHERE Customer.Visitor_ID=Visitor.Visitor_ID AND Bill.Bill_ID=Customer.Bill_ID " +
                    "ORDER BY Room_No;";

            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            //Iterating over data and adding to rooms observable list
            while (resultSet.next()) {
                //Extract data from a particular row
                int customerNum = resultSet.getInt("Room_No");
                String customerName = resultSet.getString("Name");
                String customerServiceType = resultSet.getString("Service_Type");
                int numOccupants = resultSet.getInt("Occupants_Num");
                String customerArrival = resultSet.getString("Arrival");
                String customerSpecialRequest = resultSet.getString("Special_Requests");
                int due = resultSet.getInt("Due");

                //Add data to customerList
                customerList.add(new Customer(customerNum, customerName, customerServiceType, numOccupants, customerArrival, due, customerSpecialRequest));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerList;
    }

    private void setupOrders() {
        // TODO: 07-04-2021
        //setupOrdersHeader();
        setupOrdersTable();
    }

    private void setupOrdersTable() {

        //Set Cell and Property Value Factory for the table
        orderRoomCol.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("orderRoomNo"));
        orderRequestsCol.setCellValueFactory(new PropertyValueFactory<Orders, String>("orderSpecialRequest"));
        orderInventoriesCol.setCellValueFactory(new PropertyValueFactory<Orders, String>("orderInventories"));
        orderChargesCol.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("orderExtraCharges"));
        orderTimeCol.setCellValueFactory(new PropertyValueFactory<Orders, String>("orderTime"));
        orderBellboyCol.setCellValueFactory(new PropertyValueFactory<Orders, String>("orderBellBoy"));

        // 0. Initialize the columns.
        //ObservableList<Orders> ordersList = FXCollections.observableArrayList();
        ObservableList<Orders> orderList = fetchOrdersData();


        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Orders> orderFilteredList = new FilteredList<>(orderList, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterOrderTable.textProperty().addListener((observable, oldValue, newValue) -> {
            orderFilteredList.setPredicate(orders -> {

                //If filter is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase().trim();

                //Column-wise filtering logic
                if (String.valueOf(orders.getOrderRoomNo()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (orders.getOrderSpecialRequest().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (orders.getOrderInventories().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(orders.getOrderExtraCharges()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (orders.getOrderTime().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (orders.getOrderBellBoy().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Orders> ordersSortedList = new SortedList<>(orderFilteredList);

        //4. Bind the Sortedlist comparator to the TableView comparator.
        //Otherwise, sorting the TableView would have no effect.
        ordersSortedList.comparatorProperty().bind(ordersTableView.comparatorProperty());

        //Add sorted ( and filtered ) data to the table.
        ordersTableView.setItems(ordersSortedList);

        //If we have no data to show
        ordersTableView.setPlaceholder(new Label("No search results"));


    }

    private ObservableList<Orders> fetchOrdersData() {

        ObservableList<Orders> ordersList = FXCollections.observableArrayList();

        try {
            //Querying data for orders items
            String query = "SELECT Room_No, Special_Request,Inventories,Extra_Charges,Service_Date_Time, CONCAT(Employee.FirstName, ' ',Employee.LastName) AS BellBoyName,Bell_Boy " +
                    "FROM CUSTOMER,ROOM_SERVICE,EMPLOYEE " +
                    "WHERE CUSTOMER.Customer_ID = ROOM_SERVICE.Customer_ID AND EMPLOYEE.E_ID=ROOM_SERVICE.Bell_Boy " +
                    "ORDER BY Room_No ASC;";

            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            //Iterating over data and adding to rooms observable list

            while (resultSet.next()) {
                //Extract data from a particular row
                int ordersNum = resultSet.getInt("Room_No");
                String ordersRequests = resultSet.getString("Special_Request");
                String ordersInventories = resultSet.getString("Inventories");
                if(ordersInventories.equals("NULL")){
                    ordersInventories="-";
                }
                //TODO: Change Datatype for Special_Charges
                int ordersCharges = (int) resultSet.getDouble("Extra_Charges");
                String ordersTime = resultSet.getString("Service_Date_time");
                String ordersBellboy = resultSet.getString("Bell_Boy");

                //Add data to ordersList
                ordersList.add(new Orders(ordersNum, ordersRequests, ordersInventories, ordersCharges, ordersTime, ordersBellboy));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordersList;
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

    public void txtRoomNBChanged(){

        txtRoomNB.textProperty().addListener((observable, oldValue, newValue) -> {
            if(validateRoomNum(newValue)){
                //lblRoomStatusNB.setText(newValue);
                if(isRoomAvailable(newValue.trim())){
                    lblRoomStatusNB.setText("Available!");
                    Pair<String,Integer> roomTypeData = getRoomType(newValue.trim());
                    if(roomTypeData==null){
                        lblRoomType.setText("");
                        lblRoomTypePrice.setText("");
                    }
                    else
                    {
                        lblRoomType.setText(roomTypeData.getFirst());
                        lblRoomTypePrice.setText(String.valueOf(roomTypeData.getSecond()));
                    }
                }
                else
                {
                    if(!lblRoomStatusNB.getText().equals("No such room!")){
                        lblRoomStatusNB.setText("Occupied!");
                    }
                    lblRoomType.setText("");
                    lblRoomTypePrice.setText("");
                }
            }
            else
            {
                lblRoomStatusNB.setText("Invalid Room!");
                lblRoomType.setText("");
                lblRoomTypePrice.setText("");
            }
        });
    }

    private Pair<String,Integer> getRoomType(String roomNum) {

        // TODO: 12-04-2021 Make view
        String query = "SELECT Type, Price from room,room_type where room.Room_Type = room_type.Type and Room_No = " + roomNum +" ;";
        try {
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String roomType = resultSet.getString("Type");
                Integer roomPrice = resultSet.getInt("Price");
                Pair<String,Integer> resPair = new Pair<String,Integer>(roomType,roomPrice);
                return resPair;
            }
            else{
                lblRoomType.setText("");
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            lblRoomType.setText("SQL/DB error!");
        }
        return null;
    }

    public boolean isRoomAvailable(String roomNum) {

        // TODO: 12-04-2021 Make view
        String query = "SELECT Availability from room where Room_No = "+roomNum+" ;";

        try {
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int availability = resultSet.getInt("Availability");
                if(availability==1)return true;
                else return false;
            }
            else{
                lblRoomStatusNB.setText("No such room!");
                lblRoomType.setText("");
                lblRoomTypePrice.setText("");
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            lblRoomStatusNB.setText("SQL/DB error!");
        }
        return false;
    }

    private boolean validateRoomNum(String roomNum) {
        Pattern pattern = Pattern.compile("[1-9][0-9][0-9]");
        Matcher matcher = pattern.matcher(roomNum.trim());
        boolean matchFound = matcher.find();
        if(matchFound && roomNum.trim().length()==3)
        {
            return true;
        }
        return false;
    }

    public void resetNewBooking(){
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtUIDAI.setText("");
        txtPassport.setText("");
        txtProfession.setText("");
        // TODO: 12-04-2021 Read input
        //dpArrival.setChronology(null);
        //dpDeparture.setChronology(null);
        txtAddress.setText("");
        //spinnerOccupants
    }

    public void bookRoom(){
        boolean allOK = validateAllFields();
        if(allOK){
            // TODO: 12-04-2021 Update query

        }
    }

    private boolean validateAllFields() {

        boolean allOk = true;

        String firstName = txtFirstName.getText();
        String surname = txtLastName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String adhar = txtUIDAI.getText();
        String passport = txtPassport.getText();
        String profession = txtProfession.getText();
        String address = txtAddress.getText();
        // TODO: 12-04-2021 Get data
        // dpArrival.setChronology(null);
        //dpDeparture.setChronoly
        //spinnerOccupants

        // TODO: 12-04-2021 Add Regex validations
        if(firstName.isEmpty()){
            txtFirstName.setStyle("-fx-border-color: red");
            allOk = false;
        }

        if(surname.isEmpty()){
            txtLastName.setStyle("-fx-border-color: red");
            allOk = false;
        }

        if(email.isEmpty()){
            txtEmail.setStyle("-fx-border-color: red");
            allOk = false;
        }

        if(phone.isEmpty()){
            txtPhone.setStyle("-fx-border-color: red");
            allOk = false;
        }

        if(adhar.isEmpty()){
            txtUIDAI.setStyle("-fx-border-color: red");
            allOk = false;
        }

        if(passport.isEmpty()){
            txtPassport.setStyle("-fx-border-color: red");
            allOk = false;
        }

        if(profession.isEmpty()){
            txtProfession.setStyle("-fx-border-color: red");
            allOk = false;
        }

        // TODO: 12-04-2021  Add remaining field

        return allOk;
    }

    public void txtRoomCheckoutChanged(){
        // TODO: 12-04-2021 Clear when invalid room number

        txtRoomCheckout.textProperty().addListener((observable, oldValue, newValue) -> {
            if(validateRoomNum(newValue.trim())){
                if(!isRoomAvailable(newValue.trim())){
                    lblRoomStatusCheckout.setText("Occupied!");
                    //Fill detail on Checkout scene
                    boolean fetchOK = fillCheckoutDetail(newValue.trim());
                    if(fetchOK){
                        btnFinalCheckout.setVisible(true);
                    }
                }
                else
                {
                    if(lblRoomStatusCheckout.getText().equals("No such room!")){
                        lblRoomStatusCheckout.setText("Already Vacant!");
                    }
                    btnFinalCheckout.setVisible(false);
                    clearCheckoutDetail();
                }
            }
            else
            {
                lblRoomStatusCheckout.setText("No such room!");
                btnFinalCheckout.setVisible(false);
                clearCheckoutDetail();
            }
        });
    }

    private void clearCheckoutDetail() {
        lblName.setText("");
        lblEmail.setText("");
        lblPhone.setText("");
        lblOccupants.setText("");
        lblCheckin.setText("");
        lblCheckout.setText("");
        lblRoomNumCheckOut.setText("");
        lblRoomPriceCheckOut.setText("");
        lblDuration.setText("");
        lblDurationPrice.setText("");
        lblService.setText("");
        lblServicePrice.setText("");
        lblTotalRoomPriceCheckout.setText("");
        lblSubtotal.setText("");
        lblAdditionalCharges.setText("");
        lblTax.setText("");
        lblTotalCheckout.setText("");
        lblPaid.setText("");
        lblDue.setText("");
    }

    private boolean fillCheckoutDetail(String roomNum) {

        boolean fetchOK = true;
        String query = "";

        try {
            //Customer details
            query = "SELECT CONCAT(Visitor.FirstName, ' ', Visitor.LastName) AS Name, E_mail, " +
                    "Customer.Phone AS Phone, " +
                    "Occupants_Num AS Occupants, " +
                    "Arrival AS CheckIn " +
                    "FROM CUSTOMER, " +
                    "VISITOR, " +
                    "ROOM " +
                    "WHERE Visitor.Visitor_ID = Customer.Visitor_ID " +
                    "AND room.Room_No=customer.Room_No " +
                    "AND Customer.Room_No = " + roomNum + " ;";

            System.out.println(roomNum);

            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                lblName.setText(resultSet.getString("Name"));
                lblEmail.setText(resultSet.getString("E_mail"));
                lblPhone.setText(resultSet.getString("Phone"));
                lblCheckin.setText(resultSet.getString("CheckIn").substring(0,10));
                lblOccupants.setText(resultSet.getString("Occupants"));
                //Today's date
                lblCheckout.setText(DateManipulation.getTodayDate());
            }else{
                fetchOK=false;
            }

            //Summary details
            // TODO: 13-04-2021 remove this! 
            query = "SELECT Type, Price " +
                    "from ROOM_TYPE, " +
                    " room " +
                    "WHERE room.Room_Type = room_type.Type " +
                    "  AND Room_No = " + roomNum +" ;";

            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                // TODO: 12-04-2021 Checkout Label and duration 
                lblRoomNumCheckOut.setText(resultSet.getString("Type"));
                lblRoomPriceCheckOut.setText(resultSet.getString("Price"));
            }else{
                fetchOK=false;
            }

            //Service details
            query = "SELECT Service_type.Type AS Service_Type, Service_type.Price AS Price " +
                    "from Customer, Service_type " +
                    "WHERE Customer.service_type = Service_type.type " +
                    "AND Customer.Room_No = " + roomNum + " ;";

            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                // TODO: 12-04-2021 Total Label
                lblService.setText(resultSet.getString("Service_Type"));
                lblServicePrice.setText(resultSet.getString("Price"));
            }else{
                fetchOK=false;
            }


        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            lblRoomStatusNB.setText("SQL/DB error!");
            fetchOK=false;
        }
        if(fetchOK==false){
            System.out.println("FetchOK = false!");
        }

        // TODO: 12-04-2021 Duration, Total Amount
        int roomPrice = Integer.parseInt(lblRoomPriceCheckOut.getText());
        lblDuration.setText(String.valueOf(DateManipulation.getDifference(lblCheckin.getText())));
        int duration = Integer.parseInt(lblDuration.getText());
        int roomBill = roomPrice*duration;

        lblDurationPrice.setText(String.valueOf(duration)+ "*" + String.valueOf(roomPrice));

        int serviceBill = (int)Double.parseDouble(lblServicePrice.getText());

        int totalRoomBill = roomBill+serviceBill;
        lblTotalRoomPriceCheckout.setText(String.valueOf(totalRoomBill));

        //Right pane
        lblSubtotal.setText(lblTotalRoomPriceCheckout.getText());

        int additionalCharges = findAdditionalCharges(roomNum);
        lblAdditionalCharges.setText(String.valueOf(additionalCharges));

        int withOutTaxTotal = totalRoomBill+additionalCharges;
        int tax = (int)(TAX*withOutTaxTotal);
        int total = withOutTaxTotal+tax;

        lblTax.setText(String.valueOf(tax));
        lblTotalCheckout.setText(String.valueOf(total));

        int paid = fetchPaid(roomNum);
        lblPaid.setText(String.valueOf(paid));

        int due = total-paid;
        // TODO: 12-04-2021 Fet pay listener
        lblDue.setText(String.valueOf(due));

        // TODO: 12-04-2021 Discount


        return fetchOK;
    }

    private int fetchPaid(String roomNum) {
        int paid = 0;
        String query = "SELECT Paid FROM Bill,Customer " +
                "WHERE Bill.Bill_ID = Customer.Bill_ID AND Customer.Room_No= " + roomNum +" ;";
        try{
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                paid = (int)resultSet.getDouble("Paid");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return paid;
    }

    private int findAdditionalCharges(String roomNum) {
        String query = "SELECT SUM(room_service.Extra_Charges) AS Addition_Charges_Sum " +
                "FROM room_service,customer " +
                "WHERE customer.Customer_ID = room_service.Customer_ID " +
                "AND customer.Room_No = "+roomNum +" ;";
        try{
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return (int)resultSet.getDouble("Addition_Charges_Sum");
            }else{
                return 0;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Could not fetch additional charges!");
        }
        return 0;
    }
}
    

