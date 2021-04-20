package utils.SQLQueries;

import utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RQueries {
    private static Connection con;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet=null;
    private static String query;

    private static ResultSet execute() throws SQLException {
        con = ConnectionUtil.conDB();
        if(con==null){
            System.out.println("Connection JDBC is null!");
            return null;
        }
        try{
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if(resultSet==null){
                System.out.println("ResultSet is null!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getStaffSummary() throws SQLException {
        query = "SELECT CONCAT(Employee.FirstName, ' ',Employee.LastName) AS Name, Designation, Phone, Shift, isPresent AS Availability " +
                "FROM Employee, Attendance " +
                "WHERE Employee.E_ID=Attendance.E_ID;";
        return execute();
    }

    public static ResultSet getStaffHeader(String whatStaff) throws SQLException {

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

        return execute();

    }

    public static ResultSet getOverviewHeader(String whatRooms) throws SQLException {

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

        return execute();

    }

    public static ResultSet getOverviewSummary() throws SQLException {
        query = "SELECT Room_No,Room_Type, Availability , Beds_Num, Price " +
                "FROM room,room_type " +
                "WHERE room.room_type=room_type.Type " +
                "ORDER BY room.Availability DESC, " +
                "Room_Type.Price DESC, " +
                "room.Room_No ASC;";
        return execute();
    }

    public static ResultSet getCustomerHeader(String whatCustomers) throws SQLException {

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

        return execute();

    }

    public static ResultSet getCustomerSummary() throws SQLException {
        query = "SELECT Room_No , CONCAT(Visitor.FirstName, ' ',Visitor.LastName) AS Name,Service_Type,Occupants_Num, Arrival, Special_Requests,Due " +
                "FROM Customer,Visitor,Bill " +
                "WHERE Customer.Visitor_ID=Visitor.Visitor_ID AND Bill.Bill_ID=Customer.Bill_ID " +
                "ORDER BY Room_No;";
        return execute();
    }

    public static ResultSet getOrdersSummary() throws SQLException {
        query = "SELECT Room_No, Special_Request,Inventories,Extra_Charges,Service_Date_Time, CONCAT(Employee.FirstName, ' ',Employee.LastName) AS BellBoyName,Bell_Boy " +
                "FROM CUSTOMER,ROOM_SERVICE,EMPLOYEE " +
                "WHERE CUSTOMER.Customer_ID = ROOM_SERVICE.Customer_ID AND EMPLOYEE.E_ID=ROOM_SERVICE.Bell_Boy " +
                "ORDER BY Room_No ASC;";
        return execute();
    }

    public static ResultSet getCheckoutSummary(String roomNum) throws SQLException {
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
        return execute();
    }

    public static ResultSet getType(String roomNum) throws SQLException {
        query = "SELECT Type, Price from room,room_type where room.Room_Type = room_type.Type and Room_No = " + roomNum +" ;";
        return execute();
    }

    public static ResultSet getAvailability(String roomNum) throws SQLException {
        query = "SELECT Availability from room where Room_No = "+ roomNum +" ;";
        return execute();
    }

    public static ResultSet getPaid(String roomNum) throws SQLException {
        query = "SELECT Paid FROM Bill,Customer " +
                "WHERE Bill.Bill_ID = Customer.Bill_ID AND Customer.Room_No= " + roomNum +" ;";
        return execute();
    }

    public static ResultSet getAdditionalCharges(String roomNum) throws SQLException {
        query = "SELECT SUM(room_service.Extra_Charges) AS Addition_Charges_Sum " +
                "FROM room_service,customer " +
                "WHERE customer.Customer_ID = room_service.Customer_ID " +
                "AND customer.Room_No = "+roomNum +" ;";
        return execute();
    }

    public static ResultSet getServiceSummary(String roomNum) throws SQLException {
        query = "SELECT Service_type.Type AS Service_Type, Service_type.Price AS Price " +
                "from Customer, Service_type " +
                "WHERE Customer.service_type = Service_type.type " +
                "AND Customer.Room_No = " + roomNum + " ;";
        return execute();
    }

    public static ResultSet getServiceTypes() throws SQLException {
        query = "SELECT Type AS Service_Types from service_type ;";

        try{
            resultSet = execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getServiceTypePrice(String serviceType) {
        query = "SELECT Price from service_type where Type= " + "'" + serviceType +"'" +" ;";
        try{
            resultSet = execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
