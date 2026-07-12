package deque;

import jh61b.junit.In;

public class ArrayDeque<T> {

    private T[] arrey;
    private int size;
    private int start;
    private int end;
    public ArrayDeque() {
        arrey = (T[]) new Object[8];
        size = 0;
        start = 0;
        end = 1;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        for (int i = 0; i < size; i += 1) {
            a[i] = get(i);
        }
        arrey = a;
        start = Math.floorMod(-1, arrey.length);
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
        if (size == arrey.length) {
            resize(arrey.length * 2);
        }
        arrey[start] = item;
        start = Math.floorMod((start - 1), arrey.length);
        size += 1;
    }

    public void addLast(T item) {
        if (size == arrey.length) {
            resize(arrey.length * 2);
        }
        arrey[end] = item;
        end = Math.floorMod(end + 1, arrey.length);
        size += 1;
    }
    public T get(int Index) {
        if (Index < 0 || Index >= size) {
            return null;
        } else {
            return arrey[(start + Index + 1) % arrey.length];
        }
    }

    public T removeFirst() {
        if (size < 0)
            return null;
        int next_start = Math.floorMod(start + 1, arrey.length);
        T res = arrey[next_start];
        size -= 1;
        start = next_start;
        if (size <= arrey.length / 4) {
            resize(arrey.length / 4);
        }
        return res;
    }

    public T removeLast() {
        if (size < 0)
            return null;
        int next_end = Math.floorMod(end - 1, arrey.length);
        T res = arrey[next_end];
        size -= 1;
        end = next_end;
        if (size <= arrey.length / 4) {
            resize(arrey.length / 4);
        }
        return res;
    }
}
