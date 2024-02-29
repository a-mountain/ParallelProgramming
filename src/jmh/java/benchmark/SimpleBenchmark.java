package benchmark;


import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import maksym.perevalov.simple.SimpleGlobalLock;

@Fork(value = 1, warmups = 1)
public class SimpleBenchmark {

    @Benchmark
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void test() {
        int cells = 64;
        int particles = 8;
        double transitionFactor = 0.5;
        new SimpleGlobalLock(cells, particles, transitionFactor).start();
    }
}
