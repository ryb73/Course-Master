package com.coursemaster.auth;

import java.util.HashMap;

import javax.servlet.http.Cookie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coursemaster.database.DatabaseConnectionManager;

public class DatabaseAuthenticator extends Authenticator {

    @SuppressWarnings("unchecked")
    @Override
    public Cookie doLogin(String email, String password) {
        String encryptedPassword = getHashedPassword(email, password);

        try {
            JSONObject rsp = DatabaseConnectionManager.executeQuery("select * from user where email = '" + email + "'");
            HashMap<String, Object> data;

            // Response count if email wasn't located (user not found)
            if (rsp.getInt("count") > 0 && (data = ((HashMap<String, Object>) ((JSONArray) rsp.get("data")).get(0))) != null) {
                String storedPassword = (String) data.remove("password");

                // Check password matches
                if (encryptedPassword.equals(storedPassword)) {
                    return generateSession(((Integer) data.get("id")).longValue(), (String) data.get("fullname"), email, Session.roleForInt((Integer) data.get("role")));
                }
            }
        } catch (JSONException e) {
            logger.error("Unable to establish connection with database:\n" + e.getMessage());
        }

        return null;
    }
}
