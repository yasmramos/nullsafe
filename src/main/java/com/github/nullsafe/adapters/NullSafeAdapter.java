package com.github.nullsafe.adapters;

import com.github.nullsafe.NullSafe;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

/**
 * A functional interface for NullSafe adapters. Represents a function
 * that takes a NullSafe<T> and returns a NullSafe<R>.
 *
 * @param <T> the type of the input value
 * @param <R> the type of the output value
 */
@FunctionalInterface
public interface NullSafeAdapter<T, R> extends Function<NullSafe<T>, NullSafe<R>>, Serializable {

    /**
     * Applies the adapter to the given value.
     *
     * @param input the input NullSafe<T>
     * @return the resulting NullSafe<R>
     */
    NullSafe<R> apply(NullSafe<T> input);

    /**
     * Allows chaining adapters: this.andThen(next).
     *
     * @param after the next adapter to apply
     * @param <V> the type of the final output
     * @return a new composed adapter
     */
    default <V> NullSafeAdapter<T, V> andThen(NullSafeAdapter<R, V> after) {
        Objects.requireNonNull(after);
        return (NullSafe<T> t) -> after.apply(this.apply(t));
    }

    /**
     * Converts this adapter to a standard function.
     */
    default NullSafe<R> applyFunction(NullSafe<T> t) {
        return apply(t);
    }
}
