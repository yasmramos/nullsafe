/**
 * A specialized NullSafe implementation for List collections.
 * Provides safe operations for lists that may contain null values or be null themselves.
 * 
 * @param <T> The type of elements in the list
 * @since 1.0
 */
public class NullSafeList<T> {
    private final List<T> list;
    
    /**
     * Creates a NullSafeList from an existing list.
     * If the list is null, returns an empty NullSafeList.
     * 
     * @param list the source list
     * @return NullSafeList instance
     */
    public static <T> NullSafeList<T> of(List<T> list) {
        return new NullSafeList<>(list != null ? list : Collections.emptyList());
    }
    
    /**
     * Creates a NullSafeList from individual elements.
     * 
     * @param elements the elements to include in the list
     * @return NullSafeList instance
     */
    @SafeVarargs
    public static <T> NullSafeList<T> of(T... elements) {
        return of(elements != null ? Arrays.asList(elements) : Collections.emptyList());
    }
    
    /**
     * Creates an empty NullSafeList.
     * 
     * @param <T> the type of elements
     * @return empty NullSafeList
     */
    public static <T> NullSafeList<T> empty() {
        return new NullSafeList<>(Collections.emptyList());
    }
    
    private NullSafeList(List<T> list) {
        this.list = new ArrayList<>(list);
    }
    
    /**
     * Returns the size of the list.
     * 
     * @return the number of elements
     */
    public int size() {
        return list.size();
    }
    
    /**
     * Checks if the list is empty.
     * 
     * @return true if the list has no elements
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    /**
     * Gets an element at the specified index safely.
     * Returns an empty NullSafe if the index is out of bounds.
     * 
     * @param index the index
     * @return NullSafe containing the element or empty
     */
    public NullSafe<T> get(int index) {
        if (index >= 0 && index < list.size()) {
            return NullSafe.of(list.get(index));
        }
        return NullSafe.empty();
    }
    
    /**
     * Filters the list and returns elements that match the predicate.
     * 
     * @param predicate the filter condition
     * @return new NullSafeList with filtered elements
     */
    public NullSafeList<T> filter(Predicate<T> predicate) {
        return new NullSafeList<>(list.stream()
            .filter(Objects::nonNull)
            .filter(predicate)
            .collect(Collectors.toList()));
    }
    
    /**
     * Maps each element to a new type.
     * 
     * @param <R> the result type
     * @param mapper the transformation function
     * @return new NullSafeList with mapped elements
     */
    public <R> NullSafeList<R> map(Function<T, R> mapper) {
        return new NullSafeList<>(list.stream()
            .filter(Objects::nonNull)
            .map(mapper)
            .collect(Collectors.toList()));
    }
    
    /**
     * Performs flatMap operation on the list.
     * 
     * @param <R> the result type
     * @param mapper the flatMap function
     * @return new NullSafeList with flatMapped elements
     */
    public <R> NullSafeList<R> flatMap(Function<T, Collection<R>> mapper) {
        return new NullSafeList<>(list.stream()
            .filter(Objects::nonNull)
            .flatMap(t -> mapper.apply(t).stream())
            .collect(Collectors.toList()));
    }
    
    /**
     * Finds the first element matching the predicate.
     * 
     * @param predicate the search condition
     * @return NullSafe containing the found element or empty
     */
    public NullSafe<T> find(Predicate<T> predicate) {
        return NullSafe.of(list.stream()
            .filter(Objects::nonNull)
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
        return list.stream()
            .filter(Objects::nonNull)
            .anyMatch(predicate);
    }
    
    /**
     * Checks if all elements match the predicate.
     * 
     * @param predicate the condition to check
     * @return true if all elements match
     */
    public boolean allMatch(Predicate<T> predicate) {
        return list.stream()
            .filter(Objects::nonNull)
            .allMatch(predicate);
    }
    
    /**
     * Reduces the list to a single value using the accumulator.
     * 
     * @param identity the identity value
     * @param accumulator the reduction function
     * @return the reduced value
     */
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return list.stream()
            .filter(Objects::nonNull)
            .reduce(identity, accumulator);
    }
    
    /**
     * Groups elements by a classifier function.
     * 
     * @param <K> the key type
     * @param classifier the grouping function
     * @return Map of grouped elements
     */
    public <K> Map<K, List<T>> groupBy(Function<T, K> classifier) {
        return list.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(classifier));
    }
    
    /**
     * Returns the underlying list.
     * 
     * @return the list
     */
    public List<T> toList() {
        return new ArrayList<>(list);
    }
    
    /**
     * Returns all non-null elements as a stream.
     * 
     * @return stream of non-null elements
     */
    public Stream<T> stream() {
        return list.stream().filter(Objects::nonNull);
    }
    
    @Override
    public String toString() {
        return list.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NullSafeList<?> that = (NullSafeList<?>) obj;
        return list.equals(that.list);
    }
    
    @Override
    public int hashCode() {
        return list.hashCode();
    }
}