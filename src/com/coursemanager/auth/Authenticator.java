package com.coursemanager.auth;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.servlet.http.Cookie;

/**
 * Base class for authentication, designed to support interfacing
 * with multiple database backends
 *
 * @author Graham
 */
public abstract class Authenticator {
    private static final HashMap<String, Session> sessions = new HashMap<String, Session>();
    private static final SecureRandom randomGenerator = new SecureRandom();

    public abstract Cookie login(String username, String password);

    protected static Cookie generateSession(String user) {
        // Generate a session key
        String sessionKey = new BigInteger(130, randomGenerator).toString(32);

        // Add the session to the list
        sessions.put(sessionKey, new Session(user));

        // Return the session
        return new Cookie("CourseMasterCookie", sessionKey);
    }
}
