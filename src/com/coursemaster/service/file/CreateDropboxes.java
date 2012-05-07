package com.coursemaster.service.file;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.util.log.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.coursemaster.auth.Session;
import com.coursemaster.auth.Session.Role;
import com.coursemaster.database.DatabaseConnectionManager;
import com.coursemaster.server.Settings;
import com.coursemaster.service.AbstractService;

public class CreateDropboxes extends AbstractService {

    @Override
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = (Session)request.getAttribute("session");
        if(!session.getRole().equals(Role.PROFESSOR)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
 
//            String name = data.getString("name");
//            String ext = data.getString("ext");
//            String course = data.getString("course");
//      String start = data.getString("start");
//      String end = data.getString("end");
//      String userId = String.valueOf(((Session) request.getAttribute("session")).getId());
        
        
    
        String name = request.getParameter("name");
        String ext = request.getParameter("ext");
        String course = request.getParameter("course");
     
        int id = DatabaseConnectionManager.executeInsert(String.format(
           "insert into folder (name,course,ext,start,end,disp)" +
           " values ('%s', '%s', '%s', NOW(), NOW(), NOW());",
           name, course, ext));
        
        
        //Create if not exists folders
        String pathname = Settings.courseMasterDirectory + "uploads" + Settings.courseMasterDirectory +
                course + Settings.FILESEPARATOR + new Integer(id).toString();
        if(!new File(pathname).mkdirs()){
        	//ERROR
        }
        System.out.println("PATH=" + pathname);
        
        
        response.setContentType("application/json");
        response.setContentLength(0);
    }

}
