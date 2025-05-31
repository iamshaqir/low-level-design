package com.mshaq.caching.cache;

import com.mshaq.caching.eviction.EvictionPolicy;

public class Cache<K, V> {

    private static volatile Cache<?, ?> instance;
    private final EvictionPolicy<K, V> policy;

    private Cache(EvictionPolicy<K, V> evictionPolicy) {
        this.policy = evictionPolicy;
    }

    public static <K, V> Cache<K, V> of(EvictionPolicy<K, V> evictionPolicy) {
        if (instance == null) {
            synchronized (Cache.class) {
                if (instance == null) {
                    instance = new Cache<>(evictionPolicy);
                }
            }
        }
        try {
            return (Cache<K, V>) instance;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Cache already initialized with different generic types");
        }
    }

    public void put(K key, V value) {
        policy.put(key, value);
    }

    public V get(K key) {
        return policy.get(key);
    }
}
