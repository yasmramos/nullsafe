package com.github.nullsafe.result;

import com.github.nullsafe.NullSafe;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents the result of an operation that may have succeeded or
 * failed.
 *
 * @param <T> type of the value in case of success
 * @param <E> type of the value/error in case of failure
 */
public class Result<T, E> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final T value;
    private final E error;

    private Result(boolean success, T value, E error) {
        this.success = success;
        this.value = value;
        this.error = error;
    }

    /**
     * Creates a successful result.
     */
    public static <T, E> Result<T, E> success(T value) {
        return new Result<>(true, value, null);
    }

    /**
     * Creates a failed result.
     */
    public static <T, E> Result<T, E> failure(E error) {
        return new Result<>(false, null, error);
    }

    /**
     * Checks if the operation was successful.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Verifica si la operación falló.
     */
    public boolean isFailure() {
        return !success;
    }

    /**
     * Devuelve el valor en caso de éxito o lanza una excepción con el error.
     *
     * @return el valor si tuvo éxito
     * @throws RuntimeException con el error asociado
     */
    public T get() throws RuntimeException {
        if (isSuccess()) {
            return value;
        } else {
            throw error instanceof RuntimeException ? (RuntimeException) error : new RuntimeException(error.toString());
        }
    }

    /**
     * Devuelve el valor en caso de éxito o un valor por defecto.
     */
    public T orElse(T defaultValue) {
        return isSuccess() ? value : defaultValue;
    }

    /**
     * Devuelve el valor en caso de éxito o el resultado de un proveedor.
     */
    public T orElseGet(Supplier<? extends T> supplier) {
        return isSuccess() ? value : supplier.get();
    }

    /**
     * Devuelve el valor o lanza la excepción generada por el proveedor.
     */
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isSuccess()) {
            return value;
        }
        throw exceptionSupplier.get();
    }

    /**
     * Aplica una función al valor si hay éxito y devuelve un nuevo Result.
     */
    public <R> Result<R, E> map(Function<T, R> mapper) {
        if (isSuccess()) {
            return Result.success(mapper.apply(value));
        } else {
            return Result.failure(error);
        }
    }

    /**
     * Aplica una función que devuelve otro Result si hay éxito.
     */
    public <R> Result<R, E> flatMap(Function<T, Result<R, E>> mapper) {
        if (isSuccess()) {
            return mapper.apply(value);
        } else {
            return Result.failure(error);
        }
    }

    /**
     * Mapea el error si hay fallo.
     */
    public <F> Result<T, F> mapError(Function<E, F> errorMapper) {
        if (isFailure()) {
            return Result.failure(errorMapper.apply(error));
        } else {
            return Result.success(value);
        }
    }

    /**
     * Recupera el valor usando una función si hay fallo.
     */
    public Result<T, E> recover(Function<E, T> recoveryFunction) {
        if (isSuccess()) {
            return this;
        }
        return Result.success(recoveryFunction.apply(error));
    }

    /**
     * Recupera con otro Result si hay fallo.
     */
    public Result<T, E> recoverWith(Function<E, Result<T, E>> recoveryFunction) {
        if (isSuccess()) {
            return this;
        }
        return recoveryFunction.apply(error);
    }

    /**
     * Ejecuta una acción si hay éxito.
     */
    public Result<T, E> ifSuccess(Consumer<T> action) {
        if (isSuccess()) {
            action.accept(value);
        }
        return this;
    }

    /**
     * Ejecuta una acción si hay fallo.
     */
    public Result<T, E> ifFailure(Consumer<E> action) {
        if (isFailure()) {
            action.accept(error);
        }
        return this;
    }

    /**
     * Convierte este Result en un NullSafe<T>.
     */
    public NullSafe<T> toNullSafe() {
        return NullSafe.of(value);
    }

    /**
     * Devuelve el error si lo hubo.
     */
    public Optional<E> getError() {
        return Optional.ofNullable(error);
    }

    /**
     * Crea un Result desde un NullSafe existente.
     *
     * @param nullable el NullSafe a convertir
     * @param error el error a usar si es null
     * @param <T> tipo del valor
     * @param <E> tipo del error
     * @return Result.Success si tiene valor, o Result.Failure si es null
     */
    public static <T, E> Result<T, E> fromNullSafe(NullSafe<T> nullable, E error) {
        return nullable.isPresent() ? Result.success(nullable.get()) : Result.failure(error);
    }

    /**
     * Crea un Result desde un NullSafe existente con proveedor de error.
     *
     * @param nullable el NullSafe a convertir
     * @param errorSupplier proveedor del error
     * @param <T> tipo del valor
     * @param <E> tipo del error
     * @return Result.Success si tiene valor, o Result.Failure si es null
     */
    public static <T, E> Result<T, E> fromNullSafe(NullSafe<T> nullable, Supplier<E> errorSupplier) {
        return nullable.isPresent() ? Result.success(nullable.get()) : Result.failure(errorSupplier.get());
    }

    @Override
    public String toString() {
        return isSuccess()
                ? "Result.Success[" + value + "]"
                : "Result.Failure[" + error + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Result)) {
            return false;
        }
        Result<?, ?> other = (Result<?, ?>) o;
        return success == other.success
                && Objects.equals(value, other.value)
                && Objects.equals(error, other.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, value, error);
    }
}
