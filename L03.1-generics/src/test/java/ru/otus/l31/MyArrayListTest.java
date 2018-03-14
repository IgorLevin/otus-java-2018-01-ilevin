package ru.otus.l31;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.*;

/**
 * @author Igor Levin
 */
public class MyArrayListTest {

    @Test
    public void size() {
        MyArrayList<Integer> list = new MyArrayList<>();
        assertEquals(list.size(), 0);
        list.add(-2);
        list.add(-1);
        Assert.assertEquals(list.size(), 2);
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
        assertEquals(list.size(), 10002);
    }

    @Test
    public void isEmpty() {
        MyArrayList<Integer> list = new MyArrayList<>();
        assertTrue(list.isEmpty());
        list.add(5);
        assertFalse(list.isEmpty());
    }

    @Test
    public void contains() {
        MyArrayList<Integer> list = new MyArrayList<>();
        assertFalse(list.contains(2));

        for(int i = 0; i < 10; i++) {
            list.add(i);
        }
        assertTrue(list.contains(2));
        assertTrue(list.contains(5));
        assertFalse(list.contains(20));
    }

    @Test
    public void iterator() {
        listIterator();
    }

    @Test
    public void toArray() {
        Integer[] source = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        MyArrayList<Integer> list = new MyArrayList<>();
        assertArrayEquals(new Object[]{}, list.toArray());

        for (int i = 0; i < source.length; i++) {
            list.add(source[i]);
        }
        assertArrayEquals(source, list.toArray());
    }

    @Test
    public void toArray1() {
        Integer[] source = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        MyArrayList<Integer> list = new MyArrayList<>();
        assertArrayEquals(new Integer[]{}, list.toArray(new Integer[0]));

        for (int i = 0; i < source.length; i++) {
            list.add(source[i]);
        }
        assertArrayEquals(source, list.toArray(new Integer[0]));
    }

    @Test
    public void add() {
        Integer[] source = new Integer[]{0, 1, 2, 3};
        Integer[] result = new Integer[]{0, 1, 5, 2, 3};
        MyArrayList<Integer> list = new MyArrayList<>();
        for (int i = 0; i < source.length; i++) {
            list.add(source[i]);
        }
        list.add(2, 5);
        assertArrayEquals(result, list.toArray(new Integer[0]));

        try {
            list.add(8, 5);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

        list = new MyArrayList<>();
        list.add(0, 5);
        assertArrayEquals(new Integer[]{5}, list.toArray(new Integer[0]));

        list = new MyArrayList<>();
        list.add(0, 5);
        list.add(0, 4);
        list.add(0, 3);
        list.add(0, 2);
        list.add(0, 1);
        list.add(0, 0);

        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5}, list.toArray(new Integer[0]));

        list = new MyArrayList<>();
        list.add(0, 0);
        list.add(1, 1);
        list.add(2, 2);
        list.add(3, 3);
        list.add(4, 4);
        list.add(5, 5);

        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 5}, list.toArray(new Integer[0]));
    }

    @Test
    public void removeElement() {
        MyArrayList<Integer> list = new MyArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        list.remove(new Integer(3));
        assertArrayEquals(new Integer[]{0, 1, 2, 4}, list.toArray(new Integer[0]));

        list.remove(new Integer(0));
        assertArrayEquals(new Integer[]{1, 2, 4}, list.toArray(new Integer[0]));

        list.remove(new Integer(4));
        assertArrayEquals(new Integer[]{1, 2}, list.toArray(new Integer[0]));
    }

    @Test
    public void removeByIndex() {
        MyArrayList<Integer> list = new MyArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        list.remove(3);
        assertArrayEquals(new Integer[]{0, 1, 2, 4}, list.toArray(new Integer[0]));

        list.remove(0);
        assertArrayEquals(new Integer[]{1, 2, 4}, list.toArray(new Integer[0]));

        list.remove(2);
        assertArrayEquals(new Integer[]{1, 2}, list.toArray(new Integer[0]));
    }

    @Test
    public void containsAll() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        MyArrayList<Integer> list = new MyArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
            arrayList.add(i);
        }
        assertTrue(list.containsAll(arrayList));

        arrayList.add(5);
        assertFalse(list.containsAll(arrayList));

        arrayList.clear();
        assertTrue(list.containsAll(arrayList));

        for (int i = 0; i < 5; i++) {
            arrayList.add(i * 10);
        }
        assertFalse(list.containsAll(arrayList));
    }

    @Test
    public void addAll() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        MyArrayList<Integer> list = new MyArrayList<>();

        assertEquals(list.size(), 0);
        assertEquals(arrayList.size(), list.size());
        assertArrayEquals(arrayList.toArray(), list.toArray());

        for (int i = 0; i < 5; i++) {
            arrayList.add(i);
        }
        list.addAll(arrayList);

        assertEquals(arrayList.size(), list.size());
        assertArrayEquals(arrayList.toArray(), list.toArray());
    }

    @Test
    public void addAll1() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        MyArrayList<Integer> list = new MyArrayList<>();

        for (int i = 0; i < 5; i++) {
            arrayList.add(i);
        }

        try {
            list.addAll(5, arrayList);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }
        list.addAll(0, arrayList);

        assertEquals(arrayList.size(), list.size());
        assertArrayEquals(arrayList.toArray(), arrayList.toArray());

        list.addAll(5, arrayList);

        assertEquals(arrayList.size() * 2, list.size());
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4, 0, 1, 2, 3, 4}, list.toArray());

    }

    @Test
    public void removeAll() {
        ArrayList<Integer> arrayList = new ArrayList<>();

        MyArrayList<Integer> list = new MyArrayList<>();
        assertEquals(list.size(), 0);

        for (int i = 0; i < 5; i++) {
            list.add(i);
            arrayList.add(i);
        }

        assertEquals(list.size(), 5);

        list.removeAll(arrayList);
        assertEquals(list.size(), 0);

        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        arrayList = new ArrayList<>();
        arrayList.add(2);
        arrayList.add(3);

        list.removeAll(arrayList);
        assertEquals(list.size(), 3);
    }

    @Test
    public void retainAll() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        MyArrayList<Integer> list = new MyArrayList<>();

        for (int i = 0; i < 5; i++) {
            list.add(i);
            arrayList.add(i);
        }

        list.retainAll(arrayList);
        assertArrayEquals(arrayList.toArray(), list.toArray());

        list = new MyArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        arrayList = new ArrayList<>();
        arrayList.add(2);
        arrayList.add(3);

        list.retainAll(arrayList);
        assertArrayEquals(new Integer[]{2, 3}, list.toArray());
    }

    @Test
    public void clear() {
        MyArrayList<Integer> list = new MyArrayList<>();
        assertEquals(list.size(), 0);
        list.clear();
        assertEquals(list.size(), 0);
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        assertNotEquals(list.size(), 0);
        list.clear();
        assertEquals(list.size(), 0);
    }

    @Test
    public void get() {
        MyArrayList<Integer> list = new MyArrayList<>();
        try {
            list.get(0);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        assertEquals(list.get(0), new Integer(0));
        assertEquals(list.get(2), new Integer(2));
        assertEquals(list.get(4), new Integer(4));
    }

    @Test
    public void set() {
        MyArrayList<Integer> list = new MyArrayList<>();
        try {
            list.set(0, new Integer(5));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        assertArrayEquals(new Integer[]{0, 1, 2, 3, 4}, list.toArray());
        list.set(0, new Integer(7));
        assertArrayEquals(new Integer[]{7, 1, 2, 3, 4}, list.toArray());
        list.set(2, new Integer(7));
        assertArrayEquals(new Integer[]{7, 1, 7, 3, 4}, list.toArray());
        list.set(4, new Integer(7));
        assertArrayEquals(new Integer[]{7, 1, 7, 3, 7}, list.toArray());
    }

    @Test
    public void indexOf() {
        MyArrayList<Integer> list = new MyArrayList<>();
        assertEquals(list.indexOf(new Integer(8)), -1);
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        for (int i = 0; i < 5; i++) {
            assertEquals(list.indexOf(new Integer(i)), i);
        }
        assertEquals(list.indexOf(new Integer(8)), -1);
    }

    @Test
    public void listIterator() {
        Integer[] source = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        MyArrayList<Integer> list = new MyArrayList<>();
        assertNotNull(list.listIterator());
        for(int i = 0; i < source.length; i++) {
            list.add(source[i]);
        }
        assertNotNull(list.listIterator());
        ListIterator<Integer> it = list.listIterator();

        int sum = 0;
        int pos = 0;

        while(it.hasNext()) {
            Integer val = it.next();
            assertEquals(val, source[pos++]);
            sum += val;
        }
        assertEquals(sum, 45);
    }

    @Test
    public void listIterator1() {
        Integer[] source = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        MyArrayList<Integer> list = new MyArrayList<>();
        assertNotNull(list.listIterator(0));
        for(int i = 0; i < source.length; i++) {
            list.add(source[i]);
        }
        try {
            list.listIterator(11);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }
        try {
            list.listIterator(-1);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }
        assertNotNull(list.listIterator(0));
        assertNotNull(list.listIterator(2));
        assertNotNull(list.listIterator(10));

        ListIterator<Integer> it = list.listIterator(5);

        int sum = 0;
        int pos = 5;

        while(it.hasNext()) {
            Integer val = it.next();
            assertEquals(val, source[pos++]);
            sum += val;
        }
        assertEquals(sum, 35);
    }

    @Test
    public void subList() {
        Integer[] source = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        MyArrayList<Integer> list = new MyArrayList<>();
        for(int i = 0; i < source.length; i++) {
            list.add(source[i]);
        }

        List<Integer> list2 = list.subList(0, 10);
        assertArrayEquals(source, list2.toArray(new Integer[0]));

        assertNotNull(list.subList(0, 0));
        assertEquals(0, list.subList(0, 0).size());

        try {
            list.subList(0, 20);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

        try {
            list.subList(-1, 5);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

        List<Integer> list3 = list.subList(0, 6);
        list3.clear();
        assertEquals(0, list3.size());
        assertArrayEquals(new Integer[]{6, 7, 8, 9}, list.toArray(new Integer[0]));
    }
}