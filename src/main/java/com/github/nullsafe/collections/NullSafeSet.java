import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import com.github.nullsafe.NullSafe;

public class NullSafeSet<T> {
    private final Set<T> set;
    
    public static <T> NullSafeSet<T> of(Set<T> set) {
        return new NullSafeSet<>(set != null ? set : Collections.emptySet());
    }
    
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
    
    public static <T> NullSafeSet<T> of(Collection<T> collection) {
        if (collection == null) {
            return empty();
        }
        
        Set<T> result = collection.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(LinkedHashSet::new));
        
        return new NullSafeSet<>(result);
    }
    
    public static <T> NullSafeSet<T> empty() {
        return new NullSafeSet<>(Collections.emptySet());
    }
    
    private NullSafeSet(Set<T> set) {
        this.set = new LinkedHashSet<>(set);
    }
    
    public int size() {
        return set.size();
    }
    
    public boolean isEmpty() {
        return set.isEmpty();
    }
    
    public boolean contains(T element) {
        return element != null && set.contains(element);
    }
    
    public NullSafeSet<T> filter(Predicate<T> predicate) {
        return new NullSafeSet<>(set.stream()
            .filter(predicate)
            .collect(Collectors.toCollection(LinkedHashSet::new)));
    }
    
    public <R> NullSafeSet<R> map(Function<T, R> mapper) {
        return new NullSafeSet<>(set.stream()
            .map(mapper)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(LinkedHashSet::new)));
    }
    
    public NullSafeList<T> toList() {
        return NullSafeList.of(new ArrayList<>(set));
    }
    
    public Stream<T> stream() {
        return set.stream();
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