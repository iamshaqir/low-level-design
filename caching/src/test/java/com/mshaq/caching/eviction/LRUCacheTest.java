package com.mshaq.caching.eviction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

class LRUCacheTest {
    private static final Logger log = LogManager.getLogger(LRUCacheTest.class);

    @Test
    void test_linkedHashMap() {
        Map<String, String> linkedHashMap = new LinkedHashMap<>(16, 0.75f, true);
        linkedHashMap.put("a", "A");
        linkedHashMap.put("b", "B");
        linkedHashMap.put("c", "C");
        linkedHashMap.put("d", "D");
        linkedHashMap.put("e", "E");

        for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {
            log.info("Key: {} and Value: {}", entry.getKey(), entry.getValue());
        }
        String eKey = linkedHashMap.get("a");
        String dKey = linkedHashMap.get("b");
        log.info("----------------------------");
        for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {
            log.info("Key: {} and Value: {}", entry.getKey(), entry.getValue());
        }
    }
}