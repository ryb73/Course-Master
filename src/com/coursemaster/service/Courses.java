package com.coursemaster.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.coursemaster.database.DatabaseConnectionManager;

public class Courses extends AbstractService {

    @Override
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String query = String.format(QUERY, request.getParameter("userId"));
        JSONObject responseObject = DatabaseConnectionManager.executeQuery(query);
        try {
            if (responseObject != null) {
                    responseObject.put("status", "OK");
            }
            else {
                responseObject = new JSONObject("{status: 'Failure', count: 0, data: []}");
            }
        } catch (JSONException e) { }
        response.getWriter().write(responseObject.toString());
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private static final String QUERY =
        "select c.* from " +
        "enrollment  e " +
        "join course c on e.course = c.id " +
        "where e.user = %s;";
}
