package deque;

import jh61b.junit.In;

public class ArrayDeque<T> {

    private T[] array;
    private int size;
    private int start;
    private int end;
    public ArrayDeque() {
        array = (T[]) new Object[8];
        size = 0;
        start = 0;
        end = 1;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        for (int i = 0; i < size; i += 1) {
            a[i] = get(i);
        }
        array = a;
        start = Math.floorMod(-1, array.length);
        end = size;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }

    public void addFirst(T item) {
        if (size == array.length) {
            resize(array.length * 2);
        }
        array[start] = item;
        start = Math.floorMod((start - 1), array.length);
        size += 1;
    }

    public void addLast(T item) {
        if (size == array.length) {
            resize(array.length * 2);
        }
        array[end] = item;
        end = Math.floorMod(end + 1, array.length);
        size += 1;
    }
    public T get(int Index) {
        if (Index < 0 || Index >= size) {
            return null;
        } else {
            return array[(start + Index + 1) % array.length];
        }
    }

    public T removeFirst() {
        if (isEmpty())
            return null;
        int next_start = Math.floorMod(start + 1, array.length);
        T res = array[next_start];
        size -= 1;
        start = next_start;
        if (size <= array.length / 4 && array.length >= 8) {
            resize(array.length / 4);
        }
        return res;
    }

    public T removeLast() {
        if (isEmpty())
            return null;
        int next_end = Math.floorMod(end - 1, array.length);
        T res = array[next_end];
        size -= 1;
        end = next_end;
        if (size <= array.length / 4 && array.length >= 8) {
            resize(array.length / 4);
        }
        return res;
    }
}
