/**
 * Comprehensive test suite for all new NullSafe features.
 * Tests collections, streams, async, validation, transformations, and performance features.
 * 
 * @since 1.0
 */
class NullSafeAdvancedFeaturesTest {
    
    @Test
    void testNullSafeCollections() {
        // Test NullSafeList
        List<String> testList = Arrays.asList("item1", null, "item2", null, "item3");
        NullSafeList<String> safeList = NullSafeList.of(testList);
        
        assertEquals(3, safeList.size());
        assertTrue(safeList.contains("item1"));
        assertFalse(safeList.contains(null));
        
        // Test filtering
        NullSafeList<String> filtered = safeList.filter(s -> s.startsWith("item"));
        assertEquals(3, filtered.size());
        
        // Test mapping
        NullSafeList<Integer> mapped = safeList.map(String::length);
        assertEquals(Arrays.asList(5, 5, 5), mapped.toList());
        
        // Test finding
        NullSafe<String> found = safeList.find(s -> s.equals("item2"));
        assertTrue(found.isPresent());
        assertEquals("item2", found.get());
    }
    
    @Test
    void testNullSafeMap() {
        Map<String, String> testMap = Map.of("key1", "value1", "key2", null, "key3", "value3");
        NullSafeMap<String, String> safeMap = NullSafeMap.of(testMap);
        
        assertEquals(3, safeMap.size());
        assertTrue(safeMap.containsKey("key1"));
        assertTrue(safeMap.containsKey("key2"));
        
        // Test getting values
        NullSafe<String> value1 = safeMap.get("key1");
        assertTrue(value1.isPresent());
        assertEquals("value1", value1.get());
        
        NullSafe<String> value2 = safeMap.get("key2");
        assertTrue(value2.isAbsent());
        
        // Test value mapping
        NullSafeMap<String, Integer> lengthMap = safeMap.mapValues(String::length);
        assertEquals(Integer.valueOf(6), lengthMap.get("key1").orElse(-1));
        assertEquals(Integer.valueOf(6), lengthMap.get("key3").orElse(-1));
        assertEquals(Integer.valueOf(-1), lengthMap.get("key2").orElse(-1));
    }
    
    @Test
    void testNullSafeSet() {
        Set<String> testSet = Set.of("A", "B", "C");
        NullSafeSet<String> safeSet = NullSafeSet.of(testSet);
        
        assertEquals(3, safeSet.size());
        assertTrue(safeSet.contains("A"));
        assertFalse(safeSet.contains("D"));
        
        // Test operations
        NullSafeSet<String> filtered = safeSet.filter(s -> s.compareTo("B") >= 0);
        assertTrue(filtered.contains("B"));
        assertTrue(filtered.contains("C"));
        assertFalse(filtered.contains("A"));
    }
    
    @Test
    void testNullSafeArray() {
        String[] testArray = {"element1", null, "element2", "element3"};
        NullSafeArray<String> safeArray = NullSafeArray.of(testArray);
        
        assertEquals(3, safeArray.length()); // nulls are filtered out
        
        // Test element access
        NullSafe<String> first = safeArray.get(0);
        assertTrue(first.isPresent());
        assertEquals("element1", first.get());
        
        // Test mapping
        NullSafeArray<Integer> mapped = safeArray.map(String::length);
        assertEquals(3, mapped.length());
        assertEquals(Integer.valueOf(8), mapped.get(0).orElse(-1));
    }
    
    @Test
    void testStreamIntegration() {
        List<String> data = Arrays.asList("stream", null, "test", "data");
        NullSafeStreamOperation<String> stream = NullSafeStream.of(data);
        
        // Test filtering and mapping
        List<String> processed = stream
            .filter(s -> s.length() > 4)
            .map(String::toUpperCase)
            .toList();
        
        assertTrue(processed.contains("STREAM"));
        assertTrue(processed.contains("TEST"));
        assertFalse(processed.contains("DATA"));
        
        // Test finding
        NullSafe<String> longest = stream.max(Comparator.comparingInt(String::length));
        assertTrue(longest.isPresent());
        assertEquals("stream", longest.get());
    }
    
    @Test
    void testAsyncOperations() {
        // Test NullSafeFuture creation
        NullSafeFuture<String> future = NullSafeFuture.fromSupplier(() -> "async_result");
        
        assertTrue(future != null);
        
        // Test chaining
        NullSafeFuture<String> chained = future
            .mapValue(String::toUpperCase)
            .mapValue(s -> s + "_processed");
        
        assertTrue(chained != null);
        
        // Test allOf
        List<NullSafeFuture<Integer>> futures = Arrays.asList(
            NullSafeFuture.of(1),
            NullSafeFuture.of(2),
            NullSafeFuture.of(3)
        );
        
        NullSafeFuture<NullSafeList<Integer>> all = NullSafeAsync.allOf(futures);
        assertTrue(all != null);
    }
    
    @Test
    void testValidation() {
        // Test basic validation
        ValidationResult result1 = NullSafeValidator.of(NullSafe.of("test@example.com"))
            .notNull()
            .isEmail()
            .validate();
        
        assertTrue(result1.isValid());
        
        // Test email validation failure
        ValidationResult result2 = NullSafeValidator.of(NullSafe.of("invalid-email"))
            .isEmail()
            .validate();
        
        assertFalse(result2.isValid());
        
        // Test string validation
        ValidationResult result3 = NullSafeValidator.of(NullSafe.of("password123"))
            .ifString()
            .minLength(8)
            .matches(".*[0-9].*")
            .and()
            .validate();
        
        assertTrue(result3.isValid());
        
        // Test numeric validation
        ValidationResult result4 = NullSafeValidator.of(NullSafe.of(25))
            .ifNumber()
            .range(0, 120)
            .positive()
            .and()
            .validate();
        
        assertTrue(result4.isValid());
    }
    
    @Test
    void testTransformations() {
        // Test basic transformation
        NullSafe<String> original = NullSafe.of("hello world");
        NullSafe<String> transformed = NullSafeTransformer.from(original)
            .map(String::toUpperCase)
            .map(s -> s.replace(" ", "_"))
            .transform();
        
        assertTrue(transformed.isPresent());
        assertEquals("HELLO_WORLD", transformed.get());
        
        // Test conditional transformation
        NullSafe<Integer> number = NullSafe.of(15);
        String conditionalResult = NullSafeTransformer.from(number)
            .mapConditional(
                n -> n > 10,
                n -> "Greater than 10",
                n -> "Less than or equal 10"
            )
            .transform()
            .orElse("No result");
        
        assertEquals("Greater than 10", conditionalResult);
        
        // Test error handling
        NullSafe<String> errorHandled = NullSafeTransformer.from(original)
            .mapWithErrorHandling(
                s -> {
                    throw new RuntimeException("Intentional error");
                },
                e -> "fallback_result"
            )
            .transform();
        
        assertTrue(errorHandled.isPresent());
        assertEquals("fallback_result", errorHandled.get());
    }
    
    @Test
    void testPerformanceOptimizations() {
        // Test cached NullSafe
        NullSafe<String> cached = NullSafePerformance.cached(() -> {
            return "cached_value";
        });
        
        assertTrue(cached.isPresent());
        assertEquals("cached_value", cached.get());
        
        // Test lazy NullSafe
        NullSafe<String> lazy = NullSafePerformance.lazy(() -> "lazy_value");
        assertTrue(lazy.isPresent());
        assertEquals("lazy_value", lazy.get());
        
        // Test memoized function
        Function<String, NullSafe<Integer>> memoized = NullSafePerformance.memoized(String::length);
        
        assertTrue(memoized.apply("test").isPresent());
        assertEquals(Integer.valueOf(4), memoized.apply("test").get());
        
        // Test performance monitor
        NullSafePerformanceMonitor monitor = new NullSafePerformanceMonitor();
        NullSafe<String> monitored = monitor.recordTime("test_operation", () -> 
            NullSafe.of("monitored_result")
        );
        
        assertTrue(monitored.isPresent());
        assertEquals("monitored_result", monitored.get());
        
        Map<String, NullSafePerformanceMonitor.PerformanceMetrics> metrics = monitor.getAllMetrics();
        assertTrue(metrics.containsKey("test_operation"));
        
        monitor.close();
    }
    
    @Test
    void testAdvancedPatterns() {
        // Test circuit breaker
        NullSafe<String> circuitBreaker = NullSafe.of("normal_result")
            .filter(s -> !s.equals("error"))
            .orElseGet(() -> "fallback_result");
        
        assertEquals("normal_result", circuitBreaker.get());
        
        // Test circuit breaker with error
        NullSafe<String> errorCircuit = NullSafe.of("error")
            .filter(s -> !s.equals("error"))
            .orElseGet(() -> "fallback_result");
        
        assertEquals("fallback_result", errorCircuit.get());
        
        // Test builder pattern
        NullSafeAdvancedFeaturesTest.UserBuilder builder = new NullSafeAdvancedFeaturesTest.UserBuilder();
        NullSafeAdvancedFeaturesTest.User user = builder
            .withName("John")
            .withEmail("john@example.com")
            .withAge(30)
            .build();
        
        assertEquals("John", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals(30, user.getAge());
        
        // Test factory pattern
        NullSafe<NullSafeAdvancedFeaturesTest.User> factoryUser = 
            NullSafeAdvancedFeaturesTest.UserFactory.createUser("Jane", "jane@example.com", 25);
        
        assertTrue(factoryUser.isPresent());
        assertEquals("Jane", factoryUser.get().getName());
        
        // Test observer pattern
        NullSafeAdvancedFeaturesTest.NullSafeProperty<String> property = 
            new NullSafeAdvancedFeaturesTest.NullSafeProperty<>("initial");
        
        AtomicReference<String> observedValue = new AtomicReference<>();
        property.addObserver(new NullSafeAdvancedFeaturesTest.NullSafePropertyObserver<>() {
            @Override
            public void onChanged(NullSafe<String> newValue) {
                observedValue.set(newValue.orElse("null"));
            }
        });
        
        property.setValue("updated");
        assertEquals("updated", observedValue.get());
    }
    
    @Test
    void testUtilityFunctions() {
        // Test NullSafeCollections utility
        NullSafeList<String> utilityList = NullSafeCollections.safeList(Arrays.asList("a", null, "b"));
        assertEquals(2, utilityList.size());
        assertTrue(utilityList.contains("a"));
        assertTrue(utilityList.contains("b"));
        
        // Test grouping utility
        List<String> data = Arrays.asList("apple", "apricot", "banana", "blueberry");
        Map<String, NullSafeList<String>> grouped = NullSafeCollections.groupSafely(
            data, s -> s.substring(0, 1)
        );
        
        assertTrue(grouped.containsKey("a"));
        assertTrue(grouped.containsKey("b"));
        assertEquals(2, grouped.get("a").size());
        assertEquals(2, grouped.get("b").size());
        
        // Test zip operation
        List<String> list1 = Arrays.asList("A", "B", "C");
        List<Integer> list2 = Arrays.asList(1, 2, 3);
        NullSafeList<Map.Entry<String, Integer>> zipped = NullSafeCollections.zip(list1, list2);
        
        assertEquals(3, zipped.size());
        assertEquals("A", zipped.get(0).getKey());
        assertEquals(Integer.valueOf(1), zipped.get(0).getValue());
    }
    
    @Test
    void testEdgeCases() {
        // Test with empty collections
        NullSafeList<String> emptyList = NullSafeList.empty();
        assertTrue(emptyList.isEmpty());
        assertEquals(0, emptyList.size());
        
        // Test with null input
        NullSafeList<String> nullInput = NullSafeList.of(null);
        assertEquals(0, nullInput.size());
        
        // Test with completely null array
        String[] nullArray = null;
        NullSafeArray<String> nullArrayResult = NullSafeArray.of(nullArray);
        assertEquals(0, nullArrayResult.length());
        
        // Test stream with null stream
        NullSafeStreamOperation<String> nullStream = NullSafeStream.of((Stream<String>) null);
        assertEquals(0, nullStream.count());
        
        // Test validation with null value
        ValidationResult nullValidation = NullSafeValidator.of(NullSafe.empty())
            .notNull()
            .validate();
        
        assertFalse(nullValidation.isValid());
        
        // Test transformation with empty NullSafe
        NullSafe<String> empty = NullSafe.empty();
        NullSafe<String> transformedEmpty = NullSafeTransformer.from(empty)
            .map(String::toUpperCase)
            .transform();
        
        assertTrue(transformedEmpty.isAbsent());
    }
    
    @Test
    void testPerformanceAndMemory() {
        // Test large collection performance
        List<String> largeList = IntStream.range(0, 10000)
            .mapToObj(i -> "item_" + i)
            .collect(Collectors.toList());
        
        NullSafeList<String> safeLargeList = NullSafeList.of(largeList);
        assertEquals(10000, safeLargeList.size());
        
        // Test operations on large list
        long start = System.nanoTime();
        NullSafeList<String> filtered = safeLargeList.filter(s -> s.contains("5"));
        long end = System.nanoTime();
        
        assertTrue(end - start < 1_000_000_000); // Should complete within 1 second
        assertTrue(filtered.size() > 0);
        
        // Test memory with repeated access
        NullSafe<String> cached = NullSafePerformance.cached(() -> {
            byte[] large = new byte[1024 * 1024]; // 1MB allocation
            return "large_value";
        });
        
        // Access multiple times - should not recompute
        for (int i = 0; i < 100; i++) {
            cached.get(); // Should be cached
        }
    }
}