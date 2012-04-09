package com.coursemaster.server;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;

import com.coursemaster.auth.AuthenticationManager;
import com.coursemaster.database.DatabaseConnectionManager;

/**
 * A class representing this Server's settings.
 * Note that all variables are globally accessible.
 *
 * @author Graham
 */
public class Settings {
    // System Settings
    public static final String FILESEPARATOR = System.getProperty("file.separator");
    public static String courseMasterDirectory;

    // Web information
    public static int port;

    // Database Config
    public static DatabaseConnectionManager dbManager;

    // Authentication Parts
    public static String cookieName;

    /**
     * Method to load configuration settings
     */
    public static void loadConfiguration() {

        // Check for the CMDIR to be set
        courseMasterDirectory = System.getenv("CMDIR");

        // If it's null, use the user directory
        if (courseMasterDirectory == null) {
            courseMasterDirectory = System.getProperty("user.dir");
        }

        // Force directory to end with file separator
        if (!courseMasterDirectory.endsWith(FILESEPARATOR)) {
            courseMasterDirectory = courseMasterDirectory + FILESEPARATOR;
        }

        // Initialize logging
        DOMConfigurator.configureAndWatch(courseMasterDirectory + "settings" + FILESEPARATOR  + "log4j.xml");

        // Attempt to load settings file
        Properties configProperties = new Properties();
        try {
            configProperties.load(new FileInputStream(new File(courseMasterDirectory + "settings" + FILESEPARATOR + "ServerSettings.properties")));

            port = Integer.parseInt(configProperties.getProperty("port"));

            String authenticationMode = configProperties.getProperty("auth");
            AuthenticationManager.init(authenticationMode);

            // Load the cookie name
            cookieName = configProperties.getProperty("cookie");

            // Load Database configuration
            String cmdb = configProperties.getProperty("cmdb");
            String dbloc = configProperties.getProperty("dblocation");
            String dbuser = configProperties.getProperty("dbuser");
            String dbpass = configProperties.getProperty("dbpass");

            String dbConnector = configProperties.getProperty("connector");
            DatabaseConnectionManager.init(dbConnector, dbloc, cmdb, dbuser, dbpass);
        }
        catch(Exception e) {
            System.err.println("Failed to load configuration settings.");
            e.printStackTrace();
            System.exit(0);
        }
    }
}
