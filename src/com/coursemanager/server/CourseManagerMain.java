package com.coursemanager.server;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.apache.log4j.Logger;
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

    /**
     * Main method, runs the server
     * @param args A list of arguments, currently unused
     * @throws Exception If the server fails to start
     */
    public static void main(String[] args) throws Exception {
        Settings.loadConfiguration();

        logger.trace("Spinning up servlets");
        ServletContextHandler root = new ServletContextHandler();
        root.setContextPath("/");
        root.addFilter(RequestVerifier.class, "/*", EnumSet.allOf(DispatcherType.class));
        root.addServlet(new ServletHolder(new ResourceServlet()), "/*");
        root.addServlet(new ServletHolder(new ServiceServlet()), "/service/*");

        // Instantiate the server on the specified port
        logger.info("Server starting on port " + Settings.port);
        Server server = new Server(Settings.port);
        server.setHandler(root);
        server.start();
        server.join();
    }

    private static Logger logger = Logger.getLogger(CourseManagerMain.class);
}
