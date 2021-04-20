package sample;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;
import utils.SQLQueries.LQueries;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Parent root;

    //Links Controller and login scene
    @FXML
    private TextField txtEid;
    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btn_clear, btn_signIn, btn_signUp, btn_close;

    @FXML
    private Label lbl_forgotPassword, lblErrors, lbl_msg ;

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
    @FXML
    public void signIn(ActionEvent event) throws IOException {
        if(con==null)return;
        String userType = authenticate();
        // TODO: 20-04-2021
        switch (userType){
            case "Receptionist":
                root = FXMLLoader.load(getClass().getResource("Reception.fxml"));
                break;
            case "Manager":
                root = FXMLLoader.load(getClass().getResource("Manager.fxml"));
                break;
                //add more users here!
        }
        //Show UI
        Scene scene = new Scene(root,1050,600);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
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
            try{
                resultSet = LQueries.getAuth(username);
                if (!resultSet.next()) {
                    setLblError(Color.TOMATO, "Enter Correct Employee ID");
                    status = "Error";
                }else {
                    String actualPassword = resultSet.getString("Password");
                    if(password.trim().equals(actualPassword.trim())){
                        status = resultSet.getString("Designation");
                        setLblError(Color.GREEN, "Welcome "+status +"!");
                    }
                    else{
                        setLblError(Color.TOMATO, "Enter Correct password!");
                    }
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
