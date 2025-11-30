/**
 * Comprehensive examples showcasing all NullSafe features.
 * This class demonstrates the complete functionality of the NullSafe library.
 * 
 * @since 1.0
 */
public class CompleteNullSafeExamples {
    
    public static void main(String[] args) {
        // Basic NullSafe examples
        demonstrateBasicNullSafe();
        
        // Primitive NullSafe examples
        demonstratePrimitiveNullSafe();
        
        // Collection examples
        demonstrateCollections();
        
        // Stream integration examples
        demonstrateStreamIntegration();
        
        // Async examples
        demonstrateAsyncOperations();
        
        // Validation examples
        demonstrateValidation();
        
        // Transformation examples
        demonstrateTransformations();
        
        // Performance optimization examples
        demonstratePerformanceOptimizations();
        
        // Advanced patterns examples
        demonstrateAdvancedPatterns();
    }
    
    /**
     * Demonstrates basic NullSafe operations.
     */
    public static void demonstrateBasicNullSafe() {
        System.out.println("=== Basic NullSafe Examples ===");
        
        // Create NullSafe from null
        NullSafe<String> nullValue = NullSafe.of(null);
        System.out.println("Null value: " + nullValue.orElse("Default"));
        
        // Create NullSafe from value
        NullSafe<String> presentValue = NullSafe.of("Hello, NullSafe!");
        presentValue.ifPresent(System.out::println);
        
        // Map and flatMap operations
        NullSafe<Integer> mapped = presentValue
            .map(String::length)
            .map(n -> n * 2);
        System.out.println("Mapped value: " + mapped.orElse(0));
        
        // Filter operations
        NullSafe<String> filtered = presentValue.filter(s -> s.length() > 5);
        System.out.println("Filtered result: " + filtered.orElse("Too short"));
        
        // Chained operations
        String result = NullSafe.of("test@example.com")
            .map(String::toLowerCase)
            .filter(s -> s.contains("@"))
            .map(s -> s.substring(0, s.indexOf("@")))
            .orElse("unknown");
        System.out.println("Username extraction: " + result);
        
        System.out.println();
    }
    
    /**
     * Demonstrates primitive NullSafe classes.
     */
    public static void demonstratePrimitiveNullSafe() {
        System.out.println("=== Primitive NullSafe Examples ===");
        
        // Integer operations
        NullSafeInt.of(42)
            .map(n -> n * 2)
            .filter(n -> n > 50)
            .ifPresent(n -> System.out.println("Double of 42: " + n));
        
        // Long operations
        NullSafeLong.of(1000L)
            .map(n -> n / 100)
            .orElseGet(() -> -1L);
        
        // Double operations
        NullSafeDouble.of(3.14159)
            .map(Math::sqrt)
            .ifPresent(System.out::println);
        
        // Boolean operations
        boolean isValid = NullSafeBoolean.of(true)
            .and(NullSafeBoolean.of(true))
            .or(NullSafeBoolean.of(false))
            .get();
        System.out.println("Boolean logic result: " + isValid);
        
        System.out.println();
    }
    
    /**
     * Demonstrates NullSafe collection operations.
     */
    public static void demonstrateCollections() {
        System.out.println("=== NullSafe Collections Examples ===");
        
        // NullSafeList examples
        List<String> list = Arrays.asList("apple", null, "banana", "cherry", null);
        NullSafeList<String> safeList = NullSafeList.of(list);
        
        System.out.println("Original list size: " + safeList.size());
        System.out.println("Filtered list: " + safeList.filter(s -> s.startsWith("a")).toList());
        
        // Map and transform
        List<Integer> lengths = safeList
            .map(String::length)
            .toList();
        System.out.println("String lengths: " + lengths);
        
        // Find operations
        NullSafe<String> firstLongString = safeList.find(s -> s.length() > 5);
        System.out.println("First long string: " + firstLongString.orElse("None"));
        
        // NullSafeMap examples
        Map<String, String> map = Map.of("key1", "value1", "key2", null, "key3", "value3");
        NullSafeMap<String, String> safeMap = NullSafeMap.of(map);
        
        String value = safeMap.get("key1").orElse("default");
        System.out.println("Map value: " + value);
        
        // Map operations
        Map<String, Integer> lengthMap = safeMap
            .mapValues(s -> s != null ? s.length() : 0)
            .toMap();
        System.out.println("Value lengths: " + lengthMap);
        
        // NullSafeSet examples
        Set<String> set = Set.of("A", "B", "C", "D");
        NullSafeSet<String> safeSet = NullSafeSet.of(set);
        
        NullSafeSet<String> filteredSet = safeSet
            .filter(s -> s.compareTo("B") > 0)
            .map(String::toLowerCase);
        System.out.println("Filtered set: " + filteredSet.toList());
        
        // NullSafeArray examples
        String[] array = {"element1", null, "element2", "element3"};
        NullSafeArray<String> safeArray = NullSafeArray.of(array);
        
        NullSafeArray<Integer> lengthsArray = safeArray
            .map(String::length);
        System.out.println("Array lengths: " + Arrays.toString(lengthsArray.toArray()));
        
        System.out.println();
    }
    
    /**
     * Demonstrates Java Streams integration.
     */
    public static void demonstrateStreamIntegration() {
        System.out.println("=== NullSafe Stream Integration Examples ===");
        
        // Create stream from collection
        List<String> data = Arrays.asList("stream", null, "integration", "test");
        NullSafeStreamOperation<String> stream = NullSafeStream.of(data);
        
        // Stream operations
        List<String> processed = stream
            .map(String::toUpperCase)
            .filter(s -> s.length() > 5)
            .toList();
        System.out.println("Processed stream: " + processed);
        
        // Find operations
        NullSafe<String> longest = stream
            .max(Comparator.comparingInt(String::length));
        System.out.println("Longest string: " + longest.orElse("None"));
        
        // Reduction operations
        String concatenated = stream
            .reduce("", String::concat);
        System.out.println("Concatenated: " + concatenated);
        
        // Grouping operations
        Map<Integer, NullSafeList<String>> grouped = stream
            .groupBy(String::length);
        System.out.println("Grouped by length: " + grouped);
        
        // Primitive streams
        NullSafeIntStreamOperation intStream = NullSafeStream.range(1, 10);
        int sum = intStream.sum();
        System.out.println("Sum of 1-9: " + sum);
        
        NullSafeList<Integer> doubled = intStream
            .map(n -> n * 2)
            .toList();
        System.out.println("Doubled values: " + doubled);
        
        System.out.println();
    }
    
    /**
     * Demonstrates asynchronous operations.
     */
    public static void demonstrateAsyncOperations() {
        System.out.println("=== NullSafe Async Operations Examples ===");
        
        // Create async operations
        NullSafeFuture<String> asyncResult = NullSafeFuture.fromSupplier(() -> {
            try {
                Thread.sleep(100);
                return "Async result";
            } catch (InterruptedException e) {
                return null;
            }
        });
        
        // Chain async operations
        NullSafeFuture<String> chained = asyncResult
            .mapValue(String::toUpperCase)
            .mapValue(s -> s + " (processed)");
        
        // Wait for result
        try {
            NullSafe<String> result = chained.get(1, TimeUnit.SECONDS);
            System.out.println("Async result: " + result.orElse("Timeout"));
        } catch (Exception e) {
            System.out.println("Async operation failed: " + e.getMessage());
        }
        
        // Multiple async operations
        List<NullSafeFuture<Integer>> futures = Arrays.asList(
            NullSafeFuture.fromSupplier(() -> 1),
            NullSafeFuture.fromSupplier(() -> 2),
            NullSafeFuture.fromSupplier(() -> 3)
        );
        
        NullSafeFuture<NullSafeList<Integer>> allResults = NullSafeAsync.allOf(futures);
        try {
            NullSafeList<Integer> results = allResults.get(1, TimeUnit.SECONDS);
            System.out.println("All results: " + results.toList());
        } catch (Exception e) {
            System.out.println("Multiple async failed: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Demonstrates validation operations.
     */
    public static void demonstrateValidation() {
        System.out.println("=== NullSafe Validation Examples ===");
        
        // Email validation
        String email = "user@example.com";
        ValidationResult emailValidation = NullSafeValidator.of(NullSafe.of(email))
            .notNull()
            .isEmail()
            .validate();
        
        System.out.println("Email validation: " + emailValidation.summary());
        
        // User object validation
        User user = new User("John", "john@example.com", 25);
        ValidationResult userValidation = NullSafeValidator.of(NullSafe.of(user))
            .custom(userValidator -> 
                user.getAge() >= 18 && 
                user.getEmail() != null && 
                user.getName() != null)
            .validate();
        
        System.out.println("User validation: " + userValidation.summary());
        
        // Advanced string validation
        String password = "StrongPass123!";
        ValidationResult passwordValidation = NullSafeValidator.of(NullSafe.of(password))
            .ifString()
            .minLength(8)
            .maxLength(20)
            .matches(".*[A-Z].*")
            .matches(".*[0-9].*")
            .matches(".*[!@#$%^&*].*")
            .and()
            .validate();
        
        System.out.println("Password validation: " + passwordValidation.summary());
        
        // Numeric validation
        ValidationResult ageValidation = NullSafeValidator.of(NullSafe.of(25))
            .ifNumber()
            .range(0, 120)
            .positive()
            .and()
            .validate();
        
        System.out.println("Age validation: " + ageValidation.summary());
        
        System.out.println();
    }
    
    /**
     * Demonstrates transformation operations.
     */
    public static void demonstrateTransformations() {
        System.out.println("=== NullSafe Transformation Examples ===");
        
        // Simple transformation
        NullSafe<String> original = NullSafe.of("Hello World");
        NullSafe<String> transformed = NullSafeTransformer.from(original)
            .map(String::toUpperCase)
            .map(s -> s.replace(" ", "_"))
            .transform();
        System.out.println("Transformed: " + transformed.orElse("Failed"));
        
        // Complex transformation with validation
        NullSafe<String> email = NullSafe.of("user@example.com");
        NullSafe<String> processed = NullSafeTransformer.from(email)
            .mapWithValidation(
                String::toLowerCase,
                s -> s.contains("@")
            )
            .mapWithErrorHandling(
                s -> s.split("@")[0],
                e -> "default_user"
            )
            .transform();
        System.out.println("Processed email: " + processed.orElse("Invalid"));
        
        // Conditional transformation
        NullSafe<Integer> number = NullSafe.of(15);
        String result = NullSafeTransformer.from(number)
            .mapConditional(
                n -> n > 10,
                n -> "Greater than 10",
                n -> "Less than or equal 10"
            )
            .transform()
            .orElse("No result");
        System.out.println("Conditional result: " + result);
        
        // Collection transformation
        List<String> data = Arrays.asList("test", "data", "processing");
        NullSafe<List<String>> safeList = NullSafe.of(data);
        List<Integer> lengths = NullSafeTransformer.from(safeList)
            .map(List::size)
            .transform()
            .orElse(0);
        System.out.println("List size: " + lengths);
        
        System.out.println();
    }
    
    /**
     * Demonstrates performance optimizations.
     */
    public static void demonstratePerformanceOptimizations() {
        System.out.println("=== NullSafe Performance Examples ===");
        
        // Cached NullSafe
        NullSafe<String> cached = NullSafePerformance.cached(() -> {
            System.out.println("Computing cached value...");
            return "Expensive computation result";
        });
        
        System.out.println("First access: " + cached.orElse("Empty"));
        System.out.println("Second access: " + cached.orElse("Empty")); // Should not compute again
        
        // Time-based cached NullSafe
        NullSafe<String> timeCached = NullSafePerformance.timeCached(() -> {
            return "Time-based cached value";
        }, 1000); // 1 second TTL
        
        System.out.println("Time cached: " + timeCached.orElse("Empty"));
        
        // Memoized function
        Function<String, NullSafe<Integer>> memoizedLength = NullSafePerformance.memoized(String::length);
        
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            memoizedLength.apply("test"); // Should be cached after first call
        }
        long end = System.nanoTime();
        
        System.out.println("Memoized function timing: " + (end - start) / 1_000_000 + " ms");
        
        // Performance monitoring
        NullSafePerformanceMonitor monitor = new NullSafePerformanceMonitor();
        
        NullSafe<String> monitored = monitor.recordTime("expensive_operation", () -> 
            NullSafe.of("Monitored operation result")
        );
        
        System.out.println("Monitored result: " + monitored.orElse("Failed"));
        System.out.println("Monitor summary:\n" + monitor.getSummary());
        
        monitor.close();
        
        System.out.println();
    }
    
    /**
     * Demonstrates advanced patterns.
     */
    public static void demonstrateAdvancedPatterns() {
        System.out.println("=== NullSafe Advanced Patterns ===");
        
        // Circuit breaker pattern simulation
        NullSafe<String> circuitBreaker = NullSafe.of("service_result")
            .filter(s -> !s.equals("error"))
            .orElseGet(() -> {
                // Fallback logic
                return "fallback_result";
            });
        System.out.println("Circuit breaker result: " + circuitBreaker);
        
        // Retry pattern simulation
        int attempts = 0;
        NullSafe<String> retryResult = NullSafe.empty();
        
        while (attempts < 3 && retryResult.isEmpty()) {
            final int attempt = attempts;
            retryResult = NullSafe.of("operation_result_" + attempt)
                .filter(s -> !s.contains("error"));
            attempts++;
        }
        
        System.out.println("Retry result: " + retryResult.orElse("All attempts failed"));
        
        // Builder pattern with NullSafe
        UserBuilder builder = new UserBuilder();
        User user = builder
            .withName("Alice")
            .withEmail("alice@example.com")
            .withAge(30)
            .build();
        System.out.println("Built user: " + user);
        
        // Factory pattern with NullSafe
        NullSafe<User> factoryUser = UserFactory.createUser("Bob", "bob@example.com", 25);
        System.out.println("Factory user: " + factoryUser.map(User::getName).orElse("Unknown"));
        
        // Observer pattern with NullSafe
        NullSafeProperty<String> property = new NullSafeProperty<>("initial_value");
        property.addObserver(new NullSafePropertyObserver<>() {
            @Override
            public void onChanged(NullSafe<String> newValue) {
                System.out.println("Property changed to: " + newValue.orElse("null"));
            }
        });
        
        property.setValue("new_value");
        
        System.out.println();
    }
    
    /**
     * User class for examples.
     */
    static class User {
        private final String name;
        private final String email;
        private final int age;
        
        public User(String name, String email, int age) {
            this.name = name;
            this.email = email;
            this.age = age;
        }
        
        public String getName() { return name; }
        public String getEmail() { return email; }
        public int getAge() { return age; }
        
        @Override
        public String toString() {
            return String.format("User{name='%s', email='%s', age=%d}", name, email, age);
        }
    }
    
    /**
     * User builder class.
     */
    static class UserBuilder {
        private String name;
        private String email;
        private int age;
        
        public UserBuilder withName(String name) {
            this.name = name;
            return this;
        }
        
        public UserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }
        
        public UserBuilder withAge(int age) {
            this.age = age;
            return this;
        }
        
        public User build() {
            return new User(name, email, age);
        }
    }
    
    /**
     * User factory class.
     */
    static class UserFactory {
        public static NullSafe<User> createUser(String name, String email, int age) {
            if (name != null && email != null && age > 0) {
                return NullSafe.of(new User(name, email, age));
            }
            return NullSafe.empty();
        }
    }
    
    /**
     * NullSafe property for observer pattern.
     */
    static class NullSafeProperty<T> {
        private T value;
        private final List<NullSafePropertyObserver<T>> observers = new ArrayList<>();
        
        public NullSafeProperty(T initialValue) {
            this.value = initialValue;
        }
        
        public void setValue(T newValue) {
            T oldValue = this.value;
            this.value = newValue;
            
            // Notify observers
            for (NullSafePropertyObserver<T> observer : observers) {
                observer.onChanged(NullSafe.of(newValue));
            }
        }
        
        public NullSafe<T> getValue() {
            return NullSafe.of(value);
        }
        
        public void addObserver(NullSafePropertyObserver<T> observer) {
            observers.add(observer);
        }
    }
    
    /**
     * Observer interface for NullSafe properties.
     */
    interface NullSafePropertyObserver<T> {
        void onChanged(NullSafe<T> newValue);
    }
}