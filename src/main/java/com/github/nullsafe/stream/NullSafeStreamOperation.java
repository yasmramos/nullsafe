/**
 * Enhanced stream operations for NullSafe values.
 * Provides fluent API for stream operations with null-safety.
 * 
 * @param <T> the element type
 * @since 1.0
 */
import java.util.stream.*;
import java.util.*;
import java.util.function.*;
import com.github.nullsafe.NullSafe;
import com.github.nullsafe.stream.*;

public class NullSafeStreamOperation<T> {
    private final Stream<T> stream;
    
    /**
     * Creates a new NullSafeStreamOperation.
     * 
     * @param stream the underlying stream
     */
    public NullSafeStreamOperation(Stream<T> stream) {
        this.stream = stream;
    }
    
    /**
     * Filters elements using the predicate.
     * 
     * @param predicate the filter predicate
     * @return this for chaining
     */
    public NullSafeStreamOperation<T> filter(Predicate<T> predicate) {
        return new NullSafeStreamOperation<>(stream.filter(predicate));
    }
    
    /**
     * Maps elements using the mapper function.
     * 
     * @param <R> the result type
     * @param mapper the mapping function
     * @return new NullSafeStreamOperation with mapped elements
     */
    public <R> NullSafeStreamOperation<R> map(Function<T, R> mapper) {
        return new NullSafeStreamOperation<>(stream
            .map(mapper)
            .filter(Objects::nonNull));
    }
    
    /**
     * Maps elements using a NullSafe mapper that returns Optional or NullSafe.
     * 
     * @param <R> the result type
     * @param mapper the mapping function that returns NullSafe or Optional
     * @return new NullSafeStreamOperation with mapped elements
     */
    public <R> NullSafeStreamOperation<R> mapNullSafe(Function<T, NullSafe<R>> mapper) {
        return new NullSafeStreamOperation<>(stream
            .map(t -> mapper.apply(t).orElse(null))
            .filter(Objects::nonNull));
    }
    
    /**
     * Flat maps elements using the mapper function.
     * 
     * @param <R> the result element type
     * @param mapper the flat map function
     * @return new NullSafeStreamOperation with flat mapped elements
     */
    public <R> NullSafeStreamOperation<R> flatMap(Function<T, Collection<R>> mapper) {
        return new NullSafeStreamOperation<>(stream
            .flatMap(t -> mapper.apply(t).stream())
            .filter(Objects::nonNull));
    }
    
    /**
     * Flat maps elements using a NullSafe mapper.
     * 
     * @param <R> the result element type
     * @param mapper the flat map function
     * @return new NullSafeStreamOperation with flat mapped elements
     */
    public <R> NullSafeStreamOperation<R> flatMapNullSafe(Function<T, NullSafe<Collection<R>>> mapper) {
        return new NullSafeStreamOperation<>(stream
            .flatMap(t -> mapper.apply(t).orElse(Collections.emptyList()).stream())
            .filter(Objects::nonNull));
    }
    
    /**
     * Filters and maps in a single operation.
     * 
     * @param <R> the result type
     * @param predicate the filter predicate
     * @param mapper the mapping function
     * @return new NullSafeStreamOperation with filtered and mapped elements
     */
    public <R> NullSafeStreamOperation<R> filterMap(Predicate<T> predicate, Function<T, R> mapper) {
        return new NullSafeStreamOperation<>(stream
            .filter(predicate)
            .map(mapper)
            .filter(Objects::nonNull));
    }
    
    /**
     * Finds the first element matching the predicate.
     * 
     * @param predicate the search predicate
     * @return NullSafe containing the found element or empty
     */
    public NullSafe<T> find(Predicate<T> predicate) {
        return NullSafe.of(stream.filter(predicate).findFirst().orElse(null));
    }
    
    /**
     * Finds any element.
     * 
     * @return NullSafe containing an element or empty
     */
    public NullSafe<T> findAny() {
        return NullSafe.of(stream.findAny().orElse(null));
    }
    
    /**
     * Finds the maximum element using the comparator.
     * 
     * @param comparator the comparator
     * @return NullSafe containing the maximum element or empty
     */
    public NullSafe<T> max(Comparator<T> comparator) {
        return NullSafe.of(stream.max(comparator).orElse(null));
    }
    
    /**
     * Finds the minimum element using the comparator.
     * 
     * @param comparator the comparator
     * @return NullSafe containing the minimum element or empty
     */
    public NullSafe<T> min(Comparator<T> comparator) {
        return NullSafe.of(stream.min(comparator).orElse(null));
    }
    
    /**
     * Checks if any element matches the predicate.
     * 
     * @param predicate the condition to check
     * @return true if any element matches
     */
    public boolean anyMatch(Predicate<T> predicate) {
        return stream.anyMatch(predicate);
    }
    
    /**
     * Checks if all elements match the predicate.
     * 
     * @param predicate the condition to check
     * @return true if all elements match
     */
    public boolean allMatch(Predicate<T> predicate) {
        return stream.allMatch(predicate);
    }
    
    /**
     * Checks if no elements match the predicate.
     * 
     * @param predicate the condition to check
     * @return true if no elements match
     */
    public boolean noneMatch(Predicate<T> predicate) {
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
     * Reduces the stream to a single value using the accumulator.
     * 
     * @param identity the identity value
     * @param accumulator the accumulator function
     * @return the reduced value wrapped in NullSafe
     */
    public NullSafe<T> reduce(T identity, BinaryOperator<T> accumulator) {
        return NullSafe.of(stream.reduce(identity, accumulator));
    }
    
    /**
     * Reduces the stream to a single value without identity.
     * 
     * @param accumulator the accumulator function
     * @return the reduced value wrapped in NullSafe
     */
    public NullSafe<T> reduce(BinaryOperator<T> accumulator) {
        return NullSafe.of(stream.reduce(accumulator).orElse(null));
    }
    
    /**
     * Collects to a NullSafeList.
     * 
     * @return NullSafeList instance
     */
    public NullSafeList<T> toList() {
        List<T> list = stream.collect(Collectors.toList());
        return NullSafeList.of(list);
    }
    
    /**
     * Collects to a NullSafeSet.
     * 
     * @return NullSafeSet instance
     */
    public NullSafeSet<T> toSet() {
        Set<T> set = stream.collect(Collectors.toSet());
        return NullSafeSet.of(set);
    }
    
    /**
     * Groups elements by the classifier function.
     * 
     * @param <K> the key type
     * @param classifier the grouping function
     * @return Map of NullSafeList groups
     */
    public <K> Map<K, NullSafeList<T>> groupBy(Function<T, K> classifier) {
        return stream.collect(Collectors.groupingBy(
            classifier,
            LinkedHashMap::new,
            Collectors.collectingAndThen(Collectors.toList(), NullSafeList::of)
        ));
    }
    
    /**
     * Partitions elements by the predicate.
     * 
     * @param predicate the partitioning predicate
     * @return Map with "true" and "false" keys containing partitioned elements
     */
    public Map<Boolean, NullSafeList<T>> partition(Predicate<T> predicate) {
        return stream.collect(Collectors.partitioningBy(
            predicate,
            Collectors.collectingAndThen(Collectors.toList(), NullSafeList::of)
        ));
    }
    
    /**
     * Collects using the provided collector.
     * 
     * @param <R> the result type
     * @param collector the collector
     * @return the collected result
     */
    public <R> R collect(Collector<T, ?, R> collector) {
        return stream.collect(collector);
    }
    
    /**
     * Returns the underlying stream.
     * 
     * @return the stream
     */
    public Stream<T> stream() {
        return stream;
    }
    
    /**
     * Converts to a parallel stream operation.
     * 
     * @return parallel stream operation
     */
    public NullSafeStreamOperation<T> parallel() {
        return new NullSafeStreamOperation<>(stream.parallel());
    }
    
    /**
     * Converts to a sequential stream operation.
     * 
     * @return sequential stream operation
     */
    public NullSafeStreamOperation<T> sequential() {
        return new NullSafeStreamOperation<>(stream.sequential());
    }
}