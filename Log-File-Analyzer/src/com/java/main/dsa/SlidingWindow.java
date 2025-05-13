package com.java.main.dsa;

import java.util.ArrayList;
import java.util.List;

public class SlidingWindow {
    public static List<String> getLogsInTimeWindow(List<String> logs, String startTime, String endTime) {
        List<String> result = new ArrayList<>();
        for (String log : logs) {
            String timestamp = log.substring(0, 19); // Assuming timestamp is the first 19 characters
            if (timestamp.compareTo(startTime) >= 0 && timestamp.compareTo(endTime) <= 0) {
                result.add(log);
            }
        }
        return result;
    }
}