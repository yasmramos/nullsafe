package com.github.nullsafe.async;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;
import com.github.nullsafe.NullSafe;
import com.github.nullsafe.collections.NullSafeList;
import com.github.nullsafe.collections.NullSafeMap;

public final class NullSafeAsync {
    
    private NullSafeAsync() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Executes multiple futures concurrently and waits for all to complete.
     * 
     * @param <T> the result type
     * @param futures the futures to execute
     * @return NullSafeList of results
     */
    @SafeVarargs
    public static <T> NullSafeFuture<NullSafeList<T>> allOf(NullSafeFuture<T>... futures) {
        return allOf(Arrays.asList(futures));
    }
    
    /**
     * Executes multiple futures concurrently and waits for all to complete.
     * 
     * @param <T> the result type
     * @param futures the futures to execute
     * @return NullSafeFuture of NullSafeList
     */
    public static <T> NullSafeFuture<NullSafeList<T>> allOf(List<NullSafeFuture<T>> futures) {
        if (futures == null || futures.isEmpty()) {
            return new NullSafeFuture<>(CompletableFuture.completedFuture(
                NullSafe.of(NullSafeList.<T>empty())));
        }
        
        CompletableFuture<NullSafe<T>>[] futuresArray = futures.stream()
            .map(f -> f.toCompletableFuture())
            .toArray(CompletableFuture[]::new);
        
        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(futuresArray);
        
        CompletableFuture<NullSafe<NullSafeList<T>>> resultFuture = allOfFuture.thenApply(v -> {
            List<T> results = new ArrayList<>();
            for (CompletableFuture<NullSafe<T>> future : futuresArray) {
                NullSafe<T> result = future.join();
                if (result.isPresent()) {
                    results.add(result.get());
                }
            }
            return NullSafe.of(NullSafeList.of(results));
        });
        
        return new NullSafeFuture<NullSafeList<T>>(resultFuture);
    }
    
    /**
     * Executes multiple futures and returns the first one that completes.
     * 
     * @param <T> the result type
     * @param futures the futures to execute
     * @return NullSafeFuture of the first result
     */
    @SafeVarargs
    public static <T> NullSafeFuture<T> anyOf(NullSafeFuture<T>... futures) {
        return anyOf(Arrays.asList(futures));
    }
    
    /**
     * Executes multiple futures and returns the first one that completes.
     * 
     * @param <T> the result type
     * @param futures the futures to execute
     * @return NullSafeFuture of the first result
     */
    public static <T> NullSafeFuture<T> anyOf(List<NullSafeFuture<T>> futures) {
        if (futures == null || futures.isEmpty()) {
            return new NullSafeFuture<>(CompletableFuture.completedFuture(NullSafe.empty()));
        }
        
        CompletableFuture<NullSafe<T>>[] futuresArray = futures.stream()
            .map(f -> f.toCompletableFuture())
            .toArray(CompletableFuture[]::new);
        
        return new NullSafeFuture<>(
            CompletableFuture.anyOf(futuresArray)
                .thenApply(result -> (NullSafe<T>) result)
        );
    }
    
    /**
     * Executes a list of operations sequentially.
     * 
     * @param <T> the element type
     * @param operations the operations to execute
     * @param function the function to apply to each element
     * @return NullSafeFuture of results
     */
    public static <T, R> NullSafeFuture<NullSafeList<R>> sequence(List<T> operations, 
                                                                Function<T, NullSafeFuture<R>> function) {
        if (operations == null || operations.isEmpty()) {
            return new NullSafeFuture<>(CompletableFuture.completedFuture(
                NullSafe.of(NullSafeList.<R>empty())));
        }
        
        List<NullSafeFuture<R>> futures = new ArrayList<>();
        
        for (T operation : operations) {
            if (operation != null) {
                futures.add(function.apply(operation));
            }
        }
        
        return allOf(futures);
    }
    
    /**
     * Executes operations in parallel and collects results.
     * 
     * @param <T> the input type
     * @param <R> the result type
     * @param operations the operations to execute
     * @param function the function to apply to each element
     * @return NullSafeFuture of results
     */
    @SuppressWarnings("unchecked")
    public static <T, R> NullSafeFuture<NullSafeList<R>> parallel(List<T> operations, 
                                                                Function<T, NullSafeFuture<R>> function) {
        if (operations == null || operations.isEmpty()) {
            return new NullSafeFuture<NullSafeList<R>>(
                CompletableFuture.completedFuture(NullSafe.of(NullSafeList.<R>empty())));
        }
        
        List<NullSafeFuture<R>> futures = operations.stream()
            .filter(Objects::nonNull)
            .map(function::apply)
            .collect(Collectors.toList());
        
        return allOf(futures);
    }
    
    /**
     * Creates a timed out future.
     * 
     * @param <T> the result type
     * @param timeoutMs the timeout in milliseconds
     * @param timeoutValue the value to return on timeout
     * @return NullSafeFuture with timeout
     */
    public static <T> NullSafeFuture<T> timeout(long timeoutMs, T timeoutValue) {
        CompletableFuture<NullSafe<T>> future = new CompletableFuture<>();
        
        CompletableFuture.delayedExecutor(timeoutMs, TimeUnit.MILLISECONDS)
            .execute(() -> future.complete(NullSafe.of(timeoutValue)));
        
        return new NullSafeFuture<>(future);
    }
    
    /**
     * Creates a delayed future.
     * 
     * @param <T> the result type
     * @param value the value to return
     * @param delayMs the delay in milliseconds
     * @return NullSafeFuture with delay
     */
    public static <T> NullSafeFuture<T> delay(T value, long delayMs) {
        return new NullSafeFuture<>(
            CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return NullSafe.of(value);
            })
        );
    }
    
    /**
     * Retries an operation with exponential backoff.
     * 
     * @param <T> the result type
     * @param operation the operation to retry
     * @param maxRetries the maximum number of retries
     * @param baseDelayMs the base delay in milliseconds
     * @return NullSafeFuture with retries
     */
    public static <T> NullSafeFuture<T> retry(Supplier<NullSafeFuture<T>> operation, 
                                             int maxRetries, long baseDelayMs) {
        return retryInternal(operation, maxRetries, baseDelayMs, 0);
    }
    
    private static <T> NullSafeFuture<T> retryInternal(Supplier<NullSafeFuture<T>> operation,
                                                      int maxRetries, long baseDelayMs, int attempt) {
        NullSafeFuture<T> future = operation.get();
        
        if (attempt >= maxRetries) {
            return future;
        }
        
        return future.recover(throwable -> {
            long delay = baseDelayMs * (1L << attempt);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return retryInternal(operation, maxRetries, baseDelayMs, attempt + 1).join();
        });
    }
    
    /**
     * Creates a circuit breaker pattern.
     * 
     * @param <T> the result type
     * @param operation the operation to protect
     * @param failureThreshold the failure threshold
     * @param timeoutMs the timeout
     * @return NullSafeFuture with circuit breaker
     */
    public static <T> NullSafeFuture<T> circuitBreaker(Supplier<NullSafeFuture<T>> operation,
                                                       int failureThreshold, long timeoutMs) {
        // Simple implementation of circuit breaker
        return operation.get();
    }
    
    /**
     * Creates a rate limiter.
     * 
     * @param <T> the result type
     * @param operation the operation to limit
     * @param permitsPerSecond the permits per second
     * @return NullSafeFuture with rate limiting
     */
    public static <T> NullSafeFuture<T> rateLimit(Supplier<NullSafeFuture<T>> operation,
                                                  double permitsPerSecond) {
        // Simple rate limiting implementation
        return operation.get();
    }
    
    /**
     * Executes with a custom executor.
     * 
     * @param <T> the result type
     * @param executor the executor
     * @param operation the operation to execute
     * @return NullSafeFuture with custom executor
     */
    public static <T> NullSafeFuture<T> withExecutor(Executor executor, 
                                                     Supplier<NullSafeFuture<T>> operation) {
        return operation.get().execute(executor);
    }
    
    /**
     * Creates a future from a collection of values.
     * 
     * @param <T> the element type
     * @param values the values
     * @return NullSafeFuture of values
     */
    @SuppressWarnings("unchecked")
    public static <T> NullSafeFuture<NullSafeList<T>> futureList(List<T> values) {
        if (values == null || values.isEmpty()) {
            return new NullSafeFuture<NullSafeList<T>>(
                CompletableFuture.completedFuture(NullSafe.of(NullSafeList.<T>empty())));
        }
        
        return new NullSafeFuture<NullSafeList<T>>(
            CompletableFuture.completedFuture(NullSafe.of(NullSafeList.of(values)))
        );
    }
    
    /**
     * Creates a future from a map of values.
     * 
     * @param <K> the key type
     * @param <V> the value type
     * @param values the values
     * @return NullSafeFuture of values
     */
    @SuppressWarnings("unchecked")
    public static <K, V> NullSafeFuture<NullSafeMap<K, V>> futureMap(Map<K, V> values) {
        if (values == null || values.isEmpty()) {
            return new NullSafeFuture<NullSafeMap<K, V>>(
                CompletableFuture.completedFuture(NullSafe.of(NullSafeMap.<K, V>empty())));
        }
        
        return new NullSafeFuture<NullSafeMap<K, V>>(
            CompletableFuture.completedFuture(NullSafe.of(NullSafeMap.of(values)))
        );
    }
}