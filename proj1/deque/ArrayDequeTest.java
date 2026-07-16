package deque;

import org.junit.Test;

import java.beans.IntrospectionException;

import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void addTest() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        for (int i = 0; i < 8; i += 1) {
            a.addLast(i);
        }
        for (int i = 0; i < 6; i += 1) {
            a.removeLast();
        }
    }


    @Test
    public void getTest() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        for (int i = 0; i < 1000; i += 1) {
            a.addLast(i);
        }
        for (int i = 0; i < 1000; i += 1) {
            assertEquals(i, (int) a.get(i));
        }
    }

    @Test
    public void removeEmptyTest() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        assertNull(a.removeFirst());
        assertNull(a.removeLast());
    }

    @Test
    /* Check if you can create ArreyDeques with different parameterized types*/
    public void multipleParamTest() {

        ArrayDeque<String>  lld1 = new ArrayDeque<String>();
        ArrayDeque<Double>  lld2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> lld3 = new ArrayDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    public void IteratorTest() {
        ArrayDeque<Integer> lld = new ArrayDeque<>();
        lld.addLast(32);
        lld.addLast(43);
        lld.addLast(56);
        for (int x : lld) {
            System.out.print(x + " ");
        }
    }

    @Test
    public void equalsTest() {
        ArrayDeque<Integer> lld = new ArrayDeque<>();
        lld.addLast(32);
        lld.addLast(43);
        lld.addLast(56);
        ArrayDeque<Integer> lld2 = new ArrayDeque<>();
        lld2.addLast(32);
        lld2.addLast(43);
        lld2.addLast(56);
        assertTrue(lld.equals(lld2));
        lld2.addLast(324);
        assertFalse(lld.equals(lld2));
        LinkedListDeque<Integer> lld3 = new LinkedListDeque<>();
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        assertTrue(lld1.equals(lld3));
        assertFalse(lld.equals(lld3));
        lld3.addLast(32);
        lld3.addLast(43);
        lld3.addLast(56);
        assertTrue(lld.equals(lld3));
        lld3.addLast(null);
        assertFalse(lld.equals(lld3));
    }

}
