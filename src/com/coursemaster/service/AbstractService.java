package com.coursemaster.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractService {
    /**
     * Respond to service request
     * @param request The HTTP Request
     * @param response The HTTP Response
     */
    public abstract void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
