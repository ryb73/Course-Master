package com.coursemaster.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Courses extends AbstractService {

    @Override
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write(
            "{ status: 'OK', " +
              "data:[{ id: 1, name: 'Data Structures', prof: 5, dept: 'CS', num: 351, sec: 1, cred: 3, sem: 'Spring 2012'}," +
                    "{ id: 3, name: 'Intro to Databases', prof: 7, dept: 'CS', num: 557, sec: 1, cred: 3, sem: 'Spring 2012'}, " +
                    "{ id: 5, name: 'Intro to Software Engineering', prof: 6, dept: 'CS', num: 361, sec: 1, cred: 3, sem: 'Spring 2012'},"+
                    "{ id: 7, name: 'Intro to Operating Systems', prof: 8, dept: 'CS', num: 537, sec: 1, cred: 3, sem: 'Spring 2012'},"+
                    "{ id: 9, name: 'Computer Networks', prof: 9, dept: 'CS', num: 520, sec: 2, cred: 3, sem: 'Spring 2012'}]}");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
