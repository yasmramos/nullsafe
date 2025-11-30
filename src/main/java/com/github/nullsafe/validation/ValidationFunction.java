/**
 * Functional interface for custom validation functions.
 * 
 * @param <T> the type being validated
 * @since 1.0
 */
@FunctionalInterface
public interface ValidationFunction<T> {
    /**
     * Validates the given value.
     * 
     * @param value the value to validate
     * @return true if validation passes
     */
    boolean validate(T value);
}