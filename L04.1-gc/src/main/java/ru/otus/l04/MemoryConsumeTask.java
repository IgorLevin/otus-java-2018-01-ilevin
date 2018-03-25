package ru.otus.l04;

import java.util.ArrayList;
import java.util.List;

/**
 * Allocate memory for collections with two life cycles: short and long
 * Long life cycle collection suppose to outlive young generation GC
 */
public class MemoryConsumeTask implements Runnable {
    @Override
    public void run() {
        long cycle = 0;
        List<String> longTermList = new ArrayList<>();
        while (true) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 10_000; i++) {
                list.add(new String(""));
                longTermList.add(new String(""));
            }
            for (int i = 0; i < 30_000; i++) {
                longTermList.add(new String(""));
            }

            if (cycle % 80 == 0) {
                longTermList.clear();
            }
            cycle++;

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
