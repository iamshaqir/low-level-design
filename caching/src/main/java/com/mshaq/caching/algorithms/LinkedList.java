package com.mshaq.caching.algorithms;

import java.util.NoSuchElementException;

public class LinkedList<T> {

    private static int size = 0;
    private Node<T> head;
    private Node<T> tail;

    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element);
        size++;
        if (head == null) {
            head = tail = newNode;
            return;
        }
        newNode.next = head;
        head = newNode;
    }

    public void addLast(T element) {
        Node<T> newNode = new Node<>(element);
        size++;
        if (head == null) {
            head = tail = newNode;
            return;
        }
        tail.next = newNode;
        tail = newNode;
    }

    public T removeFirst() {
        if (head == null) throw new NoSuchElementException("No Element to remove");
        T data = head.data;
        if (size == 1) {
            head = tail = null;
            size = 0;
            return data;
        }
        head = head.next;
        size--;
        return data;
    }

    public T removeLast() {
        if (head == null) throw new NoSuchElementException("No Element to remove");
        if (size == 1) return removeFirst();
        Node<T> temp = head;
        int counter = 0;
        while (counter < size - 1) {
            temp = temp.next;
            counter++;
        }
        T data = temp.data;
        temp.next = null;
        tail = temp;
        size--;
        return data;
    }

    public void add(int index, T element) {
        if (index == 0) {
            addFirst(element);
            return;
        }
        if (index == size - 1) {
            addLast(element);
            return;
        }

        Node<T> newNode = new Node<>(element);
        size++;
        int counter = 0;
        Node<T> temp = head;
        while (counter < index - 1) {
            temp = temp.next;
            counter++;
        }
        newNode.next = temp.next;
        temp.next = newNode;
    }

    public T remove(int index) {
        if (index == 0) return removeFirst();
        if (index == size - 1) return removeLast();
        int counter = 0;
        Node<T> previous = head;
        while (counter < index - 1) {
            previous = previous.next;
            counter++;
        }
        T data = previous.next.data;
        previous.next = previous.next.next;
        size--;
        return data;
    }

    public boolean contains(T data) {
        if (head == null) throw new NoSuchElementException("No Element");
        Node<T> temp = head;
        while (temp != null) {
            if (temp.data == data) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }
}
