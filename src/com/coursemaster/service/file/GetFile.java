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
import com.coursemaster.server.Settings;
import com.coursemaster.service.AbstractService;
import com.coursemaster.servlet.util.FileUtil;

public class GetFile extends AbstractService {

   
    @Override
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = String.valueOf(((Session) request.getAttribute("session")).getId());
        String courseId = request.getParameter("courseId");
        
        
        String subId = request.getParameter("");  //TODO get from front end
        JSONObject data = DatabaseConnectionManager.executeQuery(String.format(
              "select id, folder, path, name, owner, dte" +
              "from submission " + 
              "where id = %s", subId));
        try {
			String path = data.getString("path");
		} catch (JSONException e) {
			e.printStackTrace();
		} 

        //TODO Send file back
        
      
        

//        try {
//            if (rsp == null) {
//                rsp = new JSONObject("{status: 'Failure', count: 0, data: []}");
//                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            }
//        } catch (JSONException e) { }
//        response.getWriter().write(rsp.toString());
        
    }

}
