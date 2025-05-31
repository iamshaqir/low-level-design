package com.mshaq.cache;

public interface Cache<K, V> {

    /**
     * Get value for {@code key} or null
     *
     * @param key from cache
     * @return the value or {@code null}
     */
    V get(K key);

    /**
     * put a value in the cache for specified {@code key}
     *
     * @param key   to put in cache
     * @param value to put in cache
     * @return the previous value
     */
    V put(K key, V value);

    /**
     * remove the entry for {@code key} if exist or return {@code null}
     *
     * @param key from cache
     * @return the previous value or {@code null}
     */
    V remove(K key);

    /**
     * clear all the entries in the cache
     */
    void clear();

    /**
     * return the max capacity of cache
     *
     * @return max memory size
     */
    int capacity();

    /**
     * return the current size of the cache
     *
     * @return current memory size
     */
    int size();
}
