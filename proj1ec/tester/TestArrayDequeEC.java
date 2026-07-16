package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    public void StudentArrayDeque() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        String message = "";
        for (int i = 0; i < 200; i += 1) {
            sad1.addLast(i);
            message += "addLast(" + i + ")\n";
        }
        for (int i = 0; i < 200; i += 1) {
            Integer actual = sad1.removeFirst();
            message += "removeFirst()\n";
            Integer expected = (Integer) i;
            assertEquals(message,expected, actual);
        }
    }
}