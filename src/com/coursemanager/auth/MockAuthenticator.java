package com.coursemanager.auth;

import javax.servlet.http.Cookie;

public class MockAuthenticator extends Authenticator {

    @Override
    public Cookie login(String username, String password) {
        return generateSession(username);
    }

}
