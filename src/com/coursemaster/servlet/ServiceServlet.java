package com.coursemaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.coursemaster.service.Courses;
import com.coursemaster.service.Dashboard;
import com.coursemaster.service.Discussion;

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
            new Dashboard().doRequest(request, response);
        }
        else if(function.equals("discussion")) {
            new Discussion().doRequest(request, response);
        }
        else if (function.equals("courses")) {
            new Courses().doRequest(request, response);
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

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(ServiceServlet.class);
}