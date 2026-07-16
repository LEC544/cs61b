package deque;
import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T>{

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
        /*
        * if the length is 16, start is 15
        * move start to the last index of the list
        * */
        start = Math.floorMod(-1, array.length);
        /*
        * if enlarged, end equals to size.
        * if shrunk, end equals to 0, cause if shrunk, size equals to length.
        * */
        end = size % array.length;
    }

    public int size() {
        return size;
    }


    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {

        private int pos;

        @Override
        public boolean hasNext() {
            return pos < size;
        }

        @Override
        public T next() {
            T res = get(pos);
            pos += 1;
            return res;
        }

        public ArrayDequeIterator() {
            pos = 0;
        }
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
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        } else {
            return array[(start + 1 + index) % array.length]; // start + 1 point to the first item
        }
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int nextStart = Math.floorMod(start + 1, array.length);
        T res = array[nextStart];
        size -= 1;
        start = nextStart;
        if (size <= array.length / 4 && array.length >= 8) {
            resize(array.length / 4);
        }
        return res;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        int nextEnd = Math.floorMod(end - 1, array.length);
        T res = array[nextEnd];
        size -= 1;
        end = nextEnd;
        if (size <= array.length / 4 && array.length >= 8) {
            resize(array.length / 4);
        }
        return res;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof Deque)) {
            return false;
        }
        Deque<T> o = (Deque<T>) object;
        if (o.size() != this.size()) {
            return false;
        }
        for (int i = 0; i < this.size(); i += 1) {
            if (!this.get(i).equals(o.get(i))) {
                return false;
            }
        }
        return true;
    }
}
