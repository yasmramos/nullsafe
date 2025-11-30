import java.util.function.Predicate;

public class ValidationRule<T> {
    public final String name;
    public final Predicate<T> predicate;
    public final String errorMessage;
    
    public ValidationRule(String name, Predicate<T> predicate, String errorMessage) {
        this.name = name;
        this.predicate = predicate;
        this.errorMessage = errorMessage;
    }
}