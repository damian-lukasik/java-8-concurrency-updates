package obt.completablefuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static obt.parallelstreams.Utils.sleep;

public class Support {

    public static CompletableFuture<Coffee> brewCoffee() {
        return CompletableFuture.completedFuture(new Coffee());
    }

    public static CompletableFuture<Pizza> bakePizza() {
        return CompletableFuture.completedFuture(new Pizza());
    }

    public static CompletableFuture<Coffee> starbucksCoffee() {
        return CompletableFuture.completedFuture(new Coffee());
    }

    public static CompletableFuture<Horse> blackHorse() {
        return CompletableFuture.supplyAsync(() -> {
            int time = ThreadLocalRandom.current().nextInt(100);
            double chance = ThreadLocalRandom.current().nextDouble();
            if (chance > 0.75)
                sleep(1000 + time);
            else
                sleep(time);

            return new Horse("Black horse");
        });
    }

    public static CompletableFuture<Horse> prettyGoodHorse() {
        return CompletableFuture.supplyAsync(() -> {
            int time = ThreadLocalRandom.current().nextInt(100);
            sleep(200 + time);

            return new Horse("Pretty Good Horse");
        });
    }

    public static List<String> notYetIndexedSites() {
        return asList("www.github.com", "openjdk.java.net", "www.scala-lang.org");
    }

    public static Function<Horse, Horse> ignoreMe() {
        return Function.<Horse>identity();
    }
}
