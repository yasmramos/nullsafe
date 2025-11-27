package com.github.nullsafe;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for NullSafe primitive types
 */
@DisplayName("NullSafe Primitive Types Tests")
public class NullSafePrimitiveTest {

    // ===== NullSafeLong Tests =====
    @Test
    @DisplayName("NullSafeLong.of() should create instance with value")
    void testNullSafeLongOf() {
        NullSafeLong nsl = NullSafeLong.of(42L);
        assertTrue(nsl.isPresent());
        assertEquals(42L, nsl.get());
    }

    @Test
    @DisplayName("NullSafeLong.empty() should create empty instance")
    void testNullSafeLongEmpty() {
        NullSafeLong nsl = NullSafeLong.empty();
        assertTrue(nsl.isEmpty());
    }

    @Test
    @DisplayName("NullSafeLong.orElse() should return value or default")
    void testNullSafeLongOrElse() {
        NullSafeLong present = NullSafeLong.of(42L);
        NullSafeLong empty = NullSafeLong.empty();

        assertEquals(42L, present.orElse(99L));
        assertEquals(99L, empty.orElse(99L));
    }

    @Test
    @DisplayName("NullSafeLong.orElseGet() should return value or compute default")
    void testNullSafeLongOrElseGet() {
        NullSafeLong present = NullSafeLong.of(42L);
        NullSafeLong empty = NullSafeLong.empty();
        AtomicLong counter = new AtomicLong(0);

        assertEquals(42L, present.orElseGet(counter::incrementAndGet));
        assertEquals(0L, counter.get()); // Counter not incremented for present value

        assertEquals(1L, empty.orElseGet(counter::incrementAndGet));
        assertEquals(1L, counter.get()); // Counter incremented for empty value
    }

    @Test
    @DisplayName("NullSafeLong.orElseThrow() should return value or throw exception")
    void testNullSafeLongOrElseThrow() {
        NullSafeLong present = NullSafeLong.of(42L);
        NullSafeLong empty = NullSafeLong.empty();

        assertEquals(42L, present.orElseThrow(IllegalStateException::new));

        assertThrows(IllegalStateException.class, () -> {
            empty.orElseThrow(IllegalStateException::new);
        });
    }

    @Test
    @DisplayName("NullSafeLong.ifPresent() should execute action when value present")
    void testNullSafeLongIfPresent() {
        NullSafeLong present = NullSafeLong.of(42L);
        NullSafeLong empty = NullSafeLong.empty();
        AtomicLong result = new AtomicLong(0);

        present.ifPresent(result::set);
        empty.ifPresent(result::set);

        assertEquals(42L, result.get());
    }

    @Test
    @DisplayName("NullSafeLong.ifAbsent() should execute action when value absent")
    void testNullSafeLongIfAbsent() {
        NullSafeLong present = NullSafeLong.of(42L);
        NullSafeLong empty = NullSafeLong.empty();
        AtomicInteger counter = new AtomicInteger(0);

        present.ifAbsent(counter::incrementAndGet);
        empty.ifAbsent(counter::incrementAndGet);

        assertEquals(1, counter.get());
    }

    @Test
    @DisplayName("NullSafeLong.get() should return value or throw exception")
    void testNullSafeLongGet() {
        NullSafeLong present = NullSafeLong.of(42L);
        NullSafeLong empty = NullSafeLong.empty();

        assertEquals(42L, present.get());
        assertThrows(NoSuchElementException.class, empty::get);
    }

    @Test
    @DisplayName("NullSafeLong.toString() should return proper string representation")
    void testNullSafeLongToString() {
        assertEquals("NullSafeLong[42]", NullSafeLong.of(42L).toString());
        assertEquals("NullSafeLong.empty", NullSafeLong.empty().toString());
    }

    // ===== NullSafeDouble Tests =====
    @Test
    @DisplayName("NullSafeDouble.of() should create instance with value")
    void testNullSafeDoubleOf() {
        NullSafeDouble nsd = NullSafeDouble.of(3.14159);
        assertTrue(nsd.isPresent());
        assertEquals(3.14159, nsd.get(), 0.00001);
    }

    @Test
    @DisplayName("NullSafeDouble.empty() should create empty instance")
    void testNullSafeDoubleEmpty() {
        NullSafeDouble nsd = NullSafeDouble.empty();
        assertTrue(nsd.isEmpty());
    }

    @Test
    @DisplayName("NullSafeDouble.orElse() should return value or default")
    void testNullSafeDoubleOrElse() {
        NullSafeDouble present = NullSafeDouble.of(3.14);
        NullSafeDouble empty = NullSafeDouble.empty();

        assertEquals(3.14, present.orElse(2.71), 0.001);
        assertEquals(2.71, empty.orElse(2.71), 0.001);
    }

    @Test
    @DisplayName("NullSafeDouble.get() should return value or throw exception")
    void testNullSafeDoubleGet() {
        NullSafeDouble present = NullSafeDouble.of(3.14);
        NullSafeDouble empty = NullSafeDouble.empty();

        assertEquals(3.14, present.get(), 0.001);
        assertThrows(NoSuchElementException.class, empty::get);
    }

    @Test
    @DisplayName("NullSafeDouble.toString() should return proper string representation")
    void testNullSafeDoubleToString() {
        assertEquals("NullSafeDouble[3.140000]", NullSafeDouble.of(3.14).toString());
        assertEquals("NullSafeDouble.empty", NullSafeDouble.empty().toString());
    }

    // ===== NullSafeFloat Tests =====
    @Test
    @DisplayName("NullSafeFloat.of() should create instance with value")
    void testNullSafeFloatOf() {
        NullSafeFloat nsf = NullSafeFloat.of(2.5f);
        assertTrue(nsf.isPresent());
        assertEquals(2.5f, nsf.get(), 0.001f);
    }

    @Test
    @DisplayName("NullSafeFloat.empty() should create empty instance")
    void testNullSafeFloatEmpty() {
        NullSafeFloat nsf = NullSafeFloat.empty();
        assertTrue(nsf.isEmpty());
    }

    @Test
    @DisplayName("NullSafeFloat.orElse() should return value or default")
    void testNullSafeFloatOrElse() {
        NullSafeFloat present = NullSafeFloat.of(2.5f);
        NullSafeFloat empty = NullSafeFloat.empty();

        assertEquals(2.5f, present.orElse(1.5f), 0.001f);
        assertEquals(1.5f, empty.orElse(1.5f), 0.001f);
    }

    @Test
    @DisplayName("NullSafeFloat.get() should return value or throw exception")
    void testNullSafeFloatGet() {
        NullSafeFloat present = NullSafeFloat.of(2.5f);
        NullSafeFloat empty = NullSafeFloat.empty();

        assertEquals(2.5f, present.get(), 0.001f);
        assertThrows(NoSuchElementException.class, empty::get);
    }

    @Test
    @DisplayName("NullSafeFloat.toString() should return proper string representation")
    void testNullSafeFloatToString() {
        assertEquals("NullSafeFloat[2.500000]", NullSafeFloat.of(2.5f).toString());
        assertEquals("NullSafeFloat.empty", NullSafeFloat.empty().toString());
    }

    // ===== NullSafeBoolean Tests =====
    @Test
    @DisplayName("NullSafeBoolean.of() should create instance with value")
    void testNullSafeBooleanOf() {
        NullSafeBoolean nsb = NullSafeBoolean.of(true);
        assertTrue(nsb.isPresent());
        assertTrue(nsb.get());
    }

    @Test
    @DisplayName("NullSafeBoolean.empty() should create empty instance")
    void testNullSafeBooleanEmpty() {
        NullSafeBoolean nsb = NullSafeBoolean.empty();
        assertTrue(nsb.isEmpty());
    }

    @Test
    @DisplayName("NullSafeBoolean.orElse() should return value or default")
    void testNullSafeBooleanOrElse() {
        NullSafeBoolean presentTrue = NullSafeBoolean.of(true);
        NullSafeBoolean presentFalse = NullSafeBoolean.of(false);
        NullSafeBoolean empty = NullSafeBoolean.empty();

        assertTrue(presentTrue.orElse(false));
        assertFalse(presentFalse.orElse(true));
        assertTrue(empty.orElse(true));
    }

    @Test
    @DisplayName("NullSafeBoolean.ifPresent() should execute action when value present")
    void testNullSafeBooleanIfPresent() {
        NullSafeBoolean present = NullSafeBoolean.of(true);
        NullSafeBoolean empty = NullSafeBoolean.empty();
        AtomicBoolean result = new AtomicBoolean(false);

        present.ifPresent(result::set);
        empty.ifPresent(result::set);

        assertTrue(result.get());
    }

    @Test
    @DisplayName("NullSafeBoolean.get() should return value or throw exception")
    void testNullSafeBooleanGet() {
        NullSafeBoolean present = NullSafeBoolean.of(true);
        NullSafeBoolean empty = NullSafeBoolean.empty();

        assertTrue(present.get());
        assertThrows(NoSuchElementException.class, empty::get);
    }

    @Test
    @DisplayName("NullSafeBoolean.toString() should return proper string representation")
    void testNullSafeBooleanToString() {
        assertEquals("NullSafeBoolean[true]", NullSafeBoolean.of(true).toString());
        assertEquals("NullSafeBoolean[false]", NullSafeBoolean.of(false).toString());
        assertEquals("NullSafeBoolean.empty", NullSafeBoolean.empty().toString());
    }

    // ===== NullSafeByte Tests =====
    @Test
    @DisplayName("NullSafeByte.of() should create instance with value")
    void testNullSafeByteOf() {
        NullSafeByte nsb = NullSafeByte.of((byte) 127);
        assertTrue(nsb.isPresent());
        assertEquals((byte) 127, nsb.get());
    }

    @Test
    @DisplayName("NullSafeByte.empty() should create empty instance")
    void testNullSafeByteEmpty() {
        NullSafeByte nsb = NullSafeByte.empty();
        assertTrue(nsb.isEmpty());
    }

    @Test
    @DisplayName("NullSafeByte.orElse() should return value or default")
    void testNullSafeByteOrElse() {
        NullSafeByte present = NullSafeByte.of((byte) 42);
        NullSafeByte empty = NullSafeByte.empty();

        assertEquals((byte) 42, present.orElse((byte) 99));
        assertEquals((byte) 99, empty.orElse((byte) 99));
    }

    @Test
    @DisplayName("NullSafeByte.get() should return value or throw exception")
    void testNullSafeByteGet() {
        NullSafeByte present = NullSafeByte.of((byte) 42);
        NullSafeByte empty = NullSafeByte.empty();

        assertEquals((byte) 42, present.get());
        assertThrows(NoSuchElementException.class, empty::get);
    }

    @Test
    @DisplayName("NullSafeByte.toString() should return proper string representation")
    void testNullSafeByteToString() {
        assertEquals("NullSafeByte[42]", NullSafeByte.of((byte) 42).toString());
        assertEquals("NullSafeByte.empty", NullSafeByte.empty().toString());
    }

    // ===== NullSafeShort Tests =====
    @Test
    @DisplayName("NullSafeShort.of() should create instance with value")
    void testNullSafeShortOf() {
        NullSafeShort nss = NullSafeShort.of((short) 32767);
        assertTrue(nss.isPresent());
        assertEquals((short) 32767, nss.get());
    }

    @Test
    @DisplayName("NullSafeShort.empty() should create empty instance")
    void testNullSafeShortEmpty() {
        NullSafeShort nss = NullSafeShort.empty();
        assertTrue(nss.isEmpty());
    }

    @Test
    @DisplayName("NullSafeShort.orElse() should return value or default")
    void testNullSafeShortOrElse() {
        NullSafeShort present = NullSafeShort.of((short) 1000);
        NullSafeShort empty = NullSafeShort.empty();

        assertEquals((short) 1000, present.orElse((short) 2000));
        assertEquals((short) 2000, empty.orElse((short) 2000));
    }

    @Test
    @DisplayName("NullSafeShort.get() should return value or throw exception")
    void testNullSafeShortGet() {
        NullSafeShort present = NullSafeShort.of((short) 1000);
        NullSafeShort empty = NullSafeShort.empty();

        assertEquals((short) 1000, present.get());
        assertThrows(NoSuchElementException.class, empty::get);
    }

    @Test
    @DisplayName("NullSafeShort.toString() should return proper string representation")
    void testNullSafeShortToString() {
        assertEquals("NullSafeShort[1000]", NullSafeShort.of((short) 1000).toString());
        assertEquals("NullSafeShort.empty", NullSafeShort.empty().toString());
    }

    // ===== Edge Cases Tests =====
    @Test
    @DisplayName("All primitive types should handle extreme values correctly")
    void testExtremeValues() {
        // Long extremes
        assertTrue(NullSafeLong.of(Long.MAX_VALUE).isPresent());
        assertTrue(NullSafeLong.of(Long.MIN_VALUE).isPresent());
        assertEquals(Long.MAX_VALUE, NullSafeLong.of(Long.MAX_VALUE).get());
        assertEquals(Long.MIN_VALUE, NullSafeLong.of(Long.MIN_VALUE).get());

        // Double extremes
        assertTrue(NullSafeDouble.of(Double.MAX_VALUE).isPresent());
        assertTrue(NullSafeDouble.of(Double.MIN_VALUE).isPresent());
        assertTrue(NullSafeDouble.of(Double.NaN).isPresent());
        assertTrue(NullSafeDouble.of(Double.POSITIVE_INFINITY).isPresent());
        assertTrue(NullSafeDouble.of(Double.NEGATIVE_INFINITY).isPresent());

        // Float extremes
        assertTrue(NullSafeFloat.of(Float.MAX_VALUE).isPresent());
        assertTrue(NullSafeFloat.of(Float.MIN_VALUE).isPresent());
        assertTrue(NullSafeFloat.of(Float.NaN).isPresent());
        assertTrue(NullSafeFloat.of(Float.POSITIVE_INFINITY).isPresent());
        assertTrue(NullSafeFloat.of(Float.NEGATIVE_INFINITY).isPresent());

        // Byte extremes
        assertTrue(NullSafeByte.of(Byte.MAX_VALUE).isPresent());
        assertTrue(NullSafeByte.of(Byte.MIN_VALUE).isPresent());
        assertEquals(Byte.MAX_VALUE, NullSafeByte.of(Byte.MAX_VALUE).get());
        assertEquals(Byte.MIN_VALUE, NullSafeByte.of(Byte.MIN_VALUE).get());

        // Short extremes
        assertTrue(NullSafeShort.of(Short.MAX_VALUE).isPresent());
        assertTrue(NullSafeShort.of(Short.MIN_VALUE).isPresent());
        assertEquals(Short.MAX_VALUE, NullSafeShort.of(Short.MAX_VALUE).get());
        assertEquals(Short.MIN_VALUE, NullSafeShort.of(Short.MIN_VALUE).get());
    }

    @Test
    @DisplayName("All primitive types should have consistent behavior")
    void testConsistency() {
        // Test that all types behave consistently
        NullSafeInt nsi = NullSafeInt.of(0);
        NullSafeLong nsl = NullSafeLong.of(0L);
        NullSafeDouble nsd = NullSafeDouble.of(0.0);
        NullSafeFloat nsf = NullSafeFloat.of(0.0f);
        NullSafeBoolean nsb = NullSafeBoolean.of(false);
        NullSafeByte nsb2 = NullSafeByte.of((byte) 0);
        NullSafeShort nss = NullSafeShort.of((short) 0);

        assertTrue(nsi.isPresent() && nsl.isPresent() && nsd.isPresent() && 
                  nsf.isPresent() && nsb.isPresent() && nsb2.isPresent() && nss.isPresent());
    }
}