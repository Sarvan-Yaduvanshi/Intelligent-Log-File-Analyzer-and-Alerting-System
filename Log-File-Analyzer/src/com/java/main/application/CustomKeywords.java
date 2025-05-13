package com.java.main.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomKeywords {

    public static void logsForCustomKeywords(List<String> customKeywords, String fileName, boolean caseSensitive) throws IOException {
        Path logFilePath = Paths.get(fileName);

        // Read and process file lines
        List<String> fileLines = Files.lines(logFilePath)
                                      .map(line -> caseSensitive ? line : line.toUpperCase())
                                      .collect(Collectors.toList());

        // Process each keyword
        for (String keyword : customKeywords) {
            String searchKeyword = caseSensitive ? keyword : keyword.toUpperCase();
            printKeywordMessages(fileLines, searchKeyword, keyword);
        }
    }

    private static void printKeywordMessages(List<String> fileLines, String searchKeyword, String originalKeyword) {
        List<String> messages = fileLines.stream()
                                         .filter(line -> line.contains(searchKeyword))
                                         .collect(Collectors.toList());
        long count = messages.size();
        List<Integer> lineNumbers = IntStream.range(0, fileLines.size())
                                            .filter(i -> fileLines.get(i).contains(searchKeyword))
                                            .map(i -> i + 1)
                                            .boxed()
                                            .collect(Collectors.toList());

        System.out.println("---------------------- " + originalKeyword + " MESSAGES ----------------------");
        System.out.println("Total " + originalKeyword + " Messages: " + count + "\n");

        for (int i = 0; i < count; i++) {
            System.out.println("Message Number: " + (i + 1));
            System.out.println("Line Number: " + lineNumbers.get(i));
            System.out.println("Message Description: " + messages.get(i));
            System.out.println();
        }
    }

    public static void logsForCustomKeywordsCaseSensitive(List<String> customKeywords, String fileName) throws IOException {
        logsForCustomKeywords(customKeywords, fileName, true);
    }

    public static void logsForCustomKeywordsCaseInSensitive(List<String> customKeywords, String fileName) throws IOException {
        logsForCustomKeywords(customKeywords, fileName, false);
    }
}
