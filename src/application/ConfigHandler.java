package application;

import java.io.*;
import java.util.Properties;

/*
 * This class is used to handler a simple config file that has several properties.
 */

public class ConfigHandler {

    private File configFile;
    private Properties prop;

    public ConfigHandler(String fileName){
        prop = new Properties();
        FileSystem.createNotExisting(fileName);
        configFile = new File(fileName);

        try (InputStream input = new FileInputStream(configFile.getAbsolutePath())) {
            prop.load(input);
        } catch (IOException exc){
            exc.printStackTrace();
        }

    }

    public String getDirectoryProperty(){
        return prop.getProperty("directory");
    }

    /*
     * Writes a property to the config file.
     * @param property the name of the property
     * @param value the value of the property
     */
    public void writeProperty(String property, String value){
        OutputStream output = null;
        try {
            output = new FileOutputStream(configFile.getAbsolutePath());
            prop.setProperty(property, value);
            prop.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}