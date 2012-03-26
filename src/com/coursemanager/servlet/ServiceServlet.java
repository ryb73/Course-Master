package com.coursemanager.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.coursemanager.auth.Session;
import com.coursemanager.servlet.util.FileUtil;

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

        String function = request.getRequestURI().substring(9);
        if(function.equals("discussion")) {
        	doDiscussion(request, response);
        }
    }


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