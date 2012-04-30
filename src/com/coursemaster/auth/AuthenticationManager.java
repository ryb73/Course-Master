package com.coursemaster.auth;

public class AuthenticationManager {

    public static void init(String authType, String cookieName) {
        if (authType == null || authType.equals("Mock")) {
            authenticator = new MockAuthenticator(cookieName);
        }
        else if (authType.equals("Database")) {
            authenticator = new DatabaseAuthenticator(cookieName);
        }
        else {
            throw new IllegalArgumentException("Invalid authentication type specified");
        }
    }

    public static Authenticator authenticator;
}
