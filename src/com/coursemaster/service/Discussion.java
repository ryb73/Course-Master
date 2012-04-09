package com.coursemaster.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.coursemaster.auth.Session;
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
            response.setStatus(404);
        }
    }

    private void createBoard(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection dbConnection = DatabaseConnectionManager.getConnection();
            Statement statement = dbConnection.createStatement();

            // TODO: validate input. and maybe not hard code things
            String sqlStatement = String.format("insert into discussion_board (course, name, status) values (%d, '%s', %d)",
                    Integer.parseInt(request.getParameter("course")), request.getParameter("boardName"),
                    Integer.parseInt(request.getParameter("status")));
            logger.debug(sqlStatement);

            statement.execute(sqlStatement);

            response.setStatus(200); // OK
            response.setContentType("application/json");
            response.setContentLength(0);
        } catch(Exception e) {
            logger.error("An error occurred while creating board: " + e.getMessage());

            response.setStatus(500);
        }
    }

    private void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Open the template file
        String discussionAsString = FileUtil.loadTemplateFile("discussion.tpl");

        Session session = (Session) request.getAttribute("session");

        // Replace discussion board content with specified user's content
        discussionAsString = discussionAsString.replace("##USERNAME##", session.getUsername());

        response.getWriter().write(discussionAsString);
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    private static Logger logger = Logger.getLogger(Discussion.class);
}
