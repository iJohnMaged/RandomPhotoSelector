package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Random;


/*
 * The FileSystem class keeps track of a directory and the files in that directory that has an allowed extension
 */

public class FileSystem {

    private File directory;
    private StringProperty absolutePath;
    private ArrayList<File> totalFiles;
    private final String[] ALLOWED_EXTENSIONS;
    private final Random RANDOM_GEN = new Random();

    public FileSystem(String path, String[] allowed_extensions){
        totalFiles = new ArrayList<>();
        absolutePath = new SimpleStringProperty();
        ALLOWED_EXTENSIONS = allowed_extensions;
        changeDirectory(new File(path));
    }

    /*
     * Used to create a file if it doesn't exist.
     * @param fileRelativePath is the relative path to where to create the file, the file will be created in current path.
     */
    public static void createNotExisting(String fileRelativePath) {

        try {
            Path currentRelativePath = Paths.get("");
            File file = new File(currentRelativePath.toAbsolutePath().toString() + File.separator + fileRelativePath);

            if (!file.exists()) {
                file.mkdirs();
                file.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StringProperty absolutePathProperty() {
        return absolutePath;
    }

    /*
     * Recursively loads all files from a directory that has allowed extension to an ArrayList.
     * @param dir the directory to look for files in.
     */
    private void loadFiles(File dir){
        File[] files = dir.listFiles();

        if(files != null){
            for(File file: files) {
                if (file.isFile() && isAllowedExt(file)) {
                    totalFiles.add(file);
                } else if (file.isDirectory()) {
                    loadFiles(file);
                }
            }
        }

    }

    /*
     * Checks if a file has allowed extension or not.
     * @param file file to check.
     * @return Boolean true if the file's extension is allowed.
     */
    private boolean isAllowedExt(File file){
        String fileName = file.getName().toUpperCase();
        for(String ext: ALLOWED_EXTENSIONS){
            if(fileName.endsWith(ext))
                return true;
        }
        return  false;
    }

    /*
     * Selects a random file from loaded files.
     * @return Absolute path to the file or null if there is no files.
     */
    public String getRandomFile(){
        if(totalFiles.size() == 0)
            return null;
        int index = RANDOM_GEN.nextInt(totalFiles.size());
        return totalFiles.get(index).getAbsolutePath();
    }

    /*
     * Changes the current directory and re-loads all the files.
     * @param dir directory to scan.
     */
    public void changeDirectory(File dir){
        if(dir == directory)
            return;
        totalFiles.clear();
        directory = dir;
        absolutePath.setValue("Current dir: " + dir.getAbsolutePath());
        loadFiles(directory);
    }


    /*
     * Used to append text data to a file, creates the file if it doesn't exist.
     * @param fileRelativePath relative path to the file wrt current path.
     * @param text the text to be written to the file.
     */
    public static void appendToFile(String fileRelativePath, String text){
        try{
            createNotExisting(fileRelativePath);
            Files.write(Paths.get(fileRelativePath), (text+System.lineSeparator()).getBytes("UTF-8"), StandardOpenOption.APPEND);
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    /*
     * Opens a file with the default application associated with it.
     * @param filePath the file path to be opened with the associated application.
     */
    public static void startFile(String filePath){
        try {
            File file = new File(filePath);
            Desktop.getDesktop().open(file);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

}
