package com.github.nullsafe;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A specialized NullSafe implementation for primitive int values.
 * Provides the same functionality as NullSafe but optimized for int values
 * to avoid boxing/unboxing overhead.
 */
public class NullSafeInt {

    private final Integer value;

    private NullSafeInt(Integer value) {
        this.value = value;
    }

    /**
     * Creates a NullSafeInt instance containing the specified value.
     * @param value the int value to encapsulate
     * @return a new NullSafeInt instance
     */
    public static NullSafeInt of(int value) {
        return new NullSafeInt(value);
    }

    public static NullSafeInt empty() {
        return new NullSafeInt(null);
    }

    public int orElse(int defaultValue) {
        return value != null ? value : defaultValue;
    }

    public int orElseGet(Supplier<Integer> defaultValue) {
        return value != null ? value : defaultValue.get();
    }

    public <E extends Exception> int orElseThrow(Supplier<? extends E> exception) throws E {
        if (value == null) {
            throw exception.get();
        }
        return value;
    }

    public NullSafeInt ifPresent(Consumer<Integer> action) {
        if (value != null) {
            action.accept(value);
        }
        return this;
    }

    public NullSafeInt ifAbsent(Runnable action) {
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

    public int get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    @Override
    public String toString() {
        return isPresent() ? String.format("NullSafeInt[%d]", value) : "NullSafeInt.empty";
    }
}
