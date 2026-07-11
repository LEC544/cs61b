package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        System.out.println("Timing table for getLast");
        AList<Integer> Ns = new AList();
        AList<Double> times = new AList();
        AList<Integer> opCounts = new AList();
        int addLastTime = 1000;
        int opCount = 10000;
        for (int i = 0; i < 8; i += 1) {
            Ns.addLast(addLastTime);
            opCounts.addLast(opCount);
            addLastTime *= 2;
        }
        for (int i = 0; i < Ns.size(); i += 1) {
            int n = Ns.get(i);
            int m = opCounts.get(i);
            SLList<Integer> tmp = new SLList<>();
            for (int j = 0; j < n; j += 1) {
                tmp.addLast(42);
            }
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < m; j += 1) {
                tmp.getLast();
            }
            double timeINseconds = sw.elapsedTime();
            times.addLast(timeINseconds);
        }
        printTimingTable(Ns, times, opCounts);
    }

}
