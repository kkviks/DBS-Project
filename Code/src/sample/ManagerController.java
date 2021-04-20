package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import models.*;
import utils.ConnectionUtil;
import utils.Report;
import utils.SQLQueries.MQueries;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ManagerController implements Initializable {

    @FXML
    private Button btnRoomsManager;

    @FXML
    private Button btnAttendanceManager;

    @FXML
    private Button btnStaff;

    @FXML
    private Button btnFinances;

    @FXML
    private Button btnNewEmployee;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnSignout;

    //panels

    @FXML
    Pane pnlRooms;

    @FXML
    Pane pnlNewEmp;

    @FXML
    Pane pnlStaff;

    @FXML
    Pane pnlCustomer;

    @FXML
    Pane pnlAttedance;

    @FXML
    Pane pnlCheckout;

    @FXML
    Label lblServerStatus;

    @FXML
    TextField filterRoomTable, filterStaffTable,filterAttendanceTable,filterFinanceTable;

    @FXML
    Label lblRoomsTotal, lblRoomsOccupied, lblRoomsAvailable;

    @FXML
    Label lblStaffTotal, lblStaffPresent, lblStaffAbsent;

    @FXML
    Label lblFinanceWage, lblFinanceRent , lblFinanceProfit;

    @FXML
    Label lblServerStatusStaff;

    @FXML
    Label lblServerStatusOrder;

    @FXML
    Button btnRooms;


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
    TableView<Attendance> attendanceTableView;

    @FXML
    TableView<Finance> financeTableView;

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
    @FXML
    TableColumn<Staff, Integer> staffSalaryCol;

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


    //For Finance table
    @FXML
    TableColumn<Finance, String> financeDateCol;
    @FXML
    TableColumn<Finance, Double> financeWageCol;
    @FXML
    TableColumn<Finance, Double> financeRentCol;
    @FXML
    TableColumn<Finance, Double> financeProfitCol;
    @FXML
    TableColumn<Finance, Double> financeCreditCol;


    @FXML
    TableColumn<Attendance, String> attendanceNameCol;
    @FXML
    TableColumn<Attendance, Integer> attendanceIDCol;
    @FXML
    TableColumn<Attendance, String> attendancePresentCol;
    @FXML
    TableColumn<Attendance, String> attendanceDesignationCol;
    @FXML
    TableColumn<Attendance, String> attendanceDateCol;


    @FXML
    TextField filterCustomerTable;

    @FXML
    TextField filterOrderTable;

    @FXML
    Label lblServerStatusCustomer, lblVisitorCount, lblCustomerCount, lblAmountDueCount;

    //Attendance header
    @FXML
    DatePicker attendanceFrom, attendanceTo;

    @FXML
    TextField txtAttendanceEmpSearch;

    @FXML
    PieChart attendancePieChart;

    @FXML
    Button btnEmpAttendance;

    @FXML
    Label lblEmpAtdStatus, lblServerStatusFinance;;

    //Visualize
    @FXML Button btnVisualizeManager;

    @FXML Pane pnlVisualize;

    //Reports
    @FXML
    DatePicker dpEmp, dpFinanceFrom, dpFinanceTo, dpVisitorsFrom, dpVisitorsTo, dpParkingFrom, dpParkingTo;

    @FXML
    Button btnReportEmp, btnReportFinance, btnReportVisitors, btnReportParking;


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
        setupRooms();
        setupStaff();
        setupCustomer();
        setupAttendance();
        setupFinance();
        showOnly("Rooms");
    }

    private void setupAttendance() {
        // TODO: 13-04-2021
        //setupAttendanceHeader();
        setupAttendanceTable();
    }

    private void setupAttendanceTable() {
        //Set Cell and Property Value Factory for the table
        attendanceNameCol.setCellValueFactory(new PropertyValueFactory<Attendance, String>("attendanceName"));
        attendanceIDCol.setCellValueFactory(new PropertyValueFactory<Attendance, Integer>("attendanceID"));
        attendancePresentCol.setCellValueFactory(new PropertyValueFactory<Attendance, String>("attendancePresent"));
        attendanceDesignationCol.setCellValueFactory(new PropertyValueFactory<Attendance, String>("attendanceDesignation"));
        attendanceDateCol.setCellValueFactory(new PropertyValueFactory<Attendance, String>("attendanceDate"));

        // 0. Initialize the columns.
        //ObservableList<Attendance> roomList = FXCollections.observableArrayList();
        ObservableList<Attendance> attendanceList = fetchAttendanceData();


        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Attendance> attendanceFilteredList = new FilteredList<>(attendanceList, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterAttendanceTable.textProperty().addListener((observable, oldValue, newValue) -> {
            attendanceFilteredList.setPredicate(attendance -> {

                //If filter is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase().trim();
                //Column-wise filtering logic
                if (attendance.getAttendanceName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if(attendance.getAttendanceDesignation().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(attendance.getAttendanceID()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (attendance.getAttendancePresent().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if(attendance.getAttendanceDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Attendance> attendanceSortedList = new SortedList<>(attendanceFilteredList);

        //4. Bind the Sortedlist comparator to the TableView comparator.
        //Otherwise, sorting the TableView would have no effect.
        attendanceSortedList.comparatorProperty().bind(attendanceTableView.comparatorProperty());

        //Add sorted ( and filtered ) data to the table.
        attendanceTableView.setItems(attendanceSortedList);

        //If we have no data to show
        attendanceTableView.setPlaceholder(new Label("No search results"));
    }

    private ObservableList<Attendance> fetchAttendanceData() {
        ObservableList<Attendance> attendancelist = FXCollections.observableArrayList();

        try {
            //Querying data for staff items
            /*String query = "SELECT CONCAT(Employee.FirstName, ' ',Employee.LastName) AS Name, Designation, Employee.E_ID, isPresent,Attendance_Date AS Required_Attendance " +
                    "FROM Employee, Attendance " +
                    "WHERE Employee.E_ID=Attendance.E_ID AND (Attendance.Attendance_Date BETWEEN '2021-03-05' AND '2021-05-10');";

            preparedStatement = con.prepareStatement(query);*/
            resultSet = MQueries.getAttendanceSummary();

            //Iterating over data and adding to observable list
            while (resultSet.next()) {
                //Extract data from a particular row
                String attendanceName = resultSet.getString("Name");
                Integer attendanceID= resultSet.getInt("E_ID");
                String attendanceDesignation = resultSet.getString("Designation");
                String attendancePresent = resultSet.getInt("isPresent") == 1 ? "Present" : "Absent";
                String attendanceDate = resultSet.getString("Required_Attendance");
                //Add data to attendancelist
                attendancelist.add(new Attendance(attendanceName, attendanceDesignation, attendanceID, attendancePresent, attendanceDate)); }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attendancelist;
    }

    private void setupFinance() {
        // TODO: 20-04-2021
        setupFinanceHeader();
        setupFinanceTable();
    }

    private void setupFinanceTable() {
        //Set Cell and Property Value Factory for the table
        financeDateCol.setCellValueFactory(new PropertyValueFactory<Finance, String>("financeDate"));
        financeWageCol.setCellValueFactory(new PropertyValueFactory<Finance, Double>("financeWage"));
        financeRentCol.setCellValueFactory(new PropertyValueFactory<Finance, Double>("financeRent"));
        financeProfitCol.setCellValueFactory(new PropertyValueFactory<Finance, Double>("financeProfit"));
        financeCreditCol.setCellValueFactory(new PropertyValueFactory<Finance, Double>("financeCredit"));

        // 0. Initialize the columns.
        //ObservableList<Attendance> roomList = FXCollections.observableArrayList();
        ObservableList<Finance> financeList = fetchFinanceData();


        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Finance> financeFilteredList = new FilteredList<>(financeList, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterFinanceTable.textProperty().addListener((observable, oldValue, newValue) -> {
            financeFilteredList.setPredicate(finance -> {

                //If filter is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase().trim();
                //Column-wise filtering logic
                if (finance.getFinanceDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if(String.valueOf(finance.getFinanceWage()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if(String.valueOf(finance.getFinanceRent()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if(String.valueOf(finance.getFinanceProfit()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if(String.valueOf(finance.getFinanceCredit()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Finance> financeSortedList = new SortedList<>(financeFilteredList);

        //4. Bind the Sortedlist comparator to the TableView comparator.
        //Otherwise, sorting the TableView would have no effect.
        financeSortedList.comparatorProperty().bind(financeTableView.comparatorProperty());

        //Add sorted ( and filtered ) data to the table.
       financeTableView.setItems(financeSortedList);

        //If we have no data to show
        financeTableView.setPlaceholder(new Label("No search results"));
    }

    private ObservableList<Finance> fetchFinanceData() {
        ObservableList<Finance> financelist = FXCollections.observableArrayList();

        try {
            //Querying data for staff items
            /*String query = "SELECT CONCAT(Employee.FirstName, ' ',Employee.LastName) AS Name, Designation, Employee.E_ID, isPresent,Attendance_Date AS Required_Attendance " +
                    "FROM Employee, Attendance " +
                    "WHERE Employee.E_ID=Attendance.E_ID AND (Attendance.Attendance_Date BETWEEN '2021-03-05' AND '2021-05-10');";

            preparedStatement = con.prepareStatement(query);*/
            resultSet = MQueries.getFinanceSummary();

            //Iterating over data and adding to observable list
            while (resultSet.next()) {
                //Extract data from a particular row
                String financeDate = resultSet.getString("Update_Date");
                Double financeWage= resultSet.getDouble("Wages");
                Double financeRent = resultSet.getDouble("Rent");
                Double financeProfit = resultSet.getDouble("Profit");
                Double financeCredit = resultSet.getDouble("Credit");
                //Add data to attendancelist
                financelist.add(new Finance(financeDate, financeWage, financeRent, financeProfit, financeCredit)); }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return financelist;
    }

    private void setupFinanceHeader() {
        //Get room numbers
        int numFinanceWage = getFinanceNums("Total Wages");
        int numFinanceRent = getFinanceNums("Total Rent");
        int numFinanceProfit = getFinanceNums("Total Profit");

        //Set label text values
        lblFinanceWage.setText(String.valueOf(numFinanceWage));
        lblFinanceRent.setText(String.valueOf(numFinanceRent));
        lblFinanceProfit.setText(String.valueOf(numFinanceProfit));

        //Server Status
        if (con == null) {
            lblServerStatusStaff.setTextFill(Color.TOMATO);
            lblServerStatusStaff.setText("Not OK");
            return;
        } else {
            lblServerStatusFinance.setTextFill(Color.GREEN);
            lblServerStatusFinance.setText("OK");
        }

    }

    private int getFinanceNums(String whatFinance) {

        int count = 0;

        try {
            resultSet = MQueries.getFinanceHeader(whatFinance);

            if (resultSet.next()) {
                count = resultSet.getInt("VAL");
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            lblServerStatusStaff.setText("Not OK");
        }

        return count;
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
        staffSalaryCol.setCellValueFactory(new PropertyValueFactory<Staff, Integer>("staffSalary"));

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
                } else if (String.valueOf(staff.getStaffSalary()).contains(lowerCaseFilter)) {
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
            /*String query = "SELECT CONCAT(Employee.FirstName, ' ',Employee.LastName) AS Name, Designation, Phone, Shift, isPresent AS Availability,Wage " +
                    "FROM Employee, Attendance,Designation " +
                    "WHERE Employee.E_ID=Attendance.E_ID AND Designation.Designation_Name=Employee.Designation;";

            preparedStatement = con.prepareStatement(query);*/
            resultSet = MQueries.getStaffSummary();

            //Iterating over data and adding to observable list
            while (resultSet.next()) {
                //Extract data from a particular row
                String staffName = resultSet.getString("Name");
                String staffDesignation = resultSet.getString("Designation");
                String staffPhone = resultSet.getString("Phone");
                String staffShift = resultSet.getString("Shift");
                String staffAvailability = resultSet.getInt("Availability") == 1 ? "Present" : "Absent";
                Integer staffSalary = resultSet.getInt("Wage");

                //Add data to stafflist
                stafflist.add(new Staff(staffName, staffDesignation, staffPhone, staffShift, staffAvailability, staffSalary));
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

        try {
            resultSet = MQueries.getStaffHeader(whatStaff);

            if (resultSet.next()) {
                count = resultSet.getInt("VAL");
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            lblServerStatusStaff.setText("Not OK");
        }

        return count;
    }

    private void setupRooms() {
        setupRoomsHeader();
        setupRoomsTable();
    }

    private void setupRoomsHeader() {
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
    
            try {
                resultSet = MQueries.getRoomHeader(whatRooms);
    
                if (resultSet.next()) {
                    count = resultSet.getInt("VAL");
                }
    
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                lblServerStatus.setText("Not OK");
            }
    
            return count;
    }

    private void setupRoomsTable() {

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
            /*String query = "SELECT Room_No,Room_Type, Availability , Beds_Num, Price " +
                    "FROM room,room_type " +
                    "WHERE room.room_type=room_type.Type " +
                    "ORDER BY room.Availability DESC, " +
                    "Room_Type.Price DESC, " +
                    "room.Room_No ASC;";

            preparedStatement = con.prepareStatement(query);*/
            resultSet = MQueries.getOverviewSummary();

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

        try {
            resultSet = MQueries.getCustomerHeader(whatCustomers);

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

        // TODO: 08-04-2021 Test new columns query data by chaning parameter of PropertyValueFactory and finding to existing tableCols cols 
        //customerCostCol.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerCost"));


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
            /*String query = "SELECT Room_No , CONCAT(Visitor.FirstName, ' ',Visitor.LastName) AS Name,Service_Type,Occupants_Num, Arrival, Special_Requests,Due " +
                    "FROM Customer,Visitor,Bill " +
                    "WHERE Customer.Visitor_ID=Visitor.Visitor_ID AND Bill.Bill_ID=Customer.Bill_ID " +
                    "ORDER BY Room_No;";

            preparedStatement = con.prepareStatement(query);*/
            resultSet = MQueries.getCustomerSummary();

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
        orderRequestsCol.setCellValueFactory(new PropertyValueFactory<Orders, String>("orderSpecialRequests"));
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
            /*String query = "SELECT Room_No, Special_Requests,Inventories,Extra_Charges,Service_Date_Time, CONCAT(Employee.FirstName, ' ',Employee.LastName) AS BellBoyName,Bell_Boy " +
                    "FROM CUSTOMER,ROOM_SERVICE,EMPLOYEE " +
                    "WHERE CUSTOMER.Customer_ID = ROOM_SERVICE.Customer_ID AND EMPLOYEE.E_ID=ROOM_SERVICE.Bell_Boy " +
                    "ORDER BY Room_No ASC;";

            preparedStatement = con.prepareStatement(query);*/
            resultSet = MQueries.getOrdersSummary();

            //Iterating over data and adding to rooms observable list

            while (resultSet.next()) {
                //Extract data from a particular row
                int ordersNum = resultSet.getInt("Room_No");
                String ordersRequests = resultSet.getString("Special_Requests");
                String ordersInventories = resultSet.getString("Inventories");
                //TODO: Change Datatype for Special_Charges
                int ordersCharges = (int) resultSet.getDouble("Special_Charges");
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

        pnlRooms.setStyle("-fx-background-color : #02030A");
        pnlNewEmp.setStyle("-fx-background-color : #02030A");
        pnlStaff.setStyle("-fx-background-color : #02030A");
        pnlCustomer.setStyle("-fx-background-color : #02030A");
        pnlAttedance.setStyle("-fx-background-color : #02030A");
        pnlCheckout.setStyle("-fx-background-color : #02030A");
        pnlVisualize.setStyle("-fx-background-color : #02030A");

        pnlRooms.setVisible(false);
        pnlNewEmp.setVisible(false);
        pnlStaff.setVisible(false);
        pnlCustomer.setVisible(false);
        pnlAttedance.setVisible(false);
        pnlCheckout.setVisible(false);
        pnlVisualize.setVisible(true);
    }

    private void showOnly(String panel) {

        switch (panel) {
            case "Visualize":
                pnlVisualize.toFront();
                pnlVisualize.setVisible(true);
                break;
            case "Rooms":
                pnlRooms.toFront();
                pnlRooms.setVisible(true);
                break;
            case "New Employee":
                pnlNewEmp.toFront();
                pnlNewEmp.setVisible(true);
                pnlNewEmp.setStyle("-fx-background-color : #02030A");
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
            case "Attendance":
                pnlAttedance.toFront();
                pnlAttedance.setVisible(true);
                pnlAttedance.setStyle("-fx-background-color : #02030A");
                break;
            case "Finances":
                pnlCheckout.toFront();
                pnlCheckout.setVisible(true);
                pnlCheckout.setStyle("-fx-background-color : #02030A");
                break;
            default: // Do nothing
        }
    }


    public void handleClicks(ActionEvent actionEvent) {

        if(actionEvent.getSource()==btnVisualizeManager){
            showOnly("Visualize");
        }

        if (actionEvent.getSource() == btnRoomsManager) {
            showOnly("Rooms");
        }

        if (actionEvent.getSource() == btnNewEmployee) {
            showOnly("New Employee");
        }

        if (actionEvent.getSource() == btnStaff) {
            showOnly("Staff");
        }

        if (actionEvent.getSource() == btnCustomers) {
            showOnly("Customers");
        }

        if (actionEvent.getSource() == btnAttendanceManager) {
            showOnly("Attendance");
        }

        if (actionEvent.getSource() == btnFinances) {
            showOnly("Finances");
        }

        if (actionEvent.getSource() == btnSignout) {
            Platform.exit();
            System.exit(0);
        }
    }
    public void empAttendanceFetchBtn(){
        // TODO: 13-04-2021
        LocalDate dateFrom = attendanceFrom.getValue();
        LocalDate dateTo = attendanceTo.getValue();

        String e_id = txtAttendanceEmpSearch.getText().trim();
        try{
            resultSet = MQueries.getEmpAttendance(e_id);
            if(resultSet.next()){
                int daysTotal = resultSet.getInt("DaysTotal");
                int daysPresent = resultSet.getInt("DaysPresent");
                ObservableList<PieChart.Data> list = FXCollections.observableArrayList(
                        new PieChart.Data("Present",daysPresent),
                        new PieChart.Data("Absent",daysTotal-daysPresent)
                );
                attendancePieChart.setData(list);
                attendancePieChart.setVisible(true);
                for(final PieChart.Data data: attendancePieChart.getData()){
                    data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            lblEmpAtdStatus.setText(data.getName()+": "+    String.valueOf(data.getPieValue()*100.0/daysTotal)+"%");
                        }
                    });
                }
            }else{
                System.out.println("No e_id for employee attendance!");
                setupAttendance();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void handReportClicks(MouseEvent actionEvent){


        if(actionEvent.getSource()==btnReportEmp){
            System.out.println("Print emp");
            Report.printEmployee();
        }

        if(actionEvent.getSource()==btnReportFinance){
            System.out.println("Print finance");
            // TODO: 20-04-2021  
        }

        if(actionEvent.getSource()==btnReportVisitors){
            Report.printVisitor();
        }

        if(actionEvent.getSource()==btnReportParking){
            Report.printparking();
        }
    }
}
    

