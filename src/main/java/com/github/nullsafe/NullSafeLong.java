package com.github.nullsafe;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A specialized NullSafe implementation for primitive long values.
 * Provides the same functionality as NullSafe but optimized for long values
 * to avoid boxing/unboxing overhead.
 */
public class NullSafeLong {

    private final Long value;

    private NullSafeLong(Long value) {
        this.value = value;
    }

    public static NullSafeLong of(long value) {
        return new NullSafeLong(value);
    }

    public static NullSafeLong empty() {
        return new NullSafeLong(null);
    }

    public long orElse(long defaultValue) {
        return value != null ? value : defaultValue;
    }

    public long orElseGet(Supplier<Long> defaultValue) {
        return value != null ? value : defaultValue.get();
    }

    public <E extends Exception> long orElseThrow(Supplier<? extends E> exception) throws E {
        if (value == null) {
            throw exception.get();
        }
        return value;
    }

    public NullSafeLong ifPresent(Consumer<Long> action) {
        if (value != null) {
            action.accept(value);
        }
        return this;
    }

    public NullSafeLong ifAbsent(Runnable action) {
        if (value == null) {
            action.run();
        }
        return this;
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public long get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    @Override
    public String toString() {
        return isPresent() ? String.format("NullSafeLong[%d]", value) : "NullSafeLong.empty";
    }
}