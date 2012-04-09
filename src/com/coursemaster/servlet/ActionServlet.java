package com.coursemaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.coursemaster.auth.AuthenticationManager;
import com.coursemaster.auth.Session;
import com.coursemaster.database.DatabaseConnectionManager;
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

        String function = request.getRequestURI().substring(8).toLowerCase();

        if (function.equals("logout")) {
            doLogout(request, response);
        }
        else if (function.equals("update-user")) {
            doUserUpdate(request, response);
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

        String function = request.getRequestURI().substring(8).toLowerCase();

        if (function.equals("login")) {
            doLogin(request, response);
        }
        else if (function.equals("add-user")) {
            // Add user
        }
        else if (function.equals("update-user")) {
            doUserUpdate(request, response);
        }
    }

    /**
     * Method to update a user
     *
     * @param request The HTTP Request
     * @param resposne The HTTP Response
     */
    private void doUserUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.equals("")) {
            response.getWriter().write("No user specified");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String encryptedPassword = AuthenticationManager.authenticator.getHashedPassword(email, password);

        int updated = DatabaseConnectionManager.executeUpdate("update user set password = '" + encryptedPassword + "' where email = '" + email + "';");

        response.getWriter().write((updated < 1 ? "Failed to update " : "Successfully updated ") + "password for " + email);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Method to log a user out
     *
     * @param request The HTTP Request
     * @param response The HTTP Response
     */
    private void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = (Session) request.getAttribute("session");
        logger.trace("Destroying session for user: " + session.getName());
        AuthenticationManager.authenticator.logout(session);

        // Kill the cookie
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(Settings.cookieName)) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        response.sendRedirect("/login.html");
        response.addHeader("AUTHENTICATION", "Successfully logged out");
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
    }

    /**
     * Method to attempt to log a user in
     *
     * @param request The HTTP Request
     * @param response The HTTP Response
     */
    private void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email"),
               password = request.getParameter("password");

        if (email == null || email == "" ||
            password == null || password == "") {
            response.addHeader("Location", "/login.html");
            response.addHeader("FAILURE-REASON", "Both username and password are required to login");
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            return;
        }

        logger.trace("Attempting to create session for user: " + email);
        Cookie sessionCookie = AuthenticationManager.authenticator.login(email, password);

        if (sessionCookie == null) {
            response.addHeader("Location", "/login.html");
            response.addHeader("FAILURE-REASON", "Failed to login with that username/password combination");
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            return;
        }

        response.addCookie(sessionCookie);
        response.addHeader("AUTHENTICATION", "OK");
        response.sendRedirect("/service/dashboard");
    }

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(ActionServlet.class);
}
