package com.java.main.database;

import java.sql.*;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/LogAnalyzerDB";
    private static final String USER = "root";
    private static final String PASSWORD = "yaduvanshi";

    private static Connection connection;

    // Step 1: Connect to DB
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    // Step 2: Insert a log
    public static void insertLog(String level, String message, String timestamp) {
        String query = "INSERT INTO logs (log_timestamp, log_level, log_message) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, timestamp);  // set the extracted timestamp
            pstmt.setString(2, level);      // set the log level (INFO, ERROR, etc.)
            pstmt.setString(3, message);    // set the actual log message

            pstmt.executeUpdate();
            System.out.println("✅ Log inserted into database.");

        } catch (SQLException e) {
            System.out.println("❌ Error inserting log: " + e.getMessage());
        }
    }
}


