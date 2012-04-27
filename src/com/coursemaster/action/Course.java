package com.coursemaster.action;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coursemaster.auth.Session;
import com.coursemaster.auth.Session.Role;
import com.coursemaster.database.DatabaseConnectionManager;

public class Course implements RestfulResponder {

    @Override
    /**
     * Method to create a new course
     * This method should only be called by users with the ADMIN Role
     *
     * @param request The HTTP Request
     * @param response The HTTP Response
     */
    public void create(HttpServletRequest request, HttpServletResponse response) {
        logger.trace("Attempting to create a new course");

        try {
            response.setStatus(HttpServletResponse.SC_OK);

            // Only admins can add new courses
            if (!((Session)request.getAttribute("session")).getRole().equals(Role.ADMIN)) {
                response.getWriter().write(UNAUTHORIZED_RESPONSE);
            }
            else {
                // Pull off required parameters
                String name = request.getParameter("name");
                String prof = request.getParameter("prof");
                String dept = request.getParameter("dept");
                String num  = request.getParameter("num" );
                String sect = request.getParameter("sect");
                String cred = request.getParameter("cred");
                String sem  = request.getParameter("sem" );

                // Ensure all required parameters are present
                if (name == null || prof == null || num == null || dept == null ||
                    sect == null || cred == null || sem == null) {
                    response.getWriter().write(PARAMS_MISSING_RESPONSE);
                }
                else {
                    // Then execute the actual insertion
                    int res = DatabaseConnectionManager.executeInsert(String.format(
                            "insert into course (name, prof, dept, num, sect, cred, sem)" +
                            " values ('%s', %s, '%s', %s, %s, %s, '%s');",
                               name, prof, dept, num, sect, cred, sem));

                    // Check result value from database execution.
                    // Greater than 0 indicates success, less indicates failure.
                    response.getWriter().write(res > 0 ? BASIC_SUCCESS_RESPONSE : DATABASE_FAILURE_RESPONSE);
                }
            }
        }
        catch (IOException ioe) { }
    }

    @Override
    public void delete(HttpServletRequest request, HttpServletResponse response) {
        logger.trace("Attempting to remove a course");

        try {
            response.setStatus(HttpServletResponse.SC_OK);
            Session session = (Session) request.getAttribute("session");

            // Only admins can remove users
            if (!session.getRole().equals(Role.ADMIN)) {
                response.getWriter().write(UNAUTHORIZED_RESPONSE);
            }
            else {
                String id = request.getParameter("id");

                // Requires id
                if (id == null) {
                    response.getWriter().write(PARAMS_MISSING_RESPONSE);
                }
                else {
                    boolean success = DatabaseConnectionManager.executeDelete("delete from course where id = " + id);

                    response.getWriter().write(success ? BASIC_SUCCESS_RESPONSE : DATABASE_FAILURE_RESPONSE);
                }
            }
        }
        catch (IOException ioe) { }
    }

    @Override
    public void read(HttpServletRequest request, HttpServletResponse response) {
        logger.trace("Attempting to retrieve courses");

        boolean allMode = request.getRequestURI().endsWith("/all");
        Session session = (Session) request.getAttribute("session");

        try {
            response.setStatus(HttpServletResponse.SC_OK);

            if (!allMode) {
                // If all mode isn't specified, id is expected
                String idString = request.getParameter("id");

                // Report failures if missing
                if (idString == null) {
                    response.getWriter().write(PARAMS_MISSING_RESPONSE);
                }
                // Otherwise, parse to integer
                else {
                    int id = Integer.parseInt(idString);
                    // If the current user isn't an administrator
                    // TODO or the user is not enrolled in the course
                    if (!session.getRole().equals(Role.ADMIN)) {
                        response.getWriter().write(UNAUTHORIZED_RESPONSE);
                    }
                    else {
                        JSONObject rspObj = DatabaseConnectionManager.executeQuery(
                            "select id, name, prof, dept, num, sect, cred, sem from course where id = " + id);
                        response.getWriter().write(rspObj == null ? DATABASE_FAILURE_RESPONSE : rspObj.toString());
                    }
                }
            }
            // Non-admins can't view all
            else if(!session.getRole().equals(Role.ADMIN)) {
                response.getWriter().write(UNAUTHORIZED_RESPONSE);
            }
            else {
                JSONObject rspObj = DatabaseConnectionManager.executeQuery("select * from course;");
                response.getWriter().write(rspObj == null ? DATABASE_FAILURE_RESPONSE : rspObj.toString());
            }
        }
        catch (IOException ioe) {}
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(HttpServletRequest request, HttpServletResponse response) {
        logger.trace("Attempting to update a course");

        String idString = request.getParameter("id");

        try {
            // The user id must be specified
            if (idString == null) {
                response.getWriter().write(PARAMS_MISSING_RESPONSE);
            }
            else {
                int id = Integer.parseInt(idString);
                Session session = (Session) request.getAttribute("session");

                // If the current user isn't an administrator, the request is unauthorized
                if (!session.getRole().equals(Role.ADMIN)) {
                    response.getWriter().write(UNAUTHORIZED_RESPONSE);
                }
                else {
                    JSONObject coursePoll = DatabaseConnectionManager.executeQuery("select * from course where id = " + id);

                    if (coursePoll == null) {
                        response.getWriter().write(DATABASE_FAILURE_RESPONSE);
                    }
                    else {
                        HashMap<String,Object> courseData = (HashMap<String,Object>)((JSONArray) coursePoll.get("data")).get(0);

                        String name = request.getParameter("name");
                        String prof = request.getParameter("prof");
                        String dept = request.getParameter("dept");
                        String num  = request.getParameter("num" );
                        String sect = request.getParameter("sect");
                        String cred = request.getParameter("cred");
                        String sem  = request.getParameter("sem" );

                        if (name != null) { courseData.put("name", name); }
                        if (prof != null) { courseData.put("prof", Integer.parseInt(prof)); }
                        if (dept != null) { courseData.put("dept", dept); }
                        if (num  != null) { courseData.put("num", Integer.parseInt(num)); }
                        if (sect != null) { courseData.put("sect", Integer.parseInt(sect)); }
                        if (cred != null) { courseData.put("cred", Integer.parseInt(cred)); }
                        if (sem  != null) { courseData.put("sem", sem); }

                        int updated = DatabaseConnectionManager.executeUpdate(String.format(
                                "update course set" +
                                " name = '%s', prof = %s, dept = '%s', num = %s," +
                                " sect = %s, cred = %s, sem  = '%s' where id = %s;",
                                 (String)  courseData.get("name"), ((Integer) courseData.get("prof")).toString(),
                                 (String)  courseData.get("dept"), ((Integer) courseData.get("num" )).toString(),
                                ((Integer) courseData.get("sect")).toString(), ((Integer) courseData.get("cred")).toString(),
                                 (String)  courseData.get("sem"), idString));
                        response.getWriter().write(updated == 1 ? BASIC_SUCCESS_RESPONSE : DATABASE_FAILURE_RESPONSE);
                    }
                }
            }
        }
        catch (IOException ioe) { }
        catch (JSONException e) { }
    }

    private static final String BASIC_SUCCESS_RESPONSE    = "{ success: true }";
    private static final String UNAUTHORIZED_RESPONSE     = "{ success: false, errors: { message: 'You are not authorized to perform this operation' }}";
    private static final String PARAMS_MISSING_RESPONSE   = "{ success: false, errors: { message: 'Missing one or more required fields' }}";
    private static final String DATABASE_FAILURE_RESPONSE = "{ success: false, errors: { message: 'An error occured while interacting with the database. See logs for details.' }}";
    private static Logger logger = Logger.getLogger(User.class);
}
