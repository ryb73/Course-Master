package com.coursemanager.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class DefaultServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info(request.getMethod() + " " + request.getRequestURI());

        response.setContentType("text/html");
        response.getWriter().println("You have reached a page that is covered by the default servlet");
    }

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(DefaultServlet.class);
}