package com.coursemaster.service.file;

import java.io.BufferedInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coursemaster.database.DatabaseConnectionManager;
import com.coursemaster.server.Settings;
import com.coursemaster.service.AbstractService;

public class File extends AbstractService {
    @Override
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);

        byte[] partHolder;
        InputStream partReader;

        // Read parameters
        String fileName = readPart((InputStream) request.getPart("name").getInputStream());
        String courseId = readPart((InputStream) request.getPart("course").getInputStream());
        String dropboxId = "1";//= request.getParameter("dropbox");
        String fileData = readPart((InputStream) request.getPart("file1").getInputStream());

        FileWriter writer = new FileWriter(new java.io.File(
            Settings.courseMasterDirectory + "uploads/" +
            courseId + Settings.FILESEPARATOR + dropboxId + Settings.FILESEPARATOR + fileName));
        writer.write(fileData);
        writer.close();

        response.getWriter().write("{ success: true }");

//              String name = data.getString("name");
//              String ext = data.getString("ext");
//              String course = data.getString("course");
//        String start = data.getString("start");
//        String end = data.getString("end");
//        String userId = String.valueOf(((Session) request.getAttribute("session")).getId());

          //name, file1, info

          String name = request.getParameter("name");
          String ext = request.getParameter("ext");
          String course = request.getParameter("course");

          //TODO save file to disk
          //TODO insert into DB
//          DatabaseConnectionManager.executeInsert(String.format(
//             "insert into folder (name,course,ext,start,end,disp)" 
//             " values ('%s', '%s', '%s', NOW(), NOW(), NOW());",
//             name, course, ext));
//          response.setContentType("application/json");\
//          response.setContentLength(0);
    }

    private String readPart(InputStream part) throws IOException {
        byte []partHolder = new byte[part.available()];
        part.read(partHolder);
        return new String(partHolder);
    }
}

