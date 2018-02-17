package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Controller {

    @FXML
    private TextField numberOfPhotos;

    @FXML
    private CheckBox googleImageSearchCB, clipboardCB;

    @FXML
    private Text informationTxt;

    private FileSystem fileSys;
    private ConfigHandler configHandler;
    private String lastPhotoLoc = "";
    private final String CONFIG_FILE = "config.prop";
    private final String HISTORY_FILE = "history.txt";

    @FXML
    private void initialize() {
        configHandler = new ConfigHandler(CONFIG_FILE);
    }

    public void init(){

        String path = configHandler.getDirectoryProperty();

        if(path == null || path.isEmpty() || !Files.exists(Paths.get(path))){
            File dir = null;
            while(dir == null){
                FxDialog.showAlert(Alert.AlertType.INFORMATION, "Default Directory Missing or invalid.", "Select a default directory to use.");
                dir = directorySelection();
            }
            configHandler.writeProperty("directory", dir.getAbsolutePath());
        }

        String[] allowed_extensions = {".JPG", ".JPEG", ".JFIF", ".PNG"};
        fileSys = new FileSystem(configHandler.getDirectoryProperty(), allowed_extensions);
        informationTxt.textProperty().bindBidirectional(fileSys.absolutePathProperty());
    }


    @FXML
    private void incrementNumberOfPhotos(){
        int current = Integer.valueOf(numberOfPhotos.getText());
        numberOfPhotos.setText(Integer.toString(current+1));
    }

    @FXML
    private void decrementNumberOfPhotos(){
        int current = Integer.valueOf(numberOfPhotos.getText());
        if(current <= 1) return;
        numberOfPhotos.setText(Integer.toString(current-1));
    }

    @FXML
    private void openRandomPhoto() {

        for (int i = 0; i < Integer.valueOf(numberOfPhotos.getText()); i++) {
            lastPhotoLoc = fileSys.getRandomFile();

            if(lastPhotoLoc == null)
                FxDialog.showAlert(Alert.AlertType.INFORMATION, "Empty folder.", "There's no photos to choose form.");

            FileSystem.startFile(lastPhotoLoc);
            FileSystem.appendToFile(HISTORY_FILE, lastPhotoLoc);

            if(googleImageSearchCB.isSelected())
                new ThreadedImageSearch(lastPhotoLoc).start();

            if(clipboardCB.isSelected()){
                StringSelection photoPath = new StringSelection(lastPhotoLoc);
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                cb.setContents(photoPath, null);
            }
        }

    }

    @FXML
    private void directorySelector(){
        File dir = directorySelection();
        if(dir == null)
            return;
        fileSys.changeDirectory(dir);
    }

    @FXML
    private void imageSearchLastImage(){

        if(lastPhotoLoc.isEmpty() || !Files.exists(Paths.get(lastPhotoLoc))){
            FxDialog.showAlert(Alert.AlertType.ERROR, "Searching error..", "An error happened while trying to search"+
            "\nPhoto doesn't exist anymore or you didn't open a photo yet.");
            return;
        }

        new ThreadedImageSearch(lastPhotoLoc).start();
    }

    @FXML
    private void openPhotosHistory(){
        FileSystem.createNotExisting(HISTORY_FILE);
        FileSystem.startFile(HISTORY_FILE);
    }


    private File directorySelection(){
        Stage window = (Stage) numberOfPhotos.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        return directoryChooser.showDialog(window);
    }
}
