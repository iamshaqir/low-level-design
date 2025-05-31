package com.mshaq.caching.eviction;

import java.util.HashMap;
import java.util.Map;

/**
 * Leetcode: <a href="https://leetcode.com/problems/lru-cache/description/">LRU Cache</a>
 * <p>
 * Implements the Least Recently Used (LRU) cache eviction policy.
 * This cache stores key-value pairs and removes the least recently accessed item
 * when the cache reaches its capacity.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
public class LRUCache<K, V> implements EvictionPolicy<K, V> {
    private final int capacity;
    private final Node<K, V> head;
    private final Node<K, V> tail;
    private final Map<K, Node<K, V>> map;
    private static final int DEFAULT_CAPACITY = 16;


    /**
     * Constructs an LRUCache with a default capacity of 16.
     */
    public LRUCache() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructs an LRUCache with the specified capacity.
     *
     * @param capacity the maximum number of entries the cache can hold
     * @throws IllegalArgumentException if the capacity is non-positive
     */
    public LRUCache(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        this.capacity = capacity;
        head = new Node<>();
        tail = new Node<>();
        head.next = tail;
        tail.previous = head;
        map = new HashMap<>();
    }

    /**
     * Retrieves the value associated with the given key. If the key exists,
     * the corresponding entry is moved to the front of the cache (most recently used).
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null} if this cache
     * contains no mapping for the key
     */
    @Override
    public V get(K key) {
        if (!map.containsKey(key)) return null;
        Node<K, V> node = map.get(key);
        // remove and insert to make it recent
        remove(node);
        insert(node);
        return node.value;
    }

    /**
     * Associates the specified value with the specified key in this cache.
     * If the cache previously contained a mapping for the key, the old value is replaced.
     * If the cache is full, the least recently used entry is removed before adding the new entry.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     */
    @Override
    public void put(K key, V value) {
        if (map.containsKey(key)) {
            // remove key, because we need to add it at first
            remove(map.get(key));
        }

        if (isFull()) {
            // remove last key which will be at tail's previous
            remove(tail.previous);
        }
        insert(new Node<>(key, value));
    }


    /**
     * Removes a node from the linked list and the map.
     *
     * @param node the node to be removed
     */
    private void remove(Node<K, V> node) {
        map.remove(node.key);
        // point curr previous to curr next
        node.previous.next = node.next;
        node.next.previous = node.previous;
    }

    /**
     * Inserts a node at the head of the linked list (most recently used) and adds it to the map.
     *
     * @param node the node to be inserted
     */
    private void insert(Node<K, V> node) {
        map.put(node.key, node);
        node.next = head.next; // point current node to new node's next
        node.next.previous = node; // point next node's previous to new node
        head.next = node;
        node.previous = head;
    }

    /**
     * Checks if the cache is full.
     *
     * @return true if the cache is full, false otherwise
     */
    private boolean isFull() {
        return capacity == map.size();
    }

    /**
     * Represents a node in the doubly linked list used by the LRU cache.
     */
    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;
        Node<K, V> previous;

        /**
         * Constructs an empty node.
         */
        public Node() {
            this(null, null);
        }

        /**
         * Constructs a node with the specified key and value.
         *
         * @param key   the key of the node
         * @param value the value of the node
         */
        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }
}
