package deque;
import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    private class LinkedListNode{
        private T content;
        private LinkedListNode front;
        private LinkedListNode next;

        private LinkedListNode() {
            content = null;
            front = null;
            next = null;
        }
        private LinkedListNode(T item) {
            content = item;
            front = null;
            next = null;
        }
        private T getrecursive(int index) {
            if (index < 0 || index >= size) {
                return null;
            }
            if (index == 0) {
                return content;
            } else {
                return next.getrecursive(index - 1);
            }
        }
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private int pos;

        public LinkedListDequeIterator() {
            pos = 0;
        }

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
    }


    private int size;
    private LinkedListNode end;
    private LinkedListNode start;
    public LinkedListDeque() {
        size = 0;
        end = new LinkedListNode();
        start = new LinkedListNode();
        end.front = start;
        start.next = end;
    }

    public void addFirst(T item) {
        size += 1;
        LinkedListNode t = new LinkedListNode(item);
        t.front = start;
        t.next = start.next;
        start.next.front = t;
        start.next = t;
    }

    public void addLast(T item) {
        size += 1;
        LinkedListNode t = new LinkedListNode(item);
        t.front = end.front;
        t.next = end;
        end.front.next = t;
        end.front = t;
    }


    public int size() {
        return size;
    }

    public void printDeque() {
        LinkedListNode p = start.next;
        while (p != end) {
            System.out.print(p.content + " ");
            p = p.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (isEmpty())
            return null;
        LinkedListNode p = start.next;
        T res = p.content;
        start.next = p.next;
        p.next.front = start;
        size -= 1;
        return res;
    }

    public T removeLast() {
        if (isEmpty())
            return null;
        LinkedListNode p = end.front;
        T res = p.content;
        end.front = p.front;
        p.front.next = end;
        size -= 1;
        return res;
    }

    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        LinkedListNode  p = start.next;
        for (int i = 0; i < index; i += 1) {
            p = p.next;
        }
        return p.content;
    }

    public T getRecursive(int index) {
        LinkedListNode p = start.next;
        return p.getrecursive(index);
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if(!(o instanceof LinkedListDeque)) {
            return false;
        }
        LinkedListDeque object = (LinkedListDeque) o;
        if (object.size() == this.size()) {
            for (int i = 0; i < size(); i += 1) {
                if (object.get(i) != this.get(i)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}