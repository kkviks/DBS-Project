package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReceptionController implements Initializable {

    @FXML
    private VBox pnItems = null;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                pnItems.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        hideAll();
        showOnly("Overview");
    }

    private void hideAll() {
        pnlOverview.setVisible(false);
        pnlNewBooking.setVisible(false);
        pnlStaff.setVisible(false);
        pnlCustomer.setVisible(false);
        pnlOrders.setVisible(false);
        ;
        pnlCheckout.setVisible(false);
    }

    private void showOnly(String panel) {
        switch (panel) {
            case "Overview":
                pnlOverview.toFront();
                pnlOverview.setVisible(true);
                pnlOverview.setStyle("-fx-background-color : #02030A");
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
}
