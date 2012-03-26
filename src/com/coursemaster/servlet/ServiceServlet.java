package com.coursemaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.coursemaster.auth.Session;
import com.coursemaster.servlet.util.FileUtil;

/**
 * The servlet for services requests, likely
 * those that have business logic or database requests.
 *
 * @author Graham
 */
public class ServiceServlet extends HttpServlet {


    /**
     * Respond to a GET request
     *
     * @param request The HTTP Request
     * @param response The response to write
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info(request.getMethod() + " " + request.getRequestURI());

        response.setContentType("text/html");
        String function = request.getRequestURI().substring(9).toLowerCase();

        if (function.equals("dashboard")) {
            doDashboard(request, response);
        
        else if(function.equals("discussion")) {
            doDiscussion(request, response);
        }
    }


    /**
     * Respond to a POST request
     *
     * @param request The HTTP Request
     * @param response The response to write
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info(request.getMethod() + " " + request.getRequestURI());

        response.setContentType("text/html");
        response.getWriter().println("Service servlet responding to POST request");
    }

    /**
     * Method to generate a discussion board
     *
     * @param request The HTTP Request
     * @param response The HTTP Response
     */
    private void doDiscussion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Open the template file
        String discussionAsString = FileUtil.loadTemplateFile("discussion.tpl");

        Session session = (Session) request.getAttribute("session");

        // Replace discussion board content with specified user's content
        discussionAsString = discussionAsString.replace("##USERNAME##", session.getUsername());

        response.getWriter().write(discussionAsString);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Method to generate a dashboard for a user
     *
     * @param request The HTTP Request
     * @param response The HTTP Response
     */
    private void doDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Open the template file
        String dashboardAsString = FileUtil.loadTemplateFile("dashboard.tpl");

        Session session = (Session) request.getAttribute("session");

        // Replace dashboard content with specified user's content
        dashboardAsString = dashboardAsString.replace("##USERNAME##", session.getUsername());

        response.getWriter().write(dashboardAsString);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(ServiceServlet.class);
}