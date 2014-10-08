package obt.completablefuture;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Runtime.getRuntime;
import static java.lang.System.out;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static obt.completablefuture.Support.*;
import static obt.completablefuture.WebCralwer.download;
import static obt.parallelstreams.Utils.println;
import static obt.parallelstreams.Utils.printlnCurrentThread;

public class CompletableFutureTest {

    public static final ExecutorService EXECUTOR_SERVICE = newFixedThreadPool(getRuntime().availableProcessors());



















    @Test
    public void completeFuture() throws Exception {
        CompletableFuture<Long> f = new CompletableFuture<>();

        // when 'f' will be available print it!
        CompletableFuture<Void> f2 = f.thenAccept(out::println);

        // somewhere else in the system
        f.complete(42L);

        f2.get();
    }


























    @Test
    public void completeFutureWithExecutor() throws Exception {
        CompletableFuture<Long> f = new CompletableFuture<>();

        CompletableFuture<Void> f2 =
                f.thenAcceptAsync(out::println, newFixedThreadPool(777));

        f.complete(42L);

        f2.get();
    }


























    @Test
    public void supplyIt() throws Exception {
        printlnCurrentThread();

        CompletableFuture<Long> f = CompletableFuture
                .supplyAsync(() -> {
            printlnCurrentThread();
            return 42L;
        });

        f.get();
    }

























    @Test
    public void acceptIt() throws Exception {
        printlnCurrentThread();

        CompletableFuture<Void> f =
                supplyAsync(() -> {
                    printlnCurrentThread();
                    return 42l;
                })
                .thenAccept(v -> printlnCurrentThread());

        f.get();
    }





















    @Test
    public void acceptItAsynchronously() throws Exception {
        printlnCurrentThread();

        CompletableFuture<Void> f =
                supplyAsync(() -> {
                    printlnCurrentThread();
                    return 42l;
                })
                .thenAcceptAsync(v -> printlnCurrentThread());

        f.get();
    }
























    @Test
    public void chaining() throws Exception {
        CompletableFuture<Long> f = new CompletableFuture<>();
        f
                .thenApplyAsync(v -> v * 2)
                .thenApplyAsync(v -> v - 42)
                .thenAccept(out::println);

        f.complete(42L);
    }


























    @Test
    public void combining() throws Exception {
        CompletableFuture<Pizza> bakePizza = bakePizza();
        CompletableFuture<Coffee> brewCoffee = brewCoffee();

        CompletableFuture<String> code = bakePizza
                .thenCombineAsync(brewCoffee,
                        (pizza, coffee) -> "Piece of code");

        println(code.get());
    }





































    @Test
    public void composingUgly() throws Exception {
        CompletableFuture<Coffee> drinkCoffee = starbucksCoffee();
        CompletableFuture<Pizza> eatPizza = bakePizza();

        CompletableFuture<CompletableFuture<Coffee>> fed =
                eatPizza.thenApply(p -> drinkCoffee);

        fed.thenAccept(pizzaDone -> pizzaDone
                .thenAccept(
                        coffeeDone ->
                                println("Some ugly piece of code")));

        fed.get();
    }



























    @Test
    public void composingNice() throws Exception {
        CompletableFuture<Coffee> drinkCoffee = starbucksCoffee();
        CompletableFuture<Pizza> eatPizza = bakePizza();

        CompletableFuture<Coffee> fed = eatPizza.
                thenCompose(p -> drinkCoffee);
        fed.thenAccept(allDone -> println("Some ugly piece of code"));

        fed.get();
    }




























    @Test
    public void horseRacing() throws Exception {
        CompletableFuture<Horse> blackHorse = blackHorse();
        CompletableFuture<Horse> prettyGoodHorse = prettyGoodHorse();

        CompletableFuture<Horse> winner =
                blackHorse.applyToEither(prettyGoodHorse, ignoreMe());

        println(winner.get());
    }



















    @Test
    public void pageRank() throws Exception {
        List<String> websites = notYetIndexedSites();

        Stream<CompletableFuture<Long>> $$$ = websites.stream()
                .map(website -> supplyAsync(() -> download(website), EXECUTOR_SERVICE))
                .map(rawContentFuture -> rawContentFuture.thenApplyAsync(PageBuilder::parse))
                .map(documentFuture -> documentFuture.thenComposeAsync(Ranker::rank));

        List<CompletableFuture<Long>> ranks = $$$.collect(Collectors.<CompletableFuture<Long>>toList());

        CompletableFuture.allOf(ranks.toArray(new CompletableFuture[ranks.size()])).join();

        println("All ranks are ready!");
        ranks.stream().forEach(r -> println(r.join()));
    }




























}
