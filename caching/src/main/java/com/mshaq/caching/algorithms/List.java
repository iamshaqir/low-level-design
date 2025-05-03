package com.mshaq.caching.algorithms;

interface List<E> {
    void addFirst(E element);

    void addLast(E element);

    E removeFirst();

    E removeLast();

    void add(int index, E element);

    E remove(int index);

    boolean contains(E data);

    int size();

    void print();

    default void print(Node<E> head) {
        Node<E> tempNode = head;
        while (tempNode != null) {
            System.out.print(tempNode.data + " -> ");
            tempNode = tempNode.next;
        }
        System.out.println("null");
    }
}
