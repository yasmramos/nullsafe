import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import com.github.nullsafe.NullSafe;

public class NullSafeList<T> {
    private final List<T> list;
    
    public static <T> NullSafeList<T> of(List<T> list) {
        return new NullSafeList<>(list != null ? list : Collections.emptyList());
    }
    
    public static <T> NullSafeList<T> of(T... elements) {
        return of(elements != null ? Arrays.asList(elements) : Collections.emptyList());
    }
    
    public static <T> NullSafeList<T> empty() {
        return new NullSafeList<>(Collections.emptyList());
    }
    
    private NullSafeList(List<T> list) {
        this.list = new ArrayList<>(list);
    }
    
    public int size() {
        return list.size();
    }
    
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    public NullSafe<T> get(int index) {
        if (index >= 0 && index < list.size()) {
            return NullSafe.of(list.get(index));
        }
        return NullSafe.empty();
    }
    
    public NullSafeList<T> filter(Predicate<T> predicate) {
        return new NullSafeList<>(list.stream()
            .filter(Objects::nonNull)
            .filter(predicate)
            .collect(Collectors.toList()));
    }
    
    public <R> NullSafeList<R> map(Function<T, R> mapper) {
        return new NullSafeList<>(list.stream()
            .filter(Objects::nonNull)
            .map(mapper)
            .collect(Collectors.toList()));
    }
    
    public List<T> toList() {
        return new ArrayList<>(list);
    }
    
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