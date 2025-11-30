import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Represents a single validation rule.
 * 
 * @param <T> the type being validated
 * @since 1.0
 */
class ValidationRule<T> {
    private final String name;
    private final Predicate<T> predicate;
    private final String errorMessage;

    public ValidationRule(String name, Predicate<T> predicate, String errorMessage) {
        this.name = name;
        this.predicate = predicate;
        this.errorMessage = errorMessage;
    }

    public String getName() {
        return name;
    }

    public Predicate<T> getPredicate() {
        return predicate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ValidationRule<?> that = (ValidationRule<?>) obj;
        return Objects.equals(name, that.name) &&
               Objects.equals(predicate, that.predicate) &&
               Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, predicate, errorMessage);
    }
}

/**
 * Represents the result of a validation.
 * 
 * @since 1.0
 */
public class ValidationResult {
    private final String ruleName;
    private final boolean valid;
    private final String message;
    private final List<ValidationResult> subResults;
    
    /**
     * Creates a simple validation result.
     * 
     * @param ruleName the rule name
     * @param valid whether the validation passed
     * @param message the result message
     */
    public ValidationResult(String ruleName, boolean valid, String message) {
        this(ruleName, valid, message, Collections.emptyList());
    }
    
    /**
     * Creates a validation result with sub-results.
     * 
     * @param ruleName the rule name
     * @param valid whether the validation passed
     * @param subResults the sub-results
     */
    public ValidationResult(String ruleName, boolean valid, List<ValidationResult> subResults) {
        this.ruleName = ruleName;
        this.valid = valid;
        this.subResults = new ArrayList<>(subResults);
        this.message = buildMessage(valid, subResults);
    }
    
    /**
     * Creates a validation result with message and sub-results.
     * 
     * @param ruleName the rule name
     * @param valid whether the validation passed
     * @param message the result message
     * @param subResults the sub-results
     */
    public ValidationResult(String ruleName, boolean valid, String message, List<ValidationResult> subResults) {
        this.ruleName = ruleName;
        this.valid = valid;
        this.message = message;
        this.subResults = new ArrayList<>(subResults);
    }
    
    private String buildMessage(boolean valid, List<ValidationResult> subResults) {
        if (subResults.isEmpty()) {
            return valid ? "Validation passed" : "Validation failed";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(valid ? "All validations passed" : "Validation failed");
        
        List<String> failures = subResults.stream()
            .filter(result -> !result.valid)
            .map(result -> result.ruleName + ": " + result.message)
            .collect(Collectors.toList());
            
        if (!failures.isEmpty()) {
            sb.append(" - ").append(String.join("; ", failures));
        }
        
        return sb.toString();
    }
    
    /**
     * Gets the rule name.
     * 
     * @return the rule name
     */
    public String getRuleName() {
        return ruleName;
    }
    
    /**
     * Checks if validation passed.
     * 
     * @return true if validation passed
     */
    public boolean isValid() {
        return valid;
    }
    
    /**
     * Gets the result message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Gets the sub-results.
     * 
     * @return the sub-results
     */
    public List<ValidationResult> getSubResults() {
        return new ArrayList<>(subResults);
    }
    
    /**
     * Gets only the failed validations.
     * 
     * @return list of failed validations
     */
    public List<ValidationResult> getFailures() {
        return subResults.stream()
            .filter(result -> !result.valid)
            .collect(Collectors.toList());
    }
    
    /**
     * Gets only the passed validations.
     * 
     * @return list of passed validations
     */
    public List<ValidationResult> getPasses() {
        return subResults.stream()
            .filter(ValidationResult::valid)
            .collect(Collectors.toList());
    }
    
    /**
     * Adds a sub-result.
     * 
     * @param result the sub-result to add
     */
    public void addSubResult(ValidationResult result) {
        subResults.add(result);
    }
    
    /**
     * Combines this result with another.
     * 
     * @param other the other result
     * @return combined result
     */
    public ValidationResult combine(ValidationResult other) {
        boolean combinedValid = this.valid && other.valid;
        List<ValidationResult> combined = new ArrayList<>();
        combined.add(this);
        combined.add(other);
        combined.addAll(other.subResults);
        
        return new ValidationResult("combined", combinedValid, combined);
    }
    
    /**
     * Creates a summary string.
     * 
     * @return summary string
     */
    public String summary() {
        if (subResults.isEmpty()) {
            return String.format("%s: %s", ruleName, valid ? "PASS" : "FAIL");
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: %s\n", ruleName, valid ? "PASS" : "FAIL"));
        
        for (ValidationResult subResult : subResults) {
            String[] lines = subResult.summary().split("\n");
            for (String line : lines) {
                sb.append("  ").append(line).append("\n");
            }
        }
        
        return sb.toString().trim();
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s (%s)", ruleName, valid ? "VALID" : "INVALID", message);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ValidationResult that = (ValidationResult) obj;
        return Objects.equals(ruleName, that.ruleName) &&
               valid == that.valid &&
               Objects.equals(message, that.message) &&
               Objects.equals(subResults, that.subResults);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ruleName, valid, message, subResults);
    }
}