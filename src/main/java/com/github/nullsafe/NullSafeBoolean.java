package com.github.nullsafe;
/**
 * A specialized NullSafe implementation for primitive Boolean values.
 * Provides the same functionality as NullSafe but optimized for Boolean values
 * to avoid boxing/unboxing overhead.
 */

/**
 * A specialized NullSafe implementation for primitive Boolean values.
 * Provides the same functionality as NullSafe but optimized for Boolean values
 * to avoid boxing/unboxing overhead.
 */

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NullSafeBoolean {

    private final Boolean value;

    private NullSafeBoolean(Boolean value) {
        this.value = value;
    }

    public static NullSafeBoolean of(boolean value) {
        return new NullSafeBoolean(value);
    }

    public static NullSafeBoolean empty() {
        return new NullSafeBoolean(null);
    }

    public boolean orElse(boolean defaultValue) {
        return value != null ? value : defaultValue;
    }

    public boolean orElseGet(Supplier<Boolean> defaultValue) {
        return value != null ? value : defaultValue.get();
    }

    public <E extends Exception> boolean orElseThrow(Supplier<? extends E> exception) throws E {
        if (value == null) {
            throw exception.get();
        }
        return value;
    }

    public NullSafeBoolean ifPresent(Consumer<Boolean> action) {
        if (value != null) {
            action.accept(value);
        }
        return this;
    }

    public NullSafeBoolean ifAbsent(Runnable action) {
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

    public boolean get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    @Override
    public String toString() {
        return isPresent() ? String.format("NullSafeBoolean[%s]", value) : "NullSafeBoolean.empty";
    }
}