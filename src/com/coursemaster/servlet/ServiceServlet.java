package com.coursemaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.coursemaster.service.Discussion;
import com.coursemaster.service.dashboard.Courses;
import com.coursemaster.service.dashboard.Dashboard;
import com.coursemaster.service.event.CreateEvent;
import com.coursemaster.service.event.DestroyEvent;
import com.coursemaster.service.event.GetAllEvents;
import com.coursemaster.service.event.GetCourseEvents;
import com.coursemaster.service.event.UpdateEvent;

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
        else if (function.equals("events/all")) {
            new GetAllEvents().doRequest(request, response);
        }
        else if (function.equals("events/course")) {
            new GetCourseEvents().doRequest(request, response);
        }
        else if (function.startsWith("discussion")) {
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

        String function = request.getRequestURI().substring(9).toLowerCase();

        if (function.startsWith("discussion")) {
            new Discussion().doRequest(request, response);
        }
        else if (function.equals("events/create")) {
            new CreateEvent().doRequest(request, response);
        }
        else if (function.equals("events/update")) {
            new UpdateEvent().doRequest(request, response);
        }
        else if (function.equals("events/destroy")) {
            new DestroyEvent().doRequest(request, response);
        }
    }

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(ServiceServlet.class);
}