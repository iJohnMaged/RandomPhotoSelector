package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("application.fxml"));
        Parent root = loader.load();
        Controller control = loader.getController();
        primaryStage.setTitle("Random Photo Selector");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        control.init();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
