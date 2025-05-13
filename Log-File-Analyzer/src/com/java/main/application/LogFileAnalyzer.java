package com.java.main.application;

import com.java.main.database.DatabaseConnector;
import java.io.*;
import java.util.regex.*;

public class LogFileAnalyzer {
    public static void main(String[] args) {
        // Path to your log file
        String logFilePath = "/Users/sarvanyaduvanshi/Downloads/Java-Log-File-Analyzer/Log-File-Analyzer/src/com/java/main/file/logs.txt";

        // Read the log file
        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Parse log level, timestamp, and message
                String[] logParts = parseLogLine(line);

                if (logParts != null) {
                    String timestamp = logParts[0];  // Extracted timestamp (2024-08-05 08:15:42)
                    String level = logParts[1];      // Extracted log level (INFO, ERROR, etc.)
                    String message = logParts[2];    // The actual log message

                    // Insert the log into the database
                    DatabaseConnector.insertLog(level, message, timestamp);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading the log file: " + e.getMessage());
        }
    }

    // Method to parse a log line and extract timestamp, log level, and message
    private static String[] parseLogLine(String line) {
        // Regex pattern to match log entry format: [timestamp] [log level] [message]
        Pattern pattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) \\[([A-Z]+)\\] (.+)$");
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
            // Return an array with timestamp, log level, and message
            String timestamp = matcher.group(1);
            String level = matcher.group(2);
            String message = matcher.group(3);

            return new String[] { timestamp, level, message };
        }

        // Return null if the line doesn't match the expected format
        return null;
    }
}

