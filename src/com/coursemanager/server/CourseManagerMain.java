package com.coursemanager.server;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.coursemanager.server.filter.RequestVerifier;
import com.coursemanager.servlet.DefaultServlet;
import com.coursemanager.servlet.ServiceServlet;

public class CourseManagerMain {

    /**
     * Main method, runs the server
     * @param args A list of arguments, currently unused
     * @throws Exception If the server fails to start
     */
    public static void main(String[] args) throws Exception {
        loadConfiguration();
        initializeLogging();

        logger.trace("Spinning up servlets");
        ServletContextHandler root = new ServletContextHandler();
        root.setContextPath("/");
        root.addFilter(RequestVerifier.class, "/*", EnumSet.noneOf(DispatcherType.class));
        root.addServlet(new ServletHolder(new DefaultServlet()), "/*");
        root.addServlet(new ServletHolder(new ServiceServlet()), "/service/*");

        ResourceHandler resourceHandler = new ResourceHandler();
        //resourceHandler.
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });

        resourceHandler.setResourceBase("./web");
        root.setHandler(resourceHandler);

        // Instantiate the server on the specified port
        logger.info("Server started on port " + port);
        Server server = new Server(port);
        server.setHandler(root);
        server.start();
        server.join();
    }

    private static void initializeLogging() {
        ConsoleAppender defaultAppender = new ConsoleAppender(new PatternLayout("[%d{ISO8601}] %c{1}: %m%n"));
        BasicConfigurator.configure(defaultAppender);
        DOMConfigurator.configureAndWatch(log4jSettingsLocation);
    }

    private static void loadConfiguration() {
        log4jSettingsLocation = "settings/log4j.xml";
        port = 8080;
    }

    private static String log4jSettingsLocation;
    private static int port;
    private static Logger logger = Logger.getLogger(CourseManagerMain.class);
}
