package com.github.nullsafe;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A specialized NullSafe implementation for primitive float values.
 * Provides the same functionality as NullSafe but optimized for float values
 * to avoid boxing/unboxing overhead.
 */
public class NullSafeFloat {

    private final Float value;

    private NullSafeFloat(Float value) {
        this.value = value;
    }

    public static NullSafeFloat of(float value) {
        return new NullSafeFloat(value);
    }

    public static NullSafeFloat empty() {
        return new NullSafeFloat(null);
    }

    public float orElse(float defaultValue) {
        return value != null ? value : defaultValue;
    }

    public float orElseGet(Supplier<Float> defaultValue) {
        return value != null ? value : defaultValue.get();
    }

    public <E extends Exception> float orElseThrow(Supplier<? extends E> exception) throws E {
        if (value == null) {
            throw exception.get();
        }
        return value;
    }

    public NullSafeFloat ifPresent(Consumer<Float> action) {
        if (value != null) {
            action.accept(value);
        }
        return this;
    }

    public NullSafeFloat ifAbsent(Runnable action) {
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

    public float get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    @Override
    public String toString() {
        return isPresent() ? String.format("NullSafeFloat[%f]", value) : "NullSafeFloat.empty";
    }
}