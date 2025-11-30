/**
 * NullSafe operations for Future and CompletableFuture.
 * Provides safe handling of asynchronous operations.
 * 
 * @param <T> the result type
 * @since 1.0
 */
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;
import com.github.nullsafe.NullSafe;
import com.github.nullsafe.async.*;

public class NullSafeFuture<T> {
    private final CompletableFuture<NullSafe<T>> future;
    
    /**
     * Creates a NullSafeFuture from a CompletableFuture.
     * 
     * @param future the source CompletableFuture
     */
    public NullSafeFuture(CompletableFuture<NullSafe<T>> future) {
        this.future = future;
    }
    
    /**
     * Creates a NullSafeFuture with a successful result.
     * 
     * @param result the result
     */
    public NullSafeFuture(T result) {
        this.future = CompletableFuture.completedFuture(NullSafe.of(result));
    }
    
    /**
     * Creates a NullSafeFuture with a failed result.
     * 
     * @param error the error
     */
    public NullSafeFuture(Throwable error) {
        this.future = new CompletableFuture<>();
        future.completeExceptionally(error);
    }
    
    /**
     * Creates a NullSafeFuture from a supplier.
     * 
     * @param supplier the supplier function
     */
    public static <T> NullSafeFuture<T> fromSupplier(Supplier<T> supplier) {
        return new NullSafeFuture<>(
            CompletableFuture.supplyAsync(() -> {
                try {
                    return NullSafe.of(supplier.get());
                } catch (Exception e) {
                    return NullSafe.empty();
                }
            })
        );
    }
    
    /**
     * Creates a NullSafeFuture from a runnable.
     * 
     * @param runnable the runnable function
     */
    public static NullSafeFuture<Void> fromRunnable(Runnable runnable) {
        return new NullSafeFuture<>(
            CompletableFuture.runAsync(() -> {
                try {
                    runnable.run();
                } catch (Exception e) {
                    // Ignore errors for void operations
                }
            }).thenApply(v -> NullSafe.empty())
        );
    }
    
    /**
     * Creates a NullSafeFuture from a callable.
     * 
     * @param callable the callable function
     */
    public static <T> NullSafeFuture<T> fromCallable(Callable<T> callable) {
        return new NullSafeFuture<>(
            CompletableFuture.supplyAsync(() -> {
                try {
                    return NullSafe.of(callable.call());
                } catch (Exception e) {
                    return NullSafe.empty();
                }
            })
        );
    }
    
    /**
     * Maps the result of the future.
     * 
     * @param <R> the result type
     * @param mapper the mapping function
     * @return new NullSafeFuture with mapped result
     */
    public <R> NullSafeFuture<R> map(Function<NullSafe<T>, NullSafe<R>> mapper) {
        return new NullSafeFuture<>(
            future.thenApply(mapper::apply)
        );
    }
    
    /**
     * Maps the raw result value.
     * 
     * @param <R> the result type
     * @param mapper the mapping function
     * @return new NullSafeFuture with mapped value
     */
    public <R> NullSafeFuture<R> mapValue(Function<T, R> mapper) {
        return new NullSafeFuture<>(
            future.thenApply(result -> result.map(mapper))
        );
    }
    
    /**
     * Chains another future operation.
     * 
     * @param <R> the result type
     * @param next the next operation
     * @return new NullSafeFuture with chained result
     */
    public <R> NullSafeFuture<R> flatMap(Function<NullSafe<T>, NullSafeFuture<R>> next) {
        return new NullSafeFuture<>(
            future.thenCompose(result -> {
                if (result.isPresent()) {
                    return next.apply(result).future;
                } else {
                    return CompletableFuture.completedFuture(NullSafe.empty());
                }
            })
        );
    }
    
    /**
     * Filters the result.
     * 
     * @param predicate the filter predicate
     * @return new NullSafeFuture with filtered result
     */
    public NullSafeFuture<T> filter(Predicate<T> predicate) {
        return new NullSafeFuture<>(
            future.thenApply(result -> result.filter(predicate))
        );
    }
    
    /**
     * Executes an action if the result is present.
     * 
     * @param action the action to execute
     * @return this for chaining
     */
    public NullSafeFuture<T> ifPresent(Consumer<T> action) {
        future.thenAccept(result -> result.ifPresent(action));
        return this;
    }
    
    /**
     * Executes an action if the result is absent.
     * 
     * @param action the action to execute
     * @return this for chaining
     */
    public NullSafeFuture<T> ifAbsent(Runnable action) {
        future.thenAccept(result -> result.ifAbsent(action));
        return this;
    }
    
    /**
     * Provides a default value if the result is absent.
     * 
     * @param defaultValue the default value
     * @return new NullSafeFuture with default value
     */
    public NullSafeFuture<T> orElse(T defaultValue) {
        return new NullSafeFuture<>(
            future.thenApply(result -> result.orElse(defaultValue))
        );
    }
    
    /**
     * Provides a default value supplier if the result is absent.
     * 
     * @param defaultSupplier the default value supplier
     * @return new NullSafeFuture with default value
     */
    public NullSafeFuture<T> orElseGet(Supplier<T> defaultSupplier) {
        return new NullSafeFuture<>(
            future.thenApply(result -> result.orElseGet(defaultSupplier))
        );
    }
    
    /**
     * Handles the absence case with an exception.
     * 
     * @param exceptionSupplier the exception supplier
     * @return the result or throws exception
     * @throws RuntimeException if the result is absent
     */
    public T orElseThrow(Supplier<? extends RuntimeException> exceptionSupplier) throws RuntimeException {
        try {
            return future.join().orElseThrow(exceptionSupplier);
        } catch (CompletionException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw exceptionSupplier.get();
        }
    }
    
    /**
     * Waits for the future to complete and returns the result.
     * 
     * @return the result
     * @throws InterruptedException if interrupted while waiting
     */
    public NullSafe<T> get() throws InterruptedException {
        return future.get();
    }
    
    /**
     * Waits for the future to complete with timeout.
     * 
     * @param timeout the timeout value
     * @param unit the timeout unit
     * @return the result
     * @throws TimeoutException if timeout occurs
     * @throws InterruptedException if interrupted while waiting
     */
    public NullSafe<T> get(long timeout, TimeUnit unit) 
            throws TimeoutException, InterruptedException {
        return future.get(timeout, unit);
    }
    
    /**
     * Waits for the future to complete non-blocking.
     * 
     * @return the result
     */
    public NullSafe<T> join() {
        return future.join();
    }
    
    /**
     * Registers a callback for successful completion.
     * 
     * @param action the action to execute
     * @return this for chaining
     */
    public NullSafeFuture<T> onSuccess(Consumer<NullSafe<T>> action) {
        future.thenAccept(action);
        return this;
    }
    
    /**
     * Registers a callback for successful completion with value.
     * 
     * @param action the action to execute
     * @return this for chaining
     */
    public NullSafeFuture<T> onSuccessValue(Consumer<T> action) {
        future.thenAccept(result -> result.ifPresent(action));
        return this;
    }
    
    /**
     * Registers a callback for failure.
     * 
     * @param action the action to execute
     * @return this for chaining
     */
    public NullSafeFuture<T> onFailure(Consumer<Throwable> action) {
        future.exceptionally(throwable -> {
            action.accept(throwable);
            return NullSafe.empty();
        });
        return this;
    }
    
    /**
     * Recovers from errors.
     * 
     * @param recover the recovery function
     * @return new NullSafeFuture with recovery
     */
    public NullSafeFuture<T> recover(Function<Throwable, NullSafe<T>> recover) {
        return new NullSafeFuture<>(
            future.exceptionally(throwable -> {
                try {
                    return recover.apply(throwable);
                } catch (Exception e) {
                    return NullSafe.empty();
                }
            })
        );
    }
    
    /**
     * Executes in the specified executor.
     * 
     * @param executor the executor
     * @return new NullSafeFuture executing in the executor
     */
    public NullSafeFuture<T> execute(Executor executor) {
        return new NullSafeFuture<>(
            future.thenApplyAsync(result -> result, executor)
        );
    }
    
    /**
     * Converts to CompletableFuture of NullSafe.
     * 
     * @return the CompletableFuture
     */
    public CompletableFuture<NullSafe<T>> toCompletableFuture() {
        return future;
    }
    
    /**
     * Converts to Observable if using reactive streams library.
     * 
     * @return Observable of the result
     */
    @SuppressWarnings("unchecked")
    public Object toObservable() {
        // This would require reactive streams library integration
        // For now, returns a simple observable-like interface
        return new Object() {
            public void subscribe(java.util.function.Consumer<T> consumer) {
                future.thenAccept(result -> result.ifPresent(consumer));
            }
        };
    }
}