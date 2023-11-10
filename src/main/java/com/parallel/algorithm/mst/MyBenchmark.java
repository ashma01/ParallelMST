package com.parallel.algorithm.mst;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

public class MyBenchmark {
    @Benchmark@BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testMethod() {
        doMagic();
    }
    public static void doMagic() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }
}