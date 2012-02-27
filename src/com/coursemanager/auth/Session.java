package com.coursemanager.auth;

/**
 * A class representing a user session
 *
 * @author Graham
 */
public class Session {

    /**
     * Primary constructor
     * @param _username The username
     */
    public Session(String _username) {
        username = _username;
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
     * Get username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    private String username;
    private long lastActive;
}
