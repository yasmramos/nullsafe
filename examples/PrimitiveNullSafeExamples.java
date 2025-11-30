package com.github.nullsafe.examples;

import com.github.nullsafe.*;

/**
 * Comprehensive examples demonstrating all NullSafe primitive types
 * and their practical applications in real-world scenarios
 */
public class PrimitiveNullSafeExamples {

    public static void main(String[] args) {
        demonstrateBasicUsage();
        demonstrateRealWorldScenarios();
        demonstrateAdvancedFeatures();
        demonstrateEdgeCases();
    }

    /**
     * Basic usage examples for each primitive type
     */
    public static void demonstrateBasicUsage() {
        System.out.println("=== BASIC USAGE EXAMPLES ===\n");

        // NullSafeInt examples
        System.out.println("NullSafeInt Examples:");
        int intResult = NullSafeInt.of(42)
            .map(v -> v * 2)
            .filter(v -> v > 50)
            .orElse(0);
        System.out.println("Int result: " + intResult);

        // NullSafeLong examples
        System.out.println("NullSafeLong Examples:");
        long longResult = NullSafeLong.empty()
            .orElseGet(() -> System.currentTimeMillis());
        System.out.println("Long result: " + longResult);

        // NullSafeDouble examples
        System.out.println("NullSafeDouble Examples:");
        double doubleResult = NullSafeDouble.of(3.14159)
            .map(Math::sqrt)
            .orElse(0.0);
        System.out.println("Double result: " + doubleResult);

        // NullSafeFloat examples
        System.out.println("NullSafeFloat Examples:");
        float floatResult = NullSafeFloat.of(2.5f)
            .filter(f -> f > 0)
            .orElseGet(() -> {
                System.out.println("Using default float value");
                return 1.0f;
            });
        System.out.println("Float result: " + floatResult);

        // NullSafeBoolean examples
        System.out.println("NullSafeBoolean Examples:");
        boolean boolResult = NullSafeBoolean.of(true)
            .map(b -> !b)
            .orElse(false);
        System.out.println("Boolean result: " + boolResult);

        // NullSafeByte examples
        System.out.println("NullSafeByte Examples:");
        byte byteResult = NullSafeByte.of((byte) 127)
            .filter(b -> b > 0)
            .orElse((byte) 0);
        System.out.println("Byte result: " + byteResult);

        // NullSafeShort examples
        System.out.println("NullSafeShort Examples:");
        short shortResult = NullSafeShort.of((short) 1000)
            .map(s -> (short) (s / 2))
            .orElse((short) 500);
        System.out.println("Short result: " + shortResult);
    }

    /**
     * Real-world scenarios using NullSafe primitive types
     */
    public static void demonstrateRealWorldScenarios() {
        System.out.println("\n=== REAL-WORLD SCENARIOS ===\n");

        // Financial calculations with NullSafeDouble
        demonstrateFinancialCalculations();

        // Data processing with NullSafeLong
        demonstrateDataProcessing();

        // Configuration validation with NullSafeInt
        demonstrateConfigurationValidation();

        // User input validation with NullSafeBoolean
        demonstrateUserInputValidation();
    }

    /**
     * Financial calculations using NullSafeDouble
     */
    public static void demonstrateFinancialCalculations() {
        System.out.println("Financial Calculations:");

        // Calculate compound interest safely
        double principal = NullSafeDouble.of(1000.0)
            .filter(p -> p > 0)
            .map(p -> p * Math.pow(1 + 0.05, 2)) // 5% annual rate for 2 years
            .orElse(0.0);

        System.out.println("Compound interest result: $" + String.format("%.2f", principal));

        // Validate price ranges
        double productPrice = NullSafeDouble.of(29.99)
            .validate(p -> p > 0 && p < 10000, "Precio debe estar entre $0 y $10,000")
            .orElseGet(() -> {
                System.out.println("Precio inválido, usando precio por defecto");
                return 9.99;
            });

        System.out.println("Product price: $" + productPrice);
    }

    /**
     * Data processing using NullSafeLong
     */
    public static void demonstrateDataProcessing() {
        System.out.println("\nData Processing:");

        // Process user ID safely
        Long userId = NullSafeLong.of(123456L)
            .filter(id -> id > 0)
            .filter(id -> id < 1000000000L)
            .orElseGet(() -> {
                System.out.println("Invalid user ID, generating new one");
                return System.currentTimeMillis();
            });

        System.out.println("User ID: " + userId);

        // Process timestamps
        long timestamp = NullSafeLong.of(System.currentTimeMillis())
            .map(ts -> ts / 1000) // Convert to seconds
            .orElse(0L);

        System.out.println("Timestamp (seconds): " + timestamp);
    }

    /**
     * Configuration validation using NullSafeInt
     */
    public static void demonstrateConfigurationValidation() {
        System.out.println("\nConfiguration Validation:");

        // Validate port number
        int port = NullSafeInt.of(8080)
            .validate(p -> p >= 1 && p <= 65535, "Puerto debe estar entre 1 y 65535")
            .orElseThrow(() -> new IllegalArgumentException("Puerto inválido"));

        System.out.println("Server port: " + port);

        // Validate pool size
        int poolSize = NullSafeInt.of(10)
            .filter(size -> size > 0)
            .filter(size -> size <= 100)
            .map(size -> size * 2) // Double the pool size
            .orElse(5);

        System.out.println("Pool size: " + poolSize);
    }

    /**
     * User input validation using NullSafeBoolean
     */
    public static void demonstrateUserInputValidation() {
        System.out.println("\nUser Input Validation:");

        // Validate email confirmation
        boolean emailConfirmed = NullSafeBoolean.of(true)
            .orElse(false);

        System.out.println("Email confirmed: " + emailConfirmed);

        // Process feature toggle
        boolean featureEnabled = NullSafeBoolean.empty()
            .orElse(false);

        System.out.println("Feature enabled: " + featureEnabled);

        // Validate password strength
        boolean passwordStrong = NullSafeBoolean.of("password123".length() >= 8)
            .map(isStrong -> {
                System.out.println("Password validation: " + (isStrong ? "Strong" : "Weak"));
                return isStrong;
            })
            .orElse(false);

        System.out.println("Password is strong: " + passwordStrong);
    }

    /**
     * Advanced features and edge cases
     */
    public static void demonstrateAdvancedFeatures() {
        System.out.println("\n=== ADVANCED FEATURES ===\n");

        // Chaining operations
        demonstrateChainingOperations();

        // Exception handling
        demonstrateExceptionHandling();

        // Conditional execution
        demonstrateConditionalExecution();
    }

    /**
     * Chaining operations across different types
     */
    public static void demonstrateChainingOperations() {
        System.out.println("Chaining Operations:");

        // Complex calculation chain
        double result = NullSafeDouble.of(100.0)
            .map(d -> d * 1.1) // Add 10%
            .filter(d -> d > 50) // Must be > 50
            .map(d -> d - 5) // Subtract 5
            .orElse(0.0);

        System.out.println("Chained calculation result: " + result);

        // Int to Long conversion
        long convertedValue = NullSafeInt.of(42)
            .map(i -> (long) i * 1000)
            .orElse(0L);

        System.out.println("Converted value: " + convertedValue);
    }

    /**
     * Exception handling patterns
     */
    public static void demonstrateExceptionHandling() {
        System.out.println("\nException Handling:");

        // Safe division with custom exception
        double divisionResult;
        try {
            divisionResult = NullSafeDouble.of(10.0)
                .map(d -> d / 0) // This will cause ArithmeticException
                .orElseThrow(() -> new ArithmeticException("División por cero"));
        } catch (ArithmeticException e) {
            System.out.println("Caught exception: " + e.getMessage());
            divisionResult = 0.0;
        }

        System.out.println("Division result: " + divisionResult);

        // Invalid configuration handling
        int maxRetries = NullSafeInt.of(-1)
            .validate(r -> r >= 0 && r <= 10, "Max retries must be between 0 and 10")
            .orElseThrow(() -> new IllegalArgumentException("Configuración inválida"));

        System.out.println("Max retries: " + maxRetries);
    }

    /**
     * Conditional execution based on values
     */
    public static void demonstrateConditionalExecution() {
        System.out.println("\nConditional Execution:");

        // Execute actions based on presence
        NullSafeInt.of(100)
            .ifPresent(value -> System.out.println("Processing value: " + value))
            .ifAbsent(() -> System.out.println("No value to process"));

        NullSafeInt.empty()
            .ifPresent(value -> System.out.println("This won't execute"))
            .ifAbsent(() -> System.out.println("This will execute because value is absent"));

        // Dynamic default values
        int dynamicDefault = NullSafeInt.empty()
            .orElseGet(() -> {
                System.out.println("Computing dynamic default...");
                return (int) (Math.random() * 100);
            });

        System.out.println("Dynamic default value: " + dynamicDefault);
    }

    /**
     * Edge cases and special values
     */
    public static void demonstrateEdgeCases() {
        System.out.println("\n=== EDGE CASES ===\n");

        // Handling extreme values
        demonstrateExtremeValues();

        // NaN and infinity handling
        demonstrateSpecialFloatingPointValues();
    }

    /**
     * Testing with extreme values
     */
    public static void demonstrateExtremeValues() {
        System.out.println("Extreme Values:");

        // Long extremes
        long maxLong = NullSafeLong.of(Long.MAX_VALUE)
            .map(l -> l - 1)
            .orElse(0L);
        System.out.println("Max Long - 1: " + maxLong);

        long minLong = NullSafeLong.of(Long.MIN_VALUE)
            .map(l -> l + 1)
            .orElse(0L);
        System.out.println("Min Long + 1: " + minLong);

        // Int extremes
        int maxInt = NullSafeInt.of(Integer.MAX_VALUE)
            .map(i -> i - 1)
            .orElse(0);
        System.out.println("Max Int - 1: " + maxInt);

        int minInt = NullSafeInt.of(Integer.MIN_VALUE)
            .map(i -> i + 1)
            .orElse(0);
        System.out.println("Min Int + 1: " + minInt);
    }

    /**
     * Handling NaN and infinity
     */
    public static void demonstrateSpecialFloatingPointValues() {
        System.out.println("\nSpecial Floating Point Values:");

        // NaN handling
        double nanResult = NullSafeDouble.of(Double.NaN)
            .map(d -> {
                System.out.println("Value is NaN: " + d);
                return d;
            })
            .orElse(0.0);
        System.out.println("NaN handling result: " + nanResult);

        // Infinity handling
        double infResult = NullSafeDouble.of(Double.POSITIVE_INFINITY)
            .filter(d -> !Double.isInfinite(d))
            .orElseGet(() -> {
                System.out.println("Infinite value detected, using default");
                return 1.0;
            });
        System.out.println("Infinity handling result: " + infResult);

        // Very small numbers
        double tinyResult = NullSafeDouble.of(Double.MIN_VALUE)
            .map(d -> d * 2)
            .orElse(0.0);
        System.out.println("Double.MIN_VALUE * 2: " + tinyResult);
    }
}