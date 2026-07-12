package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArreyDequeTest {
    @Test
    public void addTest() {
        ArreyDeque<Integer> a = new ArreyDeque<>();
        a.addFirst(2);
        a.addLast(4);
        a.addLast(44);
        a.addFirst(45);
        a.addLast(32);
        a.addLast(33);
        a.addLast(34);
        a.addLast(35);
        assertEquals(35,(int) a.get(7));
        a.addLast(455);
        assertEquals(45,(int) a.get(0));
        assertEquals(a.size(), 9);
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();
        a.removeLast();

    }
}
