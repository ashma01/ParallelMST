package org.benchmark.parallel;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.util.ArrayList;
import java.util.List;


@State(Scope.Benchmark)
public class PrimsBenchmark {

    private ParallelPrims parallelPrims;

    private SequentialMST sequentialMST;
    private ParallelKruskal parallelKruskal;

    List<Edge> edges = new ArrayList<>();


    @Param({"100","500","5000","10000","20000"})
    int numVertices;

    @Setup
    public void setup() {
        parallelPrims = new ParallelPrims(numVertices);
        parallelKruskal = new ParallelKruskal();
        sequentialMST = new SequentialMST();

        // Add your edges here
        for (int i = 0; i < numVertices - 1; i++) {
            parallelPrims.addEdge(i, i + 1, (int) (Math.random() * 100));
        }


        for (int i = 0; i < numVertices - 1; i++) {
            int weight = (int) (Math.random() * 100);
            edges.add(new Edge(i, i + 1, weight)); // Assuming Edge is a class you have defined
        }
    }

    @Fork(value = 1, warmups = 1)
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void benchmarkParallelPrims() {
        parallelPrims.getMST();
    }

    @Fork(value = 1, warmups = 1)
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void benchmarkSequential() {
        sequentialMST.seqMst(edges.toArray(new Edge[0]),numVertices);
    }

    @Fork(value = 1, warmups = 1)
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void benchmarkParallelKruskal() {
        parallelKruskal.kruskalMST(edges.toArray(new Edge[0]),numVertices);
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder().include(PrimsBenchmark.class.getSimpleName())
                .forks(1)
                .verbosity(VerboseMode.EXTRA)
                .resultFormat(ResultFormatType.CSV)
                .output("result.csv")
                .build();

        new Runner(opt).run();
    }

}
