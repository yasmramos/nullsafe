public class ValidationResult {
    private final String ruleName;
    private final boolean valid;
    private final String message;
    
    public ValidationResult(String ruleName, boolean valid, String message) {
        this.ruleName = ruleName;
        this.valid = valid;
        this.message = message;
    }
    
    public String getRuleName() {
        return ruleName;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return "ValidationResult{ruleName='" + ruleName + "', valid=" + valid + ", message='" + message + "'}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ValidationResult that = (ValidationResult) obj;
        return valid == that.valid &&
               Objects.equals(ruleName, that.ruleName) &&
               Objects.equals(message, that.message);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ruleName, valid, message);
    }
}