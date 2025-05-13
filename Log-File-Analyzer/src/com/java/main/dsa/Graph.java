package com.java.main.dsa;

import java.util.*;

public class Graph {
    private Map<String, List<String>> adjList = new HashMap<>();

    public void addEdge(String from, String to) {
        adjList.putIfAbsent(from, new ArrayList<>());
        adjList.get(from).add(to);
    }

    public boolean hasCycle() {
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        for (String node : adjList.keySet()) {
            if (dfs(node, visited, recStack)) {
                return true;
            }
        }
        return false;
    }

    private boolean dfs(String node, Set<String> visited, Set<String> recStack) {
        if (recStack.contains(node)) return true;
        if (visited.contains(node)) return false;

        visited.add(node);
        recStack.add(node);

        for (String neighbor : adjList.getOrDefault(node, new ArrayList<>())) {
            if (dfs(neighbor, visited, recStack)) {
                return true;
            }
        }

        recStack.remove(node);
        return false;
    }
}