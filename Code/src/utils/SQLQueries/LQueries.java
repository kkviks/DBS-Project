package utils.SQLQueries;

import utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LQueries {

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

    public static ResultSet getAuth(String eid) throws SQLException {
        query = "select Password,Designation  from credentials,employee where employee.E_ID=credentials.E_ID and credentials.E_ID= '"+eid+ "' ;";
        return execute();
    }
}
