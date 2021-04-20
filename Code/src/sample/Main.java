package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;
    private Parent root;

    @Override
    public void start(Stage stage) throws Exception{

            //root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            //root = FXMLLoader.load(getClass().getResource("Reception.fxml"));
            root = FXMLLoader.load(getClass().getResource("Manager.fxml"));

            stage.initStyle(StageStyle.UNDECORATED);
            stage.setMaximized(false);
            stage.setResizable(false);

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            Scene scene = new Scene(root,1050,600);
            //Scene scene = new Scene(root,900,500);
            stage.setScene(scene);
            stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
