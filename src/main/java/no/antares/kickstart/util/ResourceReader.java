package no.antares.kickstart.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
*
* @author Tommy Skodje / �ystein L�vaas
*/
public class ResourceReader {

    /** */
    public static ResourceBundle readResourceBundle(File file) throws IOException {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            return new PropertyResourceBundle(in);
        } catch (FileNotFoundException e) {
            throw new IOException("The file does not exist: " + file.getAbsolutePath(), e);
        } catch (IOException e) {
            throw new IOException("Unable to read file: " + file.getAbsolutePath(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new IOException("Could not close file: " + file.getAbsolutePath(), e);
                }
            }
        }
    }
}
