package com.coursemaster.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.coursemaster.auth.Session;
import com.coursemaster.auth.Session.Role;
import com.coursemaster.database.DatabaseConnectionManager;
import com.coursemaster.servlet.util.FileUtil;

public class Discussion extends AbstractService {
    private static final int TOPIC_STATUS_OPEN = 1;

    private static final int BOARD_STATUS_OPEN = 1;
    private static final int BOARD_STATUS_RESPONSE_ONLY = 2;

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
        } else if(command.equals("post-topic")) {
            postTopic(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void postTopic(HttpServletRequest request, HttpServletResponse response) {
        Session session = (Session)request.getAttribute("session");

        String postQuery = String.format("insert into discussion_post (dte, owner, content) values (now(), %d, '%s')",
                session.getId(), request.getParameter("message"));
        int postId = DatabaseConnectionManager.executeInsert(postQuery);
        if(postId == -1) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        String topicQuery = String.format("insert into discussion_topic (board, root, name, status) values (%s, %d, '%s', %d)",
                request.getParameter("board"), postId, request.getParameter("topic-name"), TOPIC_STATUS_OPEN);
        int topicId = DatabaseConnectionManager.executeInsert(topicQuery);
        if(topicId == -1) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        String updatePostParentQuery = String.format("update discussion_post set parent=%d where id=%d",
                topicId, postId);
        DatabaseConnectionManager.executeUpdate(updatePostParentQuery);

        response.setContentType("application/json");
        response.setContentLength(0);
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
            } else if(command.equals("get-topics")) {
                getTopics(request, response);
            } else if(command.equals("get-topic")) {
                getTopic(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    private void getTopic(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = (Session)request.getAttribute("session");

        String query = String.format(
                "select post.id, post.owner, owner.fullname ownerName, post.dte, post.content " +
        		"from discussion_post post " +
        		"join user owner on owner.id = post.owner " +
        		"where post.parent = %s",
        		request.getParameter("topicId"));
        JSONObject rsp = DatabaseConnectionManager.executeQuery(query);

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(rsp.toString());
    }

    private void getTopics(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject rsp = DatabaseConnectionManager.executeQuery(String.format(
                "select topic.id, topic.name, user.fullname postedBy, root.dte postedOn, " +
                "(select count(*) from discussion_post post where post.parent = topic.id) replies " +
                "from discussion_topic topic " +
                "join discussion_post root on root.id = topic.root " +
                "join user on user.id = root.owner " +
                "where topic.board = %s",
                request.getParameter("boardId")));

          response.setStatus(HttpServletResponse.SC_OK);
          response.getWriter().write(rsp.toString());
    }

    private void getBoards(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = (Session)request.getAttribute("session");

        String query = String.format(
                "select board.id, board.name, count(topic.id) topicCount, board.status " +
                "from discussion_board board " +
                "left join discussion_topic topic on topic.board = board.id " +
                "where board.course = %s ",
                request.getParameter("courseId"));

        if(session.getRole() == Role.STUDENT) {
            query += String.format("and board.status in (%d, %d) ",
                    BOARD_STATUS_OPEN, BOARD_STATUS_RESPONSE_ONLY);
        }

        query += "group by board.id, board.name;";
        JSONObject rsp = DatabaseConnectionManager.executeQuery(query);

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
}
