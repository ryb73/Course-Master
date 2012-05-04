package com.coursemaster.action;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coursemaster.auth.Authenticator;
import com.coursemaster.auth.Session;
import com.coursemaster.auth.Session.Role;
import com.coursemaster.database.DatabaseConnectionManager;
import com.coursemaster.servlet.util.EmailUtil;

public class User implements RestfulResponder {

    @Override
    /**
     * Method to create a new user
     * This method should only be called by users with the ADMIN Role
     *
     * @param request The HTTP Request
     * @param response The HTTP Response
     */
    public void create(HttpServletRequest request, HttpServletResponse response) {
        logger.trace("Attempting to create a new user");

        try {
            response.setStatus(HttpServletResponse.SC_OK);

            // Only admins can add new users
            if (!((Session)request.getAttribute("session")).getRole().equals(Role.ADMIN)) {
                response.getWriter().write(UNAUTHORIZED_RESPONSE);
            }
            else {
                // Pull off required parameters
                String fullname = request.getParameter("fullname");
                String email = request.getParameter("email");
                String role = request.getParameter("role");

                // Ensure all required parameters are present
                if (fullname == null || email == null || role == null) {
                    response.getWriter().write(PARAMS_MISSING_RESPONSE);
                }
                else {
                    String password = Authenticator.generatePassword(),
                    hashedPassword = Authenticator.getHashedPassword(email, password);

                    int res = DatabaseConnectionManager.executeInsert(String.format(
                            "insert into user (fullname, email, password, role)" +
                            " values ('%s', '%s', '%s', %s);", fullname, email, hashedPassword, role));
 
                    // Send the user an email notifying them of their account creation
                    EmailUtil.sendEmail(email, "Welcome to Course Master", String.format(
                        "Hi %s,\n\nA Course Master Account has been created for you. You may log in using this email " +
                        "and the password, %s, which was randomly generated for you. Reply to this message with any questions.\n\n" +
                        "Thanks,\nThe Course Master Management", fullname, password));

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
        logger.trace("Attempting to remove a user");

        try {
            response.setStatus(HttpServletResponse.SC_OK);
            Session session = (Session) request.getAttribute("session");

            // Only admins can remove users
            if (!session.getRole().equals(Role.ADMIN)) {
                response.getWriter().write(UNAUTHORIZED_RESPONSE);
            }
            else {
                String id = new JSONObject(request.getParameter("data")).getString("id");

                // Requires id, can't delete self
                if (id == null || String.valueOf(session.getId()).equals(id)) {
                    response.getWriter().write(PARAMS_MISSING_RESPONSE);
                }
                else {
                    boolean success = DatabaseConnectionManager.executeDelete("delete from user where id = " + id);

                    response.getWriter().write(success ? BASIC_SUCCESS_RESPONSE : DATABASE_FAILURE_RESPONSE);
                }
            }
        }
        catch (IOException ioe) { }
        catch (JSONException e) { }
    }

    @Override
    public void get(HttpServletRequest request, HttpServletResponse response) {
        logger.trace("Attempting to retrieve users");

        String idString = request.getParameter("id");
        Session session = (Session) request.getAttribute("session");

        try {
            response.setStatus(HttpServletResponse.SC_OK);

            // No ID indicates an ALL request
            if (idString != null) {
                // Otherwise, parse to integer
               int id = Integer.parseInt(idString);
                // If the current user isn't an administrator, but is requesting
                // another user's information, we mark that as unauthorized
                if (session.getId() != id && !session.getRole().equals(Role.ADMIN)) {
                    response.getWriter().write(UNAUTHORIZED_RESPONSE);
                }
                else {
                    JSONObject rspObj = DatabaseConnectionManager.executeQuery("select id, fullname, email, role from user where id = " + id);
                    response.getWriter().write(rspObj == null ? DATABASE_FAILURE_RESPONSE : rspObj.toString());
                }
            }

            // Non-admins can't view all
            else if(!session.getRole().equals(Role.ADMIN)) {
                response.getWriter().write(UNAUTHORIZED_RESPONSE);
            }
            else {
                JSONObject rspObj = DatabaseConnectionManager.executeQuery("select id, fullname, email, role from user;");
                response.getWriter().write(rspObj == null ? DATABASE_FAILURE_RESPONSE : rspObj.toString());
            }
        }
        catch (IOException ioe) {}
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(HttpServletRequest request, HttpServletResponse response) {
        logger.trace("Attempting to update a user");

        String idString = request.getParameter("id");

        try {
            // The user id must be specified
            if (idString == null) {
                response.getWriter().write(PARAMS_MISSING_RESPONSE);
            }
            else {
                int id = Integer.parseInt(idString);
                Session session = (Session) request.getAttribute("session");

                // If the user id isn't the current user, and the current
                // user isn't an administrator, the request is unauthorized
                if (session.getId() != id && !session.getRole().equals(Role.ADMIN)) {
                    response.getWriter().write(UNAUTHORIZED_RESPONSE);
                }
                else {
                    JSONObject userPoll = DatabaseConnectionManager.executeQuery("select * from user where id = " + id);

                    if (userPoll == null) {
                        response.getWriter().write(DATABASE_FAILURE_RESPONSE);
                    }
                    else {
                        HashMap<String,Object> userData = (HashMap<String,Object>)((JSONArray) userPoll.get("data")).get(0);
                        String fullname = request.getParameter("fullname");
                        String password = request.getParameter("password");
                        String role = request.getParameter("role");

                        // To update password, you must hash against email
                        if (password != null) {
                            userData.put("password",
                                    Authenticator.getHashedPassword((String) userData.get("email"), password));
                        }

                        if (role != null) { userData.put("role", role); }
                        if (fullname != null) { userData.put("fullname", fullname); }

                        int updated = DatabaseConnectionManager.executeUpdate(String.format(
                                "update user set password = '%s'," +
                                " fullname = '%s', role = %s where id = %s;",
                                (String) userData.get("password"),(String) userData.get("fullname"), 
                                 ((Integer) userData.get("role")).toString(), idString));
                        response.getWriter().write(updated == 1 ? BASIC_SUCCESS_RESPONSE : DATABASE_FAILURE_RESPONSE);
                    }
                }
            }
        }
        catch (IOException ioe) { }
        catch (JSONException e) { }
    }
}
