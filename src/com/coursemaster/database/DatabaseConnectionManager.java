package com.coursemaster.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public abstract class DatabaseConnectionManager {
    private static String connectionString;
    private static String username;
    private static String password;

    /**
     * Method to initialize Manager
     * @param connector The connector type (Currently only MySQL is supported)
     * @param location Database location, including port (Ex: localhost:9000)
     * @param dbname The Database name (Ex: CourseMasterDB)
     * @param _username The database user
     * @param _password The user's password
     */
    public static void init(String connector, String location, String dbname, String _username, String _password) {
        String driver = null;
        if (connector == null || connector.equalsIgnoreCase("mysql")) {
            logger.trace("MySQL Database Configuration specified");

            driver = "com.mysql.jdbc.Driver";
            connectionString = String.format("jdbc:mysql://%s/%s", location, dbname);
        }
        else {
            logger.fatal("Unrecognized database connector");
            System.exit(0);
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            logger.fatal("Unable to load database driver class: " + driver);
            System.exit(0);
        }

        username = _username;
        password = _password;

        try {
            // Try to initialize a connection to verify settings are OK
            getConnection();
        }
        catch (SQLException e) {
            logger.fatal("Unable to create session with database: " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Gets a connection instance
     * @return A connection object
     * @throws SQLException If the connection cannot be created
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString, username, password);
    }

    private static Logger logger = Logger.getLogger(DatabaseConnectionManager.class);
}
