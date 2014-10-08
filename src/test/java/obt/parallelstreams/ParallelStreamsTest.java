package obt.parallelstreams;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static obt.parallelstreams.Utils.println;
import static obt.parallelstreams.Utils.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ParallelStreamsTest {

    @Test
    public void randomOrder() {
        List<Integer> ints = range(1, 5);

        ints
                .parallelStream()
                .forEach(Utils::println);
    }

    @Test
    public void collectorCharacteristic() {
        List<Integer> ints = range(1, 5);

        List<Integer> collected = ints
                .parallelStream()
                .collect(toList());

        assertThat(collected).isEqualTo(ints);
    }

    @Test
    public void sideEffect() {
        final int[] counter = {0};
        List<Integer> ints = range(1, 10000);

        ints
                .parallelStream()
                .map(e -> {
                    counter[0]++;
                    return e;
                });

        assertNotEquals(10000, counter[0]);
    }

    @Test
    public void fixedSideEffect() {
        AtomicInteger counter = new AtomicInteger(0);
        List<Integer> ints = range(1, 10000);
        ints
                .parallelStream()
                .forEach(e -> counter.incrementAndGet());

        assertEquals(10000, counter.get());
    }

    @Test
    public void concurrentGrouping() {
        List<Integer> ints = range(0, 99);

        ConcurrentMap<Integer, List<Integer>> groups = ints
                .parallelStream()
                .collect(Collectors.groupingByConcurrent(i -> i / 10));

        println(groups);
    }

    @Test
    public void customForkJoinPool() throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(1);

        List<Integer> ints = range(1, 10000);
        final int[] counter = {0};

        forkJoinPool.submit(() -> ints
                        .parallelStream()
                        .forEach(e -> counter[0]++)
        ).get();

        assertEquals(10000, counter[0]);
    }
}
