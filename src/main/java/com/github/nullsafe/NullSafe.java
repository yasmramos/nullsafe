package com.github.nullsafe;

import com.github.nullsafe.result.Result;
import com.github.nullsafe.adapters.NullSafeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Una clase que encapsula un valor opcional (puede ser null) y proporciona
 * métodos para manipularlo de forma segura.
 *
 * @param <T> el tipo del valor contenido
 */
public class NullSafe<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final T value;

    private NullSafe(T value) {
        this.value = value;
    }

    /**
     * Crea una instancia de NullSafe que contiene el valor especificado.
     *
     * @param value el valor a encapsular (puede ser null)
     * @param <T> el tipo del valor
     * @return una nueva instancia de NullSafe
     */
    public static <T> NullSafe<T> of(T value) {
        return new NullSafe<>(value);
    }

    /**
     * Crea una instancia vacía de NullSafe que contiene null.
     *
     * @param <T> el tipo del valor
     * @return una nueva instancia vacía
     */
    public static <T> NullSafe<T> empty() {
        return new NullSafe<>(null);
    }

    /**
     * Devuelve el valor contenido si no es null, o el valor por defecto dado.
     *
     * @param defaultValue el valor por defecto a retornar si el valor es null
     * @return el valor contenido o el valor por defecto
     */
    public T orElse(T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * Devuelve el valor contenido si no es null, o el resultado de ejecutar el
     * proveedor dado.
     *
     * @param defaultValue el proveedor del valor por defecto
     * @return el valor contenido o el valor generado por el proveedor
     */
    public T orElseGet(Supplier<? extends T> defaultValue) {
        return value != null ? value : defaultValue.get();
    }

    /**
     * Devuelve el valor contenido si no es null, o lanza la excepción generada.
     *
     * @param exception el proveedor de la excepción a lanzar
     * @param <E> el tipo de la excepción
     * @return el valor contenido
     * @throws E si el valor es null
     */
    public <E extends Exception> T orElseThrow(Supplier<? extends E> exception) throws E {
        if (value == null) {
            throw exception.get();
        }
        return value;
    }

    /**
     * Ejecuta la acción dada si el valor está presente (no es null).
     *
     * @param action la acción a ejecutar
     * @return esta instancia para encadenamiento
     */
    public NullSafe<T> ifPresent(Consumer<? super T> action) {
        if (value != null) {
            action.accept(value);
        }
        return this;
    }

    /**
     * Ejecuta la acción dada si el valor NO está presente (es null).
     *
     * @param action la acción a ejecutar
     * @return esta instancia para encadenamiento
     */
    public NullSafe<T> ifAbsent(Runnable action) {
        if (value == null) {
            action.run();
        }
        return this;
    }

    /**
     * Aplica la función al valor contenido, si no es null, y devuelve un nuevo
     * NullSafe con el resultado.
     *
     * @param function la función a aplicar
     * @param <R> el tipo del resultado
     * @return un nuevo NullSafe con el resultado o vacío si el valor es null
     */
    public <R> NullSafe<R> map(Function<? super T, ? extends R> function) {
        return value != null ? NullSafe.of(function.apply(value)) : NullSafe.empty();
    }

    /**
     * Aplica el mapeador al valor contenido, si no es null, y devuelve el
     * resultado.
     *
     * @param mapper la función que devuelve otro NullSafe
     * @param <R> el tipo del resultado
     * @return el resultado del mapeador o un NullSafe vacío
     */
    public <R> NullSafe<R> flatMap(Function<? super T, NullSafe<R>> mapper) {
        return value != null ? mapper.apply(value) : NullSafe.empty();
    }

    /**
     * Filtra el valor contenido usando el predicado dado.
     *
     * @param predicate el predicado para evaluar el valor
     * @return este mismo objeto si pasa el filtro, o uno vacío en caso
     * contrario
     */
    public NullSafe<T> filter(Predicate<? super T> predicate) {
        if (value == null || !predicate.test(value)) {
            return NullSafe.empty();
        }
        return this;
    }

    /**
     * Verifica si hay un valor presente (no es null).
     *
     * @return true si hay un valor presente
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Verifica si NO hay un valor presente (es null).
     *
     * @return true si el valor es null
     */
    public boolean isEmpty() {
        return value == null;
    }

    /**
     * Obtiene el valor contenido, lanzando una excepción si es null.
     *
     * @return el valor contenido
     * @throws NoSuchElementException si el valor es null
     */
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    /**
     * Valida el valor contenido contra una condición, lanzando una excepción si
     * falla.
     *
     * @param <E>
     * @param condition la condición a validar
     * @param exception la excepción a lanzar si falla
     * @return esta instancia si la validación pasa
     * @throws E si la condición no se cumple
     */
    public <E extends RuntimeException> NullSafe<T> validate(Predicate<T> condition, Supplier<E> exception) {
        if (isPresent() && !condition.test(value)) {
            throw exception.get();
        }
        return this;
    }

    @Override
    public String toString() {
        return isPresent() ? String.format("NullSafe[%s]", value) : "NullSafe.empty";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NullSafe<?> other)) {
            return false;
        }
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    // Métodos adicionales para colecciones
    /**
     * Filtra los elementos no nulos de una colección.
     *
     * @param collection la colección a filtrar
     * @param <T> el tipo de los elementos
     * @return una lista con los elementos no nulos
     */
    public static <T> List<T> filterNonNull(Collection<T> collection) {
        if (collection == null) {
            return Collections.emptyList();
        }
        return collection.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Mapea y filtra los elementos no nulos de una colección.
     *
     * @param collection la colección a mapear
     * @param function la función de transformación
     * @param <T> el tipo original
     * @param <R> el tipo resultante
     * @return una lista con los elementos transformados y no nulos
     */
    public static <T, R> List<R> mapNonNull(Collection<T> collection, Function<T, R> function) {
        if (collection == null) {
            return Collections.emptyList();
        }
        return collection.stream()
                .filter(Objects::nonNull)
                .map(function)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // Métodos adicionales para cadenas
    /**
     * Devuelve la cadena dada o una cadena vacía si es null.
     *
     * @param value la cadena a verificar
     * @return la cadena original o "" si es null
     */
    public static String emptyIfNull(String value) {
        return value != null ? value : "";
    }

    /**
     * Convierte la cadena a mayúsculas si no es null.
     *
     * @param value la cadena a convertir
     * @return la cadena en mayúsculas o null si era null
     */
    public static String toUpperCaseIfPresent(String value) {
        return value != null ? value.toUpperCase() : null;
    }

    /**
     * Convierte la cadena a minúsculas si no es null.
     *
     * @param value la cadena a convertir
     * @return la cadena en minúsculas o null si era null
     */
    public static String toLowerCaseIfPresent(String value) {
        return value != null ? value.toLowerCase() : null;
    }

    // Métodos adicionales para mapas
    /**
     * Filtra las entradas no nulas de un mapa.
     *
     * @param map el mapa a filtrar
     * @param <K> el tipo de clave
     * @param <V> el tipo de valor
     * @return un mapa solo con claves y valores no nulos
     */
    public static <K, V> Map<K, V> filterNonNullEntries(Map<K, V> map) {
        if (map == null) {
            return Collections.emptyMap();
        }
        return map.entrySet().stream()
                .filter(e -> e.getKey() != null && e.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Mapea los valores del mapa aplicando una función, ignorando entradas
     * nulas.
     *
     * @param map el mapa original
     * @param valueFunc la función para mapear valores
     * @param <K> el tipo de clave
     * @param <V> el tipo de valor original
     * @param <R> el tipo de valor resultante
     * @return un nuevo mapa con valores transformados
     */
    public static <K, V, R> Map<K, R> mapNonNullValues(Map<K, V> map, Function<V, R> valueFunc) {
        if (map == null) {
            return Collections.emptyMap();
        }
        return map.entrySet().stream()
                .filter(e -> e.getKey() != null && e.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> valueFunc.apply(e.getValue())));
    }

    /**
     * Convierte este NullSafe en un Optional.
     *
     * @return un Optional conteniendo el valor o vacío
     */
    public Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }

    /**
     * Crea una instancia de NullSafe a partir de un Optional.
     *
     * @param optional el Optional del cual crear el NullSafe
     * @param <T> el tipo del valor
     * @return una nueva instancia de NullSafe
     */
    public static <T> NullSafe<T> fromOptional(Optional<T> optional) {
        return optional != null ? new NullSafe<>(optional.orElse(null)) : NullSafe.empty();
    }

    /**
     * Permite mapear internamente el valor como Optional<T>, útil para
     * integración.
     *
     * @param mapper función que opera sobre Optional<T>
     * @param <R> tipo del resultado
     * @return el resultado del mapeo
     */
    public <R> R optionalMap(Function<Optional<T>, R> mapper) {
        return mapper.apply(toOptional());
    }

    /**
     * Aplica un adaptador funcional a este NullSafe.
     *
     * @param adapter el adaptador a aplicar
     * @param <R> el tipo del resultado
     * @return el resultado del adaptador
     */
    public <R> NullSafe<R> adapt(NullSafeAdapter<T, R> adapter) {
        return adapter.apply(this);
    }

    /**
     * Ejecuta una acción consumidora si el valor está presente y devuelve este
     * mismo objeto.
     *
     * @param action acción a ejecutar
     * @return this para encadenamiento
     */
    public NullSafe<T> peek(Consumer<? super T> action) {
        if (isPresent()) {
            action.accept(value);
        }
        return this;
    }

    public NullSafe<T> ifPresentOrElse(Consumer<? super T> presentAction, Runnable emptyAction) {
        if (isPresent()) {
            presentAction.accept(value);
        } else {
            emptyAction.run();
        }
        return this;
    }

    public NullSafe<T> recover(Function<Throwable, T> recoveryFunction) {
        if (isPresent()) {
            return this;
        }
        try {
            return NullSafe.of(recoveryFunction.apply(new NullPointerException()));
        } catch (Exception e) {
            return NullSafe.empty();
        }
    }

    public NullSafe<T> recoverWith(Function<Throwable, NullSafe<T>> recoveryFunction) {
        if (isPresent()) {
            return this;
        }
        try {
            return recoveryFunction.apply(new NullPointerException());
        } catch (Exception e) {
            return NullSafe.empty();
        }
    }

    public NullSafe<T> validate(Predicate<T> condition, String errorMessage) {
        if (isPresent() && !condition.test(value)) {
            throw new IllegalStateException(errorMessage);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public <R> NullSafe<R> as(Class<R> type) {
        if (type.isInstance(value)) {
            return NullSafe.of(type.cast(value));
        }
        return NullSafe.empty();
    }

    public Stream<T> stream() {
        return isPresent() ? Stream.of(value) : Stream.empty();
    }

    public static <A, B, R> NullSafe<R> combine(
            NullSafe<A> a,
            NullSafe<B> b,
            BiFunction<A, B, R> combiner) {
        return a.flatMap(av -> b.map(bv -> combiner.apply(av, bv)));
    }

    public NullSafe<T> logIfPresent(String message) {
        if (isPresent()) {
            System.out.printf(message + ": %s%n", value);
        }
        return this;
    }

    public NullSafe<T> logIfAbsent(String message) {
        if (isEmpty()) {
            System.out.println(message);
        }
        return this;
    }

    /**
     * Convierte este NullSafe en un Result.
     *
     * @param error el valor del error a usar si el valor es null
     * @param <E> el tipo del error
     * @return un Result.Success si hay valor, o Result.Failure si es null
     */
    public <E> Result<T, E> toResult(E error) {
        return isPresent() ? Result.success(value) : Result.failure(error);
    }

    /**
     * Convierte este NullSafe en un Result usando un proveedor de error.
     *
     * @param errorSupplier el proveedor del valor de error
     * @param <E> el tipo del error
     * @return un Result.Success si hay valor, o Result.Failure si es null
     */
    public <E> Result<T, E> toResult(Supplier<E> errorSupplier) {
        return isPresent() ? Result.success(value) : Result.failure(errorSupplier.get());
    }
}
