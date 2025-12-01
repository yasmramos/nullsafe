package com.github.nullsafe.validation;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.regex.Pattern;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import com.github.nullsafe.NullSafe;
import com.github.nullsafe.validation.ValidationRule;
import com.github.nullsafe.validation.ValidationResult;

public class NullSafeValidator<T> {
    private final NullSafe<T> value;
    private final List<ValidationRule<T>> rules;
    private final List<ValidationResult> results;
    
    /**
     * Creates a new NullSafeValidator.
     * 
     * @param value the value to validate
     */
    public NullSafeValidator(NullSafe<T> value) {
        this.value = value;
        this.rules = new ArrayList<>();
        this.results = new ArrayList<>();
    }
    
    /**
     * Validates using a custom predicate.
     * 
     * @param ruleName the name of the validation rule
     * @param predicate the validation predicate
     * @param errorMessage the error message if validation fails
     * @return this for chaining
     */
    public NullSafeValidator<T> rule(String ruleName, Predicate<T> predicate, String errorMessage) {
        rules.add(new ValidationRule<>(ruleName, predicate, errorMessage));
        return this;
    }
    
    /**
     * Validates that the value is not null.
     * 
     * @return this for chaining
     */
    public NullSafeValidator<T> notNull() {
        return rule("not_null", Objects::nonNull, "Value cannot be null");
    }
    
    /**
     * Validates that the value is null.
     * 
     * @return this for chaining
     */
    public NullSafeValidator<T> isNull() {
        return rule("is_null", Objects::isNull, "Value must be null");
    }
    
    /**
     * Validates string constraints.
     *
     * @return StringValidator for string-specific validations
     */
    @SuppressWarnings("unchecked")
    public StringValidator ifString() {
        return new StringValidator((NullSafeValidator<String>) this);
    }
    
    /**
     * Validates numeric constraints.
     *
     * @return NumberValidator for number-specific validations
     */
    @SuppressWarnings("unchecked")
    public NumberValidator ifNumber() {
        return new NumberValidator((NullSafeValidator<Number>) this);
    }
    
    /**
     * Validates collection constraints.
     *
     * @return CollectionValidator for collection-specific validations
     */
    @SuppressWarnings("unchecked")
    public CollectionValidator ifCollection() {
        return new CollectionValidator((NullSafeValidator<Collection<?>>) this);
    }
    
    /**
     * Validates email format.
     * 
     * @return this for chaining
     */
    public NullSafeValidator<T> isEmail() {
        if (value.isPresent() && value.get() instanceof String) {
            String email = (String) value.get();
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            return rule("is_email", 
                       s -> Pattern.matches(emailRegex, (String) s),
                       "Invalid email format");
        }
        return rule("is_email", s -> false, "Value is not a string");
    }
    
    /**
     * Validates URL format.
     * 
     * @return this for chaining
     */
    public NullSafeValidator<T> isUrl() {
        if (value.isPresent() && value.get() instanceof String) {
            return rule("is_url", 
                       s -> {
                           try {
                               new URL((String) s).toURI();
                               return true;
                           } catch (MalformedURLException | URISyntaxException e) {
                               return false;
                           }
                       },
                       "Invalid URL format");
        }
        return rule("is_url", s -> false, "Value is not a string");
    }
    
    /**
     * Validates against a regex pattern.
     * 
     * @param pattern the regex pattern
     * @param errorMessage the error message if validation fails
     * @return this for chaining
     */
    public NullSafeValidator<T> matches(String pattern, String errorMessage) {
        if (value.isPresent() && value.get() instanceof String) {
            return rule("matches", 
                       s -> Pattern.matches(pattern, (String) s),
                       errorMessage);
        }
        return rule("matches", s -> false, "Value is not a string");
    }
    
    /**
     * Validates that the value is one of the allowed values.
     * 
     * @param allowedValues the allowed values
     * @return this for chaining
     */
    @SafeVarargs
    public final NullSafeValidator<T> isOneOf(T... allowedValues) {
        Set<T> allowedSet = Arrays.stream(allowedValues).collect(Collectors.toSet());
        return rule("is_one_of", allowedSet::contains, "Value is not in the allowed set");
    }
    
    /**
     * Validates using a custom validator function.
     * 
     * @param validator the custom validator
     * @return this for chaining
     */
    public NullSafeValidator<T> custom(Function<T, Boolean> validator) {
        return rule("custom", 
                   validator::apply,
                   "Custom validation failed");
    }
    
    /**
     * Executes all validations.
     * 
     * @return the validation result
     */
    public ValidationResult validate() {
        results.clear();
        
        if (value.isEmpty()) {
            results.add(new ValidationResult("null_value", true, "Value is null"));
            return new ValidationResult("overall", results.isEmpty() || results.stream().allMatch(ValidationResult::isValid), 
                                       "No validation needed");
        }
        
        T actualValue = value.get();
        
        for (ValidationRule<T> rule : rules) {
            try {
                boolean isValid = rule.predicate.test(actualValue);
                results.add(new ValidationResult(rule.name, isValid, rule.errorMessage));
            } catch (Exception e) {
                results.add(new ValidationResult(rule.name, false, 
                                               "Validation error: " + e.getMessage()));
            }
        }
        
        boolean overallValid = results.stream().allMatch(ValidationResult::isValid);
        return new ValidationResult("overall", overallValid, "All validations passed");
    }
    
    /**
     * Gets the validation results without executing.
     * 
     * @return the results
     */
    public List<ValidationResult> getResults() {
        return new ArrayList<>(results);
    }
    
    /**
     * Creates a validated NullSafe with the value.
     * 
     * @return NullSafe containing the value if valid, empty otherwise
     */
    public NullSafe<T> getValidValue() {
        ValidationResult result = validate();
        return result.isValid() ? value : NullSafe.empty();
    }
    
    /**
     * Gets the original value regardless of validation.
     * 
     * @return the original NullSafe value
     */
    public NullSafe<T> getValue() {
        return value;
    }
    
    /**
     * Validator for string-specific validations.
     */
    public static class StringValidator {
        private final NullSafeValidator<String> validator;
        
        public StringValidator(NullSafeValidator<String> validator) {
            this.validator = validator;
        }
        
        /**
         * Validates minimum length.
         */
        public StringValidator minLength(int minLength) {
            validator.rule("min_length", 
                         s -> s != null && s.length() >= minLength,
                         "String must be at least " + minLength + " characters");
            return this;
        }
        
        /**
         * Validates maximum length.
         */
        public StringValidator maxLength(int maxLength) {
            validator.rule("max_length", 
                         s -> s != null && s.length() <= maxLength,
                         "String must be at most " + maxLength + " characters");
            return this;
        }
        
        /**
         * Validates exact length.
         */
        public StringValidator length(int exactLength) {
            validator.rule("exact_length", 
                         s -> s != null && s.length() == exactLength,
                         "String must be exactly " + exactLength + " characters");
            return this;
        }
        
        /**
         * Validates that string is not empty.
         */
        public StringValidator notEmpty() {
            validator.rule("not_empty", 
                         s -> s != null && !s.trim().isEmpty(),
                         "String cannot be empty");
            return this;
        }
        
        /**
         * Validates against a regex pattern.
         */
        public StringValidator matches(String pattern) {
            validator.rule("matches", 
                         s -> s != null && Pattern.matches(pattern, s),
                         "String does not match pattern: " + pattern);
            return this;
        }
        
        /**
         * Validates that string contains substring.
         */
        public StringValidator contains(String substring) {
            validator.rule("contains", 
                         s -> s != null && s.contains(substring),
                         "String must contain: " + substring);
            return this;
        }
        
        /**
         * Validates that string starts with prefix.
         */
        public StringValidator startsWith(String prefix) {
            validator.rule("starts_with", 
                         s -> s != null && s.startsWith(prefix),
                         "String must start with: " + prefix);
            return this;
        }
        
        /**
         * Validates that string ends with suffix.
         */
        public StringValidator endsWith(String suffix) {
            validator.rule("ends_with", 
                         s -> s != null && s.endsWith(suffix),
                         "String must end with: " + suffix);
            return this;
        }
        
        public NullSafeValidator<String> and() {
            return validator;
        }
    }
    
    /**
     * Validator for number-specific validations.
     */
    public static class NumberValidator {
        private final NullSafeValidator<Number> validator;
        
        public NumberValidator(NullSafeValidator<Number> validator) {
            this.validator = validator;
        }
        
        /**
         * Validates minimum value.
         */
        public NumberValidator min(Number minValue) {
            validator.rule("min_value", 
                         n -> n != null && n.doubleValue() >= minValue.doubleValue(),
                         "Number must be at least " + minValue);
            return this;
        }
        
        /**
         * Validates maximum value.
         */
        public NumberValidator max(Number maxValue) {
            validator.rule("max_value", 
                         n -> n != null && n.doubleValue() <= maxValue.doubleValue(),
                         "Number must be at most " + maxValue);
            return this;
        }
        
        /**
         * Validates range.
         */
        public NumberValidator range(Number min, Number max) {
            validator.rule("range", 
                         n -> n != null && 
                              n.doubleValue() >= min.doubleValue() && 
                              n.doubleValue() <= max.doubleValue(),
                         "Number must be between " + min + " and " + max);
            return this;
        }
        
        /**
         * Validates that number is positive.
         */
        public NumberValidator positive() {
            validator.rule("positive", 
                         n -> n != null && n.doubleValue() > 0,
                         "Number must be positive");
            return this;
        }
        
        /**
         * Validates that number is non-negative.
         */
        public NumberValidator nonNegative() {
            validator.rule("non_negative", 
                         n -> n != null && n.doubleValue() >= 0,
                         "Number must be non-negative");
            return this;
        }
        
        /**
         * Validates that number is negative.
         */
        public NumberValidator negative() {
            validator.rule("negative", 
                         n -> n != null && n.doubleValue() < 0,
                         "Number must be negative");
            return this;
        }
        
        /**
         * Validates integer values.
         */
        public NumberValidator isInteger() {
            validator.rule("is_integer", 
                         n -> n != null && n instanceof Integer,
                         "Number must be an integer");
            return this;
        }
        
        /**
         * Validates long values.
         */
        public NumberValidator isLong() {
            validator.rule("is_long", 
                         n -> n != null && n instanceof Long,
                         "Number must be a long");
            return this;
        }
        
        /**
         * Validates double values.
         */
        public NumberValidator isDouble() {
            validator.rule("is_double", 
                         n -> n != null && n instanceof Double,
                         "Number must be a double");
            return this;
        }
        
        public NullSafeValidator<Number> and() {
            return validator;
        }
    }
    
    /**
     * Validator for collection-specific validations.
     */
    public static class CollectionValidator {
        private final NullSafeValidator<Collection<?>> validator;
        
        public CollectionValidator(NullSafeValidator<Collection<?>> validator) {
            this.validator = validator;
        }
        
        /**
         * Validates minimum size.
         */
        public CollectionValidator minSize(int minSize) {
            validator.rule("min_size", 
                         c -> c != null && c.size() >= minSize,
                         "Collection must have at least " + minSize + " elements");
            return this;
        }
        
        /**
         * Validates maximum size.
         */
        public CollectionValidator maxSize(int maxSize) {
            validator.rule("max_size", 
                         c -> c != null && c.size() <= maxSize,
                         "Collection must have at most " + maxSize + " elements");
            return this;
        }
        
        /**
         * Validates exact size.
         */
        public CollectionValidator size(int exactSize) {
            validator.rule("exact_size", 
                         c -> c != null && c.size() == exactSize,
                         "Collection must have exactly " + exactSize + " elements");
            return this;
        }
        
        /**
         * Validates that collection is not empty.
         */
        public CollectionValidator notEmpty() {
            validator.rule("not_empty", 
                         c -> c != null && !c.isEmpty(),
                         "Collection cannot be empty");
            return this;
        }
        
        public NullSafeValidator<Collection<?>> and() {
            return validator;
        }
    }
}