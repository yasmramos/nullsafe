package com.github.nullsafe.collections;

import java.util.*;
import java.util.function.*;
import com.github.nullsafe.NullSafe;

public class NullSafeSet<T> {
    private final Set<T> set;
    
    private NullSafeSet(Set<T> set) {
        this.set = set != null ? new HashSet<>(set) : new HashSet<>();
    }
    
    public static <T> NullSafeSet<T> of(Set<T> set) {
        return new NullSafeSet<>(set != null ? set : new HashSet<>());
    }
    
    public static <T> NullSafeSet<T> of(T... elements) {
        return of(elements != null ? new HashSet<>(Arrays.asList(elements)) : new HashSet<>());
    }
    
    public static <T> NullSafeSet<T> empty() {
        return new NullSafeSet<>(new HashSet<>());
    }
    
    public int size() {
        return set.size();
    }
    
    public boolean isEmpty() {
        return set.isEmpty();
    }
    
    public NullSafe<T> find(Predicate<T> predicate) {
        for (T element : set) {
            if (predicate.test(element)) {
                return NullSafe.of(element);
            }
        }
        return NullSafe.empty();
    }
    
    public boolean contains(T element) {
        return set.contains(element);
    }
    
    public NullSafeSet<T> add(T element) {
        Set<T> newSet = new HashSet<>(set);
        newSet.add(element);
        return new NullSafeSet<>(newSet);
    }

    public NullSafeSet<T> remove(T element) {
        Set<T> newSet = new HashSet<>(set);
        newSet.remove(element);
        return new NullSafeSet<>(newSet);
    }

    public NullSafeSet<T> filter(Predicate<T> predicate) {
        Set<T> filtered = new HashSet<>();
        for (T element : set) {
            if (predicate.test(element)) {
                filtered.add(element);
            }
        }
        return new NullSafeSet<>(filtered);
    }

    public <R> NullSafeSet<R> map(Function<T, R> mapper) {
        Set<R> mapped = new HashSet<>();
        for (T element : set) {
            R result = mapper.apply(element);
            if (result != null) {
                mapped.add(result);
            }
        }
        return new NullSafeSet<>(mapped);
    }
    
    public Set<T> toJavaSet() {
        return new HashSet<>(set);
    }
    
    public List<T> toList() {
        return new ArrayList<>(set);
    }
    
    @Override
    public String toString() {
        return "NullSafeSet" + set;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NullSafeSet<?> that = (NullSafeSet<?>) obj;
        return Objects.equals(set, that.set);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(set);
    }
}