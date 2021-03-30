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

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField txtEid;
    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btn_clear, btn_signIn, btn_signUp, btn_close;

    @FXML
    private Label lbl_forgotPassword, lblErrors, lbl_msg;

    @FXML ImageView img_close;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbl_msg.setText("");
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
}
