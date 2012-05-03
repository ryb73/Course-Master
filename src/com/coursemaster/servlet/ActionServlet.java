package com.coursemaster.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.coursemaster.auth.Authenticator;
import com.coursemaster.auth.Session;
import com.coursemaster.servlet.util.StringUtil;

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
        else {
            doRestfulResponse(function, request, response);
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
        else {
            doRestfulResponse(function, request, response);
        }
    }

    /**
     * Attempt to respond to a restful request
     * IE: /user/create would invoke User.create()
     *
     * @param function The URI function piece
     * @param request The HTTP Request
     * @param response The HTTP Response
     */
    private void doRestfulResponse(String function, HttpServletRequest request, HttpServletResponse response) {
        // Split the method by slashes
        String restPieces[] = function.split("/");
        try {
            // Find the class (within our package) scheme
            Class<?> cls = Class.forName("com.coursemaster.action." + StringUtil.capitalize(restPieces[0]));
            // Attempt to locate method
            Method method = cls.getDeclaredMethod(restPieces[1], HttpServletRequest.class, HttpServletResponse.class);
            // Call method
            method.invoke(cls.newInstance(), request, response);
        } catch (Exception e) {
            try {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{ success: false, errors: { message: 'The model and method combination you specified could not be located' }}");
            }
            catch (IOException ioe) {}
        }
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

        // Kill the cookie
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(Authenticator.cookieName)) {
                Authenticator.logout(cookie.getValue());
                cookie.setMaxAge(-1);
                response.addCookie(cookie);
            }
        }

        response.sendRedirect("/login.html?logout=true");
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

        if (email == null || email == "" || password == null || password == "") {
            response.addHeader("Location", "/login.html?login=false");
            response.addHeader("FAILURE-REASON", "Both username and password are required to login");
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            return;
        }

        logger.trace("Attempting to create session for user: " + email);
        Cookie sessionCookie = Authenticator.login(email, password);

        if (sessionCookie == null) {
            response.addHeader("Location", "/login.html?login=false");
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
