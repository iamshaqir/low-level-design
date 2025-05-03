package com.mshaq.caching.algorithms;

import java.util.NoSuchElementException;

public class SinglyLinkedList<E> implements List<E> {

    private static int size = 0;
    private Node<E> head;
    private Node<E> tail;

    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);
        size++;
        if (head == null) {
            head = tail = newNode;
            return;
        }
        newNode.next = head;
        head = newNode;
    }

    public void addLast(E element) {
        Node<E> newNode = new Node<>(element);
        size++;
        if (head == null) {
            head = tail = newNode;
            return;
        }
        tail.next = newNode;
        tail = newNode;
    }

    public E removeFirst() {
        if (head == null) throw new NoSuchElementException("No Element to remove");
        E data = head.data;
        if (size == 1) {
            head = tail = null;
            size = 0;
            return data;
        }
        head = head.next;
        size--;
        return data;
    }

    public E removeLast() {
        if (head == null) throw new NoSuchElementException("No Element to remove");
        if (size == 1) return removeFirst();
        Node<E> temp = head;
        int counter = 0;
        while (counter < size - 2) {
            temp = temp.next;
            counter++;
        }
        E data = temp.next.data;
        temp.next = null;
        tail = temp;
        size--;
        return data;
    }

    public void add(int index, E element) {
        if (index == 0) {
            addFirst(element);
            return;
        }
        if (index == size - 1) {
            addLast(element);
            return;
        }

        Node<E> newNode = new Node<>(element);
        size++;
        int counter = 0;
        Node<E> temp = head;
        while (counter < index - 1) {
            temp = temp.next;
            counter++;
        }
        newNode.next = temp.next;
        temp.next = newNode;
    }

    public E remove(int index) {
        if (index == 0) return removeFirst();
        if (index == size - 1) return removeLast();
        int counter = 0;
        Node<E> previous = head;
        while (counter < index - 1) {
            previous = previous.next;
            counter++;
        }
        E data = previous.next.data;
        previous.next = previous.next.next;
        size--;
        return data;
    }

    public boolean contains(E data) {
        if (head == null) throw new NoSuchElementException("No Element");
        Node<E> temp = head;
        while (temp != null) {
            if (temp.data == data) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public void print() {
        Node<E> tempNode = head;
        while (tempNode != null) {
            System.out.print(tempNode.data + " -> ");
            tempNode = tempNode.next;
        }
        System.out.println("null");
    }
}
