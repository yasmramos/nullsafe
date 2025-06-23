package com.github.nullsafe;

import com.github.nullsafe.result.Result;
import org.junit.jupiter.api.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NullSafeTest {

    @Test
    void testOf_createsNonNullInstance() {
        NullSafe<String> ns = NullSafe.of("test");
        assertTrue(ns.isPresent());
        assertEquals("test", ns.get());
    }

    @Test
    void testEmpty_createsNullInstance() {
        NullSafe<String> ns = NullSafe.empty();
        assertTrue(ns.isEmpty());
        assertThrows(NoSuchElementException.class, ns::get);
    }

    @Test
    void testIsPresent_returnsTrueForNonNull() {
        assertTrue(NullSafe.of("value").isPresent());
    }

    @Test
    void testIsEmpty_returnsTrueForNull() {
        assertTrue(NullSafe.empty().isEmpty());
    }

    @Test
    void testGet_returnsValueIfPresent() {
        assertEquals("hello", NullSafe.of("hello").get());
    }

    @Test
    void testGet_throwsExceptionIfEmpty() {
        assertThrows(NoSuchElementException.class, NullSafe.<String>empty()::get);
    }

    @Test
    void testOrElse_returnsValueIfPresent() {
        assertEquals("default", NullSafe.<String>empty().orElse("default"));
        assertEquals("actual", NullSafe.of("actual").orElse("default"));
    }

    @Test
    void testOrElseGet_returnsValueIfPresent() {
        assertEquals("def", NullSafe.<String>empty().orElseGet(() -> "def"));
        assertEquals("abc", NullSafe.of("abc").orElseGet(() -> "def"));
    }

    @Test
    void testOrElseThrow_throwsIfEmpty() {
        assertThrows(IllegalStateException.class,
                () -> NullSafe.<String>empty().orElseThrow(() -> new IllegalStateException("Missing")));
    }

    @Test
    void testOrElseThrow_returnsValueIfPresent() {
        assertEquals("value", NullSafe.of("value").orElseThrow(() -> new IllegalStateException()));
    }

    @Test
    void testIfPresent_executesActionIfValueExists() {
        final String[] result = {null};
        NullSafe.of("hello").ifPresent(v -> result[0] = v);
        assertEquals("hello", result[0]);
    }

    @Test
    void testIfAbsent_executesActionIfNoValue() {
        final boolean[] called = {false};
        NullSafe.<String>empty().ifAbsent(() -> called[0] = true);
        assertTrue(called[0]);
    }

    @Test
    void testMap_appliesFunctionIfValueExists() {
        NullSafe<Integer> mapped = NullSafe.of(10).map(x -> x * 2);
        assertEquals(20, mapped.get().intValue());
    }

    @Test
    void testMap_returnsEmptyIfEmpty() {
        assertTrue(NullSafe.<Integer>empty().map(x -> x * 2).isEmpty());
    }

    @Test
    void testFlatMap_appliesFunctionIfValueExists() {
        NullSafe<String> result = NullSafe.of(10)
                .flatMap(x -> NullSafe.of("value" + x));
        assertEquals("value10", result.get());
    }

    @Test
    void testFlatMap_returnsEmptyIfEmpty() {
        assertTrue(NullSafe.<Integer>empty()
                .flatMap(x -> NullSafe.of("value"))
                .isEmpty());
    }

    @Test
    void testFilter_returnsSameIfMatches() {
        NullSafe<Integer> filtered = NullSafe.of(10).filter(x -> x > 5);
        assertEquals(10, filtered.get().intValue());
    }

    @Test
    void testFilter_returnsEmptyIfNotMatches() {
        assertTrue(NullSafe.of(3).filter(x -> x > 5).isEmpty());
    }

    @Test
    void testToOptional_convertsCorrectly() {
        Optional<String> optional = NullSafe.of("test").toOptional();
        assertTrue(optional.isPresent());
        assertEquals("test", optional.get());
    }

    @Test
    void testFromOptional_convertsCorrectly() {
        NullSafe<String> ns = NullSafe.fromOptional(Optional.of("hello"));
        assertEquals("hello", ns.get());
    }

    @Test
    void testOptionalMap_mapsCorrectly() {
        String result = NullSafe.of("hello")
                .optionalMap(opt -> opt.map(String::toUpperCase).orElse("DEFAULT"));
        assertEquals("HELLO", result);
    }

    @Test
    void testAdapt_usesAdapterCorrectly() {
        NullSafe<String> result = NullSafe.of("  world  ")
                .adapt(ns -> ns.map(String::trim).map(s -> "hello " + s));
        assertEquals("hello world", result.get());
    }

    @Test
    void testRecover_recoversWithDefaultValue() {
        Integer recovered = NullSafe.<Integer>empty()
                .recover(ex -> 42)
                .orElse(-1);
        assertEquals(42, recovered);
    }

    @Test
    void testRecoverWith_recoversUsingAnotherNullSafe() {
        Integer recovered = NullSafe.<Integer>empty()
                .recoverWith(ex -> NullSafe.of(99))
                .orElse(-1);
        assertEquals(99, recovered);
    }

    @Test
    void testValidate_passesValidation() {
        NullSafe.of("valid")
                .validate(s -> s.equals("valid"), () -> new IllegalArgumentException("Invalid"))
                .ifPresent(v -> {
                });
    }

    @Test
    void testValidate_throwsOnFailedValidation() {
        assertThrows(IllegalArgumentException.class, ()
                -> NullSafe.of("invalid")
                        .validate(s -> s.equals("valid"), () -> new IllegalArgumentException("Invalid format"))
        );
    }

    @Test
    void testAs_castsCorrectType() {
        NullSafe<Number> number = NullSafe.of((Number) 123);
        Integer i = number.as(Integer.class).orElse(-1);
        assertEquals(123, i.intValue());
    }

    @Test
    void testAsStream_returnsSingleElementStream() {
        assertEquals(1, NullSafe.of("test").stream().count());
    }

    @Test
    void testAsStream_returnsEmptyStreamWhenEmpty() {
        assertEquals(0, NullSafe.<String>empty().stream().count());
    }

    @Test
    void testCombine_twoValuesCombined() {
        NullSafe<Integer> result = NullSafe.combine(
                NullSafe.of(3),
                NullSafe.of(5),
                (a, b) -> a + b
        );
        assertEquals(8, result.get().intValue());
    }

    @Test
    void testCombine_oneEmpty_returnsEmpty() {
        assertTrue(NullSafe.combine(
                NullSafe.of(3),
                NullSafe.<Integer>empty(),
                (a, b) -> a + b
        ).isEmpty());
    }

    @Test
    void testLogIfPresent_printsValue() {
        // You can check console output manually if needed
        NullSafe.of("logging-test").logIfPresent("Value present: ");
    }

    @Test
    void testLogIfAbsent_printsMessageWhenEmpty() {
        // You can check console output manually if needed
        NullSafe.<String>empty().logIfAbsent("No value found");
    }

    @Test
    void testToResult_returnsSuccess() {
        Result<String, String> result = NullSafe.of("success").toResult("error");
        assertTrue(result.isSuccess());
        assertEquals("success", result.get());
    }

    @Test
    void testToResult_returnsFailure() {
        Result<String, String> result = NullSafe.<String>empty().toResult("error");
        assertTrue(result.isFailure());
        assertEquals("error", result.getError().get());
    }

    @Test
    void testToResult_withSupplier_returnsFailure() {
        Result<String, String> result = NullSafe.<String>empty().toResult(() -> "custom error");
        assertTrue(result.isFailure());
        assertEquals("custom error", result.getError().get());
    }
}
