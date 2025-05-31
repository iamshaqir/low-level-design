package com.mshaq.caching.storage;

import com.mshaq.caching.exceptions.NotFoundException;
import com.mshaq.caching.exceptions.StorageFullException;

import java.util.HashMap;
import java.util.Map;

public class HashMapBasedStorage<K, V> implements Storage<K, V> {

    private final int capacity;
    private final Map<K, V> inMemoryDatabase;

    public HashMapBasedStorage(int capacity) {
        this.capacity = capacity;
        this.inMemoryDatabase = new HashMap<>(capacity);
    }

    @Override
    public void save(K key, V value) {
        if (isFull()) throw new StorageFullException("In memory database is full");
        inMemoryDatabase.put(key, value);
    }

    @Override
    public V remove(K key) {
        if (!inMemoryDatabase.containsKey(key)) throw new NotFoundException(key + " does not exist in database");
        return inMemoryDatabase.remove(key);
    }

    @Override
    public V get(K key) {
        if (!inMemoryDatabase.containsKey(key)) throw new NotFoundException(key + " does not exist in database");
        return inMemoryDatabase.get(key);
    }

    private boolean isFull() {
        return capacity == inMemoryDatabase.size();
    }
}
