/**
 * Enhanced LongStream operations for NullSafe values.
 * 
 * @since 1.0
 */
import java.util.stream.*;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.OptionalDouble;
import java.util.function.*;
import com.github.nullsafe.stream.*;

public class NullSafeLongStreamOperation {
    private final LongStream stream;
    
    /**
     * Creates a new NullSafeLongStreamOperation.
     * 
     * @param stream the underlying LongStream
     */
    public NullSafeLongStreamOperation(LongStream stream) {
        this.stream = stream;
    }
    
    /**
     * Filters elements using the predicate.
     * 
     * @param predicate the filter predicate
     * @return this for chaining
     */
    public NullSafeLongStreamOperation filter(LongPredicate predicate) {
        return new NullSafeLongStreamOperation(stream.filter(predicate));
    }
    
    /**
     * Maps longs using the mapper function.
     * 
     * @param mapper the mapping function
     * @return new NullSafeStreamOperation with mapped elements
     */
    public NullSafeStreamOperation<Integer> mapToInt(LongToIntFunction mapper) {
        return new NullSafeStreamOperation<>(stream.mapToInt(mapper).boxed().filter(Objects::nonNull));
    }
    
    /**
     * Maps longs using the mapper function.
     * 
     * @param mapper the mapping function
     * @return new NullSafeStreamOperation with mapped elements
     */
    public NullSafeStreamOperation<Double> mapToDouble(LongToDoubleFunction mapper) {
        return new NullSafeStreamOperation<>(stream.mapToDouble(mapper).boxed().filter(Objects::nonNull));
    }
    
    /**
     * Maps longs using the mapper function.
     * 
     * @param <R> the result type
     * @param mapper the mapping function
     * @return new NullSafeStreamOperation with mapped elements
     */
    public <R> NullSafeStreamOperation<R> mapToObj(LongFunction<R> mapper) {
        return new NullSafeStreamOperation<>(stream.mapToObj(mapper).filter(Objects::nonNull));
    }
    
    /**
     * Limits the stream to the specified number of elements.
     * 
     * @param maxSize the maximum number of elements
     * @return new NullSafeLongStreamOperation
     */
    public NullSafeLongStreamOperation limit(long maxSize) {
        return new NullSafeLongStreamOperation(stream.limit(maxSize));
    }
    
    /**
     * Skips the specified number of elements.
     * 
     * @param n the number of elements to skip
     * @return new NullSafeLongStreamOperation
     */
    public NullSafeLongStreamOperation skip(long n) {
        return new NullSafeLongStreamOperation(stream.skip(n));
    }
    
    /**
     * Sorts the elements.
     * 
     * @return new NullSafeLongStreamOperation
     */
    public NullSafeLongStreamOperation sorted() {
        return new NullSafeLongStreamOperation(stream.sorted());
    }
    
    /**
     * Finds the maximum element.
     * 
     * @return OptionalLong containing the maximum or empty
     */
    public OptionalLong max() {
        return stream.max();
    }
    
    /**
     * Finds the minimum element.
     * 
     * @return OptionalLong containing the minimum or empty
     */
    public OptionalLong min() {
        return stream.min();
    }
    
    /**
     * Checks if any element matches the predicate.
     * 
     * @param predicate the condition to check
     * @return true if any element matches
     */
    public boolean anyMatch(LongPredicate predicate) {
        return stream.anyMatch(predicate);
    }
    
    /**
     * Checks if all elements match the predicate.
     * 
     * @param predicate the condition to check
     * @return true if all elements match
     */
    public boolean allMatch(LongPredicate predicate) {
        return stream.allMatch(predicate);
    }
    
    /**
     * Checks if no elements match the predicate.
     * 
     * @param predicate the condition to check
     * @return true if no elements match
     */
    public boolean noneMatch(LongPredicate predicate) {
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
    public long reduce(long identity, LongBinaryOperator accumulator) {
        return stream.reduce(identity, accumulator);
    }
    
    /**
     * Reduces the stream to a single value without identity.
     * 
     * @param accumulator the accumulator function
     * @return OptionalLong containing the reduced value or empty
     */
    public OptionalLong reduce(LongBinaryOperator accumulator) {
        return stream.reduce(accumulator);
    }
    
    /**
     * Sums all elements.
     * 
     * @return the sum of all elements
     */
    public long sum() {
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
                        ObjLongConsumer<R> accumulator,
                        BinaryOperator<R> combiner) {
        return stream.collect(supplier, accumulator, combiner);
    }
    
    /**
     * Converts to a stream of Longs.
     * 
     * @return NullSafeStreamOperation of Longs
     */
    public NullSafeStreamOperation<Long> boxed() {
        return new NullSafeStreamOperation<>(stream.boxed().filter(Objects::nonNull));
    }
    
    /**
     * Converts to a parallel stream operation.
     * 
     * @return parallel stream operation
     */
    public NullSafeLongStreamOperation parallel() {
        return new NullSafeLongStreamOperation(stream.parallel());
    }
    
    /**
     * Converts to a sequential stream operation.
     * 
     * @return sequential stream operation
     */
    public NullSafeLongStreamOperation sequential() {
        return new NullSafeLongStreamOperation(stream.sequential());
    }
    
    /**
     * Converts to NullSafeList.
     * 
     * @return NullSafeList of Longs
     */
    public NullSafeList<Long> toList() {
        List<Long> list = stream.collect(Collectors.toList());
        return NullSafeList.of(list);
    }
}