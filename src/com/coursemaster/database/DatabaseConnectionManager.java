package com.coursemaster.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.json.JSONObject;

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
            driver = connector;
            logger.fatal("Unrecognized database connector. Database connectivity cannot be ensured.");
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            logger.fatal("Unable to load database driver class: " + driver);
        }

        username = _username;
        password = _password;

        try {
            // Try to initialize a connection to verify settings are OK
            getConnection();
        }
        catch (SQLException e) {
            logger.fatal("Unable to create session with database:\n" + e.getMessage());
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

    /**
     * Executes an update operation
     * @param sqlUpdate The update SQL String
     * @return The number of rows affected
     */
    public static int executeUpdate(String sqlUpdate) {
        int res = 0;

        try {
            Connection conn = getConnection();
            Statement stmt  = conn.createStatement();
            res = stmt.executeUpdate(sqlUpdate);
        }
        catch (SQLException e) {
            logger.error("An exception occured while trying to execute an update:\n" + e.getMessage());
            res = -1;
        }

        return res;
    }

    /**
     * Executes an insert operation
     * @param sqlInsert The insert SQL String
     * @return The ID of the record, or -1 for failure
     */
    public static int executeInsert(String sqlInsert) {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute(sqlInsert);
        } catch (SQLException e) {
            logger.error("An exception was thrown while processing a query: " + e.getMessage());
            return -1;
        }

        // TODO Return newly create ID
        return 1;
    }

    /**
     * Executes a delete operation
     * @param sqlDelete The delete SQL String
     * @return Whether the operation succeeded
     */
    public static boolean executeDelete(String sqlDelete) {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute(sqlDelete);
        } catch (SQLException e) {
            logger.error("An exception was thrown while processing a query: " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Executes a query and returns the results
     * in JSON form,
     *   {
     *     status: 'OK|Failed',
     *     count: # Records in Result Set,
     *     data: [{(query objects)}]
     *   }
     * @param sqlQuery The query to execute
     * @return The result set
     */
    public static JSONObject executeQuery(String sqlQuery) {
        return executeQuery(sqlQuery, 0, Integer.MAX_VALUE);
    }

    /**
     * Executes a query and returns a limited set of results
     * in JSON form,
     *   {
     *     status: 'OK|Failed',
     *     count:# Records in Result Set,
     *     data: [{(query objects)}]
     *   }
     * @param sqlQuery The query to execute
     * @return The result set
     */
    public static JSONObject executeQuery(String sqlQuery, int start, int limit) {
        JSONObject rsp = new JSONObject();
        Vector<Map<String, Object>> data = new Vector<Map<String,Object>>();
        int count = 0;

        //Remove unnecessary recalculations
        limit = limit + start;

        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet res  = stmt.executeQuery(sqlQuery);

            ResultSetMetaData rsmd = res.getMetaData();
            int numColumns = rsmd.getColumnCount();

            // Move to starting row
            while (res.next()) {
                if (count >= start && count < limit) {
                    Map<String, Object> row = new HashMap<String, Object>();
                    for (int i = 1; i <= numColumns; i++) {
                        row.put(rsmd.getColumnName(i), res.getObject(i));
                    }
                    data.add(row);
                }
                ++count;
            }

            // Set the row count
            rsp.put("count", count);
            rsp.put("data", data);
            rsp.put("status", "OK");

            conn.close();
        }
        catch (Exception e) {
            logger.error("An exception was thrown while processing a query: " + e.getMessage());
            return null;
        }

        return rsp;
    }

    /**
     * Create necessary tables
     */
    public static void setupDatabase() {
        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            stmt.execute(USER_TABLE_SCRIPT);
            stmt.execute(COURSE_TABLE_SCRIPT);
            stmt.execute(ENROLLMENT_TABLE_SCRIPT);
            stmt.execute(EVENT_TABLE_SCRIPT);
            stmt.execute(FOLDER_TABLE_SCRIPT);
            stmt.execute(SUBMISSION_TABLE_SCRIPT);
            stmt.execute(DISCUSSION_BOARD_TABLE_SCRIPT);
            stmt.execute(DISCUSSION_POST_TABLE_SCRIPT);
            stmt.execute(DISCUSSION_TOPIC_TABLE_SCRIPT);
        }
        catch (SQLException e) {
           logger.fatal("An exception was thrown while creating the tables, causing the process to fail:\n" + e.getMessage());
           System.exit(0);
        }
    }

    private static Logger logger = Logger.getLogger(DatabaseConnectionManager.class);

    private static final String USER_TABLE_SCRIPT =
        "create table user(" +
        "id       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY," +
        "fullname VARCHAR(32)  NOT NULL," +
        "email    VARCHAR(32)  NOT NULL," +
        "password VARCHAR(256) NOT NULL," +
        "role     INT          NOT NULL," +
        "UNIQUE (email));";
    private static final String COURSE_TABLE_SCRIPT =
        "create table course(" +
        "id   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY," +
        "name VARCHAR(256) NOT NULL," +
        "prof INT          NOT NULL," +
        "dept VARCHAR(128) NOT NULL," +
        "num  INT          NOT NULL," +
        "sect INT          NOT NULL," +
        "cred INT          NOT NULL," +
        "sem  VARCHAR(16)  NOT NULL," +
        "FOREIGN KEY (prof) REFERENCES user(id));";
    private static final String ENROLLMENT_TABLE_SCRIPT =
        "create table enrollment(" +
        "user   INT NOT NULL," +
        "course INT NOT NULL," +
        "role   INT NOT NULL," +
        "FOREIGN KEY (user)   REFERENCES user(id)," +
        "FOREIGN KEY (course) REFERENCES course(id));";
    private static final String EVENT_TABLE_SCRIPT =
        "create table event(" +
        "id     INT          NOT NULL AUTO_INCREMENT PRIMARY KEY," +
        "name   VARCHAR(256) NOT NULL," +
        "descr  VARCHAR(512) NOT NULL," +
        "start  DATETIME     NOT NULL," +
        "end    DATETIME     NOT NULL," +
        "disp   DATETIME     NOT NULL," +
        "owner  INT          NOT NULL," +
        "course INT," +
        "type   INT," +
        "FOREIGN KEY (owner) REFERENCES user(id));";
    private static final String FOLDER_TABLE_SCRIPT =
        "create table folder(" +
        "id     INT          NOT NULL AUTO_INCREMENT PRIMARY KEY," +
        "name   VARCHAR(128) NOT NULL," +
        "course INT          NOT NULL," +
        "ext    VARCHAR(256)," +
        "start  DATETIME     NOT NULL," +
        "end    DATETIME     NOT NULL," +
        "disp   DATETIME     NOT NULL," +
        "FOREIGN KEY (course) REFERENCES course(id));";
    private static final String SUBMISSION_TABLE_SCRIPT =
        "create table submission(" +
        "id     INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
        "folder INT NOT NULL," +
        "path   INT NOT NULL," +
        "name   VARCHAR(128) NOT NULL," +
        "owner  INT NOT NULL," +
        "dte   DATETIME NOT NULL," +
        "FOREIGN KEY (folder) REFERENCES folder(id)," +
        "FOREIGN KEY (owner)  REFERENCES user(id));";
    private static final String DISCUSSION_BOARD_TABLE_SCRIPT =
        "create table discussion_board(" +
        "id     INT          NOT NULL AUTO_INCREMENT PRIMARY KEY," +
        "name   VARCHAR(256) NOT NULL," +
        "course INT          NOT NULL," +
        "status INT          NOT NULL," +
        "FOREIGN KEY (course) REFERENCES course(id));";
    private static final String DISCUSSION_TOPIC_TABLE_SCRIPT =
        "create table discussion_topic(" +
        "id     INT          NOT NULL AUTO_INCREMENT PRIMARY KEY," +
        "board  INT          NOT NULL," +
        "root   INT          NOT NULL," +
        "name   VARCHAR(256) NOT NULL," +
        "status INT          NOT NULL," +
        "FOREIGN KEY (board) REFERENCES discussion_board(id)," +
        "FOREIGN KEY (root)  REFERENCES discussion_post(id));";
    private static final String DISCUSSION_POST_TABLE_SCRIPT =
        "create table discussion_post(" +
        "id      INT           NOT NULL AUTO_INCREMENT PRIMARY KEY," +
        "owner   INT           NOT NULL," +
        "parent  INT           NOT NULL," +
        "dte     DATETIME      NOT NULL," +
        "content VARCHAR(1024) NOT NULL," +
        "FOREIGN KEY (owner)  REFERENCES user(id)," +
        "FOREIGN KEY (parent) REFERENCES discussion_post(id));";
}
