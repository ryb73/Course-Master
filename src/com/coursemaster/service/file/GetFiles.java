package com.coursemaster.service.file;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.coursemaster.auth.Session;
import com.coursemaster.database.DatabaseConnectionManager;
import com.coursemaster.service.AbstractService;

public class GetFiles extends AbstractService {
    @Override
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = String.valueOf(((Session) request.getAttribute("session")).getId());
        String courseId = request.getParameter("courseId");
        String fId = request.getParameter("fId");

        JSONObject rsp = DatabaseConnectionManager.executeQuery("select id, folder, path, name, owner, dte from submission ");

        try {
            if (rsp == null) {
                rsp = new JSONObject("{status: 'Failure', count: 0, data: []}");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (JSONException e) { }

        response.getWriter().write(rsp.toString());
    }
}
