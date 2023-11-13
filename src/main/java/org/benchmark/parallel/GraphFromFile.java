package org.benchmark.parallel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphFromFile {
//    public SequentialPrimMST readSeqPrimGraph(String filePath) {
//        SequentialPrimMST mst = null;
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            // Read the number of vertices from the first line
//            int numVertices = Integer.parseInt(reader.readLine());
//            mst = new SequentialPrimMST(numVertices);
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split("\\s+"); // Split line by whitespace
//                int src = Integer.parseInt(parts[0]);
//                int dest = Integer.parseInt(parts[1]);
//                int weight = Integer.parseInt(parts[2]);
//
//                mst.addEdge(src, dest, weight);
//            }
//
//        } catch (IOException e) {
//            System.out.println("An error occurred while reading the file: " + filePath);
//            e.printStackTrace();
//        }
//        return mst;
//    }

    public ParallelPrims readParallelPrimGraph(String filePath) {
        ParallelPrims mst = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the number of vertices from the first line
            int numVertices = Integer.parseInt(reader.readLine());
            mst = new ParallelPrims(numVertices);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+"); // Split line by whitespace
                int src = Integer.parseInt(parts[0]);
                int dest = Integer.parseInt(parts[1]);
                int weight = Integer.parseInt(parts[2]);

                mst.addEdge(src, dest, weight);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + filePath);
            e.printStackTrace();
        }
        return mst;
    }


    public List<Edge> readParallelKruskalGraph(String filename) throws IOException {
        ParallelKruskal parallelKruskal = new ParallelKruskal();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        int vertices = Integer.parseInt(reader.readLine()); // First line contains the number of vertices

        List<Edge> edges = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            int src = Integer.parseInt(parts[0]);
            int dest = Integer.parseInt(parts[1]);
            int weight = Integer.parseInt(parts[2]);
            edges.add(new Edge(src, dest, weight));
        }
        reader.close();

        return parallelKruskal.kruskalMST(edges.toArray(new Edge[0]), vertices);
    }

    public List<Edge> readSeqGraph(String filename) throws IOException {
        SequentialMST sequentialMST = new SequentialMST();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        int vertices = Integer.parseInt(reader.readLine());

        List<Edge> edges = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            int src = Integer.parseInt(parts[0]);
            int dest = Integer.parseInt(parts[1]);
            int weight = Integer.parseInt(parts[2]);
            edges.add(new Edge(src, dest, weight));
        }
        reader.close();

        return sequentialMST.seqMst(edges.toArray(new Edge[0]), vertices);
    }
}

