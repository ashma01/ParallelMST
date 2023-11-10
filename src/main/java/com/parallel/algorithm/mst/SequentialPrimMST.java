package com.parallel.algorithm.mst;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;


import java.util.*;

public class SequentialPrimMST {
    private final List<List<Edge>> graph;
    private final int numVertices;
    private final int[] parent;
    private final int[] key;
    private final boolean[] mstSet;

    public SequentialPrimMST(int numVertices) {
        this.numVertices = numVertices;
        this.graph = new ArrayList<>(numVertices);
        this.parent = new int[numVertices];
        this.key = new int[numVertices];
        this.mstSet = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++) {
            graph.add(new ArrayList<>());
            key[i] = Integer.MAX_VALUE;
            mstSet[i] = false;
        }
    }

    public void addEdge(int src, int dest, int weight) {
        graph.get(src).add(new Edge(dest, weight));
        graph.get(dest).add(new Edge(src, weight));
    }

    public void primMST() {
        key[0] = 0;
        parent[0] = -1;

        for (int i = 0; i < numVertices - 1; i++) {
            int u = minKey();

            // If no minimum key is found, break out of the loop as the remaining vertices are not connected
            if (u == -1) {
                break;
            }

            mstSet[u] = true;

            for (Edge edge : graph.get(u)) {
                if (!mstSet[edge.destination] && edge.weight < key[edge.destination]) {
                    parent[edge.destination] = u;
                    key[edge.destination] = edge.weight;
                }
            }
        }
    }


    private int minKey() {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int v = 0; v < numVertices; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    public void printMST() {
        int totalWeight = 0;
        System.out.println("Edge \tWeight");
        for (int i = 1; i < numVertices; i++) {
            if (parent[i] != -1) { // Only include vertices that are reached
                System.out.println(parent[i] + " - " + i + "\t" + key[i]);
                totalWeight += key[i];
            }
        }
        System.out.println("Total weight of MST: " + totalWeight);
    }

    static class Edge {
        int destination;
        int weight;

        Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }


}
