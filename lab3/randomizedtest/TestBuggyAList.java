package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.checkerframework.dataflow.qual.TerminatesExecution;
import org.junit.Assert;
import org.junit.Test;
import timingtest.AList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        int N = 500;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                System.out.println("size: " + size);
            } else if (operationNumber == 2) {
                //get Last
                if (L.size() < 1) {
                    System.out.println("no element");
                } else {
                    int Lastelement = L.getLast();
                    System.out.println("getLast(" + Lastelement + ")");
                }
            } else if (operationNumber == 3) {
                if (L.size() < 1) {
                    System.out.println("no element");
                } else {
                    int Lastelement = L.removeLast();
                    System.out.println("removeLast(" + Lastelement + ")");
                }
            }
        }
    }
    @Test
    public void bugalist() {
        BuggyAList<Integer> L = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                System.out.println("size: " + size);
            } else if (operationNumber == 2) {
                //get Last
                if (L.size() < 1) {
                    System.out.println("no element");
                } else {
                    int Lastelement = L.getLast();
                    System.out.println("getLast(" + Lastelement + ")");
                }
            } else if (operationNumber == 3) {
                if (L.size() < 1) {
                    System.out.println("no element");
                } else {
                    int Lastelement = L.removeLast();
                    System.out.println("removeLast(" + Lastelement + ")");
                }
            }
        }
    }
}
