package com.coursemaster.auth;

public class AuthenticationManager {

    public static void init(String authType) {
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

    public static Authenticator authenticator;
}
