package org.benchmark.parallel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequentialMST {

    public List<Edge> seqMst(Edge[] edges, int verticesCount) {
        Arrays.sort(edges);

        DisjointSet set = new DisjointSet(verticesCount);

        List<Edge> result = new ArrayList<>();
        for (Edge edge : edges) {
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

