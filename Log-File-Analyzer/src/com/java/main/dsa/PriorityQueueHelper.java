package com.java.main.dsa;

import java.util.PriorityQueue;

public class PriorityQueueHelper {
    public static PriorityQueue<String> getTopNLogs(PriorityQueue<String> pq, int N) {
        PriorityQueue<String> result = new PriorityQueue<>((a, b) -> b.compareTo(a));
        while (!pq.isEmpty() && result.size() < N) {
            result.offer(pq.poll());
        }
        return result;
    }
}
