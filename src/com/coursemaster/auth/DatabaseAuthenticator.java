package com.coursemaster.auth;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.Cookie;

import com.coursemaster.database.DatabaseConnectionManager;

public class DatabaseAuthenticator extends Authenticator {

    @Override
    public Cookie login(String email, String password) {
        String encryptedPassword = getHashedPassword(email, password);
        ResultSet res;

        try {
            Connection conn = DatabaseConnectionManager.getConnection();
            Statement stmt = conn.createStatement();
            res = stmt.executeQuery("select * from user where email = '" + email + "';");

            // Email not found
            if (res == null || !res.first()) { return null; }

            String storedPassword = res.getString("password");

            if (encryptedPassword.equals(storedPassword)) {
                return generateSession(res.getLong("id"), res.getString("fullname"), email, Session.roleForInt(res.getInt("role")));
            }
        } catch (SQLException e) {
            logger.error("Unable to establish connection with database:\n" + e.getMessage());
        }

        return null;
    }
}
