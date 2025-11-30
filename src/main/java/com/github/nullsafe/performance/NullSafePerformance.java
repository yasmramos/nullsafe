/**
 * Performance optimization utilities for NullSafe operations.
 * Provides caching, pooling, and performance monitoring features.
 * 
 * @since 1.0
 */
public final class NullSafePerformance {
    
    private NullSafePerformance() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Creates a cached NullSafe with automatic memoization.
     * 
     * @param <T> the type of the value
     * @param supplier the supplier function
     * @return cached NullSafe
     */
    public static <T> NullSafe<T> cached(Supplier<T> supplier) {
        return new CachedNullSafe<>(supplier);
    }
    
    /**
     * Creates a time-based cached NullSafe.
     * 
     * @param <T> the type of the value
     * @param supplier the supplier function
     * @param ttl the time to live in milliseconds
     * @return time-based cached NullSafe
     */
    public static <T> NullSafe<T> timeCached(Supplier<T> supplier, long ttl) {
        return new TimeCachedNullSafe<>(supplier, ttl);
    }
    
    /**
     * Creates a memory-efficient cached NullSafe using weak references.
     * 
     * @param <T> the type of the value
     * @param supplier the supplier function
     * @return weak cached NullSafe
     */
    public static <T> NullSafe<T> weakCached(Supplier<T> supplier) {
        return new WeakCachedNullSafe<>(supplier);
    }
    
    /**
     * Creates an object pool for NullSafe instances.
     * 
     * @param <T> the type of objects in the pool
     * @param factory the object factory
     * @param initialSize the initial pool size
     * @param maxSize the maximum pool size
     * @return object pool
     */
    public static <T> NullSafeObjectPool<T> objectPool(Supplier<T> factory, int initialSize, int maxSize) {
        return new NullSafeObjectPool<>(factory, initialSize, maxSize);
    }
    
    /**
     * Creates a performance monitor for NullSafe operations.
     * 
     * @return performance monitor
     */
    public static NullSafePerformanceMonitor monitor() {
        return new NullSafePerformanceMonitor();
    }
    
    /**
     * Creates a null-safe memoized function.
     * 
     * @param <T> the input type
     * @param <R> the return type
     * @param function the function to memoize
     * @return memoized function
     */
    public static <T, R> Function<T, NullSafe<R>> memoized(Function<T, R> function) {
        return new MemoizedFunction<>(function).andThen(NullSafe::of);
    }
    
    /**
     * Creates a lazy NullSafe that evaluates on first access.
     * 
     * @param <T> the type of the value
     * @param supplier the supplier function
     * @return lazy NullSafe
     */
    public static <T> NullSafe<T> lazy(Supplier<T> supplier) {
        return new LazyNullSafe<>(supplier);
    }
    
    /**
     * NullSafe with automatic caching.
     */
    private static class CachedNullSafe<T> implements NullSafe<T> {
        private volatile T value;
        private volatile boolean computed = false;
        private final Supplier<T> supplier;
        
        public CachedNullSafe(Supplier<T> supplier) {
            this.supplier = supplier;
        }
        
        @Override
        public boolean isEmpty() {
            computeIfNeeded();
            return !computed || value == null;
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
            } else if (isAbsent()) {
                return this;
            } else {
                return NullSafe.empty();
            }
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
    
    /**
     * NullSafe with time-based caching.
     */
    private static class TimeCachedNullSafe<T> implements NullSafe<T> {
        private volatile T value;
        private volatile boolean valid = false;
        private final Supplier<T> supplier;
        private final long ttl;
        private final AtomicLong lastCompute = new AtomicLong();
        
        public TimeCachedNullSafe(Supplier<T> supplier, long ttl) {
            this.supplier = supplier;
            this.ttl = ttl;
        }
        
        @Override
        public boolean isEmpty() {
            return !isValid();
        }
        
        @Override
        public T get() {
            if (isValid()) {
                return value;
            }
            return compute();
        }
        
        @Override
        public boolean isPresent() {
            return isValid();
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
        
        private boolean isValid() {
            return valid && (System.currentTimeMillis() - lastCompute.get()) < ttl;
        }
        
        private T compute() {
            synchronized (this) {
                if (!isValid()) {
                    try {
                        value = supplier.get();
                        valid = value != null;
                        lastCompute.set(System.currentTimeMillis());
                    } catch (Exception e) {
                        value = null;
                        valid = false;
                    }
                }
                return value;
            }
        }
    }
    
    /**
     * NullSafe with weak reference caching.
     */
    private static class WeakCachedNullSafe<T> implements NullSafe<T> {
        private final WeakReference<T> weakReference;
        
        public WeakCachedNullSafe(Supplier<T> supplier) {
            T value;
            try {
                value = supplier.get();
            } catch (Exception e) {
                value = null;
            }
            this.weakReference = new WeakReference<>(value);
        }
        
        @Override
        public boolean isEmpty() {
            return weakReference.get() == null;
        }
        
        @Override
        public T get() {
            return weakReference.get();
        }
        
        @Override
        public boolean isPresent() {
            return weakReference.get() != null;
        }
        
        @Override
        public boolean isAbsent() {
            return !isPresent();
        }
        
        @Override
        public void ifPresent(Consumer<? super T> action) {
            T value = weakReference.get();
            if (value != null) {
                action.accept(value);
            }
        }
        
        @Override
        public void ifAbsent(Runnable action) {
            if (weakReference.get() == null) {
                action.run();
            }
        }
        
        @Override
        public NullSafe<T> filter(Predicate<? super T> predicate) {
            T value = weakReference.get();
            if (value != null && predicate.test(value)) {
                return this;
            }
            return NullSafe.empty();
        }
        
        @Override
        public <U> NullSafe<U> map(Function<? super T, ? extends U> mapper) {
            T value = weakReference.get();
            if (value != null) {
                return NullSafe.of(mapper.apply(value));
            }
            return NullSafe.empty();
        }
        
        @Override
        public <U> NullSafe<U> flatMap(Function<? super T, NullSafe<U>> mapper) {
            T value = weakReference.get();
            if (value != null) {
                return mapper.apply(value);
            }
            return NullSafe.empty();
        }
        
        @Override
        public T orElse(T other) {
            T value = weakReference.get();
            return value != null ? value : other;
        }
        
        @Override
        public T orElseGet(Supplier<? extends T> other) {
            T value = weakReference.get();
            return value != null ? value : other.get();
        }
        
        @Override
        public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
            T value = weakReference.get();
            if (value != null) {
                return value;
            } else {
                throw exceptionSupplier.get();
            }
        }
    }
    
    /**
     * Object pool for NullSafe instances.
     */
    public static class NullSafeObjectPool<T> {
        private final BlockingQueue<NullSafe<T>> pool;
        private final Supplier<T> factory;
        private final int maxSize;
        private final AtomicInteger currentSize = new AtomicInteger(0);
        
        public NullSafeObjectPool(Supplier<T> factory, int initialSize, int maxSize) {
            this.factory = factory;
            this.maxSize = maxSize;
            this.pool = new LinkedBlockingQueue<>(maxSize);
            
            // Pre-populate pool
            for (int i = 0; i < Math.min(initialSize, maxSize); i++) {
                addToPool();
            }
        }
        
        /**
         * Borrow a NullSafe from the pool.
         * 
         * @return borrowed NullSafe
         * @throws InterruptedException if interrupted while waiting
         */
        public NullSafe<T> borrow() throws InterruptedException {
            NullSafe<T> item = pool.poll(1, TimeUnit.SECONDS);
            if (item == null) {
                return createNew();
            }
            return item;
        }
        
        /**
         * Return a NullSafe to the pool.
         * 
         * @param item the item to return
         */
        public void returnToPool(NullSafe<T> item) {
            if (item != null) {
                pool.offer(item);
            }
        }
        
        /**
         * Get current pool size.
         * 
         * @return pool size
         */
        public int getPoolSize() {
            return pool.size();
        }
        
        /**
         * Get current active objects.
         * 
         * @return active object count
         */
        public int getActiveCount() {
            return currentSize.get() - getPoolSize();
        }
        
        private NullSafe<T> createNew() {
            if (currentSize.get() < maxSize) {
                currentSize.incrementAndGet();
                return NullSafe.cached(factory);
            } else {
                throw new RuntimeException("Pool exhausted");
            }
        }
        
        private void addToPool() {
            NullSafe<T> item = createNew();
            pool.offer(item);
        }
    }
}