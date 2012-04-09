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

    private void createBoard(HttpServletRequest request, HttpServletResponse response) {
        Session session = (Session)request.getAttribute("session");
        if(!session.getRole().equals(Role.PROFESSOR)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Connection dbConnection = DatabaseConnectionManager.getConnection();

            // TODO: validate input. and maybe not hard code things
            PreparedStatement statement = dbConnection.prepareStatement("insert into discussion_board (course, name, status) values (?, ?, ?)");
            statement.setInt(1, Integer.parseInt(request.getParameter("course")));
            statement.setString(2, request.getParameter("boardName"));
            statement.setInt(3, Integer.parseInt(request.getParameter("status")));

            statement.execute();

            response.setStatus(HttpServletResponse.SC_OK); // OK
            response.setContentType("application/json");
            response.setContentLength(0);
        } catch(SQLException e) {
            logger.error("An error occurred while creating board: " + e.getMessage());

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
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
        try {
            Connection connection = DatabaseConnectionManager.getConnection();

            PreparedStatement statement = connection.prepareStatement(
                    "select board.id, board.name, count(topic.id) topics " +
                    "from discussion_board board " +
                    "left join discussion_topic topic on topic.board = board.id " +
                    "where board.course = ? " +
                    "group by board.id, board.name");
            statement.setInt(1, Integer.parseInt(request.getParameter("courseId")));
            statement.execute();

            StringBuilder jsonResult = new StringBuilder("{ status: 'OK', boards:[");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()) {
                jsonResult.append("{id:").append(resultSet.getInt("id"))
                    .append(",name:'").append(resultSet.getString("name"))
                    .append("',topicCount:").append(resultSet.getInt("topics")).append("}");

                if(!resultSet.isLast()) {
                    jsonResult.append(",");
                }
            }

            jsonResult.append("] }");

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write(jsonResult.toString());
        } catch(SQLException e) {
            logger.error("An error occurred while getting list of boards: " + e.getMessage());

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
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
