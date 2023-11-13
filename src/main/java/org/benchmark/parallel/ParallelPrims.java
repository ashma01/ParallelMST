package org.benchmark.parallel;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.ReentrantLock;

class ParallelPrims {

    private final List<List<Edge>> adjList;
    private final AtomicIntegerArray minEdgeWeight;
    private final int[] parent;
    private final PriorityBlockingQueue<Vertex> minHeap;
    private final int numVertices;
    private final ForkJoinPool stealer;

    private final Set<Integer> inMST = ConcurrentHashMap.newKeySet();



    public ParallelPrims(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = Collections.synchronizedList(new ArrayList<>(numVertices));
        for (int i = 0; i < numVertices; i++) {
            adjList.add(new ArrayList<>());
        }
        this.minEdgeWeight = new AtomicIntegerArray(numVertices);
        this.parent = new int[numVertices];
        Arrays.fill(parent, -1);
        this.minHeap = new PriorityBlockingQueue<>(numVertices, Comparator.comparingInt(Vertex::getWeight));
        this.stealer = (ForkJoinPool)Executors.newWorkStealingPool(10);
    }

    public void addEdge(int src, int dest, int weight) {
        adjList.get(src).add(new Edge(src, dest, weight));
        adjList.get(dest).add(new Edge(dest, src, weight)); // For undirected graph
    }


    public List<Edge> getMST() {
        List<Edge> mst = Collections.synchronizedList(new ArrayList<>());

        // Initialize keys and add the first vertex to the heap
        for (int i = 0; i < numVertices; i++) {
            minEdgeWeight.set(i, Integer.MAX_VALUE);
        }
        minEdgeWeight.set(0, 0);
        minHeap.add(new Vertex(0, 0));

        // Main loop of the Prim's algorithm
        while (mst.size() < numVertices - 1 && !minHeap.isEmpty()) {
            Vertex current = minHeap.poll();
            if (current == null) {
                break;
            }
            int u = current.id;
            if (!inMST.add(u)) { // Add returns false if the element was already in the set
                continue; // Skip if the vertex is already in MST
            }

            // Add edge to MST (if not the first vertex)
            if (parent[u] != -1) {
                mst.add(new Edge(parent[u], u, minEdgeWeight.get(u)));
            }

            // Process adjacent vertices using concurrent tasks
            List<Future<?>> futures = new ArrayList<>();
            for (Edge edge : adjList.get(u)) {
                futures.add(stealer.submit(() -> {
                    int v = edge.to;
                    int weight = edge.weight;

                    while (!inMST.contains(v) && minEdgeWeight.get(v) > weight) {
                        if (minEdgeWeight.compareAndSet(v, minEdgeWeight.get(v), weight)) {
                            parent[v] = u;
                            minHeap.add(new Vertex(v, weight));
                            break;
                        }
                    }
                }));
            }

            // Wait for all tasks to complete before proceeding to the next vertex
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        stealer.shutdown();
        return mst;
    }


    static class Edge {
        int from;
        int to;
        int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }


    static class Vertex {
        int id;
        int weight;

        Vertex(int id, int weight) {
            this.id = id;
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }
    }

    public int printMST() {
        int totalWeight = 0;
        for (int i = 1; i < numVertices; i++) {
            totalWeight += minEdgeWeight.get(i);
        }
        return totalWeight;
    }


}


