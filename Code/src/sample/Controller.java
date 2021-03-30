package sample;


import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.ConnectionUtil;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    //Links Controller and login scene
    @FXML
    private TextField txtEid;
    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btn_clear, btn_signIn, btn_signUp, btn_close;

    @FXML
    private Label lbl_forgotPassword, lblErrors, lbl_msg;

    @FXML ImageView img_close;

    //SQL setup
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbl_msg.setText("");

        //Connect with DB
        con = ConnectionUtil.conDB();

        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Error : Check");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Server is up : Good to go");
        }

    }

    public void signIn(){
        if(con==null)return;
        String userType = authenticate();
    }

    private String authenticate() {
        String status = "";

        //Fetch username and password
        String username = txtEid.getText();
        String password = txtPassword.getText();

        //Check if fields empty or not
        if(username.isEmpty() || password.isEmpty())
        {
            setLblError(Color.TOMATO, "Empty credentials");
            status = "Error";
        }
        else
        {
            //If fields not empty write query
            // TODO: 30-03-2021 Ritka fetch usernames and password
            String query = "SELECT * FROM auth where eid=? and password=?";
            try{
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    setLblError(Color.TOMATO, "Enter Correct Email/Password");
                    status = "Error";
                }else {
                    setLblError(Color.GREEN, "Login Successful..Redirecting..");
                }

            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                status = "Exception";
            }
        }
        return status;
    }

    public void clear(){
        txtEid.setText("");
        txtPassword.setText("");
    }

    public void referAdmin(){
        lbl_msg.setText("Contact Admin!");
    }

    public void clearReferAdmin(){
        lbl_msg.setText("");
    }

    public void exit(){
        Platform.exit();
        System.exit(0);
    }

    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        System.out.println(text);
    }
}
