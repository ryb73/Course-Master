package com.coursemanager.auth;

import javax.servlet.http.Cookie;

/**
 * Basic authentication -- enter a username
 * and password combination, get a session
 *
 * @author Graham
 */
public class MockAuthenticator extends Authenticator {

    @Override
    /**
     * Overrides base login functionality.
     * Simply creates a session with the username specified
     *
     * @param username The username to assign to the session
     * @param password Unused
     * @return A session cookie
     */
    public Cookie login(String username, String password) {
        return generateSession(username);
    }
}
