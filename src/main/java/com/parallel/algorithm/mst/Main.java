package com.parallel.algorithm.mst;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        GraphFromFile graph = new GraphFromFile();
        SequentialPrimMST mst = graph.readSeqGraph("/Users/ashmaparveen/Desktop/DesktopData/StudyMaterial/Fall23/ParallelAlgorithm/mstAlgorithm/src/main/resources/input/somuchdata.txt");

        long startTime = System.currentTimeMillis();
        mst.primMST();
        mst.printMST();
        long endTime = System.currentTimeMillis();


        ParallelPrims mstAlgorithm = graph.readParallel("/Users/ashmaparveen/Desktop/DesktopData/StudyMaterial/Fall23/ParallelAlgorithm/mstAlgorithm/src/main/resources/input/somuchdata.txt");
        long startTimeP = System.currentTimeMillis();
        try {
            mstAlgorithm.getMST(); // Construct the MST
            mstAlgorithm.printMST();     // Print the MST
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTimeP = System.currentTimeMillis();

        System.out.println("Seq Prim's MST Runtime: " + (endTime - startTime) + " ms");
        System.out.println("Parallel Prim's MST Runtime: " + (endTimeP - startTimeP) + " ms");
    }


}
