package com.coursemanager.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class DefaultServlet extends HttpServlet {

    /**
     * Constructor the the DefaultServlet
     * 
     * The default servlet acts primarily as a "Resource" or file handler
     * internally, all requests are sent to a resource handler, and if they
     * aren't found, a message is returned stating this.
     * 
     * This servlet exists because I was unable to find a way to "filter"
     * file requests. All content that reaches this serve has come through
     * the RequestVerifier class first, so no secure resources leak through.
     */
    public DefaultServlet() {
        resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
        resourceHandler.setResourceBase("./web");
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info(request.getMethod() + " " + request.getRequestURI());

        resourceHandler.handle(request.getRequestURI(), (Request) request, request, response);

        if (!response.isCommitted()) {
            logger.trace("Requested resource not found");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("text/html");
            response.getWriter().println("<h1>Resource Not Found</h1><br/>Sorry, the resource you've requested could not be located.");
        }
    }

    private static ResourceHandler resourceHandler;

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(DefaultServlet.class);
}