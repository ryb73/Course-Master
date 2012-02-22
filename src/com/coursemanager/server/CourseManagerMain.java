package com.coursemanager.server;

import java.io.File;
import java.io.FileInputStream;
import java.util.EnumSet;
import java.util.Properties;

import javax.servlet.DispatcherType;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.coursemanager.server.filter.RequestVerifier;
import com.coursemanager.servlet.ResourceServlet;
import com.coursemanager.servlet.ServiceServlet;

/**
 * Main server component of the entire project.
 * Contains the server and all of its configuration,
 * and assigns the servlets and filtering, as well
 * as contexts, etc.
 *
 * @author Graham
 */
public class CourseManagerMain {
    public static final String FILESEPARATOR = System.getProperty("file.separator");

    /**
     * Main method, runs the server
     * @param args A list of arguments, currently unused
     * @throws Exception If the server fails to start
     */
    public static void main(String[] args) throws Exception {
        loadConfiguration();

        logger.trace("Spinning up servlets");
        ServletContextHandler root = new ServletContextHandler();
        root.setContextPath("/");
        root.addFilter(RequestVerifier.class, "/*", EnumSet.allOf(DispatcherType.class));
        root.addServlet(new ServletHolder(new ResourceServlet()), "/*");
        root.addServlet(new ServletHolder(new ServiceServlet()), "/service/*");

        // Instantiate the server on the specified port
        logger.info("Server starting on port " + port);
        Server server = new Server(port);
        server.setHandler(root);
        server.start();
        server.join();
    }

    /**
     * Method to load configuration settings
     */
    private static void loadConfiguration() {

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
        }
        catch(Exception e) {
            System.err.println("Failed to load configuration files.");
            System.exit(0);
        }

        port = Integer.parseInt(configProperties.getProperty("port"));
        DOMConfigurator.configureAndWatch(courseMasterDirectory + "settings" + FILESEPARATOR  + "log4j.xml");
    }

    private static int port;
    private static String courseMasterDirectory;
    private static Logger logger = Logger.getLogger(CourseManagerMain.class);
}
