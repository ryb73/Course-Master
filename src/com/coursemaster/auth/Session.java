package com.coursemaster.auth;

/**
 * A class representing a user session
 *
 * @author Graham
 */
public class Session {
    public enum Role {
        ADMIN(3), PROFESSOR(2), STUDENT(1);
    
        private int value;
        private Role(int v) {
            value = v;
        }

        public int getValue() {
            return value;
        }
    };

    /**
     * Primary constructor
     * @param _id The numeric id
     * @param _name The name
     * @param _email The email account
     * @param _role The role
     */
    public Session(long _id, String _name, String _email, Role _role) {
        id    = _id;
        name  = _name;
        email = _email;
        role  = _role;
        updateLastActive();
    }

    /**
     * Update last active time to now
     */
    public void updateLastActive() {
        lastActive = System.currentTimeMillis();
    }

    /**
     * Get last active time
     * @return Last active time in milliseconds
     */
    public long getLastActive() {
        return lastActive;
    }
    
    /**
     * Get user name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get user email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get user id
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Get role
     * @return role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Get role enum for int
     * @param role Int role
     * @return Role enum
     */
    protected static Role roleForInt(int role) {
        switch (role) {
            case 1: return Role.STUDENT;
            case 2: return Role.PROFESSOR;
            case 3: return Role.ADMIN;
            default: return null;
        }
    }

    private String name;
    private String email;
    private long id;
    private long lastActive;
    private Role role;
}
