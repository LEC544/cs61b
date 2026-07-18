package flik;

import org.junit.*;

public class TestFlik {
    @Test
    public void Test() {
        int i = 10;
        int j = 10;
        Assert.assertTrue(Flik.isSameNumber(i, j));
    }
}