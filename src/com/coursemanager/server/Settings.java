package com.coursemanager.server;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;

import com.coursemanager.auth.Authenticator;
import com.coursemanager.auth.MockAuthenticator;

/**
 * A class representing this Server's settings.
 * Note that all variables are globally accessible.
 *
 * @author Graham
 */
public class Settings {
    public static final String FILESEPARATOR = System.getProperty("file.separator");
    public static String courseMasterDirectory;

    public static int port;

    public static String authenticationMode;
    public static Authenticator authenticator;
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

        // Attempt to load settings file
        Properties configProperties = new Properties();
        try {
            configProperties.load(new FileInputStream(new File(courseMasterDirectory + "settings" + FILESEPARATOR + "ServerSettings.properties")));

            port = Integer.parseInt(configProperties.getProperty("port"));
            authenticationMode = configProperties.getProperty("auth");

            if (authenticationMode == null || !authenticationMode.matches("Mock")) {
                throw new IllegalArgumentException("Invalid authentication type");
            }

            if (authenticationMode.equals("Mock")) {
                authenticator = new MockAuthenticator();
            }

            // Load the cookie name
            cookieName = configProperties.getProperty("cookie");
        }
        catch(Exception e) {
            System.err.println("Failed to load configuration settings.");
            e.printStackTrace();
            System.exit(0);
        }

        DOMConfigurator.configureAndWatch(courseMasterDirectory + "settings" + FILESEPARATOR  + "log4j.xml");
    }
}
