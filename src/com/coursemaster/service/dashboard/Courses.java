package com.coursemaster.service.dashboard;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.coursemaster.auth.Session;
import com.coursemaster.database.DatabaseConnectionManager;
import com.coursemaster.service.AbstractService;

public class Courses extends AbstractService {

    @Override
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);

        String userId = String.valueOf(((Session) request.getAttribute("session")).getId());

        JSONObject responseObject = DatabaseConnectionManager.executeQuery(String.format(
                "select c.* from " +
                "enrollment  e " +
                "join course c on e.course = c.id " +
                "where e.user = %s;", userId));
        try {
            if (responseObject == null) {
                responseObject = new JSONObject("{status: 'Failure', count: 0, data: []}");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (JSONException e) { }
        response.getWriter().write(responseObject.toString());
    }
}
