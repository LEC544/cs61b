package flik;

import org.junit.*;

public class TestFlik {
    @Test
    public void Test() {
        int i = 128;
        int j = 128;
        Assert.assertTrue(Flik.isSameNumber(i, j));
    }
}