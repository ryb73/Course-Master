package com.coursemaster.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coursemaster.auth.Session;
import com.coursemaster.servlet.util.FileUtil;

public class Discussion extends AbstractService {
    @Override
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Open the template file
        String discussionAsString = FileUtil.loadTemplateFile("discussion.tpl");

        Session session = (Session) request.getAttribute("session");

        // Replace discussion board content with specified user's content
        discussionAsString = discussionAsString.replace("##USERNAME##", session.getUsername());

        response.getWriter().write(discussionAsString);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
