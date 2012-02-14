package com.coursemanager.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coursemanager.logger.Logger;

public class ServiceServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger.log(this.getClass(), request.getMethod() + " " + request.getRequestURI());

        response.setContentType("text/html");
        response.getWriter().println("Service servlet responding to GET request");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger.log(this.getClass(), request.getMethod() + " " + request.getRequestURI());

        response.setContentType("text/html");
        response.getWriter().println("Service servlet responding to POST request");
    }

    public void log(String message) {
        System.out.println(this.getClass().getSimpleName() + ": " + message);
    }
    private static final long serialVersionUID = 1L;
}