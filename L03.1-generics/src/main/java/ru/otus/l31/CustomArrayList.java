package ru.otus.l31;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class CustomArrayList {

    public static void main(String... args) {

        System.out.println("Create list");
        MyArrayList<Integer> list = new MyArrayList<>();
        System.out.println("Size(): " + list.size());

        System.out.println("Add elements into list");
        for (int i = 0; i < 24; i++) {
            list.add(i);
        }
        System.out.println("Size(): " + list.size());
        printList(list);

        System.out.println("Remove one element");
        list.remove(0);
        System.out.println("Size(): " + list.size());
        printList(list);


        List<Integer> testSourceList = new ArrayList<>();
        for (int i = 25; i > 0; i--) {
            testSourceList.add(i);
        }
        list.clear();
        list.addAll(testSourceList);

        System.out.println("List before sort");
        printList(list);

        Collections.sort(list);

        System.out.println("Sorted list");
        printList(list);

        System.out.println("Copy source list into test list");
        Collections.copy(list, testSourceList);

        System.out.println("List after copying");
        printList(list);
    }

    static void printList(MyArrayList<?> list) {
        for (Object o : list) {
            System.out.print(o + " ");
        }
        System.out.println();
    }
}
