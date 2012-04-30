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

/**
 * Base class for authentication, designed to support interfacing
 * with multiple database backends
 *
 * @author Graham
 */
public abstract class Authenticator {

    /**
     * Initializer for Authenticator. Manages an internal
     * instance of the authenticator a subclass.
     *
     * @param authType User to determine which subclass to create
     * @param _cookieName Cookie Name
     */
    public static void init(String authType, String _cookieName) {
        cookieName = _cookieName;
        if (authType == null || authType.equals("Mock")) {
            authenticator = new MockAuthenticator();
        }
        else if (authType.equals("Database")) {
            authenticator = new DatabaseAuthenticator();
        }
        else {
            throw new IllegalArgumentException("Invalid authentication type specified");
        }
    }

    /**
     * Base login method to be statically accessed
     * @param email The email
     * @param password The password
     * @return A cookie on valid credential entry, or null
     */
    public static Cookie login(String email, String password) {
        return authenticator.doLogin(email, password);
    }

    /**
     * Subclass dependent login method
     * @param email The email
     * @param password The password
     * @return A cookie on valid credential entry, or null
     */
    public abstract Cookie doLogin(String email, String password);

    /**
     * Kill a session
     * @param sessionKey The session to kill
     */
    public static void logout(String sessionKey) {
        sessions.remove(sessionKey);
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
        Cookie sessionCookie =  new Cookie(cookieName, sessionKey);
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
    public static boolean hasSession(String sessionKey) {
        return sessions.containsKey(sessionKey);
    }

    /**
     * Method to retrieve a specified session
     *
     * @param sessionKey The key of the desired session
     * @return The session for the sessionKey
     */
    public static Session getSession(String sessionKey) {
        return sessions.get(sessionKey);
    }

    /**
     * Method to take a password and hash it
     *
     * @param email The email, for salting
     * @param password The password
     * @return Encrypted password
     */
    public static String getHashedPassword(String email, String password) {
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
    public static Authenticator authenticator;
    public static String cookieName;
    private static final HashMap<String, Session> sessions = new HashMap<String, Session>();
    private static final SecureRandom randomGenerator = new SecureRandom();
}
