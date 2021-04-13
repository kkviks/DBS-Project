package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Queries {
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
                System.out.println("Resultset is null!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getSummary(String roomNum) throws SQLException {
        query = "SELECT Type, Price " +
                "from ROOM_TYPE, " +
                " room " +
                "WHERE room.Room_Type = room_type.Type " +
                "  AND Room_No = " + roomNum + " ;";
        return execute();
    }

}
