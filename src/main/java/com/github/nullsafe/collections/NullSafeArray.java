/**
 * A specialized NullSafe implementation for Array operations.
 * Provides safe operations for arrays that may contain null values or be null themselves.
 * 
 * @param <T> The type of elements in the array
 * @since 1.0
 */
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import com.github.nullsafe.NullSafe;
import com.github.nullsafe.collections.*;

public class NullSafeArray<T> {
    private final T[] array;
    
    /**
     * Creates a NullSafeArray from individual elements.
     * 
     * @param elements the elements to include in the array
     * @return NullSafeArray instance
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> NullSafeArray<T> of(T... elements) {
        if (elements == null) {
            return new NullSafeArray<>(Arrays.copyOf(elements, 0, (Class<T[]>) elements.getClass()));
        }
        
        T[] filtered = Arrays.stream(elements)
            .filter(Objects::nonNull)
            .toArray(len -> Arrays.copyOf(elements, len, (Class<T[]>) elements.getClass()));
        
        return new NullSafeArray<>(filtered);
    }
    
    /**
     * Creates a NullSafeArray from a collection.
     * 
     * @param collection the source collection
     * @return NullSafeArray instance
     */
    @SuppressWarnings("unchecked")
    public static <T> NullSafeArray<T> of(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return new NullSafeArray<>(Arrays.copyOf(new Object[0], 0, (Class<T[]>) Object[].class));
        }
        
        T[] array = collection.stream()
            .filter(Objects::nonNull)
            .toArray(len -> Arrays.copyOf(new Object[len], len, (Class<T[]>) Object[].class));
        
        return new NullSafeArray<>(array);
    }
    
    private NullSafeArray(T[] array) {
        this.array = Arrays.copyOf(array, array.length);
    }
    
    /**
     * Returns the length of the array.
     * 
     * @return the number of elements
     */
    public int length() {
        return array.length;
    }
    
    /**
     * Checks if the array is empty.
     * 
     * @return true if the array has no elements
     */
    public boolean isEmpty() {
        return array.length == 0;
    }
    
    /**
     * Gets an element at the specified index safely.
     * Returns an empty NullSafe if the index is out of bounds.
     * 
     * @param index the index
     * @return NullSafe containing the element or empty
     */
    public NullSafe<T> get(int index) {
        if (index >= 0 && index < array.length) {
            return NullSafe.of(array[index]);
        }
        return NullSafe.empty();
    }
    
    /**
     * Sets an element at the specified index.
     * 
     * @param index the index
     * @param element the element to set
     * @return new NullSafeArray with the element set
     */
    public NullSafeArray<T> set(int index, T element) {
        if (index < 0 || index >= array.length) {
            return this;
        }
        
        T[] newArray = Arrays.copyOf(array, array.length);
        newArray[index] = element;
        return new NullSafeArray<>(newArray);
    }
    
    /**
     * Filters the array and returns elements that match the predicate.
     * 
     * @param predicate the filter condition
     * @return new NullSafeArray with filtered elements
     */
    public NullSafeArray<T> filter(Predicate<T> predicate) {
        T[] filtered = Arrays.stream(array)
            .filter(predicate)
            .toArray(len -> Arrays.copyOf(array, len, array.getClass()));
        
        return new NullSafeArray<>(filtered);
    }
    
    /**
     * Maps each element to a new type.
     * 
     * @param <R> the result type
     * @param mapper the transformation function
     * @return new NullSafeArray with mapped elements
     */
    public <R> NullSafeArray<R> map(Function<T, R> mapper) {
        R[] mapped = Arrays.stream(array)
            .map(mapper)
            .filter(Objects::nonNull)
            .toArray(len -> Arrays.copyOf(new Object[len], len, (Class<R[]>) Object[].class));
        
        return new NullSafeArray<>(mapped);
    }
    
    /**
     * Performs flatMap operation on the array.
     * 
     * @param <R> the result type
     * @param mapper the flatMap function
     * @return new NullSafeArray with flatMapped elements
     */
    public <R> NullSafeArray<R> flatMap(Function<T, Collection<R>> mapper) {
        List<R> flattened = Arrays.stream(array)
            .flatMap(t -> mapper.apply(t).stream())
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        
        @SuppressWarnings("unchecked")
        R[] result = flattened.toArray(len -> Arrays.copyOf(new Object[len], len, (Class<R[]>) Object[].class));
        
        return new NullSafeArray<>(result);
    }
    
    /**
     * Finds the first element matching the predicate.
     * 
     * @param predicate the search condition
     * @return NullSafe containing the found element or empty
     */
    public NullSafe<T> find(Predicate<T> predicate) {
        return NullSafe.of(Arrays.stream(array)
            .filter(predicate)
            .findFirst()
            .orElse(null));
    }
    
    /**
     * Checks if any element matches the predicate.
     * 
     * @param predicate the condition to check
     * @return true if any element matches
     */
    public boolean anyMatch(Predicate<T> predicate) {
        return Arrays.stream(array).anyMatch(predicate);
    }
    
    /**
     * Checks if all elements match the predicate.
     * 
     * @param predicate the condition to check
     * @return true if all elements match
     */
    public boolean allMatch(Predicate<T> predicate) {
        return Arrays.stream(array).allMatch(predicate);
    }
    
    /**
     * Sorts the array using the provided comparator.
     * 
     * @param comparator the comparator to use for sorting
     * @return new NullSafeArray with sorted elements
     */
    public NullSafeArray<T> sorted(Comparator<T> comparator) {
        T[] sorted = Arrays.copyOf(array, array.length);
        Arrays.sort(sorted, comparator);
        return new NullSafeArray<>(sorted);
    }
    
    /**
     * Reduces the array to a single value using the accumulator.
     * 
     * @param identity the identity value
     * @param accumulator the reduction function
     * @return the reduced value
     */
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return Arrays.stream(array).reduce(identity, accumulator);
    }
    
    /**
     * Groups elements by a classifier function.
     * 
     * @param <K> the key type
     * @param classifier the grouping function
     * @return Map of grouped elements
     */
    public <K> Map<K, List<T>> groupBy(Function<T, K> classifier) {
        return Arrays.stream(array)
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(classifier));
    }
    
    /**
     * Gets the underlying array.
     * 
     * @return the array
     */
    public T[] toArray() {
        return Arrays.copyOf(array, array.length);
    }
    
    /**
     * Converts to a NullSafeList.
     * 
     * @return NullSafeList representation
     */
    public NullSafeList<T> toList() {
        return NullSafeList.of(Arrays.asList(array));
    }
    
    /**
     * Converts to a NullSafeSet.
     * 
     * @return NullSafeSet representation
     */
    public NullSafeSet<T> toSet() {
        return NullSafeSet.of(Arrays.asList(array));
    }
    
    /**
     * Returns all elements as a stream.
     * 
     * @return stream of elements
     */
    public Stream<T> stream() {
        return Arrays.stream(array);
    }
    
    /**
     * Concatenates this array with another array.
     * 
     * @param other the other array
     * @return new NullSafeArray with concatenated elements
     */
    @SuppressWarnings("unchecked")
    public NullSafeArray<T> concat(NullSafeArray<T> other) {
        if (other == null || other.array.length == 0) {
            return this;
        }
        
        T[] concatenated = Arrays.copyOf(array, array.length + other.array.length);
        System.arraycopy(other.array, 0, concatenated, array.length, other.array.length);
        
        return new NullSafeArray<>(concatenated);
    }
    
    /**
     * Gets a subarray from the specified range.
     * 
     * @param fromIndex the starting index (inclusive)
     * @param toIndex the ending index (exclusive)
     * @return new NullSafeArray with the subarray
     */
    public NullSafeArray<T> subArray(int fromIndex, int toIndex) {
        if (fromIndex < 0) fromIndex = 0;
        if (toIndex > array.length) toIndex = array.length;
        if (fromIndex > toIndex) {
            T[] empty = Arrays.copyOf(array, 0, array.getClass());
            return new NullSafeArray<>(empty);
        }
        
        T[] subarray = Arrays.copyOfRange(array, fromIndex, toIndex);
        return new NullSafeArray<>(subarray);
    }
    
    @Override
    public String toString() {
        return Arrays.toString(array);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NullSafeArray<?> that = (NullSafeArray<?>) obj;
        return Arrays.equals(array, that.array);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }
}