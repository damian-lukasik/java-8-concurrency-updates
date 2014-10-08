package obt.contended;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class AtContendedBenchmark {

    // ============== BASELINE ==============

    @State(Scope.Group)
    public static class StateBaseline {
        int writeOnly;
        int readOnly;
    }

    @org.openjdk.jmh.annotations.Benchmark
    @Group("baseline")
    public int reader(StateBaseline s) {
        return s.readOnly;
    }

    @org.openjdk.jmh.annotations.Benchmark
    @Group("baseline")
    public void writer(StateBaseline s) {
        s.writeOnly++;
    }


    // ============== AT_CONTENDED ==============

    @State(Scope.Group)
    public static class StateContended {
        int readOnly;

        @sun.misc.Contended
        int writeOnly;
    }

    @Benchmark
    @Group("contended")
    public int reader(StateContended s) {
        return s.readOnly;
    }

    @Benchmark
    @Group("contended")
    public void writer(StateContended s) {
        s.writeOnly++;
    }


    // ==========================================

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AtContendedBenchmark.class.getSimpleName())
                .threads(Runtime.getRuntime().availableProcessors())
                .jvmArgs("-XX:-RestrictContended")
                .build();

        new Runner(opt).run();
    }
}
