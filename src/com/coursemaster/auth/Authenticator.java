package com.coursemaster.auth;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.coursemaster.auth.Session.Role;
import com.coursemaster.server.Settings;

/**
 * Base class for authentication, designed to support interfacing
 * with multiple database backends
 *
 * @author Graham
 */
public abstract class Authenticator {
    /**
     * Base login method, to be overridden
     * @param email The email
     * @param password The password
     * @return A cookie on valid credentials, or null
     */
    public abstract Cookie login(String email, String password);

    public void logout(Session session) {
        sessions.remove(session);
    }
    /**
     * Internal method to construct a random key for the cookie
     * @param user The user to assign the key to
     * @return A cookie with a random value
     */
    protected static Cookie generateSession(long id, String user, String email, Role role) {
        // Generate a session key
        String sessionKey = new BigInteger(130, randomGenerator).toString(32);

        // Add the session to the list
        sessions.put(sessionKey, new Session(id, user, email, role));

        // Generate and return the session
        Cookie sessionCookie =  new Cookie(Settings.cookieName, sessionKey);
        sessionCookie.setPath("/");

        return sessionCookie;
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

    /**
     * Method to take a password and hash it
     *
     * @param email The email, for salting
     * @param password The password
     * @return Encrypted password
     */
    public String getHashedPassword(String email, String password) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Unable to find encryption algorithm");
            return null;
        }

        digest.reset();

        // Encrypt the password
        String encryptedPassword;
        try {
            digest.update(email.getBytes("UTF-8"));
            encryptedPassword = new String(digest.digest(password.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            logger.error("Couldn't find UTF-8 encoding");
            return null;
        }

        return encryptedPassword;
    }

    protected static Logger logger = Logger.getLogger(Authenticator.class);
    private static final HashMap<String, Session> sessions = new HashMap<String, Session>();
    private static final SecureRandom randomGenerator = new SecureRandom();
}
