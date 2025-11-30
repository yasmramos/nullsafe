/**
 * Performance monitoring utilities for NullSafe operations.
 * 
 * @since 1.0
 */
public class NullSafePerformanceMonitor {
    private final Map<String, PerformanceMetrics> metrics = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    
    /**
     * Records a performance metric.
     * 
     * @param operationName the operation name
     * @param durationNs the duration in nanoseconds
     * @param success whether the operation succeeded
     */
    public void recordMetric(String operationName, long durationNs, boolean success) {
        metrics.computeIfAbsent(operationName, k -> new PerformanceMetrics())
               .record(durationNs, success);
    }
    
    /**
     * Records a performance metric using lambda timing.
     * 
     * @param <T> the return type
     * @param operationName the operation name
     * @param operation the operation to execute and time
     * @return the result of the operation
     */
    public <T> T recordTime(String operationName, Supplier<T> operation) {
        long start = System.nanoTime();
        try {
            T result = operation.get();
            recordMetric(operationName, System.nanoTime() - start, true);
            return result;
        } catch (Exception e) {
            recordMetric(operationName, System.nanoTime() - start, false);
            throw e;
        }
    }
    
    /**
     * Gets metrics for an operation.
     * 
     * @param operationName the operation name
     * @return the performance metrics
     */
    public PerformanceMetrics getMetrics(String operationName) {
        return metrics.get(operationName);
    }
    
    /**
     * Gets all metrics.
     * 
     * @return map of all metrics
     */
    public Map<String, PerformanceMetrics> getAllMetrics() {
        return new HashMap<>(metrics);
    }
    
    /**
     * Gets a summary report.
     * 
     * @return formatted summary string
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("NullSafe Performance Summary:\n");
        sb.append("============================\n");
        
        metrics.forEach((name, metric) -> {
            sb.append(String.format("%s:\n", name));
            sb.append(String.format("  Total calls: %d\n", metric.getTotalCalls()));
            sb.append(String.format("  Success rate: %.2f%%\n", metric.getSuccessRate() * 100));
            sb.append(String.format("  Avg duration: %.2f ms\n", metric.getAverageDurationMs()));
            sb.append(String.format("  Min duration: %.2f ms\n", metric.getMinDurationMs()));
            sb.append(String.format("  Max duration: %.2f ms\n", metric.getMaxDurationMs()));
            sb.append("\n");
        });
        
        return sb.toString();
    }
    
    /**
     * Cleans up resources.
     */
    public void close() {
        executor.shutdown();
    }
    
    /**
     * Performance metrics container.
     */
    public static class PerformanceMetrics {
        private final AtomicInteger totalCalls = new AtomicInteger(0);
        private final AtomicInteger successCalls = new AtomicInteger(0);
        private final LongAdder totalDuration = new LongAdder();
        private final AtomicLong minDuration = new AtomicLong(Long.MAX_VALUE);
        private final AtomicLong maxDuration = new AtomicLong(0);
        
        void record(long durationNs, boolean success) {
            totalCalls.incrementAndGet();
            totalDuration.add(durationNs);
            
            if (success) {
                successCalls.incrementAndGet();
            }
            
            // Update min/max
            long currentMin = minDuration.get();
            if (durationNs < currentMin) {
                minDuration.compareAndSet(currentMin, durationNs);
            }
            
            long currentMax = maxDuration.get();
            if (durationNs > currentMax) {
                maxDuration.compareAndSet(currentMax, durationNs);
            }
        }
        
        public int getTotalCalls() {
            return totalCalls.get();
        }
        
        public double getSuccessRate() {
            int total = totalCalls.get();
            return total > 0 ? (double) successCalls.get() / total : 0.0;
        }
        
        public double getAverageDurationMs() {
            int total = totalCalls.get();
            return total > 0 ? (double) totalDuration.sum() / total / 1_000_000.0 : 0.0;
        }
        
        public double getMinDurationMs() {
            return minDuration.get() == Long.MAX_VALUE ? 0.0 : minDuration.get() / 1_000_000.0;
        }
        
        public double getMaxDurationMs() {
            return maxDuration.get() / 1_000_000.0;
        }
        
        public void reset() {
            totalCalls.set(0);
            successCalls.set(0);
            totalDuration.reset();
            minDuration.set(Long.MAX_VALUE);
            maxDuration.set(0);
        }
    }
    
    /**
     * Memoized function wrapper.
     */
    private static class MemoizedFunction<T, R> implements Function<T, R> {
        private final Map<T, R> cache = new ConcurrentHashMap<>();
        private final Function<T, R> function;
        
        public MemoizedFunction(Function<T, R> function) {
            this.function = function;
        }
        
        @Override
        public R apply(T input) {
            return cache.computeIfAbsent(input, function::apply);
        }
    }
    
    /**
     * Lazy NullSafe implementation.
     */
    private static class LazyNullSafe<T> implements NullSafe<T> {
        private volatile T value;
        private volatile boolean computed = false;
        private final Supplier<T> supplier;
        
        public LazyNullSafe(Supplier<T> supplier) {
            this.supplier = supplier;
        }
        
        @Override
        public boolean isEmpty() {
            return !isPresent();
        }
        
        @Override
        public T get() {
            computeIfNeeded();
            return value;
        }
        
        @Override
        public boolean isPresent() {
            computeIfNeeded();
            return computed && value != null;
        }
        
        @Override
        public boolean isAbsent() {
            return !isPresent();
        }
        
        @Override
        public void ifPresent(Consumer<? super T> action) {
            if (isPresent()) {
                action.accept(get());
            }
        }
        
        @Override
        public void ifAbsent(Runnable action) {
            if (isAbsent()) {
                action.run();
            }
        }
        
        @Override
        public NullSafe<T> filter(Predicate<? super T> predicate) {
            if (isPresent() && predicate.test(get())) {
                return this;
            }
            return NullSafe.empty();
        }
        
        @Override
        public <U> NullSafe<U> map(Function<? super T, ? extends U> mapper) {
            if (isPresent()) {
                return NullSafe.of(mapper.apply(get()));
            }
            return NullSafe.empty();
        }
        
        @Override
        public <U> NullSafe<U> flatMap(Function<? super T, NullSafe<U>> mapper) {
            if (isPresent()) {
                return mapper.apply(get());
            }
            return NullSafe.empty();
        }
        
        @Override
        public T orElse(T other) {
            return isPresent() ? get() : other;
        }
        
        @Override
        public T orElseGet(Supplier<? extends T> other) {
            return isPresent() ? get() : other.get();
        }
        
        @Override
        public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
            if (isPresent()) {
                return get();
            } else {
                throw exceptionSupplier.get();
            }
        }
        
        private void computeIfNeeded() {
            if (!computed) {
                synchronized (this) {
                    if (!computed) {
                        try {
                            value = supplier.get();
                        } finally {
                            computed = true;
                        }
                    }
                }
            }
        }
    }
}