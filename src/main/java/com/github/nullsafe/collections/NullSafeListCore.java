import java.util.*;
import java.util.function.*;
import com.github.nullsafe.NullSafe;

public class NullSafeListCore<T> {
    private final List<T> list;

    private NullSafeListCore(List<T> list) {
        this.list = list != null ? new ArrayList<>(list) : new ArrayList<>();
    }

    public static <T> NullSafeListCore<T> of(List<T> list) {
        return new NullSafeListCore<>(list != null ? list : new ArrayList<>());
    }

    public static <T> NullSafeListCore<T> of(T... elements) {
        return of(elements != null ? Arrays.asList(elements) : new ArrayList<>());
    }

    public static <T> NullSafeListCore<T> empty() {
        return new NullSafeListCore<>(new ArrayList<>());
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
    
    public boolean contains(T element) {
        return list.contains(element);
    }
    
    public NullSafeListCore<T> add(T element) {
        List<T> newList = new ArrayList<>(list);
        newList.add(element);
        return new NullSafeListCore<>(newList);
    }
    
    public NullSafeListCore<T> remove(T element) {
        List<T> newList = new ArrayList<>(list);
        newList.remove(element);
        return new NullSafeListCore<>(newList);
    }
    
    public NullSafeListCore<T> filter(Predicate<T> predicate) {
        List<T> filtered = new ArrayList<>();
        for (T element : list) {
            if (predicate.test(element)) {
                filtered.add(element);
            }
        }
        return new NullSafeListCore<>(filtered);
    }
    
    public <R> NullSafeListCore<R> map(Function<T, R> mapper) {
        List<R> mapped = new ArrayList<>();
        for (T element : list) {
            R result = mapper.apply(element);
            if (result != null) {
                mapped.add(result);
            }
        }
        return new NullSafeListCore<>(mapped);
    }
    
    public List<T> toJavaList() {
        return new ArrayList<>(list);
    }
    
    @Override
    public String toString() {
        return "NullSafeList" + list;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NullSafeList<?> that = (NullSafeList<?>) obj;
        return Objects.equals(list, that.list);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}