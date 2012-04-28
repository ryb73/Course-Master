package com.coursemaster.service.file;

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

public class CreateDropboxes extends AbstractService {

    @Override
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = (Session)request.getAttribute("session");
        if(!session.getRole().equals(Role.PROFESSOR)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
    	try {
            JSONObject data = new JSONObject(request.getParameter("data"));
            String name = data.getString("name");
            String ext = data.getString("ext");
            String course = data.getString("course");
//            String start = data.getString("start");
//            String end = data.getString("end");

//            String userId = String.valueOf(((Session) request.getAttribute("session")).getId());

            DatabaseConnectionManager.executeUpdate(String.format(
               "insert into folder (name,course,ext,start,end,disp)" +
              " values ('%s', '%s', '%s', NOW(), NOW(), NOW());",
              name, course, ext));
 
//            System.out.println("INSERTED");
            
            response.setContentType("application/json");
            response.setContentLength(0);
        } catch (JSONException e) { e.printStackTrace(); }
    }

}
