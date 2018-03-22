package ru.otus.l31;

import java.util.*;

/**
 *
 */
public class MyArrayList<T> implements List<T> {

    private static final int INITIAL_CAPACITY = 16;

    private int startView;
    private int endView;
    private T[] values;
    private MyArrayList<T> parent;

    public MyArrayList() {
        values = (T[])new Object[INITIAL_CAPACITY];
        startView = 0;
        endView = startView;
    }

    private MyArrayList(int startView, int endView, T[] values) {
        this.startView = startView;
        this.endView = endView;
        this.values = values;
    }

    private MyArrayList(int startView, int endView, T[] values, MyArrayList<T> parent) {
        this.startView = startView;
        this.endView = endView;
        this.values = values;
        this.parent = parent;
    }


    @Override
    public int size() {
        return endView - startView;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (isEmpty())
            return false;

        return indexOf(o) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] copy = new Object[size()];
        System.arraycopy(values, startView, copy, 0, size());
        return copy;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        T1[] copy = a;
        if (copy.length < size()) {
            copy = Arrays.copyOf(a, size());
        } else {
            for (int i = size(); i < copy.length; i++) {
                copy[i] = null;
            }
        }
        for (int i = 0; i < copy.length; i++) {
            copy[i] = (T1)get(i);
        }
        return copy;
    }

    @Override
    public boolean add(T t) {

        if (t == null) {
            throw new RuntimeException("Adding null element doesn't permitted");
        }

        if (contains(t)) {
            return false;
        }

        if ((endView) >= values.length) {
            increaseArraySize();
        }
        values[endView] = t;
        endView++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!contains(o)) {
            return false;
        }
        remove(indexOf(o));
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = false;
        for (T t : c) {
            changed |= add(t);
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        for (T t : c) {
            add(index++, t);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed |= remove(o);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int i = startView;
        while (i < endView) {
            if (!c.contains(values[i])) {
                remove(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        if (parent != null) {
            parent.remove(startView, endView);
        } else {
            values = (T[]) new Object[INITIAL_CAPACITY];
        }
        startView = 0;
        endView = 0;
    }

    @Override
    public T get(int index) {
        int i = getRealIndex(index);
        checkRealIndex(i);
        if (i == endView) {
            throw new IndexOutOfBoundsException();
        }
        return values[i];
    }

    @Override
    public T set(int index, T element) {
        int i = getRealIndex(index);
        checkRealIndex(i);
        if (i == endView) {
            throw new IndexOutOfBoundsException();
        }
        return values[i] = element;
    }

    @Override
    public void add(int index, T element) {
        int i = getRealIndex(index);
        checkRealIndex(i);
        if (endView >= values.length) {
            increaseArraySize();
        }
        System.arraycopy(values, i, values, i + 1, endView - i);
        values[i] = element;
        endView++;
    }

    @Override
    public T remove(int index) {
        checkRealIndex(index);

        T removedElement = values[index];
        values[index] = null;

        System.arraycopy(values, index + 1, values, index, values.length - index - 1);
        values[values.length - 1] = null;

        endView--;

        return removedElement;
    }

    private void remove(int from, int to) {
        for(int i = 0; i < (to - from); i++) {
            remove(from);
        }
    }

    @Override
    public int indexOf(Object o) {
        for(int i = startView; i < endView; i++) {
            if (values[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Метод по факту вырождается до indexOf т.к. реализация на допускает повторяющихся элементов
     */
    @Override
    public int lastIndexOf(Object o) {
        for(int i = endView - 1; i <= startView; i--) {
            if (values[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new MyListIterator<>(startView, endView, this);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        checkIndex(index);
        return new MyListIterator<>(startView, endView, index - 1, this);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (parent != null) {
            return parent.subList(fromIndex, toIndex);
        } else {
            if (fromIndex < 0 || toIndex < 0 || toIndex > endView) {
                throw new IndexOutOfBoundsException();
            }
            if (toIndex < fromIndex) {
                throw new IllegalArgumentException();
            }
            return new MyArrayList<>(startView + fromIndex, startView + toIndex, values, this);
        }
    }


    private int getRealIndex(int index) {
        return startView + index;
    }

    private void checkIndex(int index) {
        checkRealIndex(getRealIndex(index));
    }

    private void checkRealIndex(int index) {
        if (index < startView || index > endView) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void increaseArraySize() {
        T[] newValues = (T[]) new Object[values.length * 2];
        System.arraycopy(values, 0, newValues, 0, values.length);
        values = newValues;
    }

    public class MyListIterator<T> implements ListIterator<T> {

        private int start;
        private int end;
        private int curr;
        private MyArrayList<T> list;

        private MyListIterator(int start, int end, MyArrayList<T> list) {
            this.start = start;
            this.end = end;
            curr = -1;
            this.list = list;
        }

        private MyListIterator(int start, int end, int curr, MyArrayList<T> list) {
            this.start = start;
            this.end = end;
            this.curr = curr;
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            return (curr + 1) < end;
        }

        @Override
        public T next() {
            //System.out.println("curr: " + curr);
            if (curr == -1) {
                curr = start;
            } else {
                curr += 1;
            }
            return list.get(curr);
        }

        @Override
        public boolean hasPrevious() {
            return curr > start;
        }

        @Override
        public T previous() {
            if (hasPrevious()) {
                return list.get(curr);
            } else {
                return null;
            }
        }

        @Override
        public int nextIndex() {
            return curr + 1;
        }

        @Override
        public int previousIndex() {
            return curr;
        }

        @Override
        public void remove() {
            if (curr != -1) {
                list.remove(curr);
            }
        }

        @Override
        public void set(T t) {
            if (curr != -1) {
                list.set(curr, t);
            }
        }

        @Override
        public void add(T t) {
            if (curr != -1) {
                list.add(curr, t);
            }
        }
    }
}
