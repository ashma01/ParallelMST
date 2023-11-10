package com.parallel.algorithm.mst;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GraphFromFile {
    public SequentialPrimMST readSeqGraph(String filePath) {
        SequentialPrimMST mst = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the number of vertices from the first line
            int numVertices = Integer.parseInt(reader.readLine());
            mst = new SequentialPrimMST(numVertices);
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

    public ParallelPrims readParallel(String filePath) {
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
}

// Include the rest of the SequentialPrimMST class as previously defined.
