package com.coursemaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.coursemaster.server.Settings;

/**
 * This servlet acts as a sort of basic function servlet.
 * It is primarily responsible for session management actions
 * such as logging users in and out
 *
 * @author Graham
 */
public class ActionServlet extends HttpServlet {

    /**
     * Respond to a GET request
     *
     * @param request The HTTP Request
     * @param response The response to write
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info(request.getMethod() + " " + request.getRequestURI());

        //String function = request.getRequestURI().substring(8).toLowerCase();
    }

    /**
     * Respond to a POST request
     *
     * @param request The HTTP Request
     * @param response The response to write
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info(request.getMethod() + " " + request.getRequestURI());

        String function = request.getRequestURI().substring(8).toLowerCase();

        if (function.equals("login")) {
            doLogin(request, response);
        }
    }

    /**
     * Method to attempt to log a user in
     *
     * @param request The HTTP Request
     * @param response The HTTP Response
     */
    private void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username"),
               password = request.getParameter("password");

        if (username == null || username == "" ||
            password == null || password == "") {
            response.addHeader("Location", "/login.html");
            response.addHeader("FAILURE-REASON", "Both username and password are required to login");
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            return;
        }

        Cookie sessionCookie = Settings.authenticator.login(username, password);

        if (sessionCookie == null) {
            response.addHeader("Location", "/login.html");
            response.addHeader("FAILURE-REASON", "Failed to login with that username/password combination");
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            return;
        }

        response.addCookie(sessionCookie);
        response.sendRedirect("/service/dashboard");
        response.setContentLength(0);
    }

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(ActionServlet.class);
}
