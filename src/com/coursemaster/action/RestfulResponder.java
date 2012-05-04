package com.coursemaster.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public interface RestfulResponder {

    /**
     * Method to Create a new instance of the class
     * @param request The HttpRequest object
     * @param response The HttpResponse object
     */
    public void create(HttpServletRequest request, HttpServletResponse response);

    /**
     * Method to Get an instance, or all of the object of this class
     * @param request The HttpRequest object
     * @param response The HttpResponse object
     */
    public void get(HttpServletRequest request, HttpServletResponse response);

    /**
     * Method to update an instance of this class
     * @param request The HttpRequest object
     * @param response The HttpResponse object
     */
    public void update(HttpServletRequest request, HttpServletResponse response);

    /**
     * Method to delete an instance of this class
     * @param request The HttpRequest object
     * @param response The HttpResponse object
     */
    public void delete(HttpServletRequest request, HttpServletResponse response);

    /** Default Success Response */
    static final String BASIC_SUCCESS_RESPONSE    = "{ success: true }";

    /** Default Unauthorized Request Response */
    static final String UNAUTHORIZED_RESPONSE     = "{ success: false, errors: { message: 'You are not authorized to perform this operation' }}";

    /** Default Missing Parameters for Request Response */
    static final String PARAMS_MISSING_RESPONSE   = "{ success: false, errors: { message: 'Missing parameters for one or more required fields' }}";

    /** Default Database Interaction Failure Response */
    static final String DATABASE_FAILURE_RESPONSE = "{ success: false, errors: { message: 'An error occured while interacting with the database. See logs for details.' }}";

    /** Default Logger for subclasses */
    static Logger logger = Logger.getLogger(RestfulResponder.class);
}
