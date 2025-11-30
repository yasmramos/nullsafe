/**
 * A specialized NullSafe implementation for Set collections.
 * Provides safe operations for sets that may contain null values or be null themselves.
 * 
 * @param <T> The type of elements in the set
 * @since 1.0
 */
public class NullSafeSet<T> {
    private final Set<T> set;
    
    /**
     * Creates a NullSafeSet from an existing set.
     * If the set is null, returns an empty NullSafeSet.
     * 
     * @param set the source set
     * @return NullSafeSet instance
     */
    public static <T> NullSafeSet<T> of(Set<T> set) {
        return new NullSafeSet<>(set != null ? set : Collections.emptySet());
    }
    
    /**
     * Creates a NullSafeSet from individual elements.
     * 
     * @param elements the elements to include in the set
     * @return NullSafeSet instance
     */
    @SafeVarargs
    public static <T> NullSafeSet<T> of(T... elements) {
        if (elements == null) {
            return empty();
        }
        
        Set<T> result = new HashSet<>();
        for (T element : elements) {
            if (element != null) {
                result.add(element);
            }
        }
        return new NullSafeSet<>(result);
    }
    
    /**
     * Creates a NullSafeSet from a collection.
     * 
     * @param collection the source collection
     * @return NullSafeSet instance
     */
    public static <T> NullSafeSet<T> of(Collection<T> collection) {
        if (collection == null) {
            return empty();
        }
        
        Set<T> result = collection.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(LinkedHashSet::new));
        
        return new NullSafeSet<>(result);
    }
    
    /**
     * Creates an empty NullSafeSet.
     * 
     * @param <T> the type of elements
     * @return empty NullSafeSet
     */
    public static <T> NullSafeSet<T> empty() {
        return new NullSafeSet<>(Collections.emptySet());
    }
    
    private NullSafeSet(Set<T> set) {
        this.set = new LinkedHashSet<>(set);
    }
    
    /**
     * Returns the size of the set.
     * 
     * @return the number of elements
     */
    public int size() {
        return set.size();
    }
    
    /**
     * Checks if the set is empty.
     * 
     * @return true if the set has no elements
     */
    public boolean isEmpty() {
        return set.isEmpty();
    }
    
    /**
     * Checks if the set contains the element.
     * 
     * @param element the element to check
     * @return true if the set contains the element
     */
    public boolean contains(T element) {
        return element != null && set.contains(element);
    }
    
    /**
     * Adds an element to the set.
     * 
     * @param element the element to add
     * @return true if the element was added
     */
    public boolean add(T element) {
        if (element == null) {
            return false;
        }
        return set.add(element);
    }
    
    /**
     * Removes an element from the set.
     * 
     * @param element the element to remove
     * @return true if the element was removed
     */
    public boolean remove(T element) {
        if (element == null) {
            return false;
        }
        return set.remove(element);
    }
    
    /**
     * Filters the set and returns elements that match the predicate.
     * 
     * @param predicate the filter condition
     * @return new NullSafeSet with filtered elements
     */
    public NullSafeSet<T> filter(Predicate<T> predicate) {
        return new NullSafeSet<>(set.stream()
            .filter(predicate)
            .collect(Collectors.toCollection(LinkedHashSet::new)));
    }
    
    /**
     * Maps each element to a new type.
     * 
     * @param <R> the result type
     * @param mapper the transformation function
     * @return new NullSafeSet with mapped elements
     */
    public <R> NullSafeSet<R> map(Function<T, R> mapper) {
        return new NullSafeSet<>(set.stream()
            .map(mapper)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(LinkedHashSet::new)));
    }
    
    /**
     * Performs flatMap operation on the set.
     * 
     * @param <R> the result type
     * @param mapper the flatMap function
     * @return new NullSafeSet with flatMapped elements
     */
    public <R> NullSafeSet<R> flatMap(Function<T, Collection<R>> mapper) {
        return new NullSafeSet<>(set.stream()
            .flatMap(t -> mapper.apply(t).stream())
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(LinkedHashSet::new)));
    }
    
    /**
     * Gets all elements as a NullSafeList.
     * 
     * @return NullSafeList of elements
     */
    public NullSafeList<T> toList() {
        return NullSafeList.of(new ArrayList<>(set));
    }
    
    /**
     * Gets the set as a stream.
     * 
     * @return stream of elements
     */
    public Stream<T> stream() {
        return set.stream();
    }
    
    /**
     * Finds the first element matching the predicate.
     * 
     * @param predicate the search condition
     * @return NullSafe containing the found element or empty
     */
    public NullSafe<T> find(Predicate<T> predicate) {
        return NullSafe.of(set.stream()
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
        return set.stream().anyMatch(predicate);
    }
    
    /**
     * Checks if all elements match the predicate.
     * 
     * @param predicate the condition to check
     * @return true if all elements match
     */
    public boolean allMatch(Predicate<T> predicate) {
        return set.stream().allMatch(predicate);
    }
    
    /**
     * Performs union with another set.
     * 
     * @param other the other set
     * @return new NullSafeSet with union of elements
     */
    public NullSafeSet<T> union(Set<T> other) {
        if (other == null || other.isEmpty()) {
            return this;
        }
        
        Set<T> result = new LinkedHashSet<>(set);
        other.stream().filter(Objects::nonNull).forEach(result::add);
        return new NullSafeSet<>(result);
    }
    
    /**
     * Performs intersection with another set.
     * 
     * @param other the other set
     * @return new NullSafeSet with intersection of elements
     */
    public NullSafeSet<T> intersection(Set<T> other) {
        if (other == null || other.isEmpty()) {
            return empty();
        }
        
        Set<T> result = new LinkedHashSet<>(set);
        result.retainAll(other.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
        return new NullSafeSet<>(result);
    }
    
    /**
     * Performs difference with another set.
     * 
     * @param other the other set
     * @return new NullSafeSet with elements in this set but not in other
     */
    public NullSafeSet<T> difference(Set<T> other) {
        if (other == null || other.isEmpty()) {
            return this;
        }
        
        Set<T> result = new LinkedHashSet<>(set);
        Set<T> otherFiltered = other.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        result.removeAll(otherFiltered);
        return new NullSafeSet<>(result);
    }
    
    /**
     * Reduces the set to a single value using the accumulator.
     * 
     * @param identity the identity value
     * @param accumulator the reduction function
     * @return the reduced value
     */
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return set.stream().reduce(identity, accumulator);
    }
    
    /**
     * Converts to immutable set.
     * 
     * @return immutable copy of the set
     */
    public Set<T> toSet() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(set));
    }
    
    /**
     * Groups elements by a classifier function.
     * 
     * @param <K> the key type
     * @param classifier the grouping function
     * @return Map of grouped elements
     */
    public <K> Map<K, List<T>> groupBy(Function<T, K> classifier) {
        return set.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(classifier, LinkedHashMap::new, Collectors.toList()));
    }
    
    @Override
    public String toString() {
        return set.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NullSafeSet<?> that = (NullSafeSet<?>) obj;
        return set.equals(that.set);
    }
    
    @Override
    public int hashCode() {
        return set.hashCode();
    }
}