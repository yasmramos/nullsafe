package com.github.nullsafe;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NullSafeDouble {

    private final Double value;

    private NullSafeDouble(Double value) {
        this.value = value;
    }

    public static NullSafeDouble of(double value) {
        return new NullSafeDouble(value);
    }

    public static NullSafeDouble empty() {
        return new NullSafeDouble(null);
    }

    public double orElse(double defaultValue) {
        return value != null ? value : defaultValue;
    }

    public double orElseGet(Supplier<Double> defaultValue) {
        return value != null ? value : defaultValue.get();
    }

    public <E extends Exception> double orElseThrow(Supplier<? extends E> exception) throws E {
        if (value == null) {
            throw exception.get();
        }
        return value;
    }

    public NullSafeDouble ifPresent(Consumer<Double> action) {
        if (value != null) {
            action.accept(value);
        }
        return this;
    }

    public NullSafeDouble ifAbsent(Runnable action) {
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

    public double get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    @Override
    public String toString() {
        return isPresent() ? String.format("NullSafeDouble[%f]", value) : "NullSafeDouble.empty";
    }
}