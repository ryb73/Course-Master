package com.coursemaster.auth;

import javax.servlet.http.Cookie;

import com.coursemaster.auth.Session.Role;

/**
 * Basic authentication -- enter a username
 * and password combination, get a session
 *
 * This should not be used, except in the
 * case that no data is desired.
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
    public Cookie doLogin(String email, String password) {
        return generateSession(0, email, email, Role.STUDENT);
    }
}
