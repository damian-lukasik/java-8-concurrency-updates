package obt.parallelstreams;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class ParallelStreamsBenchmark {

    @Param({"1", "10", "100", "1000", "10000", "100000", "1000000"})
    int size;

    @Benchmark
    public double sequential() {
        return newDoubleStream(size)
                .map(Math::sin)
                .sum();
    }

    @Benchmark
    public double parallel() {
        return newDoubleStream(size)
                .parallel()
                .map(Math::sin)
                .sum();
    }

    private DoubleStream newDoubleStream(int end) {
        return IntStream.range(1, end).mapToDouble(i -> (double) i);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ParallelStreamsBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
