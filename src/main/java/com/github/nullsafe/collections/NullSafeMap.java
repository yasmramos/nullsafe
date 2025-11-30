/**
 * A specialized NullSafe implementation for Map collections.
 * Provides safe operations for maps that may contain null values or be null themselves.
 * 
 * @param <K> The type of keys
 * @param <V> The type of values
 * @since 1.0
 */
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import com.github.nullsafe.NullSafe;
import com.github.nullsafe.collections.*;

public class NullSafeMap<K, V> {
    private final Map<K, V> map;
    
    /**
     * Creates a NullSafeMap from an existing map.
     * If the map is null, returns an empty NullSafeMap.
     * 
     * @param map the source map
     * @return NullSafeMap instance
     */
    public static <K, V> NullSafeMap<K, V> of(Map<K, V> map) {
        return new NullSafeMap<>(map != null ? map : Collections.emptyMap());
    }
    
    /**
     * Creates an empty NullSafeMap.
     * 
     * @param <K> the type of keys
     * @param <V> the type of values
     * @return empty NullSafeMap
     */
    public static <K, V> NullSafeMap<K, V> empty() {
        return new NullSafeMap<>(Collections.emptyMap());
    }
    
    /**
     * Creates a NullSafeMap from key-value pairs.
     * 
     * @param entries the key-value entries
     * @return NullSafeMap instance
     */
    @SafeVarargs
    public static <K, V> NullSafeMap<K, V> of(Map.Entry<K, V>... entries) {
        Map<K, V> result = new HashMap<>();
        if (entries != null) {
            for (Map.Entry<K, V> entry : entries) {
                if (entry != null && entry.getKey() != null) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return new NullSafeMap<>(result);
    }
    
    private NullSafeMap(Map<K, V> map) {
        this.map = new HashMap<>(map);
    }
    
    /**
     * Gets a value for the specified key safely.
     * 
     * @param key the key
     * @return NullSafe containing the value or empty
     */
    public NullSafe<V> get(K key) {
        if (key == null) {
            return NullSafe.empty();
        }
        return NullSafe.of(map.get(key));
    }
    
    /**
     * Checks if the map contains the key.
     * 
     * @param key the key
     * @return true if the map contains the key
     */
    public boolean containsKey(K key) {
        return key != null && map.containsKey(key);
    }
    
    /**
     * Checks if the map contains the value.
     * 
     * @param value the value
     * @return true if the map contains the value
     */
    public boolean containsValue(V value) {
        return map.containsValue(value);
    }
    
    /**
     * Returns the size of the map.
     * 
     * @return the number of entries
     */
    public int size() {
        return map.size();
    }
    
    /**
     * Checks if the map is empty.
     * 
     * @return true if the map has no entries
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    /**
     * Filters the map entries by key and/or value.
     * 
     * @param keyPredicate optional key filter
     * @param valuePredicate optional value filter
     * @return new NullSafeMap with filtered entries
     */
    public NullSafeMap<K, V> filter(BiPredicate<K, V> keyPredicate, BiPredicate<K, V> valuePredicate) {
        BiPredicate<K, V> combinedPredicate = (keyPredicate != null && valuePredicate != null) ?
            (k, v) -> keyPredicate.test(k, v) && valuePredicate.test(k, v) :
            (keyPredicate != null ? keyPredicate : valuePredicate);
        
        Map<K, V> filtered = map.entrySet().stream()
            .filter(entry -> entry.getKey() != null)
            .filter(entry -> combinedPredicate.test(entry.getKey(), entry.getValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        
        return new NullSafeMap<>(filtered);
    }
    
    /**
     * Maps values to a new type.
     * 
     * @param <R> the result value type
     * @param valueMapper the value transformation function
     * @return new NullSafeMap with mapped values
     */
    public <R> NullSafeMap<K, R> mapValues(Function<V, R> valueMapper) {
        Map<K, R> mapped = map.entrySet().stream()
            .filter(entry -> entry.getKey() != null)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> valueMapper.apply(entry.getValue())
            ));
        return new NullSafeMap<>(mapped);
    }
    
    /**
     * Maps keys to a new type.
     * 
     * @param <R> the result key type
     * @param keyMapper the key transformation function
     * @return new NullSafeMap with mapped keys
     */
    public <R> NullSafeMap<R, V> mapKeys(Function<K, R> keyMapper) {
        Map<R, V> mapped = map.entrySet().stream()
            .filter(entry -> entry.getKey() != null)
            .collect(Collectors.toMap(
                entry -> keyMapper.apply(entry.getKey()),
                Map.Entry::getValue
            ));
        return new NullSafeMap<>(mapped);
    }
    
    /**
     * Performs flatMap operation on values.
     * 
     * @param <R> the result value type
     * @param valueMapper the flatMap function for values
     * @return Map of mapped values
     */
    public <R> Map<K, List<R>> flatMapValues(Function<V, Collection<R>> valueMapper) {
        return map.entrySet().stream()
            .filter(entry -> entry.getKey() != null && entry.getValue() != null)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> new ArrayList<>(valueMapper.apply(entry.getValue()))
            ));
    }
    
    /**
     * Gets all keys as a NullSafeList.
     * 
     * @return NullSafeList of keys
     */
    public NullSafeList<K> keys() {
        return NullSafeList.of(new ArrayList<>(map.keySet()));
    }
    
    /**
     * Gets all values as a NullSafeList.
     * 
     * @return NullSafeList of values
     */
    public NullSafeList<V> values() {
        return NullSafeList.of(new ArrayList<>(map.values()));
    }
    
    /**
     * Gets all entries as a NullSafeList.
     * 
     * @return NullSafeList of entries
     */
    public NullSafeList<Map.Entry<K, V>> entries() {
        return NullSafeList.of(new ArrayList<>(map.entrySet()));
    }
    
    /**
     * Converts to immutable map.
     * 
     * @return immutable copy of the map
     */
    public Map<K, V> toMap() {
        return Collections.unmodifiableMap(new HashMap<>(map));
    }
    
    /**
     * Merges another map into this one.
     * 
     * @param other the other map
     * @param mergeFunction function to resolve conflicts
     * @return new NullSafeMap with merged entries
     */
    public NullSafeMap<K, V> merge(Map<K, V> other, BinaryOperator<V> mergeFunction) {
        if (other == null || other.isEmpty()) {
            return this;
        }
        
        Map<K, V> merged = new HashMap<>(map);
        for (Map.Entry<K, V> entry : other.entrySet()) {
            if (entry.getKey() != null) {
                merged.merge(entry.getKey(), entry.getValue(), 
                    (existing, newValue) -> mergeFunction.apply(existing, newValue));
            }
        }
        
        return new NullSafeMap<>(merged);
    }
    
    /**
     * Gets a computed value for a key, computing if absent.
     * 
     * @param key the key
     * @param mappingFunction the function to compute the value
     * @return NullSafe containing the computed or existing value
     */
    public NullSafe<V> getOrCompute(K key, Function<K, V> mappingFunction) {
        if (key == null) {
            return NullSafe.empty();
        }
        
        if (map.containsKey(key)) {
            return NullSafe.of(map.get(key));
        }
        
        V computedValue = mappingFunction.apply(key);
        map.put(key, computedValue);
        return NullSafe.of(computedValue);
    }
    
    @Override
    public String toString() {
        return map.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NullSafeMap<?, ?> that = (NullSafeMap<?, ?>) obj;
        return map.equals(that.map);
    }
    
    @Override
    public int hashCode() {
        return map.hashCode();
    }
}