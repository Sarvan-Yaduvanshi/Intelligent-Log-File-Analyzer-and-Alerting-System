package com.java.main.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.java.main.database.DatabaseConnector;

public class Main {
    // ANSI color codes for terminal
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String BG_BLACK = "\u001B[40m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_PURPLE = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    public static final String BG_WHITE = "\u001B[47m";
    
    // Terminal width for beautiful UI
    private static final int TERMINAL_WIDTH = 90;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        clearScreen();
        printSplashScreen();
        
        try {
            System.out.print(BOLD + CYAN + "📂 Enter log file path: " + RESET);
            String fileName = sc.nextLine();
            Path logFilePath = Paths.get(fileName);

            // Validate file existence
            if (!Files.exists(logFilePath)) {
                System.out.println(RED + BOLD + "⚠️  Error: " + RESET + RED + "File not found at: " + fileName + RESET);
                System.out.println("\nExiting application...");
                TimeUnit.SECONDS.sleep(1);
                return;
            }

            // Show loading animation
            System.out.print(YELLOW + "🔍 Analyzing log file ");
            for (int i = 0; i < 5; i++) {
                System.out.print(".");
                TimeUnit.MILLISECONDS.sleep(200);
            }
            System.out.println(" Done!" + RESET);
            TimeUnit.MILLISECONDS.sleep(500);

            boolean isRunning = true;

            while (isRunning) {
                clearScreen();
                List<String> fileLines = Files.lines(logFilePath).map(String::toUpperCase).collect(Collectors.toList());
                printHeader("LOG ANALYZER PROFESSIONAL");
                printMenu();
                
                System.out.print(BOLD + CYAN + "❯ " + RESET);
                String choice = sc.nextLine();
                System.out.println();
                
                try {
                    switch (choice) {
                        case "1":
                            analyzeDefaultLogs(fileLines);
                            break;
                        case "2":
                            analyzeCustomLogs(sc, fileLines, fileName);
                            break;
                        case "3":
                            searchByKeyword(sc, fileLines);
                            break;
                        case "4":
                            filterByDateRange(sc, fileLines);
                            break;
                        case "5":
                            showErrorStatistics(fileLines);
                            break;
                        case "6":
                            simulateAlertSystem(fileLines);
                            break;
                        case "7":
                            saveReportToFile(sc, fileLines);
                            break;
                        case "8":
                            isRunning = confirmExit(sc);
                            break;
                        default:
                            System.out.println(RED + "⚠️  Invalid choice. Please try again." + RESET);
                    }
                    
                    if (isRunning && !choice.equals("8")) {
                        System.out.print(YELLOW + "\nPress Enter to continue..." + RESET);
                        sc.nextLine();
                    }
                } catch (Exception e) {
                    System.out.println(RED + "⚠️  An error occurred: " + e.getMessage() + RESET);
                    System.out.print(YELLOW + "\nPress Enter to continue..." + RESET);
                    sc.nextLine();
                }
            }
            
            exitApplication();
            
        } catch (Exception e) {
            System.out.println(RED + "⚠️  Failed to process file: " + e.getMessage() + RESET);
            System.out.println("\nExiting application...");
        }
    }

    private static void printSplashScreen() {
        System.out.println(BOLD + CYAN);
        System.out.println(centerText(" ╔═══════════════════════════════════════════════════════════════╗ "));
        System.out.println(centerText(" ║                                                               ║ "));
        System.out.println(centerText(" ║   " + BOLD + BLUE + "██╗      ██████╗  ██████╗      █████╗ ███╗   ██╗ █████╗ ██╗  ██╗   " + CYAN + "║ "));
        System.out.println(centerText(" ║   " + BOLD + BLUE + "██║     ██╔═══██╗██╔════╝     ██╔══██╗████╗  ██║██╔══██╗╚██╗██╔╝   " + CYAN + "║ "));
        System.out.println(centerText(" ║   " + BOLD + BLUE + "██║     ██║   ██║██║  ███╗    ███████║██╔██╗ ██║███████║ ╚███╔╝    " + CYAN + "║ "));
        System.out.println(centerText(" ║   " + BOLD + BLUE + "██║     ██║   ██║██║   ██║    ██╔══██║██║╚██╗██║██╔══██║ ██╔██╗    " + CYAN + "║ "));
        System.out.println(centerText(" ║   " + BOLD + BLUE + "███████╗╚██████╔╝╚██████╔╝    ██║  ██║██║ ╚████║██║  ██║██╔╝ ██╗   " + CYAN + "║ "));
        System.out.println(centerText(" ║   " + BOLD + BLUE + "╚══════╝ ╚═════╝  ╚═════╝     ╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝   " + CYAN + "║ "));
        System.out.println(centerText(" ║                                                               ║ "));
        System.out.println(centerText(" ║              " + GREEN + "Professional Log Analysis System" + CYAN + "                 ║ "));
        System.out.println(centerText(" ║                     " + YELLOW + "v2.0.0" + CYAN + "                               ║ "));
        System.out.println(centerText(" ║                                                               ║ "));
        System.out.println(centerText(" ╚═══════════════════════════════════════════════════════════════╝ "));
        System.out.println(RESET);
        
        // Display current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(PURPLE + centerText("Session started at: " + now.format(formatter)) + RESET);
        System.out.println();
    }

    private static void printHeader(String title) {
        System.out.println(BOLD + BG_BLUE + BLACK + centerText(" " + title + " ") + RESET);
        System.out.println(BLUE + "═".repeat(TERMINAL_WIDTH) + RESET);
    }

    private static void printMenu() {
        System.out.println(CYAN + BOLD + " MAIN MENU:" + RESET);
        System.out.println();
        System.out.println("  " + BOLD + GREEN + "[1]" + RESET + " " + YELLOW + "📊 Get Default Log Highlights" + RESET);
        System.out.println("  " + BOLD + GREEN + "[2]" + RESET + " " + YELLOW + "🔍 Get Customized Log Highlights" + RESET);
        System.out.println("  " + BOLD + GREEN + "[3]" + RESET + " " + YELLOW + "🔎 Search by Keyword / Level" + RESET);
        System.out.println("  " + BOLD + GREEN + "[4]" + RESET + " " + YELLOW + "📅 Filter by Date Range" + RESET);
        System.out.println("  " + BOLD + GREEN + "[5]" + RESET + " " + YELLOW + "❌ Show Error Statistics" + RESET);
        System.out.println("  " + BOLD + GREEN + "[6]" + RESET + " " + YELLOW + "🚨 Simulate Alert System" + RESET);
        System.out.println("  " + BOLD + GREEN + "[7]" + RESET + " " + YELLOW + "💾 Save Report to File" + RESET);
        System.out.println("  " + BOLD + GREEN + "[8]" + RESET + " " + YELLOW + "🚪 Exit Application" + RESET);
        System.out.println();
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static String centerText(String text) {
        int padding = (TERMINAL_WIDTH - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }

    private static boolean confirmExit(Scanner sc) {
        System.out.print(YELLOW + "Are you sure you want to exit? (y/n): " + RESET);
        String confirm = sc.nextLine().trim().toLowerCase();
        return !confirm.startsWith("y");
    }

    private static void exitApplication() {
        clearScreen();
        System.out.println(BLUE + "╔" + "═".repeat(TERMINAL_WIDTH - 2) + "╗" + RESET);
        System.out.println(BLUE + "║" + RESET + centerText("Thank you for using Log Analyzer Professional") + BLUE + "║" + RESET);
        System.out.println(BLUE + "╚" + "═".repeat(TERMINAL_WIDTH - 2) + "╝" + RESET);
        System.out.println();
        
        System.out.print(YELLOW + "Exiting application");
        try {
            for (int i = 0; i < 3; i++) {
                System.out.print(".");
                TimeUnit.MILLISECONDS.sleep(300);
            }
        } catch (InterruptedException e) {
            // Ignore
        }
        System.out.println(RESET);
    }

    private static void analyzeDefaultLogs(List<String> fileLines) {
        printHeader("DEFAULT LOG HIGHLIGHTS");
        System.out.println(YELLOW + "Analyzing your file..." + RESET);
        System.out.println();

        // Define a pattern to extract information from log lines
        // Format: YYYY-MM-DD HH:MM:SS [LEVEL] MESSAGE
        Pattern logPattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) \\[([A-Z]+)\\] (.+)$");

        // Processing ERROR logs
        printLogHighlights(fileLines, "ERROR", RED);
        for (String line : fileLines) {
            if (line.contains("ERROR")) {
                // Use the regex pattern to extract components
                Matcher matcher = logPattern.matcher(line);
                if (matcher.find()) {
                    String timestamp = matcher.group(1);  // Timestamp only
                    String level = matcher.group(2);      // Log level
                    String message = matcher.group(3);    // Log message
                    
                    // Insert the log into the database
                    DatabaseConnector.insertLog(level, message, timestamp);
                }
            }
        }

        // Processing WARNING logs
        printLogHighlights(fileLines, "WARNING", YELLOW);
        for (String line : fileLines) {
            if (line.contains("WARNING")) {
                // Use the regex pattern to extract components
                Matcher matcher = logPattern.matcher(line);
                if (matcher.find()) {
                    String timestamp = matcher.group(1);  // Timestamp only
                    String level = matcher.group(2);      // Log level
                    String message = matcher.group(3);    // Log message
                    
                    // Insert the log into the database
                    DatabaseConnector.insertLog(level, message, timestamp);
                }
            }
        }

        // Processing SUCCESS logs (looking for SUCCESS in the message part)
        printLogHighlights(fileLines, "SUCCESS", GREEN);
        for (String line : fileLines) {
            if (line.contains("SUCCESS")) {
                // Use the regex pattern to extract components
                Matcher matcher = logPattern.matcher(line);
                if (matcher.find()) {
                    String timestamp = matcher.group(1);  // Timestamp only
                    String level = matcher.group(2);      // Log level
                    String message = matcher.group(3);    // Log message
                    
                    // Insert the log into the database with appropriate level
                    DatabaseConnector.insertLog(level, message, timestamp);
                }
            }
        }

        // Processing INFO logs
        printLogHighlights(fileLines, "INFO", CYAN);
        for (String line : fileLines) {
            if (line.contains("INFO")) {
                // Use the regex pattern to extract components
                Matcher matcher = logPattern.matcher(line);
                if (matcher.find()) {
                    String timestamp = matcher.group(1);  // Timestamp only
                    String level = matcher.group(2);      // Log level
                    String message = matcher.group(3);    // Log message
                    
                    // Insert the log into the database
                    DatabaseConnector.insertLog(level, message, timestamp);
                }
            }
        }
    }

    private static void printLogHighlights(List<String> fileLines, String keyword, String color) {
        List<String> messages = fileLines.stream()
                .filter(line -> line.contains(keyword))
                .collect(Collectors.toList());
        
        long count = messages.size();
        List<Integer> lineNumbers = IntStream.range(0, fileLines.size())
                .filter(i -> fileLines.get(i).contains(keyword))
                .map(i -> i + 1)
                .boxed()
                .collect(Collectors.toList());

        System.out.println(color + BOLD + "▶ " + keyword + " MESSAGES (" + count + ")" + RESET);
        System.out.println(color + "─".repeat(TERMINAL_WIDTH) + RESET);

        if (count == 0) {
            System.out.println(YELLOW + "  No " + keyword + " messages found." + RESET);
        } else {
            for (int i = 0; i < count; i++) {
                System.out.println(WHITE + BOLD + "  #" + (i + 1) + RESET + " | Line " + 
                                 BOLD + lineNumbers.get(i) + RESET);
                System.out.println("  " + color + messages.get(i) + RESET);
                System.out.println();
            }
        }
        System.out.println();
    }

    private static void analyzeCustomLogs(Scanner sc, List<String> fileLines, String fileName) throws IOException {
        printHeader("CUSTOMIZED LOG HIGHLIGHTS");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of keywords to search: ");
        int numKeywords = 0;

        try {
             numKeywords = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number. Please enter a numeric value.");
            return; // stop this method here
        }

List<String> keywords = new ArrayList<>();
for (int i = 1; i <= numKeywords; i++) {
    System.out.print("Enter keyword #" + i + ": ");
    String keyword = scanner.nextLine();
    keywords.add(keyword.toLowerCase());
}

        
        System.out.println();
        System.out.println(YELLOW + "■ " + RESET + "Press " + GREEN + "1" + RESET + " for case-sensitive search");
        System.out.println(YELLOW + "■ " + RESET + "Press " + GREEN + "2" + RESET + " for case-insensitive search");
        System.out.print(BOLD + CYAN + "❯ " + RESET);
        String caseChoice = sc.nextLine().trim();
        
        List<String> customKeywords = new ArrayList<>();
        System.out.println();
        System.out.println(CYAN + "Enter your keywords:" + RESET);
        
        for (int i = 0; i < numKeywords; i++) { 
            System.out.print(YELLOW + "  Keyword #" + (i + 1) + ": " + RESET);
            customKeywords.add(sc.nextLine());
        }

        System.out.println();
        System.out.println(GREEN + "Searching for keywords..." + RESET);
        System.out.println();

        if ("1".equals(caseChoice)) {
            CustomKeywords.logsForCustomKeywordsCaseSensitive(customKeywords, fileName);
        } else if ("2".equals(caseChoice)) {
            CustomKeywords.logsForCustomKeywordsCaseInSensitive(customKeywords, fileName);
        } else {
            System.out.println(RED + "⚠️  Invalid choice" + RESET);
        }
    }

    private static void searchByKeyword(Scanner sc, List<String> fileLines) {
        printHeader("SEARCH BY KEYWORD / LEVEL");
        
        System.out.print(CYAN + "Enter keyword or log level: " + RESET);
        String keyword = sc.nextLine().toUpperCase();

        List<String> matchingLines = fileLines.stream()
                .filter(line -> line.contains(keyword))
                .collect(Collectors.toList());

        System.out.println();
        System.out.println(BOLD + "🔍 Search Results for '" + keyword + "': " + 
                         matchingLines.size() + " matches" + RESET);
        System.out.println(YELLOW + "─".repeat(TERMINAL_WIDTH) + RESET);

        if (matchingLines.isEmpty()) {
            System.out.println(RED + "No matching logs found." + RESET);
        } else {
            for (int i = 0; i < matchingLines.size(); i++) {
                String line = matchingLines.get(i);
                // Highlight the keyword
                String highlightedLine = line.replace(keyword, RED + BOLD + keyword + RESET + YELLOW);
                System.out.println(CYAN + "[" + (i + 1) + "] " + YELLOW + highlightedLine + RESET);
            }
        }
    }

    private static void filterByDateRange(Scanner sc, List<String> fileLines) {
        printHeader("FILTER BY DATE RANGE");
        
        System.out.print(CYAN + "Enter start date (YYYY-MM-DD): " + RESET);
        String startDate = sc.nextLine();
        
        System.out.print(CYAN + "Enter end date (YYYY-MM-DD): " + RESET);
        String endDate = sc.nextLine();

        try {
            List<String> filteredLogs = fileLines.stream()
                    .filter(line -> {
                        try {
                            String date = line.substring(0, 10); // Assuming date is the first 10 characters
                            return date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0;
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

            System.out.println();
            System.out.println(BOLD + "📅 Logs between " + startDate + " and " + endDate + ": " + 
                             filteredLogs.size() + " entries" + RESET);
            System.out.println(YELLOW + "─".repeat(TERMINAL_WIDTH) + RESET);

            if (filteredLogs.isEmpty()) {
                System.out.println(RED + "No logs found in the specified date range." + RESET);
            } else {
                for (int i = 0; i < filteredLogs.size(); i++) {
                    String line = filteredLogs.get(i);
                    // Highlight the date
                    String highlightedLine = CYAN + line.substring(0, 10) + RESET + WHITE + line.substring(10) + RESET;
                    System.out.println(YELLOW + "[" + (i + 1) + "] " + highlightedLine);
                }
            }
        } catch (Exception e) {
            System.out.println(RED + "⚠️  Error processing date range: " + e.getMessage() + RESET);
        }
    }

    private static void showErrorStatistics(List<String> fileLines) {
        printHeader("ERROR STATISTICS");
        
        List<String> errorLogs = fileLines.stream()
                .filter(line -> line.contains("ERROR"))
                .collect(Collectors.toList());

        System.out.println(BOLD + "❌ Total ERROR Messages: " + RED + errorLogs.size() + RESET);
        System.out.println(YELLOW + "─".repeat(TERMINAL_WIDTH) + RESET);
        System.out.println();

        if (errorLogs.isEmpty()) {
            System.out.println(GREEN + "No error messages found in the log file." + RESET);
        } else {
            Map<String, Long> errorFrequency = errorLogs.stream()
                    .collect(Collectors.groupingBy(line -> line, Collectors.counting()));

            int counter = 1;
            for (Map.Entry<String, Long> entry : errorFrequency.entrySet()) {
                System.out.println(BOLD + WHITE + "Error #" + counter + RESET);
                System.out.println(RED + "  " + entry.getKey() + RESET);
                System.out.println(YELLOW + "  Occurrences: " + BOLD + entry.getValue() + RESET);
                System.out.println();
                counter++;
            }
        }
    }

    private static void simulateAlertSystem(List<String> fileLines) {
        printHeader("ALERT SYSTEM SIMULATION");
        
        System.out.println(YELLOW + "🔄 Simulating Alert System..." + RESET);
        System.out.println();

        // Look for critical errors
        List<String> criticalErrors = fileLines.stream()
                .filter(line -> line.contains("CRITICAL"))
                .collect(Collectors.toList());
        
        if (criticalErrors.isEmpty()) {
            System.out.println(GREEN + "✓ No critical errors found." + RESET);
            System.out.println(GREEN + "✓ System status: Normal" + RESET);
        } else {
            System.out.println(BG_RED + BLACK + BOLD + " ⚠️  CRITICAL ERRORS DETECTED ⚠️  " + RESET);
            System.out.println();
            
            for (int i = 0; i < criticalErrors.size(); i++) {
                System.out.println(RED + BOLD + "[CRITICAL #" + (i + 1) + "]" + RESET);
                System.out.println(RED + criticalErrors.get(i) + RESET);
                System.out.println();
            }
            
            System.out.println(BG_RED + BLACK + BOLD + " ALERT: Immediate action required! " + RESET);
        }
        
        // Look for errors
        List<String> errors = fileLines.stream()
                .filter(line -> line.contains("ERROR") && !line.contains("CRITICAL"))
                .collect(Collectors.toList());
        
        if (!errors.isEmpty()) {
            System.out.println();
            System.out.println(YELLOW + BOLD + "⚠️  " + errors.size() + " standard errors found." + RESET);
        }
    }

    private static void saveReportToFile(Scanner sc, List<String> fileLines) throws IOException {
        printHeader("SAVE REPORT TO FILE");
        
        System.out.print(CYAN + "Enter the file name for the report: " + RESET);
        String reportFileName = sc.nextLine();

        if (!reportFileName.contains(".")) {
            reportFileName += ".txt";
        }

        Path reportPath = Paths.get(reportFileName);
        
        System.out.println();
        System.out.print(YELLOW + "Saving report ");
        
        for (int i = 0; i < 5; i++) {
            System.out.print(".");
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        
        try {
            // Create a formatted report with timestamp
            List<String> reportLines = new ArrayList<>();
            reportLines.add("===== LOG ANALYZER PROFESSIONAL REPORT =====");
            reportLines.add("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            reportLines.add("===========================================");
            reportLines.add("");
            reportLines.add("LOG CONTENTS:");
            reportLines.add("-------------");
            reportLines.addAll(fileLines);
            reportLines.add("");
            reportLines.add("LOG SUMMARY:");
            reportLines.add("-----------");
            reportLines.add("Total Lines: " + fileLines.size());
            reportLines.add("Error Messages: " + fileLines.stream().filter(line -> line.contains("ERROR")).count());
            reportLines.add("Warning Messages: " + fileLines.stream().filter(line -> line.contains("WARNING")).count());
            reportLines.add("Info Messages: " + fileLines.stream().filter(line -> line.contains("INFO")).count());
            
            Files.write(reportPath, reportLines);
            System.out.println(" " + GREEN + "Success!" + RESET);
            System.out.println();
            System.out.println(GREEN + "✓ Report saved successfully to '" + reportFileName + "'" + RESET);
        } catch (IOException e) {
            System.out.println(" " + RED + "Failed!" + RESET);
            System.out.println();
            System.out.println(RED + "⚠️  Error saving report: " + e.getMessage() + RESET);
            throw e;
        }
    }
}