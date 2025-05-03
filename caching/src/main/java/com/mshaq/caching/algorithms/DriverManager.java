package com.mshaq.caching.algorithms;

public class DriverManager {

    public static void main(String[] args) {

//        List<String> linkedList = new SinglyLinkedList<>();
        List<String> linkedList = new LinkedList<>();

        linkedList.addLast("E");
        linkedList.addFirst("D");
        linkedList.addFirst("C");
        linkedList.addFirst("B");
        linkedList.addFirst("A");
        linkedList.add(0, "A");
        linkedList.add(2, "B");
        linkedList.print();
        linkedList.removeFirst();
        linkedList.print();
        linkedList.removeLast();
        linkedList.print();
        String removed = linkedList.remove(2);
        linkedList.print();
        System.out.println(linkedList.size());
        linkedList.remove(linkedList.size() - 1);
        linkedList.print();
    }
}
