package application;

import javafx.scene.control.Alert;

/*
 * This class allows via static methods to create and show the basic dialogs that are supported in JavaFX8.
 */
public class FxDialog {

    public static void showAlert(Alert.AlertType type, String title, String headerText, String contextText){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
    }

    public static void showAlert(Alert.AlertType type, String title, String contextText){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contextText);
        alert.showAndWait();
    }
}
