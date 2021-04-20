package utils.SQLQueries;

import utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MQueries {

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

    public static ResultSet getAttendanceSummary() throws SQLException {
        query = "SELECT CONCAT(Employee.FirstName, ' ',Employee.LastName) AS Name, Designation, Employee.E_ID, isPresent,Attendance_Date AS Required_Attendance " +
                "FROM Employee, Attendance " +
                "WHERE Employee.E_ID=Attendance.E_ID AND (Attendance.Attendance_Date BETWEEN '2021-03-05' AND '2021-05-10');";
        return execute();
    }

    public static ResultSet getFinanceSummary() throws SQLException {
        query = "SELECT Update_Date,Wages,Rent,Profit,Credit " +
                "FROM Finance " +
                "WHERE Update_Date BETWEEN '2021-01-10' AND '2021-01-20');";
        return execute();
    }

    public static ResultSet getFinanceHeader(String whatFinance) throws SQLException {

        switch (whatFinance) {
            case "Total Wages":
                query = "SELECT SUM(Wage) FROM Finance";
                break;
            case "Total Rent":
                query = "SELECT SUM(Rent) FROM Finance";
                break;
            case "Total Profit":
                query = "SELECT SUM(Profit) FROM Finance";
        }

        return execute();

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

    public static ResultSet getRoomHeader(String whatRooms) throws SQLException {

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
}
