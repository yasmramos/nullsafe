/**
 * Utility class for working with NullSafe collection operations.
 * Provides static methods to create and manipulate NullSafe collections.
 * 
 * @since 1.0
 */
import java.util.*;
import java.util.stream.*;
import java.util.function.Function;
import java.util.function.Predicate;
import com.github.nullsafe.collections.*;

public final class NullSafeCollections {
    
    private NullSafeCollections() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Creates a NullSafeList from any collection, filtering out null values.
     * 
     * @param <T> the element type
     * @param collection the source collection
     * @return NullSafeList instance
     */
    public static <T> NullSafeList<T> safeList(Collection<T> collection) {
        return NullSafeList.of(collection);
    }
    
    /**
     * Creates a NullSafeMap from any map, filtering out null keys.
     * 
     * @param <K> the key type
     * @param <V> the value type
     * @param map the source map
     * @return NullSafeMap instance
     */
    public static <K, V> NullSafeMap<K, V> safeMap(Map<K, V> map) {
        return NullSafeMap.of(map);
    }
    
    /**
     * Creates a NullSafeSet from any collection, filtering out null values.
     * 
     * @param <T> the element type
     * @param collection the source collection
     * @return NullSafeSet instance
     */
    public static <T> NullSafeSet<T> safeSet(Collection<T> collection) {
        return NullSafeSet.of(collection);
    }
    
    /**
     * Creates a NullSafeArray from any array or collection.
     * 
     * @param <T> the element type
     * @param elements the source array or collection
     * @return NullSafeArray instance
     */
    @SafeVarargs
    public static <T> NullSafeArray<T> safeArray(T... elements) {
        return NullSafeArray.of(elements);
    }
    
    /**
     * Safely converts a stream to a NullSafeList.
     * 
     * @param <T> the element type
     * @param stream the source stream
     * @return NullSafeList instance
     */
    public static <T> NullSafeList<T> safeListFromStream(Stream<T> stream) {
        if (stream == null) {
            return NullSafeList.empty();
        }
        return NullSafeList.of(stream.collect(Collectors.toList()));
    }
    
    /**
     * Safely converts a stream to a NullSafeSet.
     * 
     * @param <T> the element type
     * @param stream the source stream
     * @return NullSafeSet instance
     */
    public static <T> NullSafeSet<T> safeSetFromStream(Stream<T> stream) {
        if (stream == null) {
            return NullSafeSet.empty();
        }
        return NullSafeSet.of(stream.collect(Collectors.toSet()));
    }
    
    /**
     * Groups elements by a key function, returning NullSafe collections.
     * 
     * @param <T> the element type
     * @param <K> the key type
     * @param elements the elements to group
     * @param keyFunction the grouping function
     * @return Map of NullSafeList groups
     */
    public static <T, K> Map<K, NullSafeList<T>> groupSafely(Collection<T> elements, 
                                                            Function<T, K> keyFunction) {
        if (elements == null || elements.isEmpty()) {
            return Collections.emptyMap();
        }
        
        return elements.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(
                keyFunction,
                LinkedHashMap::new,
                Collectors.collectingAndThen(Collectors.toList(), NullSafeList::of)
            ));
    }
    
    /**
     * Partitions elements by a predicate into NullSafe collections.
     * 
     * @param <T> the element type
     * @param elements the elements to partition
     * @param predicate the partitioning predicate
     * @return Map with "true" and "false" keys containing partitioned elements
     */
    public static <T> Map<Boolean, NullSafeList<T>> partitionSafely(Collection<T> elements, 
                                                                   Predicate<T> predicate) {
        if (elements == null || elements.isEmpty()) {
            Map<Boolean, NullSafeList<T>> emptyResult = new HashMap<>();
            emptyResult.put(true, NullSafeList.empty());
            emptyResult.put(false, NullSafeList.empty());
            return emptyResult;
        }
        
        return elements.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.partitioningBy(
                predicate,
                Collectors.collectingAndThen(Collectors.toList(), NullSafeList::of)
            ));
    }
    
    /**
     * Creates a zip operation between two collections.
     * 
     * @param <A> the first collection element type
     * @param <B> the second collection element type
     * @param first the first collection
     * @param second the second collection
     * @return List of pairs
     */
    public static <A, B> NullSafeList<Map.Entry<A, B>> zip(Iterable<A> first, Iterable<B> second) {
        if (first == null || second == null) {
            return NullSafeList.empty();
        }
        
        Iterator<A> iterator1 = first.iterator();
        Iterator<B> iterator2 = second.iterator();
        List<Map.Entry<A, B>> pairs = new ArrayList<>();
        
        while (iterator1.hasNext() && iterator2.hasNext()) {
            A item1 = iterator1.next();
            B item2 = iterator2.next();
            if (item1 != null && item2 != null) {
                pairs.add(Map.entry(item1, item2));
            }
        }
        
        return NullSafeList.of(pairs);
    }
    
    /**
     * Unzips a list of pairs into two collections.
     * 
     * @param <A> the first element type
     * @param <B> the second element type
     * @param pairs the list of pairs
     * @return array containing the two collections
     */
    @SuppressWarnings("unchecked")
    public static <A, B> NullSafeList<A>[] unzip(NullSafeList<Map.Entry<A, B>> pairs) {
        if (pairs == null) {
            return new NullSafeList[]{NullSafeList.empty(), NullSafeList.empty()};
        }
        
        List<A> first = new ArrayList<>();
        List<B> second = new ArrayList<>();
        
        pairs.get(0).forEach(entry -> {
            if (entry != null) {
                first.add(entry.getKey());
                second.add(entry.getValue());
            }
        });
        
        return new NullSafeList[]{
            NullSafeList.of(first),
            NullSafeList.of(second)
        };
    }
    
    /**
     * Creates a cartesian product of two collections.
     * 
     * @param <A> the first collection element type
     * @param <B> the second collection element type
     * @param first the first collection
     * @param second the second collection
     * @return List of pairs representing the cartesian product
     */
    public static <A, B> NullSafeList<Map.Entry<A, B>> cartesianProduct(Iterable<A> first, Iterable<B> second) {
        if (first == null || second == null) {
            return NullSafeList.empty();
        }
        
        List<A> firstList = new ArrayList<>();
        List<B> secondList = new ArrayList<>();
        
        first.forEach(item -> {
            if (item != null) {
                firstList.add(item);
            }
        });
        
        second.forEach(item -> {
            if (item != null) {
                secondList.add(item);
            }
        });
        
        List<Map.Entry<A, B>> product = new ArrayList<>();
        
        for (A item1 : firstList) {
            for (B item2 : secondList) {
                product.add(Map.entry(item1, item2));
            }
        }
        
        return NullSafeList.of(product);
    }
    
    /**
     * Safely converts any object to a collection.
     * 
     * @param obj the object to convert
     * @return NullSafeList containing the object if it's a collection, or single element if not
     */
    public static NullSafeList<Object> fromObject(Object obj) {
        if (obj == null) {
            return NullSafeList.empty();
        }
        
        if (obj instanceof Collection) {
            return NullSafeList.of((Collection<?>) obj);
        } else if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            List<Map.Entry<?, ?>> entries = new ArrayList<>(map.entrySet());
            return NullSafeList.of(entries);
        } else if (obj.getClass().isArray()) {
            Object[] array = (Object[]) obj;
            return NullSafeList.of(Arrays.asList(array));
        } else {
            return NullSafeList.of(obj);
        }
    }
}