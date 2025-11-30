import java.util.*;
import java.util.function.*;
import com.github.nullsafe.NullSafe;

public class NullSafeList<T> {
    private final List<T> list;

    private NullSafeList(List<T> list) {
        this.list = list != null ? new ArrayList<>(list) : new ArrayList<>();
    }

    public static <T> NullSafeList<T> of(List<T> list) {
        return new NullSafeList<>(list != null ? list : new ArrayList<>());
    }

    public static <T> NullSafeList<T> of(T... elements) {
        return of(elements != null ? Arrays.asList(elements) : new ArrayList<>());
    }

    public static <T> NullSafeList<T> empty() {
        return new NullSafeList<>(new ArrayList<>());
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
    
    public NullSafeList<T> add(T element) {
        List<T> newList = new ArrayList<>(list);
        newList.add(element);
        return new NullSafeList<>(newList);
    }
    
    public NullSafeList<T> remove(T element) {
        List<T> newList = new ArrayList<>(list);
        newList.remove(element);
        return new NullSafeList<>(newList);
    }
    
    public NullSafeList<T> filter(Predicate<T> predicate) {
        List<T> filtered = new ArrayList<>();
        for (T element : list) {
            if (predicate.test(element)) {
                filtered.add(element);
            }
        }
        return new NullSafeList<>(filtered);
    }
    
    public <R> NullSafeList<R> map(Function<T, R> mapper) {
        List<R> mapped = new ArrayList<>();
        for (T element : list) {
            R result = mapper.apply(element);
            if (result != null) {
                mapped.add(result);
            }
        }
        return new NullSafeList<>(mapped);
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