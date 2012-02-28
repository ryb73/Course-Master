package com.coursemanager.auth;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.servlet.http.Cookie;

import com.coursemanager.server.Settings;

/**
 * Base class for authentication, designed to support interfacing
 * with multiple database backends
 *
 * @author Graham
 */
public abstract class Authenticator {
    private static final HashMap<String, Session> sessions = new HashMap<String, Session>();
    private static final SecureRandom randomGenerator = new SecureRandom();

    /**
     * Base login method, to be overridden
     * @param username The username
     * @param password The passowrd
     * @return A cookie on valid credentials, or null
     */
    public abstract Cookie login(String username, String password);

    /**
     * Internal method to construct a random key for the cookie
     * @param user The user to assign the key to
     * @return A cookie with a random value
     */
    protected static Cookie generateSession(String user) {
        // Generate a session key
        String sessionKey = new BigInteger(130, randomGenerator).toString(32);

        // Add the session to the list
        sessions.put(sessionKey, new Session(user));

        // Return the session
        return new Cookie(Settings.cookieName, sessionKey);
    }

    /**
     * Method to check if a specified
     * session exists in the system
     *
     * @param sessionKey The key to look for
     * @return Whether the key exists in the session list
     */
    public boolean hasSession(String sessionKey) {
        return sessions.containsKey(sessionKey);
    }

    /**
     * Method to retrieve a specified session
     *
     * @param sessionKey The key of the desired session
     * @return The session for the sessionKey
     */
    public Session getSession(String sessionKey) {
        return sessions.get(sessionKey);
    }
}
