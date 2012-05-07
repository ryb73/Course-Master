package com.coursemaster.service.event;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.coursemaster.auth.Session;
import com.coursemaster.auth.Session.Role;
import com.coursemaster.database.DatabaseConnectionManager;
import com.coursemaster.service.AbstractService;

public class CreateEvent extends AbstractService {

    @Override
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Session session = (Session) request.getAttribute("session");
            JSONObject data = new JSONObject(request.getParameter("data"));
            String name = data.getString("name");
            String descr = data.getString("descr");
            String course = data.getString("course");
            String start = data.getString("start");
            String end = data.getString("end");
            String userId = String.valueOf(session.getId());
            String visible = (session.getRole().equals(Role.PROFESSOR)) ? '\'' + data.getString("visible") + '\'' : "NOW()";
            char type = (session.getRole().equals(Role.PROFESSOR)) ? '0' : '1';

            int res = DatabaseConnectionManager.executeInsert(String.format(
               "insert into event (name, descr, start, end, disp, owner, course, type)" +
              " values ('%s', '%s', '%s', '%s', %s, %s, %s, %c);",
            name, descr, start, end, visible, userId, course, type));

            response.getWriter().write("{ success : " + (res > 0) + " }");
        } catch (JSONException e) { }
    }

}
