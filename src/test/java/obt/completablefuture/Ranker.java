package obt.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class Ranker {

    public static CompletableFuture<Long> rank(Document document) {
        return CompletableFuture.completedFuture(ThreadLocalRandom.current().nextLong(1000));
    }
}
