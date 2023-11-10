package com.parallel.algorithm.mst;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class ParallelPrimsBenchmark {

    private ParallelPrims parallelPrims;

    @Setup
    public void setup() {
        // Initialize your graph with vertices and edges here
        int numVertices = 1000; // example number of vertices
        parallelPrims = new ParallelPrims(numVertices);

        // Add your edges here
         for (int i = 0; i < numVertices - 1; i++) {
             parallelPrims.addEdge(i, i + 1, (int) (Math.random() * 100));
         }
    }

    @Benchmark
    @Fork(1)
    @Warmup(iterations = 5)
    @Measurement(iterations = 10)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkGetMST() {
        // This method benchmarks the getMST method of the ParallelPrims class
        parallelPrims.getMST();
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ParallelPrimsBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
