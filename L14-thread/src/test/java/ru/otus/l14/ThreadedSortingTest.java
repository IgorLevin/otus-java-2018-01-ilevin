package ru.otus.l14;

import org.junit.*;
import static ru.otus.l14.Main.mergeSorted;
import static ru.otus.l14.Main.sortInThreadsSync;

public class ThreadedSortingTest {

    @Test
    public void mergeSortedTest1() {
        int[] src1 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] src2 = new int[]{10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
        int[] out = mergeSorted(src1, src2);
        int[] expected = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
        Assert.assertArrayEquals(expected, out);
    }

    @Test
    public void mergeSortedTest2() {
        int[] src1 = new int[]{10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
        int[] src2 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] out = mergeSorted(src1, src2);
        int[] expected = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
        Assert.assertArrayEquals(expected, out);
    }

    @Test
    public void mergeSortedTest3() {
        int[] src1 = new int[]{0, 2, 4, 6, 8, 10};
        int[] src2 = new int[]{1, 3, 5, 7, 9, 11};
        int[] out = mergeSorted(src1, src2);
        int[] expected = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        Assert.assertArrayEquals(expected, out);
    }

    @Test
    public void mergeSortedTest4() {
        int[] src1 = new int[]{1, 3, 5, 7, 9, 11};
        int[] src2 = new int[]{0, 2, 4, 6, 8, 10};
        int[] out = mergeSorted(src1, src2);
        int[] expected = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        Assert.assertArrayEquals(expected, out);
    }

    @Test
    public void mergeSortedTest5() {
        int[] src1 = new int[]{1};
        int[] src2 = new int[]{0, 2, 4, 6, 8, 10};
        int[] out = mergeSorted(src1, src2);
        int[] expected = new int[]{0, 1, 2, 4, 6, 8, 10};
        Assert.assertArrayEquals(expected, out);
    }

    @Test
    public void mergeSortedTest6() {
        int[] src1 = new int[]{1, 3, 5, 7, 9, 11};
        int[] src2 = new int[]{4};
        int[] out = mergeSorted(src1, src2);
        int[] expected = new int[]{1, 3, 4, 5, 7, 9, 11};
        Assert.assertArrayEquals(expected, out);
    }

    @Test
    public void mergeSortedTest7() {
        int[] src1 = new int[]{1, 3, 5, 7, 9, 11};
        int[] src2 = new int[]{};
        int[] out = mergeSorted(src1, src2);
        int[] expected = new int[]{1, 3, 5, 7, 9, 11};
        Assert.assertArrayEquals(expected, out);
    }

    @Test
    public void mergeSortedTest8() {
        int[] src1 = new int[]{};
        int[] src2 = new int[]{0, 2, 4, 6, 8, 10};
        int[] out = mergeSorted(src1, src2);
        int[] expected = new int[]{0, 2, 4, 6, 8, 10};
        Assert.assertArrayEquals(expected, out);
    }

    @Test
    public void syncSortTest() {
        int[] data     = new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] expected = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        try {
            for (int numOfThreads = 1; numOfThreads < 8; numOfThreads++) {
                int[] out = sortInThreadsSync(data, numOfThreads);
                Assert.assertArrayEquals(expected, out);
            }
        } catch (Exception e) {
            System.err.println(e);
            Assert.fail();
        }
    }
}