package com.github.nullsafe.adapters;

import com.github.nullsafe.NullSafe;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

/**
 * Una interfaz funcional para adaptadores de NullSafe. Representa una función
 * que toma un NullSafe<T> y devuelve un NullSafe<R>.
 *
 * @param <T> el tipo del valor de entrada
 * @param <R> el tipo del valor de salida
 */
@FunctionalInterface
public interface NullSafeAdapter<T, R> extends Function<NullSafe<T>, NullSafe<R>>, Serializable {

    /**
     * Aplica el adaptador al valor dado.
     *
     * @param input el NullSafe<T> de entrada
     * @return el NullSafe<R> resultante
     */
    NullSafe<R> apply(NullSafe<T> input);

    /**
     * Permite encadenar adaptadores: this.andThen(next).
     *
     * @param after el siguiente adaptador a aplicar
     * @param <V> el tipo de salida final
     * @return un nuevo adaptador compuesto
     */
    default <V> NullSafeAdapter<T, V> andThen(NullSafeAdapter<R, V> after) {
        Objects.requireNonNull(after);
        return (NullSafe<T> t) -> after.apply(this.apply(t));
    }

    /**
     * Convierte este adaptador en una función estándar.
     */
    default NullSafe<R> applyFunction(NullSafe<T> t) {
        return apply(t);
    }
}
