package com.mshaq.caching.eviction;

import java.util.HashMap;
import java.util.Map;

/**
 * Leetcode: <a href="https://leetcode.com/problems/lru-cache/description/">LRU Cache</a>
 */
public class LRUCache<K, V> {
    private final int size;
    private Node<K, V> head;
    private Node<K, V> tail;
    private final Map<K, Node<K, V>> map;

    public LRUCache(int size) {
        this.size = size;
        head = new Node<>();
        tail = new Node<>();
        head.next = tail;
        tail.previous = head;
        map = new HashMap<>();
    }

    public V get(K key) {
        if (!map.containsKey(key)) {
            return null;
        }
        Node<K, V> node = map.get(key);
        // remove and insert to make it recent
        remove(node);
        insert(node);
        return node.value;
    }

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

    private void remove(Node<K, V> node) {
        map.remove(node.key);
        // point curr previous to curr next
        node.previous.next = node.next;
        node.next.previous = node.previous;
    }

    private void insert(Node<K, V> node) {
        map.put(node.key, node);
        node.next = head.next; // point current node to new node's next
        node.next.previous = node; // point next node's previous to new node
        head.next = node;
        node.previous = head;
    }

    private boolean isFull() {
        return size == map.size();
    }

    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;
        Node<K, V> previous;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Node() {
            this(null, null);
        }
    }
}
