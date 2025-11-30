/**
 * Advanced transformation system for NullSafe values.
 * Provides sophisticated data transformation capabilities.
 * 
 * @param <T> the input type
 * @param <R> the output type
 * @since 1.0
 */
public class NullSafeTransformer<T, R> {
    private final NullSafe<T> input;
    private final List<TransformationStep<R>> steps;
    
    /**
     * Creates a new NullSafeTransformer.
     * 
     * @param input the input value
     */
    public NullSafeTransformer(NullSafe<T> input) {
        this.input = input;
        this.steps = new ArrayList<>();
    }
    
    /**
     * Creates a transformer from a value.
     * 
     * @param <T> the input type
     * @param <R> the output type
     * @param value the input value
     * @return the transformer
     */
    public static <T, R> NullSafeTransformer<T, R> from(NullSafe<T> value) {
        return new NullSafeTransformer<>(value);
    }
    
    /**
     * Creates a transformer from a value using function.
     * 
     * @param <T> the input type
     * @param <R> the output type
     * @param value the input value
     * @param transformer the transformation function
     * @return the transformer
     */
    public static <T, R> NullSafeTransformer<T, R> from(NullSafe<T> value, Function<T, R> transformer) {
        NullSafeTransformer<T, R> t = new NullSafeTransformer<>(value);
        return t.map(transformer);
    }
    
    /**
     * Applies a transformation function.
     * 
     * @param <R2> the next output type
     * @param transformer the transformation function
     * @return new transformer with applied transformation
     */
    public <R2> NullSafeTransformer<R, R2> map(Function<R, R2> transformer) {
        return new NullSafeTransformer<>(input, new FunctionTransformation<>(transformer));
    }
    
    /**
     * Applies a transformation that might return null.
     * 
     * @param <R2> the next output type
     * @param transformer the transformation function
     * @return new transformer with applied transformation
     */
    public <R2> NullSafeTransformer<R, R2> mapSafe(Function<R, R2> transformer) {
        return new NullSafeTransformer<>(input, new SafeFunctionTransformation<>(transformer));
    }
    
    /**
     * Applies a transformation with validation.
     * 
     * @param <R2> the next output type
     * @param transformer the transformation function
     * @param validator the validation function
     * @return new transformer with applied transformation
     */
    public <R2> NullSafeTransformer<R, R2> mapWithValidation(
            Function<R, R2> transformer, 
            Predicate<R2> validator) {
        return new NullSafeTransformer<>(input, new ValidatingTransformation<>(transformer, validator));
    }
    
    /**
     * Applies a conditional transformation.
     * 
     * @param <R2> the next output type
     * @param condition the condition to check
     * @param trueTransformer transformer for true case
     * @param falseTransformer transformer for false case
     * @return new transformer with applied conditional transformation
     */
    public <R2> NullSafeTransformer<R, R2> mapConditional(
            Predicate<R> condition,
            Function<R, R2> trueTransformer,
            Function<R, R2> falseTransformer) {
        return new NullSafeTransformer<>(input, new ConditionalTransformation<>(condition, trueTransformer, falseTransformer));
    }
    
    /**
     * Applies a transformation with fallback.
     * 
     * @param <R2> the next output type
     * @param transformer the transformation function
     * @param fallback the fallback value
     * @return new transformer with applied transformation
     */
    public <R2> NullSafeTransformer<R, R2> mapWithFallback(Function<R, R2> transformer, R2 fallback) {
        return new NullSafeTransformer<>(input, new FallbackTransformation<>(transformer, fallback));
    }
    
    /**
     * Applies a transformation with error handling.
     * 
     * @param <R2> the next output type
     * @param transformer the transformation function
     * @param errorHandler the error handler
     * @return new transformer with applied transformation
     */
    public <R2> NullSafeTransformer<R, R2> mapWithErrorHandling(
            Function<R, R2> transformer,
            Function<Exception, R2> errorHandler) {
        return new NullSafeTransformer<>(input, new ErrorHandlingTransformation<>(transformer, errorHandler));
    }
    
    /**
     * Filters elements based on a predicate.
     * 
     * @param predicate the filter predicate
     * @return new transformer with filter applied
     */
    public NullSafeTransformer<R, R> filter(Predicate<R> predicate) {
        return new NullSafeTransformer<>(input, new FilterTransformation<>(predicate));
    }
    
    /**
     * Applies a transformation that transforms collections.
     * 
     * @param <C> the collection type
     * @param <E> the element type
     * @param <R2> the next output type
     * @param collectionTransformer the collection transformation function
     * @return new transformer with collection transformation applied
     */
    public <C extends Collection<R>, E, R2> NullSafeTransformer<C, R2> flatMapCollection(
            Function<R, C> collectionTransformer,
            Function<E, R2> elementTransformer) {
        return new NullSafeTransformer<>(input, new FlatMapCollectionTransformation<>(collectionTransformer, elementTransformer));
    }
    
    /**
     * Groups elements by a key function.
     * 
     * @param <K> the key type
     * @param keyMapper the key mapping function
     * @return new transformer with grouping applied
     */
    public <K> NullSafeTransformer<R, Map<K, List<R>>> groupBy(Function<R, K> keyMapper) {
        return new NullSafeTransformer<>(input, new GroupByTransformation<>(keyMapper));
    }
    
    /**
     * Reduces elements to a single value.
     * 
     * @param <R2> the result type
     * @param reducer the reduction function
     * @return new transformer with reduction applied
     */
    public <R2> NullSafeTransformer<R, R2> reduce(R2 identity, BiFunction<R2, R, R2> reducer) {
        return new NullSafeTransformer<>(input, new ReduceTransformation<>(identity, reducer));
    }
    
    /**
     * Applies a pipeline of transformations.
     * 
     * @param <R2> the final result type
     * @param transformers the transformer functions
     * @return new transformer with pipeline applied
     */
    @SafeVarargs
    public final <R2> NullSafeTransformer<R, R2> pipeline(Function<R, R2>... transformers) {
        NullSafeTransformer<R, R2> current = this;
        for (Function<R, R2> transformer : transformers) {
            current = current.map(transformer);
        }
        return current;
    }
    
    /**
     * Executes the transformation.
     * 
     * @return the transformed result
     */
    public NullSafe<R> transform() {
        if (input.isEmpty()) {
            return NullSafe.empty();
        }
        
        Object currentValue = input.get();
        for (TransformationStep<R> step : steps) {
            try {
                if (currentValue == null) {
                    return NullSafe.empty();
                }
                @SuppressWarnings("unchecked")
                R result = (R) step.apply(currentValue);
                currentValue = result;
                
                if (result == null && !step.allowsNull()) {
                    return NullSafe.empty();
                }
            } catch (Exception e) {
                return NullSafe.empty();
            }
        }
        
        @SuppressWarnings("unchecked")
        R finalResult = (R) currentValue;
        return NullSafe.of(finalResult);
    }
    
    private NullSafeTransformer(NullSafe<T> input, TransformationStep<R> initialStep) {
        this.input = input;
        this.steps = new ArrayList<>();
        this.steps.add(initialStep);
    }
    
    /**
     * Represents a transformation step.
     * 
     * @param <R> the output type
     */
    private interface TransformationStep<R> {
        Object apply(Object input);
        boolean allowsNull();
    }
    
    /**
     * Simple function transformation.
     */
    private record FunctionTransformation<R>(Function<R, R> transformer) implements TransformationStep<R> {
        @Override
        public Object apply(Object input) {
            return transformer.apply((R) input);
        }
        
        @Override
        public boolean allowsNull() {
            return false;
        }
    }
    
    /**
     * Safe function transformation that handles null inputs.
     */
    private record SafeFunctionTransformation<R>(Function<R, R> transformer) implements TransformationStep<R> {
        @Override
        public Object apply(Object input) {
            if (input == null) {
                return null;
            }
            return transformer.apply((R) input);
        }
        
        @Override
        public boolean allowsNull() {
            return true;
        }
    }
    
    /**
     * Validating transformation.
     */
    private record ValidatingTransformation<R>(Function<R, R> transformer, Predicate<R> validator) implements TransformationStep<R> {
        @Override
        public Object apply(Object input) {
            R transformed = transformer.apply((R) input);
            if (!validator.test(transformed)) {
                throw new ValidationException("Validation failed after transformation");
            }
            return transformed;
        }
        
        @Override
        public boolean allowsNull() {
            return false;
        }
    }
    
    /**
     * Conditional transformation.
     */
    private record ConditionalTransformation<R>(Predicate<R> condition, 
                                               Function<R, R> trueTransformer, 
                                               Function<R, R> falseTransformer) implements TransformationStep<R> {
        @Override
        public Object apply(Object input) {
            R value = (R) input;
            return condition.test(value) ? trueTransformer.apply(value) : falseTransformer.apply(value);
        }
        
        @Override
        public boolean allowsNull() {
            return false;
        }
    }
    
    /**
     * Fallback transformation.
     */
    private record FallbackTransformation<R>(Function<R, R> transformer, R fallback) implements TransformationStep<R> {
        @Override
        public Object apply(Object input) {
            try {
                return transformer.apply((R) input);
            } catch (Exception e) {
                return fallback;
            }
        }
        
        @Override
        public boolean allowsNull() {
            return false;
        }
    }
    
    /**
     * Error handling transformation.
     */
    private record ErrorHandlingTransformation<R>(Function<R, R> transformer, 
                                                  Function<Exception, R> errorHandler) implements TransformationStep<R> {
        @Override
        public Object apply(Object input) {
            try {
                return transformer.apply((R) input);
            } catch (Exception e) {
                return errorHandler.apply(e);
            }
        }
        
        @Override
        public boolean allowsNull() {
            return false;
        }
    }
    
    /**
     * Filter transformation.
     */
    private record FilterTransformation<R>(Predicate<R> predicate) implements TransformationStep<R> {
        @Override
        public Object apply(Object input) {
            R value = (R) input;
            if (!predicate.test(value)) {
                throw new FilterException("Value does not match filter condition");
            }
            return value;
        }
        
        @Override
        public boolean allowsNull() {
            return false;
        }
    }
    
    /**
     * Collection flat map transformation.
     */
    private record FlatMapCollectionTransformation<R, E>(Function<R, Collection<E>> collectionTransformer, 
                                                        Function<E, R> elementTransformer) implements TransformationStep<R> {
        @Override
        public Object apply(Object input) {
            R value = (R) input;
            Collection<E> collection = collectionTransformer.apply(value);
            
            List<R> results = collection.stream()
                .map(elementTransformer)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            
            return results.isEmpty() ? null : results.get(0);
        }
        
        @Override
        public boolean allowsNull() {
            return true;
        }
    }
    
    /**
     * Group by transformation.
     */
    private record GroupByTransformation<R, K>(Function<R, K> keyMapper) implements TransformationStep<R> {
        @Override
        public Object apply(Object input) {
            R value = (R) input;
            K key = keyMapper.apply(value);
            Map<K, List<R>> group = new HashMap<>();
            group.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            return group;
        }
        
        @Override
        public boolean allowsNull() {
            return false;
        }
    }
    
    /**
     * Reduce transformation.
     */
    private record ReduceTransformation<R, R2>(R2 identity, BiFunction<R2, R, R2> reducer) implements TransformationStep<R> {
        @Override
        public Object apply(Object input) {
            return reducer.apply(identity, (R) input);
        }
        
        @Override
        public boolean allowsNull() {
            return false;
        }
    }
    
    /**
     * Exception for validation failures.
     */
    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
    
    /**
     * Exception for filter failures.
     */
    public static class FilterException extends RuntimeException {
        public FilterException(String message) {
            super(message);
        }
    }
}