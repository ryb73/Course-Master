package com.coursemanager.server;

import org.eclipse.jetty.server.Server;

import com.coursemanager.handlers.TestHandler;

public class CourseManagerMain {

    /**
     * Main method, runs the server
     * @param args A list of arguments, currently unused
     * @throws Exception If the server fails to start
     */
    public static void main(String[] args) throws Exception {

        loadConfiguration();

        // Instantiate the server on the specified port
        Server server = new Server(port);
        server.setHandler(new TestHandler());
        server.start();
        server.join();
    }

    private static void loadConfiguration() {
        port = 8080;
    }

    private static int port;
}
