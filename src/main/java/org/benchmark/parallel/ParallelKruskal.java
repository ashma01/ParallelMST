package org.benchmark.parallel;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelKruskal {

    private static ForkJoinPool forkJoinPool = new ForkJoinPool();

    static class SortTask extends RecursiveTask<Edge[]> {
        private Edge[] edges;

        SortTask(Edge[] edges) {
            this.edges = edges;
        }

        @Override
        protected Edge[] compute() {
            if (edges.length <= 1) { // Threshold for parallel execution
                Arrays.sort(edges);
                return edges;
            } else {
                int mid = edges.length / 2;
                SortTask left = new SortTask(Arrays.copyOfRange(edges, 0, mid));
                SortTask right = new SortTask(Arrays.copyOfRange(edges, mid, edges.length));
                left.fork();
                Edge[] rightResult = right.compute();
                Edge[] leftResult = left.join();
                return merge(leftResult, rightResult);
            }
        }

        private Edge[] merge(Edge[] left, Edge[] right) {
            Edge[] result = new Edge[left.length + right.length];
            int i = 0, j = 0, k = 0;
            while (i < left.length && j < right.length) {
                if (left[i].compareTo(right[j]) <= 0) {
                    result[k++] = left[i++];
                } else {
                    result[k++] = right[j++];
                }
            }
            while (i < left.length) {
                result[k++] = left[i++];
            }
            while (j < right.length) {
                result[k++] = right[j++];
            }
            return result;
        }
    }

    public List<Edge> kruskalMST(Edge[] edges, int verticesCount) {
        DisjointSet set = new DisjointSet(verticesCount);

        Edge[] sortedEdges = forkJoinPool.invoke(new SortTask(edges));

        List<Edge> result = new ArrayList<>();
        for (Edge edge : sortedEdges) {
            int x = set.find(edge.src);
            int y = set.find(edge.dest);

            if (x != y) {
                result.add(edge);
                set.union(x, y);
            }
        }
        return result;
    }

    public int printMST( List<Edge> mst) {
        int totalWeight = 0;
        for (Edge edge : mst) {
            totalWeight += edge.weight;
        }
        return totalWeight;
    }
}
