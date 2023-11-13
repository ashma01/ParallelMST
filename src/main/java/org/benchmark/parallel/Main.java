package org.benchmark.parallel;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        GraphFromFile graph = new GraphFromFile();
        int totalWeightParallelPrim = 0;
        int totalWeightParallelKruskal = 0;
        int totalWeightSeq = 0;

        if (args.length < 1) {
            System.out.println("Please provide the file path as a command-line argument.");
            return;
        }

        String filePath = args[0];


        ParallelPrims mstAlgorithm = graph.readParallelPrimGraph(filePath);
        long startTimeP = System.currentTimeMillis();
        try {
            mstAlgorithm.getMST();
            totalWeightParallelPrim = mstAlgorithm.printMST();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTimeP = System.currentTimeMillis();

        long startTimeKruskal = System.currentTimeMillis();
        try {
            ParallelKruskal parallelKruskal = new ParallelKruskal();
            List<Edge> kruskal = graph.readParallelKruskalGraph(filePath);
            totalWeightParallelKruskal = parallelKruskal.printMST(kruskal);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTimeKruskal = System.currentTimeMillis();


        long startTimeSeq = System.currentTimeMillis();
        try {
            SequentialMST sequentialMST = new SequentialMST();
            List<Edge> edges = graph.readSeqGraph(filePath);
            totalWeightSeq = sequentialMST.printMST(edges);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTimeSeq = System.currentTimeMillis();

        System.out.println("\n******** Sequential Algorithm Results ********");
        System.out.println("Total weight of Sequential MST: " + totalWeightSeq);
        System.out.println("Sequential MST Runtime: " + (endTimeSeq - startTimeSeq) + " ms");


        System.out.println("\n******** KRUSKAL and PRIM'S Parallel Algorithm Results ********");

        System.out.println("Total weight of Parallel Prims MST: " + totalWeightParallelPrim);
        System.out.println("Parallel Prim's MST Runtime: " + (endTimeP - startTimeP) + " ms");


        System.out.println("Total weight of Parallel Kruskal MST: " + totalWeightParallelKruskal);
        System.out.println("Kruskal's Parallel MST Runtime: " + (endTimeKruskal - startTimeKruskal) + " ms");

    }


}
