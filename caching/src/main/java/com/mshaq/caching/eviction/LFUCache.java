package com.mshaq.caching.eviction;

import java.util.HashMap;
import java.util.Map;

public class LFUCache<K, V> implements EvictionPolicy<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private final int capacity;
    private int size;
    private int minimumFrequency;
    Map<K, Node<K, V>> cache;
    Map<Integer, LinkedList<K, V>> freqMap;

    public LFUCache() {
        this(DEFAULT_CAPACITY);
    }

    public LFUCache(int capacity) {
        this.size = 0;
        this.capacity = capacity;
        this.minimumFrequency = 0;
        this.cache = new HashMap<>();
        this.freqMap = new HashMap<>();
    }

    @Override
    public void put(K key, V value) {
        if (capacity <= 0) throw new RuntimeException("No capacity to put element");

        if (cache.containsKey(key)) {
            Node<K, V> node = cache.get(key);
            node.value = value;
            updateNode(node);
            return;
        }

        Node<K, V> newNode = new Node<>(key, value);
        size++;
        if (size > capacity) {
            LinkedList<K, V> currentList = freqMap.get(minimumFrequency);
            // remove LFU(get node with least frequency) + LRU(get last node of list)
            currentList.remove(currentList.tail.previous);
            cache.remove(currentList.tail.previous.key);
            size--;
        }
        minimumFrequency = 1; // For every first node created is minimum frequency will be 1

        // First unique newNode to be added, check if list with that frequency exists
        // if exists get and add, if not create new and add
        LinkedList<K, V> currentList = freqMap.getOrDefault(newNode.frequency, new LinkedList<>());
        currentList.add(newNode);
        freqMap.put(newNode.frequency, currentList);
        cache.put(key, newNode);
    }

    @Override
    public V get(K key) {
        if (!cache.containsKey(key)) return null;
        Node<K, V> node = cache.get(key);
        updateNode(node);
        return node.value;
    }

    private void updateNode(Node<K, V> node) {
        int nodeFreq = node.frequency;
        LinkedList<K, V> currentList = freqMap.get(nodeFreq);
        currentList.remove(node);
        node.frequency += 1;
        // At some point you won't have the least frequency, use minimumFrequency as a global variable to check that
        if (nodeFreq == minimumFrequency && currentList.size == 0) {
            minimumFrequency += 1;
        }
        LinkedList<K, V> newList = freqMap.getOrDefault(node.frequency, new LinkedList<>());
        newList.add(node);
        freqMap.put(node.frequency, newList);
    }


    private static class LinkedList<K, V> {
        Node<K, V> head;
        Node<K, V> tail;
        int size;

        public LinkedList() {
            this.size = 0;
            head = new Node<>();
            tail = new Node<>();
            head.next = tail;
            tail.previous = head;
        }

        public void add(Node<K, V> node) {
            node.next = head.next;
            node.next.previous = node;
            head.next = node;
            node.previous = head;
            size++;
        }

        public void remove(Node<K, V> node) {
            node.next.previous = node.previous;
            node.previous.next = node.next;
            size--;
        }

    }

    private static class Node<K, V> {
        K key;
        V value;
        int frequency;
        Node<K, V> next;
        Node<K, V> previous;

        public Node() {
            this(null, null);
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.frequency = 1;
        }
    }
}
