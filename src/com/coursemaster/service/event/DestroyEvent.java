package com.coursemaster.service.event;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.coursemaster.database.DatabaseConnectionManager;
import com.coursemaster.service.AbstractService;

public class DestroyEvent extends AbstractService {

    @Override
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JSONObject data = new JSONObject(request.getParameter("data"));

            DatabaseConnectionManager.executeDelete(String.format("delete from event where id = %s;", data.getString("id")));
        } catch (JSONException e) { }
    }
}
