package com.coursemanager.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

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

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/");
        handler.addServlet(new ServletHolder(new DefaultServlet()), "/*");
        handler.addServlet(new ServletHolder(new ServiceServlet()), "/service/*");
 
        // Instantiate the server on the specified port
        Server server = new Server(port);
        server.setHandler(handler);
        server.start();
        server.join();
    }

    private static void loadConfiguration() {
        port = 8080;
    }

    private static int port;
}
