package com.parallel.algorithm.mst;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class SequentialPrimMSTBenchmark {

    private SequentialPrimMST sequentialPrimMST;

    @Setup
    public void setup() {
        // Initialize your graph with vertices and edges here
        int numVertices = 1000; // example number of vertices
        sequentialPrimMST = new SequentialPrimMST(numVertices);

        // Add your edges here
         for (int i = 0; i < numVertices - 1; i++) {
             sequentialPrimMST.addEdge(i, i + 1, (int) (Math.random() * 100));
         }
    }

    @Benchmark
    @Fork(1)
    @Warmup(iterations = 5)
    @Measurement(iterations = 10)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkPrimMST() {
        // This method benchmarks the primMST method of the SequentialPrimMST class
        sequentialPrimMST.primMST();
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(SequentialPrimMSTBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
