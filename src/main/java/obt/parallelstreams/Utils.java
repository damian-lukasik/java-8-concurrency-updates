package obt.parallelstreams;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.out;

public class Utils {
    public static void print(Object o) {
        out.print(o + " ");
    }

    public static void println(Object o) {
        out.println(o);
    }

    public static void printlnCurrentThread() {
        out.println(Thread.currentThread().getName());
    }

    public static List<Integer> range(int startInclusive, int endInclusive) {
        return IntStream.rangeClosed(startInclusive, endInclusive).boxed().collect(Collectors.<Integer>toList());
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
