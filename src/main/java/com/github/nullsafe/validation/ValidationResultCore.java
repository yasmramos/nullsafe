import java.util.*;

public class ValidationResultCore {
    private final String ruleName;
    private final boolean valid;
    private final String message;
    
    public ValidationResultCore(String ruleName, boolean valid, String message) {
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
        return String.format("%s: %s (%s)", ruleName, valid ? "VALID" : "INVALID", message);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ValidationResultCore that = (ValidationResultCore) obj;
        return Objects.equals(ruleName, that.ruleName) &&
               valid == that.valid &&
               Objects.equals(message, that.message);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ruleName, valid, message);
    }
}