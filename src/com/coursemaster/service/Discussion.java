package com.coursemaster.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.coursemaster.auth.Session;
import com.coursemaster.auth.Session.Role;
import com.coursemaster.database.DatabaseConnectionManager;
import com.coursemaster.servlet.util.FileUtil;

public class Discussion extends AbstractService {
    @Override
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getMethod().equals("GET")) {
            doGet(request, response);
        } else if(request.getMethod().equals("POST")) {
            doPost(request, response);
        }
    }

    private void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getRequestURI().substring(20); // remove "/service/discussion/" -- yeah, I know
        if(command.equals("create-board")) {
            createBoard(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void createBoard(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = (Session)request.getAttribute("session");
        if(!session.getRole().equals(Role.PROFESSOR)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String query = String.format("insert into discussion_board (course, name, status) values (%s, '%s', %s)",
                request.getParameter("course"), request.getParameter("boardName"), request.getParameter("status"));
        DatabaseConnectionManager.executeInsert(query);
        
        response.setContentType("application/json");
        response.setContentLength(0);
    }

    private void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String command = request.getRequestURI().substring(9);
        if(command.equals("discussion")) {
            // Simple get
            getMain(request, response);
        } else {
            command = command.substring(11);
            if(command.equals("get-boards")) {
                getBoards(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    private void getBoards(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject rsp = DatabaseConnectionManager.executeQuery(String.format(
              "select board.id, board.name, count(topic.id) topicCount " +
              "from discussion_board board " +
              "left join discussion_topic topic on topic.board = board.id " +
              "where board.course = %s " +
              "group by board.id, board.name;",
              request.getParameter("courseId")));

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(rsp.toString());
    }

    private void getMain(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Open the template file
        String discussionAsString = FileUtil.loadTemplateFile("discussion.tpl");
   
        Session session = (Session) request.getAttribute("session");
   
        // Replace discussion board content with specified user's content
        discussionAsString = discussionAsString.replace("##USERNAME##", session.getName());
   
        response.getWriter().write(discussionAsString);
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    private static Logger logger = Logger.getLogger(Discussion.class);
}
