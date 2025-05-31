package com.mshaq.cache;


import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {
    private LRUCache<String, Product> cache;
    private final int CACHE_CAPACITY = 3;

    private ByteArrayOutputStream logOutput;
    private StreamHandler logHandler;
    private Logger lruMapLogger;

    @BeforeEach
    void setUp() {
        cache = new LRUCache<>(CACHE_CAPACITY);
        logOutput = new ByteArrayOutputStream();
        lruMapLogger = Logger.getLogger("com.mshaq.cache.LRUCache$LRUMap");

        // Remove existing handlers to avoid console pollution during tests
        for (java.util.logging.Handler handler : lruMapLogger.getHandlers()) {
            lruMapLogger.removeHandler(handler);
        }

        logHandler = new StreamHandler(logOutput, new SimpleFormatter());
        logHandler.setLevel(Level.INFO);
        lruMapLogger.addHandler(logHandler);
        lruMapLogger.setLevel(Level.INFO); // Ensure logger itself is INFO or lower
        lruMapLogger.setUseParentHandlers(false); // Prevent logs from going to console via parent
    }

    @AfterEach
    void tearDown() {
        // Clean up log handler after each test
        logHandler.close();
        lruMapLogger.removeHandler(logHandler);
        // Reset logger to default behavior if needed for other tests or next run
        lruMapLogger.setLevel(Level.INFO); // Default level
        lruMapLogger.setUseParentHandlers(true); // Allow parent handlers again
    }

    @Test
    @DisplayName("Test LRUCache initialization with default capacity")
    void testDefaultConstructor() {
        Cache<String, String> defaultCache = new LRUCache<>();
        assertEquals(16, defaultCache.capacity(), "Default capacity should be 16");
        assertEquals(0, defaultCache.size(), "New cache should be empty");
    }

    @Test
    @DisplayName("Test LRUCache initialization with specified capacity")
    void testParameterizedConstructor() {
        assertEquals(CACHE_CAPACITY, cache.capacity(), "Cache capacity should match constructor argument");
        assertEquals(0, cache.size(), "New cache should be empty");
    }

    @Test
    @DisplayName("Test put and get operations for basic functionality")
    void testPutAndGet() {
        Product p1 = new Product("P001", "Laptop", 1200.0);
        Product p2 = new Product("P002", "Mouse", 25.0);

        cache.put("P001", p1);
        cache.put("P002", p2);

        assertAll(
                () -> assertEquals(2, cache.size(), "Cache size should be 2 after two puts"),
                () -> assertEquals(p1, cache.get("P001"), "Should retrieve P001 correctly"),
                () -> assertEquals(p2, cache.get("P002"), "Should retrieve P002 correctly"),
                () -> assertNull(cache.get("P003"), "Should return null for non-existent key")
        );
    }

    @Test
    @DisplayName("Test LRU eviction policy")
    void testLRUEviction() {
        Product p1 = new Product("P001", "Laptop", 1200.0);
        Product p2 = new Product("P002", "Mouse", 25.0);
        Product p3 = new Product("P003", "Keyboard", 75.0);
        Product p4 = new Product("P004", "Monitor", 300.0);
        Product p5 = new Product("P005", "Webcam", 50.0);

        // Fill the cache to capacity (3)
        cache.put("P001", p1); // P001
        cache.put("P002", p2); // P001, P002
        cache.put("P003", p3); // P001, P002, P003 (Cache full)

        cache.get("P001"); // P002, P003, P001
        cache.put("P004", p4); // P003, P001, P004 (P002 evicted)

        Assertions.assertAll(
                () -> assertEquals(CACHE_CAPACITY, cache.size(), "Cache size should remain at capacity"),
                () -> assertNull(cache.get("P002"), "P002 should be evicted"),
                () -> assertEquals(p1, cache.get("P001"), "P001 should still be in cache"),
                () -> assertEquals(p3, cache.get("P003"), "P003 should still be in cache"),
                () -> assertEquals(p4, cache.get("P004"), "P004 should be in cache")
        );

        // Access P003 to make it recently used
        cache.get("P003"); // P001, P004, P003

        // Add P005, P001 should be evicted (LRU)
        cache.put("P005", p5); // P004, P003, P005 (P001 evicted)

        Assertions.assertAll(
                () -> assertEquals(CACHE_CAPACITY, cache.size(), "Cache size should remain at capacity"),
                () -> assertNull(cache.get("P001"), "P001 should be evicted"),
                () -> assertEquals(p3, cache.get("P003"), "P003 should still be in cache"),
                () -> assertEquals(p4, cache.get("P004"), "P004 should still be in cache"),
                () -> assertEquals(p5, cache.get("P005"), "P005 should be in cache")
        );

    }

    @Test
    @DisplayName("Test put updates existing value and updates recency")
    void testPutUpdateAndRecency() {
        Product p1 = new Product("P001", "Laptop", 1200.0);
        Product p2 = new Product("P002", "Mouse", 25.0);
        Product p3 = new Product("P003", "Keyboard", 75.0);
        Product p1Updated = new Product("P001", "Gaming Laptop", 1500.0);

        cache.put("P001", p1);
        cache.put("P002", p2);
        cache.put("P003", p3); // P001, P002, P003

        cache.put("P001", p1Updated); // P002, P003, P001(updated)

        // P002 should be evicted it's the least recent among P002, P003
        Product p4 = new Product("P004", "Monitor", 300.0);
        cache.put("P004", p4); // P003, P001(updated), P004 (P002 evicted)

        Assertions.assertAll(
                () -> assertEquals(CACHE_CAPACITY, cache.size(), "Cache size should remain at capacity"),
                () -> assertEquals(p1Updated, cache.get("P001"), "P001 should be updated and retrieved correctly"),
                () -> assertNull(cache.get("P002"), "P002 should be evicted"),
                () -> assertEquals(p3, cache.get("P003"), "P003 should still be in cache"),
                () -> assertEquals(p4, cache.get("P004"), "P004 should be in cache")
        );
    }

    @Test
    @DisplayName("Test remove operation")
    void testRemove() {
        Product p1 = new Product("P001", "Laptop", 1200.0);
        Product p2 = new Product("P002", "Mouse", 25.0);

        cache.put("P001", p1);
        cache.put("P002", p2);

        Product removedP1 = cache.remove("P001");
        Assertions.assertAll(
                () -> assertEquals(p1, removedP1, "Removed product should be P001"),
                () -> assertEquals(1, cache.size(), "Cache size should be 1 after removal"),
                () -> assertNull(cache.get("P001"), "P001 should no longer be in cache"),
                () -> assertEquals(p2, cache.get("P002"), "P002 should still be in cache")
        );

        // Try removing a non-existent key
        Product removedNonExistent = cache.remove("P999");
        Assertions.assertAll(
                () -> assertNull(removedNonExistent, "Removing non-existent key should return null"),
                () -> assertEquals(1, cache.size(), "Cache size should remain 1")
        );
    }

    @Test
    @DisplayName("Test clear operation")
    void testClear() {
        cache.put("P1", new Product("P1", "A", 10.0));
        cache.put("P2", new Product("P2", "B", 20.0));

        assertEquals(2, cache.size(), "Cache should have 2 elements before clear");
        cache.clear();
        assertEquals(0, cache.size(), "Cache should be empty after clear");
        assertNull(cache.get("P1"), "P1 should not be in cache after clear");
    }

    @Test
    @DisplayName("Test size method")
    void testSize() {
        assertEquals(0, cache.size(), "Initial size should be 0");
        cache.put("P1", new Product("P1", "A", 10.0));
        assertEquals(1, cache.size(), "Size should be 1 after one put");
        cache.put("P2", new Product("P2", "B", 20.0));
        assertEquals(2, cache.size(), "Size should be 2 after two puts");
        cache.remove("P1");
        assertEquals(1, cache.size(), "Size should be 1 after one remove");
    }

    @Test
    @DisplayName("Test toString method")
    void testToString() {
        Product p1 = new Product("P001", "Laptop", 1200.0);
        Product p2 = new Product("P002", "Mouse", 25.0);

        cache.put("P001", p1);
        cache.put("P002", p2);

        String actualString = cache.toString();
        assertTrue(actualString.contains("P001: Product[id=P001, name=Laptop, price=1200.0]"), "toString should contain P001");
        assertTrue(actualString.contains("P002: Product[id=P002, name=Mouse, price=25.0]"), "toString should contain P002");
        assertTrue(actualString.contains("maxMemorySize=3"), "toString should contain maxCapacity");
        assertTrue(actualString.contains("memorySize=2"), "toString should contain current size");
    }

    @Test
    @DisplayName("Test snapshot method provides a copy and maintains order")
    void testSnapshot() {

        Product p1 = new Product("P001", "Laptop", 1200.0);
        Product p2 = new Product("P002", "Mouse", 25.0);
        Product p3 = new Product("P003", "Keyboard", 75.0);

        cache.put("P001", p1);
        cache.put("P002", p2);
        cache.put("P003", p3);

        Map<String, Product> snapshot = cache.snapshot();
        snapshot.remove("P001");
        assertEquals(3, cache.size(), "Original cache size should not change after snapshot modification");
        assertEquals(2, snapshot.size(), "Snapshot size should change after modification");

        Map<String, Product> expectedMap = new LinkedHashMap<>();
        expectedMap.put("P002", p2);
        expectedMap.put("P003", p3);

        assertIterableEquals(expectedMap.entrySet(), snapshot.entrySet(), "Snapshot should maintain insertion order and content");
    }

    @Test
    @DisplayName("Test concurrent access to LRUCache (basic check)")
    void testConcurrentAccess() throws InterruptedException {
        int noOfThreads = 5;
        int threadOperations = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(noOfThreads);
        for (int i = 0; i < noOfThreads; i++) {
            final int threadId = i;
            executorService.submit(() -> {
                for (int j = 0; j < threadOperations; j++) {
                    String key = String.format("key-%d", (threadId * threadOperations + j));
                    Product value = new Product(key, "Product " + key, (double) j);
                    cache.put(key, value);
                    cache.get("key-" + (threadId * threadOperations + (j % CACHE_CAPACITY)));
                }
            });
        }

        executorService.shutdown();
        assertTrue(executorService.awaitTermination(10, TimeUnit.SECONDS));
        assertEquals(CACHE_CAPACITY, cache.size(), "Cache size should be at its max capacity after concurrent operations");
    }

    public record Product(String id, String name, double price) {
    }
}