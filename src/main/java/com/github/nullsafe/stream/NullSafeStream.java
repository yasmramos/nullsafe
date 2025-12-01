package com.github.nullsafe.stream;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import com.github.nullsafe.NullSafe;
import com.github.nullsafe.collections.NullSafeList;
import com.github.nullsafe.collections.NullSafeSet;
import com.github.nullsafe.collections.NullSafeMap;

public final class NullSafeStream {
    
    private NullSafeStream() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Creates a NullSafe stream from an existing stream.
     * Filters out null values automatically.
     * 
     * @param <T> the element type
     * @param stream the source stream
     * @return NullSafe-enhanced stream
     */
    public static <T> NullSafeStreamOperation<T> of(Stream<T> stream) {
        if (stream == null) {
            return new NullSafeStreamOperation<>(Stream.empty());
        }
        return new NullSafeStreamOperation<>(stream.filter(Objects::nonNull));
    }
    
    /**
     * Creates a NullSafe stream from a collection.
     * 
     * @param <T> the element type
     * @param collection the source collection
     * @return NullSafe-enhanced stream
     */
    public static <T> NullSafeStreamOperation<T> of(Collection<T> collection) {
        if (collection == null) {
            return new NullSafeStreamOperation<>(Stream.empty());
        }
        return new NullSafeStreamOperation<>(collection.stream().filter(Objects::nonNull));
    }
    
    /**
     * Creates a NullSafe stream from an array.
     * 
     * @param <T> the element type
     * @param array the source array
     * @return NullSafe-enhanced stream
     */
    @SafeVarargs
    public static <T> NullSafeStreamOperation<T> of(T... array) {
        if (array == null || array.length == 0) {
            return new NullSafeStreamOperation<>(Stream.empty());
        }
        return new NullSafeStreamOperation<>(Arrays.stream(array).filter(Objects::nonNull));
    }
    
    /**
     * Generates a null-safe infinite stream.
     * 
     * @param <T> the element type
     * @param supplier the supplier function
     * @return NullSafe-enhanced stream
     */
    public static <T> NullSafeStreamOperation<T> generate(Supplier<T> supplier) {
        return new NullSafeStreamOperation<>(Stream.generate(supplier).filter(Objects::nonNull));
    }
    
    /**
     * Creates a null-safe stream by iterating.
     * 
     * @param <T> the element type
     * @param seed the initial value
     * @param next the next value function
     * @return NullSafe-enhanced stream
     */
    public static <T> NullSafeStreamOperation<T> iterate(T seed, UnaryOperator<T> next) {
        return new NullSafeStreamOperation<>(Stream.iterate(seed, next).filter(Objects::nonNull));
    }
    
    /**
     * Creates a null-safe stream from a range.
     * 
     * @param startInclusive the start value (inclusive)
     * @param endExclusive the end value (exclusive)
     * @return IntStream operation
     */
    public static NullSafeIntStreamOperation range(int startInclusive, int endExclusive) {
        return new NullSafeIntStreamOperation(IntStream.range(startInclusive, endExclusive));
    }
    
    /**
     * Creates a null-safe stream from a range.
     * 
     * @param startInclusive the start value (inclusive)
     * @param endExclusive the end value (exclusive)
     * @return LongStream operation
     */
    public static NullSafeLongStreamOperation rangeLong(long startInclusive, long endExclusive) {
        return new NullSafeLongStreamOperation(LongStream.range(startInclusive, endExclusive));
    }
    
    /**
     * Filters the stream safely.
     * 
     * @param <T> the element type
     * @param predicate the filter predicate
     * @return filtered stream operation
     */
    public static <T> NullSafeStreamOperation<T> filter(Stream<T> stream, Predicate<T> predicate) {
        if (stream == null) {
            return new NullSafeStreamOperation<>(Stream.empty());
        }
        return new NullSafeStreamOperation<>(stream.filter(Objects::nonNull).filter(predicate));
    }
    
    /**
     * Maps the stream safely.
     * 
     * @param <T> the source element type
     * @param <R> the result element type
     * @param mapper the mapping function
     * @return mapped stream operation
     */
    public static <T, R> NullSafeStreamOperation<R> map(Stream<T> stream, Function<T, R> mapper) {
        if (stream == null) {
            return new NullSafeStreamOperation<>(Stream.empty());
        }
        return new NullSafeStreamOperation<>(stream
            .filter(Objects::nonNull)
            .map(mapper)
            .filter(Objects::nonNull));
    }
    
    /**
     * Flat maps the stream safely.
     * 
     * @param <T> the source element type
     * @param <R> the result element type
     * @param mapper the flat map function
     * @return flat mapped stream operation
     */
    public static <T, R> NullSafeStreamOperation<R> flatMap(Stream<T> stream, 
                                                           Function<T, Collection<R>> mapper) {
        if (stream == null) {
            return new NullSafeStreamOperation<>(Stream.empty());
        }
        return new NullSafeStreamOperation<>(stream
            .filter(Objects::nonNull)
            .flatMap(t -> mapper.apply(t).stream())
            .filter(Objects::nonNull));
    }
    
    /**
     * Reduces the stream to a single value safely.
     * 
     * @param <T> the element type
     * @param stream the source stream
     * @param identity the identity value
     * @param accumulator the accumulator function
     * @return reduced value wrapped in NullSafe
     */
    public static <T> NullSafe<T> reduce(Stream<T> stream, T identity, BinaryOperator<T> accumulator) {
        if (stream == null) {
            return NullSafe.empty();
        }
        T result = stream.filter(Objects::nonNull).reduce(identity, accumulator);
        return NullSafe.of(result);
    }
    
    /**
     * Collects the stream to a NullSafeList.
     * 
     * @param <T> the element type
     * @param stream the source stream
     * @return NullSafeList instance
     */
    public static <T> NullSafeList<T> toList(Stream<T> stream) {
        if (stream == null) {
            return NullSafeList.empty();
        }
        List<T> list = stream.filter(Objects::nonNull).collect(Collectors.toList());
        return NullSafeList.of(list);
    }
    
    /**
     * Collects the stream to a NullSafeSet.
     * 
     * @param <T> the element type
     * @param stream the source stream
     * @return NullSafeSet instance
     */
    public static <T> NullSafeSet<T> toSet(Stream<T> stream) {
        if (stream == null) {
            return NullSafeSet.empty();
        }
        Set<T> set = stream.filter(Objects::nonNull).collect(Collectors.toSet());
        return NullSafeSet.of(set);
    }
    
    /**
     * Collects the stream to a NullSafeMap.
     * 
     * @param <T> the element type
     * @param <K> the key type
     * @param <V> the value type
     * @param stream the source stream
     * @param keyMapper the key mapping function
     * @param valueMapper the value mapping function
     * @return NullSafeMap instance
     */
    public static <T, K, V> NullSafeMap<K, V> toMap(Stream<T> stream, 
                                                    Function<T, K> keyMapper, 
                                                    Function<T, V> valueMapper) {
        if (stream == null) {
            return NullSafeMap.empty();
        }
        Map<K, V> map = stream.filter(Objects::nonNull)
            .collect(Collectors.toMap(keyMapper, valueMapper));
        return NullSafeMap.of(map);
    }
    
    /**
     * Groups elements safely.
     * 
     * @param <T> the element type
     * @param <K> the key type
     * @param stream the source stream
     * @param classifier the grouping function
     * @return Map of NullSafeList groups
     */
    public static <T, K> Map<K, NullSafeList<T>> groupBy(Stream<T> stream, 
                                                         Function<T, K> classifier) {
        if (stream == null) {
            return Collections.emptyMap();
        }
        
        return stream.filter(Objects::nonNull)
            .collect(Collectors.groupingBy(
                classifier,
                LinkedHashMap::new,
                Collectors.collectingAndThen(Collectors.toList(), NullSafeList::of)
            ));
    }
}