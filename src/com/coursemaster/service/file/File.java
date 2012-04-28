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
import com.coursemaster.servlet.util.FileUtil;

public class File extends AbstractService {

	 @Override
	    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        response.setStatus(HttpServletResponse.SC_OK);

	        String courseId = request.getParameter("courseId");

	        //TODO get file
	        JSONObject responseObject = DatabaseConnectionManager.executeQuery(String.format(
	                "select * from event " +
	                "where course = %s " +
	                "and owner = (select prof from course where id = %s);",
	                courseId, courseId));

	        try {
	            if (responseObject == null) {
	                responseObject = new JSONObject("{status: 'Failure', count: 0, data: []}");
	                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            }
	        } catch (JSONException e) { }

	        response.getWriter().write(responseObject.toString());
	    }

}

