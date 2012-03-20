package com.coursemaster.servlet.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.coursemaster.server.Settings;

/**
 * Utility class for ease of file reading
 *
 * @author Graham
 */
public class FileUtil {

    /**
     * Method to read a web template file, such as the dashboard
     *
     * @param fileName The file to read
     * @return The string form of the file
     */
    public static String loadTemplateFile(String fileName) {
        File templateFile = new File(Settings.courseMasterDirectory + "/web/template/" + fileName);
        char[] fileContents = new char[(int) templateFile.length()];

        try {
            new FileReader(templateFile).read(fileContents, 0, (int) templateFile.length());
        } catch (FileNotFoundException e) {
            logger.warn("Unable to locate requested template file: " + fileName);
        } catch (IOException e) {
            logger.warn("Failed to read requested template file: " + fileName);
        }

        return new String(fileContents);
    }
    
    private static Logger logger = Logger.getLogger(FileUtil.class);
}
