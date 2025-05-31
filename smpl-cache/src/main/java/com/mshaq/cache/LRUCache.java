package com.mshaq.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class LRUCache<K, V> implements Cache<K, V> {

    private static final Logger log = Logger.getLogger(LRUCache.class.getName());
    private int maxCapacity = 0;
    private static final int DEFAULT_CAPACITY = 16;
    private Map<K, V> cache;

    public LRUCache() {
        this(DEFAULT_CAPACITY);
    }

    public LRUCache(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("capacity <= 0");
        this.maxCapacity = capacity;
        this.cache = Collections.synchronizedMap(new LRUMap<>(capacity));
    }

    @Override
    public V put(K key, V value) {
        return cache.put(key, value);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }


    @Override
    public V remove(K key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public int capacity() {
        return maxCapacity;
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        cache.forEach((key, value) -> {
            stringBuilder.append(String.format("%s: %s  ", key, value));
        });
        stringBuilder.append("maxMemorySize=")
                .append(maxCapacity)
                .append(",")
                .append("memorySize=")
                .append(size())
                .append("]");
        return stringBuilder.toString();
    }

    /**
     * Returns a copy of the current contents of the cache.
     */
    public synchronized final Map<K, V> snapshot() {
        return new LinkedHashMap<>(cache);
    }

    private static class LRUMap<K, V> extends LinkedHashMap<K, V> {
        private final int MAX_ENTRIES;

        public LRUMap(int capacity) {
            super(capacity, 0.75f, true);
            this.MAX_ENTRIES = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            boolean shouldRemove = size() > MAX_ENTRIES;
            if (shouldRemove) {
                log.info(String.format("Evicting eldest entry : %s -> %s", eldest.getKey(), eldest.getValue()));
            }
            return shouldRemove;
        }
    }
}
