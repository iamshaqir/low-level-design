package com.mshaq.caching.eviction;

public interface EvictionPolicy<K, V> {
    void put(K key, V value);
    V get(K key);
}
