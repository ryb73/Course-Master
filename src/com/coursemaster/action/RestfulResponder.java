package com.coursemaster.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RestfulResponder {
    public void create(HttpServletRequest request, HttpServletResponse response);
    public void read  (HttpServletRequest request, HttpServletResponse response);
    public void update(HttpServletRequest request, HttpServletResponse response);
    public void delete(HttpServletRequest request, HttpServletResponse response);
}
