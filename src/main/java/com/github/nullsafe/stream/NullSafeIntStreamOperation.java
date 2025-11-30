/**
 * Enhanced IntStream operations for NullSafe values.
 * 
 * @since 1.0
 */
import java.util.stream.*;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalDouble;
import java.util.function.*;
import com.github.nullsafe.stream.*;

public class NullSafeIntStreamOperation {
    private final IntStream stream;
    
    /**
     * Creates a new NullSafeIntStreamOperation.
     * 
     * @param stream the underlying IntStream
     */
    public NullSafeIntStreamOperation(IntStream stream) {
        this.stream = stream;
    }
    
    /**
     * Filters elements using the predicate.
     * 
     * @param predicate the filter predicate
     * @return this for chaining
     */
    public NullSafeIntStreamOperation filter(IntPredicate predicate) {
        return new NullSafeIntStreamOperation(stream.filter(predicate));
    }
    
    /**
     * Maps integers using the mapper function.
     * 
     * @param mapper the mapping function
     * @return new NullSafeStreamOperation with mapped elements
     */
    public NullSafeStreamOperation<Long> mapToLong(IntToLongFunction mapper) {
        return new NullSafeStreamOperation<>(stream.mapToLong(mapper).boxed().filter(Objects::nonNull));
    }
    
    /**
     * Maps integers using the mapper function.
     * 
     * @param mapper the mapping function
     * @return new NullSafeStreamOperation with mapped elements
     */
    public NullSafeStreamOperation<Double> mapToDouble(IntToDoubleFunction mapper) {
        return new NullSafeStreamOperation<>(stream.mapToDouble(mapper).boxed().filter(Objects::nonNull));
    }
    
    /**
     * Maps integers using the mapper function.
     * 
     * @param <R> the result type
     * @param mapper the mapping function
     * @return new NullSafeStreamOperation with mapped elements
     */
    public <R> NullSafeStreamOperation<R> mapToObj(IntFunction<R> mapper) {
        return new NullSafeStreamOperation<>(stream.mapToObj(mapper).filter(Objects::nonNull));
    }
    
    /**
     * Limits the stream to the specified number of elements.
     * 
     * @param maxSize the maximum number of elements
     * @return new NullSafeIntStreamOperation
     */
    public NullSafeIntStreamOperation limit(long maxSize) {
        return new NullSafeIntStreamOperation(stream.limit(maxSize));
    }
    
    /**
     * Skips the specified number of elements.
     * 
     * @param n the number of elements to skip
     * @return new NullSafeIntStreamOperation
     */
    public NullSafeIntStreamOperation skip(long n) {
        return new NullSafeIntStreamOperation(stream.skip(n));
    }
    
    /**
     * Sorts the elements.
     * 
     * @return new NullSafeIntStreamOperation
     */
    public NullSafeIntStreamOperation sorted() {
        return new NullSafeIntStreamOperation(stream.sorted());
    }
    
    /**
     * Finds the maximum element.
     * 
     * @return OptionalInt containing the maximum or empty
     */
    public OptionalInt max() {
        return stream.max();
    }
    
    /**
     * Finds the minimum element.
     * 
     * @return OptionalInt containing the minimum or empty
     */
    public OptionalInt min() {
        return stream.min();
    }
    
    /**
     * Checks if any element matches the predicate.
     * 
     * @param predicate the condition to check
     * @return true if any element matches
     */
    public boolean anyMatch(IntPredicate predicate) {
        return stream.anyMatch(predicate);
    }
    
    /**
     * Checks if all elements match the predicate.
     * 
     * @param predicate the condition to check
     * @return true if all elements match
     */
    public boolean allMatch(IntPredicate predicate) {
        return stream.allMatch(predicate);
    }
    
    /**
     * Checks if no elements match the predicate.
     * 
     * @param predicate the condition to check
     * @return true if no elements match
     */
    public boolean noneMatch(IntPredicate predicate) {
        return stream.noneMatch(predicate);
    }
    
    /**
     * Counts the elements.
     * 
     * @return the count of elements
     */
    public long count() {
        return stream.count();
    }
    
    /**
     * Reduces the stream to a single value.
     * 
     * @param identity the identity value
     * @param accumulator the accumulator function
     * @return the reduced value
     */
    public int reduce(int identity, IntBinaryOperator accumulator) {
        return stream.reduce(identity, accumulator);
    }
    
    /**
     * Reduces the stream to a single value without identity.
     * 
     * @param accumulator the accumulator function
     * @return OptionalInt containing the reduced value or empty
     */
    public OptionalInt reduce(IntBinaryOperator accumulator) {
        return stream.reduce(accumulator);
    }
    
    /**
     * Sums all elements.
     * 
     * @return the sum of all elements
     */
    public int sum() {
        return stream.sum();
    }
    
    /**
     * Gets the average of all elements.
     * 
     * @return OptionalDouble containing the average or empty
     */
    public OptionalDouble average() {
        return stream.average();
    }
    
    /**
     * Collects to a collection using the collector.
     * 
     * @param <R> the result type
     * @param collector the collector
     * @return the collected result
     */
    public <R> R collect(Supplier<R> supplier, 
                        ObjIntConsumer<R> accumulator,
                        BinaryOperator<R> combiner) {
        return stream.collect(supplier, accumulator, combiner);
    }
    
    /**
     * Converts to a stream of Integers.
     * 
     * @return NullSafeStreamOperation of Integers
     */
    public NullSafeStreamOperation<Integer> boxed() {
        return new NullSafeStreamOperation<>(stream.boxed().filter(Objects::nonNull));
    }
    
    /**
     * Converts to a parallel stream operation.
     * 
     * @return parallel stream operation
     */
    public NullSafeIntStreamOperation parallel() {
        return new NullSafeIntStreamOperation(stream.parallel());
    }
    
    /**
     * Converts to a sequential stream operation.
     * 
     * @return sequential stream operation
     */
    public NullSafeIntStreamOperation sequential() {
        return new NullSafeIntStreamOperation(stream.sequential());
    }
    
    /**
     * Converts to NullSafeList.
     * 
     * @return NullSafeList of Integers
     */
    public NullSafeList<Integer> toList() {
        List<Integer> list = stream.collect(Collectors.toList());
        return NullSafeList.of(list);
    }
}