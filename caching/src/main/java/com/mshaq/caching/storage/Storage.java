package com.mshaq.caching.storage;

import com.sun.jdi.Value;

public interface Storage<K, V> {
    void save(K key, V value);

    V remove(K key);

    V get(K key);
}
