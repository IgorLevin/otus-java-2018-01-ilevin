package ru.otus.l04;

/**
 * Task to produce heap dump after certain amount of time
 *
 * Heap dump analysis will show that the biggest objects are arrays java.lang.Object[]
 */
public class HeapDumpCauseTask implements Runnable {

    private static final int POOL_SIZE = 10;
    private Object[] pool = new Object[POOL_SIZE];
    private long delayMs;

    HeapDumpCauseTask(long dumpAfterSeconds) {
        delayMs = (dumpAfterSeconds * 1000)/ POOL_SIZE;
    }

    @Override
    public void run() {
        Runtime runtime = Runtime.getRuntime();
        long allocatedArraySize = runtime.freeMemory()/(POOL_SIZE * 4 /*Size of reference*/);

        if (allocatedArraySize > Integer.MAX_VALUE) {
            throw new RuntimeException("Current realization can't cause dump in certain circumstances");
        }

        long cycle = 0;
        while (true) {
            int poolIndex = (int)(cycle % POOL_SIZE);
            pool[poolIndex] = new Object[(int)allocatedArraySize];
            cycle++;
            try {
                Thread.sleep(delayMs, 0);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

