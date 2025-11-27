package com.github.nullsafe;
/**
 * A specialized NullSafe implementation for primitive Short values.
 * Provides the same functionality as NullSafe but optimized for Short values
 * to avoid boxing/unboxing overhead.
 */

/**
 * A specialized NullSafe implementation for primitive Short values.
 * Provides the same functionality as NullSafe but optimized for Short values
 * to avoid boxing/unboxing overhead.
 */

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NullSafeShort {

    private final Short value;

    private NullSafeShort(Short value) {
        this.value = value;
    }

    public static NullSafeShort of(short value) {
        return new NullSafeShort(value);
    }

    public static NullSafeShort empty() {
        return new NullSafeShort(null);
    }

    public short orElse(short defaultValue) {
        return value != null ? value : defaultValue;
    }

    public short orElseGet(Supplier<Short> defaultValue) {
        return value != null ? value : defaultValue.get();
    }

    public <E extends Exception> short orElseThrow(Supplier<? extends E> exception) throws E {
        if (value == null) {
            throw exception.get();
        }
        return value;
    }

    public NullSafeShort ifPresent(Consumer<Short> action) {
        if (value != null) {
            action.accept(value);
        }
        return this;
    }

    public NullSafeShort ifAbsent(Runnable action) {
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

    public short get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    @Override
    public String toString() {
        return isPresent() ? String.format("NullSafeShort[%d]", value) : "NullSafeShort.empty";
    }
}