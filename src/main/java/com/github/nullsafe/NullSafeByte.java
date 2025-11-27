package com.github.nullsafe;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NullSafeByte {

    private final Byte value;

    private NullSafeByte(Byte value) {
        this.value = value;
    }

    public static NullSafeByte of(byte value) {
        return new NullSafeByte(value);
    }

    public static NullSafeByte empty() {
        return new NullSafeByte(null);
    }

    public byte orElse(byte defaultValue) {
        return value != null ? value : defaultValue;
    }

    public byte orElseGet(Supplier<Byte> defaultValue) {
        return value != null ? value : defaultValue.get();
    }

    public <E extends Exception> byte orElseThrow(Supplier<? extends E> exception) throws E {
        if (value == null) {
            throw exception.get();
        }
        return value;
    }

    public NullSafeByte ifPresent(Consumer<Byte> action) {
        if (value != null) {
            action.accept(value);
        }
        return this;
    }

    public NullSafeByte ifAbsent(Runnable action) {
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

    public byte get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    @Override
    public String toString() {
        return isPresent() ? String.format("NullSafeByte[%d]", value) : "NullSafeByte.empty";
    }
}